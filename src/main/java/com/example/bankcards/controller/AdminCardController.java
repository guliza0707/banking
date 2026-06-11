package com.example.bankcards.controller;


import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.AdminCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminCardController {

    private final AdminCardService adminCardService;

    @GetMapping
    public ResponseEntity<Page<CardResponse>> getAllCards(Pageable pageable) {
        Page<CardResponse> cards = adminCardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardCreateRequest request) {

        CardResponse createdCard = adminCardService.generateCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<String> activateCard(@PathVariable Long id) {
        adminCardService.activateCard(id);
        return ResponseEntity.ok("Card has been successfully activated.");
    }


    @PatchMapping("/{id}/block")
    public ResponseEntity<String> blockCard(@PathVariable Long id) {
        adminCardService.blockCard(id);
        return ResponseEntity.ok("Card with ID " + id + " has been blocked by administrator.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        adminCardService.deleteCard(id);
        return ResponseEntity.ok("Card with ID " + id + " has been permanently deleted from the system.");
    }
}