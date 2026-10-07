package com.bytebank.bytebank_patterns.dto;

import com.bytebank.bytebank_patterns.model.Usuario;

public record CadastroResponse(
        Long id,
        String nome,
        String email
) {

    public static CadastroResponse from(Usuario usuario) {
        return new CadastroResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
