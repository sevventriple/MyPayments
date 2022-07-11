package com.example.mypayments.service.impl;

import com.example.mypayments.model.BankAccount;
import com.example.mypayments.model.BankCard;
import com.example.mypayments.model.Client;
import com.example.mypayments.model.Remittance;
import com.example.mypayments.repository.BankAccountRepository;
import com.example.mypayments.repository.BankCardRepository;
import com.example.mypayments.repository.RemittanceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {
    @Mock
    RemittanceRepository remittanceRepository;
    @Mock
    BankAccountRepository bankAccountRepository;
    @Mock
    BankCardRepository bankCardRepository;
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    void processCardToCardPayment() {
        //Given
        String fromIdentificationNumber = "0000";
        String toIdentificationNumber = "1111";
        BigDecimal paymentSum = new BigDecimal(10);
        BigDecimal exceptedFromBalance = new BigDecimal("9E+1");
        BigDecimal exceptedToBalance = new BigDecimal("1.1E+2");
        BankCard fromBankCard = BankCard.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(0))
                .identificationNumber(fromIdentificationNumber)
                .build();
        BankCard toBankCard = BankCard.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(1))
                .identificationNumber(toIdentificationNumber)
                .build();

        //When
        Mockito.when(bankCardRepository.findByIdentificationNumber(fromIdentificationNumber)).thenReturn(fromBankCard);
        Mockito.when(bankCardRepository.findByIdentificationNumber(toIdentificationNumber)).thenReturn(toBankCard);
        paymentService.processCardToCardPayment("aa", fromIdentificationNumber, toIdentificationNumber, paymentSum);

        //Then
        assertEquals(exceptedFromBalance, fromBankCard.getBalance());
        assertEquals(exceptedToBalance, toBankCard.getBalance());
    }

    @Test
    void processCardToAccountPayment() {
        //Given
        String fromIdentificationNumber = "0000";
        String toIdentificationNumber = "1111";
        BigDecimal paymentSum = new BigDecimal(10);
        BigDecimal exceptedFromBalance = new BigDecimal("9E+1");
        BigDecimal exceptedToBalance = new BigDecimal("1.1E+2");
        BankCard fromBankCard = BankCard.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(0))
                .identificationNumber(fromIdentificationNumber)
                .build();
        BankAccount toBankAccount = BankAccount.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(1))
                .identificationNumber(toIdentificationNumber)
                .build();

        //When
        Mockito.when(bankCardRepository.findByIdentificationNumber(fromIdentificationNumber)).thenReturn(fromBankCard);
        Mockito.when(bankAccountRepository.findByIdentificationNumber(toIdentificationNumber)).thenReturn(toBankAccount);
        paymentService.processCardToAccountPayment("aa", fromIdentificationNumber, toIdentificationNumber, paymentSum);

        //Then
        assertEquals(exceptedFromBalance, fromBankCard.getBalance());
        assertEquals(exceptedToBalance, toBankAccount.getBalance());
    }

    @Test
    void processAccountToAccountPayment() {
        //Given
        String fromIdentificationNumber = "0000";
        String toIdentificationNumber = "1111";
        BigDecimal paymentSum = new BigDecimal(10);
        BigDecimal exceptedFromBalance = new BigDecimal("9E+1");
        BigDecimal exceptedToBalance = new BigDecimal("1.1E+2");
        BankAccount fromBankAccount = BankAccount.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(0))
                .identificationNumber(fromIdentificationNumber)
                .build();
        BankAccount toBankAccount = BankAccount.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(1))
                .identificationNumber(toIdentificationNumber)
                .build();

        //When
        Mockito.when(bankAccountRepository.findByIdentificationNumber(fromIdentificationNumber)).thenReturn(fromBankAccount);
        Mockito.when(bankAccountRepository.findByIdentificationNumber(toIdentificationNumber)).thenReturn(toBankAccount);
        paymentService.processAccountToAccountPayment("aa", fromIdentificationNumber, toIdentificationNumber, paymentSum);

        //Then
        assertEquals(exceptedFromBalance, fromBankAccount.getBalance());
        assertEquals(exceptedToBalance, toBankAccount.getBalance());
    }

    @Test
    void processAccountToCardPayment() {
        //Given
        String fromIdentificationNumber = "0000";
        String toIdentificationNumber = "1111";
        BigDecimal paymentSum = new BigDecimal(10);
        BigDecimal exceptedFromBalance = new BigDecimal("9E+1");
        BigDecimal exceptedToBalance = new BigDecimal("1.1E+2");
        BankAccount fromBankAccount = BankAccount.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(0))
                .identificationNumber(fromIdentificationNumber)
                .build();
        BankCard toBankCard = BankCard.builder()
                .balance(new BigDecimal(100))
                .currency("RUB")
                .holder(getClientDataSamples().get(1))
                .identificationNumber(toIdentificationNumber)
                .build();

        //When
        Mockito.when(bankAccountRepository.findByIdentificationNumber(fromIdentificationNumber)).thenReturn(fromBankAccount);
        Mockito.when(bankCardRepository.findByIdentificationNumber(toIdentificationNumber)).thenReturn(toBankCard);
        paymentService.processAccountToCardPayment("aa", fromIdentificationNumber, toIdentificationNumber, paymentSum);

        //Then
        assertEquals(exceptedFromBalance, fromBankAccount.getBalance());
        assertEquals(exceptedToBalance, toBankCard.getBalance());
    }

    @Test
    void getLastPaymentBySenderIdentificationNumber() {
        //Given
        String correctBillingDetails = "1";
        String incorrectBillingDetails = "-1";

        //When
        Mockito.when(remittanceRepository.findFirstByUserFromOrderByIdDesc(correctBillingDetails)).thenReturn(new Remittance());
        Mockito.when(remittanceRepository.findFirstByUserFromOrderByIdDesc(incorrectBillingDetails)).thenReturn(null);

        //Then
        assertNotNull(paymentService.getLastPaymentBySenderIdentificationNumber(correctBillingDetails));
        assertNull(paymentService.getLastPaymentBySenderIdentificationNumber(incorrectBillingDetails));
    }

    private List<Client> getClientDataSamples(){
        Client firstClient = Client.builder()
                .username("aa")
                .build();
        Client secondClient = Client.builder()
                .username("bb")
                .build();
        return Arrays.asList(firstClient, secondClient);
    }
}