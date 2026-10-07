package com.bytebank.bytebank_patterns.strategy;

import com.bytebank.bytebank_patterns.model.Conta;

import java.math.BigDecimal;

public interface OperacaoStrategy {

    void executar(Conta conta, BigDecimal valor);
}
