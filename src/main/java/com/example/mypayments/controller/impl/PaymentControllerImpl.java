package com.example.mypayments.controller.impl;

import com.example.mypayments.controller.PaymentController;
import com.example.mypayments.model.Remittance;
import com.example.mypayments.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@AllArgsConstructor
public class PaymentControllerImpl implements PaymentController {
    PaymentService paymentService;

    @Override
    public ResponseEntity<Remittance> cardToCardPayment(Principal principal, String from, String to, BigDecimal sum) {
        paymentService.processCardToCardPayment(principal.getName(), from, to, sum);
        return new ResponseEntity<>(paymentService.getLastPaymentBySenderIdentificationNumber(from), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Remittance> cardToAccountPayment(Principal principal, String from, String to, BigDecimal sum) {
        paymentService.processCardToAccountPayment(principal.getName(), from, to, sum);
        return new ResponseEntity<>(paymentService.getLastPaymentBySenderIdentificationNumber(from), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Remittance> AccountToAccountPayment(Principal principal, String from, String to, BigDecimal sum) {
        paymentService.processAccountToAccountPayment(principal.getName(), from, to, sum);
        return new ResponseEntity<>(paymentService.getLastPaymentBySenderIdentificationNumber(from), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Remittance> AccountToCardPayment(Principal principal, String from, String to, BigDecimal sum) {
        paymentService.processAccountToCardPayment(principal.getName(), from, to, sum);
        return new ResponseEntity<>(paymentService.getLastPaymentBySenderIdentificationNumber(from), HttpStatus.OK);
    }
}
