package com.bytebank.bytebank_patterns.repository;

import com.bytebank.bytebank_patterns.model.TokenRecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacaoSenhaRepository
        extends JpaRepository<TokenRecuperacaoSenha, Long> {

    Optional<TokenRecuperacaoSenha> findByTokenHash(String tokenHash);
}
