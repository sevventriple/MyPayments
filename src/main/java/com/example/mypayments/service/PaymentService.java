package com.example.mypayments.service;

import com.example.mypayments.model.Remittance;

import java.math.BigDecimal;

public interface PaymentService {
    void processCardToCardPayment(String username, String from, String to, BigDecimal sum);
    void processCardToAccountPayment(String username, String from, String to, BigDecimal sum);
    void processAccountToAccountPayment(String username, String from, String to, BigDecimal sum);
    void processAccountToCardPayment(String username, String from, String to, BigDecimal sum);
    Remittance getLastPaymentBySenderIdentificationNumber(String accountNumber);
}
