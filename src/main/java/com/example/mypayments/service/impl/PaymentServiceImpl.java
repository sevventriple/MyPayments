package com.example.mypayments.service.impl;

import com.example.mypayments.exception.BillingDetailsNotFoundException;
import com.example.mypayments.exception.NotAccessRightsToAccountException;
import com.example.mypayments.model.*;
import com.example.mypayments.model.BankAccount;
import com.example.mypayments.model.BankCard;
import com.example.mypayments.repository.BankAccountRepository;
import com.example.mypayments.repository.BankCardRepository;
import com.example.mypayments.repository.RemittanceRepository;
import com.example.mypayments.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.money.CurrencyUnit;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;

@AllArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    BankCardRepository bankCardRepository;
    BankAccountRepository bankAccountRepository;
    RemittanceRepository remittanceRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processCardToCardPayment(String username, String from, String to, BigDecimal sum) {
        BankCard fromBankCard = bankCardRepository.findByIdentificationNumber(from);
        BankCard toBankCard = bankCardRepository.findByIdentificationNumber(to);
        validatePayment(fromBankCard, toBankCard, username);
        processPayment(fromBankCard, toBankCard, sum);
        Remittance remittance = Remittance.builder()
                .userFrom(from)
                .userTo(to)
                .sum(sum)
                .remittanceCurrency(fromBankCard.getCurrency())
                .remittanceType(RemittanceType.CARD_TO_CARD)
                .build();
        bankCardRepository.save(fromBankCard);
        bankCardRepository.save(toBankCard);
        remittanceRepository.save(remittance);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processCardToAccountPayment(String username, String from, String to, BigDecimal sum) {
        BankCard fromBankCard = bankCardRepository.findByIdentificationNumber(from);
        BankAccount toBankAccount = bankAccountRepository.findByIdentificationNumber(to);
        validatePayment(fromBankCard, toBankAccount, username);
        processPayment(fromBankCard, toBankAccount, sum);
        Remittance remittance = Remittance.builder()
                .userFrom(from)
                .userTo(to)
                .sum(sum)
                .remittanceCurrency(fromBankCard.getCurrency())
                .remittanceType(RemittanceType.CARD_TO_ACCOUNT)
                .build();
        bankCardRepository.save(fromBankCard);
        bankAccountRepository.save(toBankAccount);
        remittanceRepository.save(remittance);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processAccountToAccountPayment(String username, String from, String to, BigDecimal sum) {
        BankAccount fromBankAccount = bankAccountRepository.findByIdentificationNumber(from);
        BankAccount toBankAccount = bankAccountRepository.findByIdentificationNumber(to);
        validatePayment(fromBankAccount, toBankAccount, username);
        processPayment(fromBankAccount, toBankAccount, sum);
        Remittance remittance = Remittance.builder()
                .userFrom(from)
                .userTo(to)
                .sum(sum)
                .remittanceCurrency(fromBankAccount.getCurrency())
                .remittanceType(RemittanceType.ACCOUNT_TO_ACCOUNT)
                .build();
        bankAccountRepository.save(fromBankAccount);
        bankAccountRepository.save(toBankAccount);
        remittanceRepository.save(remittance);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processAccountToCardPayment(String username, String from, String to, BigDecimal sum) {
        BankAccount fromBankAccount = bankAccountRepository.findByIdentificationNumber(from);
        BankCard toBankCard = bankCardRepository.findByIdentificationNumber(to);
        validatePayment(fromBankAccount, toBankCard, username);
        processPayment(fromBankAccount, toBankCard, sum);
        Remittance remittance = Remittance.builder()
                .userFrom(from)
                .userTo(to)
                .sum(sum)
                .remittanceCurrency(String.valueOf(fromBankAccount.getCurrency()))
                .remittanceType(RemittanceType.ACCOUNT_TO_CARD)
                .build();
        bankAccountRepository.save(fromBankAccount);
        bankCardRepository.save(toBankCard);
        remittanceRepository.save(remittance);
    }

    private void processPayment(BillingDetails from, BillingDetails to, BigDecimal sum) {
        Money fromBalance = Money.of(from.getBalance(), from.getCurrency());
        Money toBalance = Money.of(to.getBalance(), to.getCurrency());
        CurrencyUnit fromCurrency = fromBalance.getCurrency();
        CurrencyUnit toCurrency = toBalance.getCurrency();
        Money paymentSum = Money.of(sum, fromCurrency);
        fromBalance = fromBalance.subtract(paymentSum);
        from.setBalance(fromBalance.getNumberStripped());
        if (!fromCurrency.equals(toCurrency)) {
            CurrencyConversion conversionToRecipientCurrency = MonetaryConversions.getConversion(toCurrency);
            paymentSum = paymentSum.with(conversionToRecipientCurrency);
        }
        toBalance = toBalance.add(paymentSum);
        to.setBalance(toBalance.getNumberStripped());
    }

    private void validatePayment(BillingDetails from, BillingDetails to, String senderUsername) {
        if (from == null || to == null)
            throw new BillingDetailsNotFoundException();
        if (!senderUsername.equals(from.getHolder().getUsername())) {
            log.error("user with username: {} trying to have access to account or card with identification number: {}",
                    senderUsername, from.getIdentificationNumber());
            throw new NotAccessRightsToAccountException();
        }
    }

    @Override
    public Remittance getLastPaymentBySenderIdentificationNumber(String accountNumber) {
        return remittanceRepository.findFirstByUserFromOrderByIdDesc(accountNumber);
    }
}