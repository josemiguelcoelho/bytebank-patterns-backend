package com.bytebank.bytebank_patterns.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OperacaoRequest(

        @NotBlank(message = "A operação é obrigatória.")
        String operacao,

        @NotNull(message = "O valor é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor

) {
}
