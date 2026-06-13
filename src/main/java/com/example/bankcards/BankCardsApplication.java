package com.example.bankcards;

import com.example.bankcards.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BankCardsApplication {

    public static void main(String[] Object) {
        SpringApplication.run(BankCardsApplication.class, Object);
    }

    @Bean
    public CommandLineRunner fixUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            userRepository.findByUsername("guliza").ifPresent(user -> {
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole("ADMIN");
                userRepository.save(user);

            });
            userRepository.findByUsername("guliza_dev").ifPresent(user -> {
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("USER");
                userRepository.save(user);

                        });
        };
    }
}
