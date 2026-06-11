package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(@NotNull(message = "Source card ID is required")
                              Long fromCardId,

                              @NotNull(message = "Destination card ID is required")
                              Long toCardId,

                              @NotNull(message = "Transfer amount is required")
                              @Positive(message = "Transfer amount must be greater than zero")
                              BigDecimal amount){
}
