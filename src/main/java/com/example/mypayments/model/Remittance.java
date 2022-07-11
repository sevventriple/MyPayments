package com.example.mypayments.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Remittance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userFrom;
    private String userTo;
    private BigDecimal sum;
    @Enumerated
    private RemittanceType remittanceType;
    private String remittanceCurrency;
}
