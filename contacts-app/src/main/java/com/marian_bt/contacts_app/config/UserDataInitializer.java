package com.marian_bt.contacts_app.config;


import com.marian_bt.contacts_app.domain.AppUser;
import com.marian_bt.contacts_app.domain.UserRole;
import com.marian_bt.contacts_app.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

@Configuration
public class UserDataInitializer {
    private static final Logger log = LoggerFactory.getLogger(UserDataInitializer.class);

    /**
     * Creates default users ONLY in development profile.
     * For production, create users manually via database or admin interface.
     *
     * WARNING: Default passwords are WEAK and should be changed immediately!
     */
    @Bean
    @Profile("dev")  // Only runs when 'dev' profile is active
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

                log.warn("=".repeat(80));
                log.warn("DEVELOPMENT MODE: Created default users with WEAK passwords");
                log.warn("Admin user: admin / Change credentials immediately!");
                log.warn("Regular user: user / Change credentials immediately!");
                log.warn("DO NOT use these accounts in production!");
                log.warn("=".repeat(80));
            }
        };
    }
}
