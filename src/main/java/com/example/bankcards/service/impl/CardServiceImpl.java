package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.CardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public Page<CardResponse> getUserCards(Long userId, CardStatus status, String mask, Pageable pageable) {
        return cardRepository.searchUserCards(userId, status, mask, pageable)
                .map(card -> new CardResponse(
                        card.getId(),
                        card.getCardMask(),
                        card.getBalance(),
                        card.getCardStatus(),
                        card.getExpiryDate(),
                        card.getOwner().getUsername()
                ));
    }

    @Override
    @Transactional
    public void requestBlockCard(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new RuntimeException("Card not found or does not belong to you"));

        if (card.getCardStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException("Card is already blocked");
        }

        card.setCardStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void transferBetweenOwnCards(Long userId, TransferRequest request) {
        if (request.fromCardId().equals(request.toCardId())) {
            throw new IllegalArgumentException("Transfer to the same card is not allowed");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        Card fromCard = cardRepository.findByIdAndOwnerId(request.fromCardId(), userId)
                .orElseThrow(() -> new RuntimeException("Source card not found or does not belong to you"));

        Card toCard = cardRepository.findByIdAndOwnerId(request.toCardId(), userId)
                .orElseThrow(() -> new RuntimeException("Destination card not found or does not belong to you"));

        if (fromCard.getCardStatus() != CardStatus.ACTIVE || toCard.getCardStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException("One or both cards are not active");
        }

        if (fromCard.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(request.amount()));
        toCard.setBalance(toCard.getBalance().add(request.amount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }
}
