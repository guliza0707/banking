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
            // 1. Guliza (ADMIN) ni tekshirish va parolini to'g'rilash
            userRepository.findByUsername("guliza").ifPresent(user -> {
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole("ADMIN");
                userRepository.save(user);
                System.out.println(">>>>> 'guliza' paroli 'admin123' ga yangilandi! <<<<<");
            });

            // 2. Guliza_dev (USER) ni tekshirish va parolini to'g'rilash
            userRepository.findByUsername("guliza_dev").ifPresent(user -> {
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("USER");
                userRepository.save(user);
                System.out.println(">>>>> 'guliza_dev' paroli 'user123' ga yangilandi! <<<<<");
            });
        };
    }
}
