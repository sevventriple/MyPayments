package com.example.mypayments.repository;

import com.example.mypayments.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    BankAccount findByIdentificationNumber(String IdentificationAccountNumber);
}
