package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c WHERE c.owner.id = :userId " +
            "AND (:status IS NULL OR c.cardStatus = :status) " +
            "AND (:mask IS NULL OR c.cardMask LIKE %:mask%) " +
            "AND c.isDeleted = false")
    Page<Card> searchUserCards(@Param("userId") Long userId,
                               @Param("status") CardStatus status,
                               @Param("mask") String mask,
                               Pageable pageable);
    Page<Card> findByOwnerId(Long ownerId, Pageable pageable);
    Optional<Card> findByIdAndOwnerId(Long id, Long ownerId);

}
