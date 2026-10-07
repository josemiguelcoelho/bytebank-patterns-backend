package com.bytebank.bytebank_patterns.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_usuario_email",
                        columnNames = "email"
                )
        }
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private String senha;

    @OneToOne
    @JoinColumn(
            name = "conta_id",
            unique = true
    )
    private Conta conta;

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Conta getConta() {
        return conta;
    }

    public void vincularConta(Conta conta) {
        this.conta = conta;
    }

    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }
}
