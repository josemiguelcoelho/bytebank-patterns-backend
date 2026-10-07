package com.bytebank.bytebank_patterns.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarContaRequest(

        @NotBlank(message = "O nome do titular é obrigatório.")
        String titular

) {
}
