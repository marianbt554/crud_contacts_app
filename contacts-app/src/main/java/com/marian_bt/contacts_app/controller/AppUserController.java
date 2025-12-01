package com.marian_bt.contacts_app.controller;

import com.marian_bt.contacts_app.domain.AppUser;
import com.marian_bt.contacts_app.domain.UserRole;
import com.marian_bt.contacts_app.repository.AppUserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class AppUserController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<AppUser> users =
                userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
        model.addAttribute("users", users);
        model.addAttribute("roles", UserRole.values());

        return "users/list";
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userForm", new CreateUserForm());
        model.addAttribute("roles", UserRole.values());
        return "users/new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("userForm") CreateUserForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        // basic manual validation
        if (form.getUsername() == null || form.getUsername().isBlank()) {
            bindingResult.rejectValue("username", "username.blank", "Username is required");
        }

        if (form.getPassword() == null || form.getPassword().isBlank()) {
            bindingResult.rejectValue("password", "password.blank", "Password is required");
        }

        userRepository.findByUsername(form.getUsername())
                .ifPresent(u ->
                        bindingResult.rejectValue("username", "username.already.exists",
                                "This username already exists"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return "users/new";
        }

        AppUser user = new AppUser();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(form.getRole());
        user.setEnabled(true);

        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "message",
                "User '" + user.getUsername() + "' created successfully"
        );
        return "redirect:/users";
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("User with id " + id + " does not exist"));

        EditUserForm form = new EditUserForm();
        form.setUsername(user.getUsername());
        form.setRole(user.getRole());
        // password left empty on purpose

        model.addAttribute("userForm", form);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("userId", id);

        return "users/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("userForm") EditUserForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        AppUser user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("User with id " + id + " does not exist"));

        // simple validation: role required
        if (form.getRole() == null) {
            bindingResult.rejectValue("role", "role.null", "Role is required");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("userId", id);
            return "users/edit";
        }


        user.setRole(form.getRole());


        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "message",
                "User '" + user.getUsername() + "' updated successfully."
        );

        return "redirect:/users";
    }

    public String toggleEnabled(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        AppUser user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("User with id " + id + " does not exist"));

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "message",
                "User '" + user.getUsername() + "' is now " +
                        (user.isEnabled() ? "enabled" : "disabled") + "."
        );
        return "redirect:/users";
    }


    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam("role") UserRole role,
                             RedirectAttributes redirectAttributes) {

        AppUser user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("User with id " + id + " does not exist"));

        user.setRole(role);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "message",
                "Role for '" + user.getUsername() + "' set to " + role + "."
        );
        return "redirect:/users";
    }


    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        AppUser user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("User with id " + id + " does not exist"));

        String currentUsername = authentication.getName();

        if (user.getUsername().equals(currentUsername)) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "You can not delete your own account."
            );
            return "redirect:/users";
        }

        if (user.getRole() == UserRole.ADMIN) {
            long adminCount = userRepository.countByRole(UserRole.ADMIN);
            if (adminCount <= 1) {
                redirectAttributes.addFlashAttribute(
                        "message",
                        "You can not delete the last administrator account."
                );
                return "redirect:/users";
            }
        }

        userRepository.delete(user);

        redirectAttributes.addFlashAttribute(
                "message",
                "User " + user.getUsername() + " has been deleted."
        );

        return "redirect:/users";
    }


    public static class CreateUserForm {
        @NotBlank
        private String username;

        @NotBlank
        private String password;

        @NotNull
        private UserRole role = UserRole.USER;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }
    }

    public static class EditUserForm {

        private String username; // read-only in UI
        private String password; // optional (blank = keep current)
        @NotNull
        private UserRole role = UserRole.USER;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }
    }
}
