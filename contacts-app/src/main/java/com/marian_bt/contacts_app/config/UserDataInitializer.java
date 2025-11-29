package com.marian_bt.contacts_app.config;


import com.marian_bt.contacts_app.domain.AppUser;
import com.marian_bt.contacts_app.domain.UserRole;
import com.marian_bt.contacts_app.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDataInitializer {

    @Bean
    CommandLineRunner initUsers (AppUserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0){
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEnabled(true);
                admin.setRole(UserRole.ADMIN);
                userRepository.save(admin);

                AppUser user = new AppUser();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEnabled(true);
                user.setRole(UserRole.USER);
                userRepository.save(user);

                System.out.println(">>Created default users: admin/admin123 and user/user123");
            }
        };
    }
}
