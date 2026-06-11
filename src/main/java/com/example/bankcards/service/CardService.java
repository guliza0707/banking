package com.example.bankcards.service;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {
     Page<CardResponse> getUserCards(Long userId, CardStatus status, String mask, Pageable pageable);
     void requestBlockCard(Long cardId, Long userId);
     void transferBetweenOwnCards(Long userId, TransferRequest request);
}
