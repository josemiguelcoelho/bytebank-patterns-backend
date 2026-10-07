package com.bytebank.bytebank_patterns.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    public Conta() {
    }

    public Conta(String titular) {
        this.titular = titular;
        this.saldo = BigDecimal.ZERO;
    }

    public void depositar(BigDecimal valor) {
        validarValor(valor);
        saldo = saldo.add(valor);
    }

    public void sacar(BigDecimal valor) {
        validarValor(valor);

        if (saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        saldo = saldo.subtract(valor);
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
