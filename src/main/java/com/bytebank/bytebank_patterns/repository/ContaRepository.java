package com.bytebank.bytebank_patterns.repository;

import com.bytebank.bytebank_patterns.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
