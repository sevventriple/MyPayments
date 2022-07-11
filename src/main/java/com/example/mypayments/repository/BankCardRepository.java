package com.example.mypayments.repository;

import com.example.mypayments.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, String> {
    BankCard findByIdentificationNumber(String cardNumber);
}
