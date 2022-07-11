package com.example.mypayments.repository;

import com.example.mypayments.model.Remittance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemittanceRepository extends JpaRepository<Remittance, Long> {
    Remittance findFirstByUserFromOrderByIdDesc(String from);
}
