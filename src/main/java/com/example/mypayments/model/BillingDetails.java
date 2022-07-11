package com.example.mypayments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BillingDetails {
    @Id
    private String identificationNumber;
    private BigDecimal balance;
    private String currency;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Client holder;
}