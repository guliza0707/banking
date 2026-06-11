package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminCardService;

import com.example.bankcards.util.CardEncUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardEncUtil cardEncUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
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
    public CardResponse generateCard(CardCreateRequest request) {
        User owner = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User is not found"));

        String encryptedNumber = cardEncUtil.encrypt(request.cardNumber());
        String mask = cardEncUtil.maskCardNumber(request.cardNumber());

        Card card = Card.builder()
                .encCardNumber(encryptedNumber)
                .cardMask(mask)
                .balance(request.initialBalance())
                .cardStatus(CardStatus.ACTIVE)
                .expiryDate(LocalDate.now().plusYears(4))
                .owner(owner)
                .build();

        Card savedCard = cardRepository.save(card);

        return new CardResponse(
                savedCard.getId(),
                savedCard.getCardMask(),
                savedCard.getBalance(),
                savedCard.getCardStatus(),
                savedCard.getExpiryDate(),
                owner.getUsername()
        );
    }

    @Override
    @Transactional
    public void activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card is not found"));
        card.setCardStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card "+ cardId+ " is not found"));
        card.setCardStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card with this id " + cardId + " is not found or already deleted"));


        card.setCardStatus(CardStatus.BLOCKED);
        cardRepository.delete(card);
    }
}