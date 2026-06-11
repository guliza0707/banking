package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CardCreateRequest(@NotBlank(message = "Card number cannot be blank")
                                @Size(min = 16, max = 16, message = "Card number must be exactly 16 digits")
                                String cardNumber,

                                @NotNull(message = "Initial balance is required")
                                @PositiveOrZero(message = "Initial balance cannot be negative")
                                BigDecimal initialBalance,

                                @NotNull(message = "User ID is required")
                                Long userId) {
}
