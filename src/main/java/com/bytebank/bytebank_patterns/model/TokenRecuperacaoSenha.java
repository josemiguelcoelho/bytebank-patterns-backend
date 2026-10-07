package com.bytebank.bytebank_patterns.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_recuperacao_senha")
public class TokenRecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiracao;

    @Column(nullable = false)
    private boolean utilizado = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    protected TokenRecuperacaoSenha() {
    }

    public TokenRecuperacaoSenha(
            String tokenHash,
            LocalDateTime expiracao,
            Usuario usuario
    ) {
        this.tokenHash = tokenHash;
        this.expiracao = expiracao;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getExpiracao() {
        return expiracao;
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean estaExpirado() {
        return LocalDateTime.now().isAfter(expiracao);
    }

    public void marcarComoUtilizado() {
        this.utilizado = true;
    }
}
