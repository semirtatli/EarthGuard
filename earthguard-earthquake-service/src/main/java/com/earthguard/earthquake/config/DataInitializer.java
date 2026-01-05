package com.earthguard.earthquake.config;

import com.earthguard.common.entity.User;
import com.earthguard.common.enums.Role;
import com.earthguard.earthquake.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                log.info("Initializing default users...");

                // Create admin user
                User admin = User.builder()
                        .username("admin")
                        .email("admin@earthguard.com")
                        .password(passwordEncoder.encode("admin123"))
                        .fullName("System Administrator")
                        .role(Role.ADMIN)
                        .enabled(true)
                        .accountNonLocked(true)
                        .accountNonExpired(true)
                        .credentialsNonExpired(true)
                        .build();

                // Create test user
                User user = User.builder()
                        .username("testuser")
                        .email("user@earthguard.com")
                        .password(passwordEncoder.encode("user123"))
                        .fullName("Test User")
                        .role(Role.USER)
                        .enabled(true)
                        .accountNonLocked(true)
                        .accountNonExpired(true)
                        .credentialsNonExpired(true)
                        .build();

                userRepository.save(admin);
                userRepository.save(user);

                log.info("✅ Default users created:");
                log.info("   - Admin: username=admin, password=admin123");
                log.info("   - User: username=testuser, password=user123");
            }
        };
    }
}