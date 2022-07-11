package com.example.mypayments.controller;

import com.example.mypayments.model.Remittance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;

public interface PaymentController {
    @PostMapping(path = "/card_to_card")
    ResponseEntity<Remittance> cardToCardPayment(Principal principal, String from, String to, BigDecimal sum);

    @PostMapping(path = "/card_to_account")
    ResponseEntity<Remittance> cardToAccountPayment(Principal principal, String from, String to, BigDecimal sum);

    @PostMapping(path = "/account_to_account")
    ResponseEntity<Remittance> AccountToAccountPayment(Principal principal, String from, String to, BigDecimal sum);

    @PostMapping(path = "/account_to_card")
    ResponseEntity<Remittance> AccountToCardPayment(Principal principal, String from, String to, BigDecimal sum);
}
