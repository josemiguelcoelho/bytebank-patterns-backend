package com.bytebank.bytebank_patterns.dto;

import com.bytebank.bytebank_patterns.model.Movimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        String tipo,
        BigDecimal valor,
        LocalDateTime dataHora
) {

    public static MovimentacaoResponse from(Movimentacao movimentacao) {
        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getTipo(),
                movimentacao.getValor(),
                movimentacao.getDataHora()
        );
    }
}
