package com.example.bankcards.controller;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardUserController {

    private final CardService cardService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<CardResponse>> getMyCards(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) String mask,
            Pageable pageable) {
        Page<CardResponse> cards = cardService.getUserCards(userPrincipal.getId(), status, mask, pageable);
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/{id}/block")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> blockCard(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        cardService.requestBlockCard(id, userPrincipal.getId());
        return ResponseEntity.ok("Card is successfully blocked");
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> transfer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody TransferRequest transferRequest) {
        cardService.transferBetweenOwnCards(userPrincipal.getId(), transferRequest);
        return ResponseEntity.ok("Transfer is successfully completed");
    }
}
