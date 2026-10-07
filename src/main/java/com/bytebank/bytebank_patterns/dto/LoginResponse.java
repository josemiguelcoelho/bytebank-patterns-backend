package com.bytebank.bytebank_patterns.dto;

import com.bytebank.bytebank_patterns.model.Usuario;

public record LoginResponse(
        Long id,
        String nome,
        String email,
        Long contaId,
        String token
) {

    public static LoginResponse from(
            Usuario usuario,
            String token
    ) {
        return new LoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getConta().getId(),
                token
        );
    }
}
