package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCardService {
    Page<CardResponse> getAllCards(Pageable pageable);
    CardResponse generateCard(CardCreateRequest request);
    void activateCard(Long cardId);
    void blockCard(Long cardId);
    void deleteCard(Long cardId);
}
