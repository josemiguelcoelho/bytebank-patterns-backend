package com.bytebank.bytebank_patterns.service;

import com.bytebank.bytebank_patterns.dto.EsqueciSenhaRequest;
import com.bytebank.bytebank_patterns.dto.RedefinirSenhaRequest;
import com.bytebank.bytebank_patterns.model.TokenRecuperacaoSenha;
import com.bytebank.bytebank_patterns.model.Usuario;
import com.bytebank.bytebank_patterns.repository.TokenRecuperacaoSenhaRepository;
import com.bytebank.bytebank_patterns.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class RecuperacaoSenhaService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacaoSenhaRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final SecureRandom secureRandom = new SecureRandom();

    public RecuperacaoSenhaService(
            UsuarioRepository usuarioRepository,
            TokenRecuperacaoSenhaRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void solicitarRecuperacao(
            EsqueciSenhaRequest request
    ) {

        String email = request.email()
                .trim()
                .toLowerCase();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElse(null);

        /*
         * Não informamos se o e-mail existe ou não.
         * Isso evita que alguém use a API para descobrir
         * quais endereços possuem conta no ByteBank.
         */
        if (usuario == null) {
            return;
        }

        String token = gerarTokenSeguro();
        String tokenHash = gerarHash(token);

        TokenRecuperacaoSenha recuperacao =
                new TokenRecuperacaoSenha(
                        tokenHash,
                        LocalDateTime.now().plusMinutes(15),
                        usuario
                );

        tokenRepository.save(recuperacao);

        emailService.enviarEmailRecuperacao(
                usuario.getEmail(),
                token
        );
    }

    @Transactional
    public void redefinirSenha(
            RedefinirSenhaRequest request
    ) {

        if (!request.novaSenha()
                .equals(request.confirmarSenha())) {

            throw new IllegalArgumentException(
                    "As senhas não coincidem."
            );
        }

        String tokenHash = gerarHash(request.token());

        TokenRecuperacaoSenha recuperacao =
                tokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Token inválido ou expirado."
                                )
                        );

        if (recuperacao.isUtilizado()
                || recuperacao.estaExpirado()) {

            throw new IllegalArgumentException(
                    "Token inválido ou expirado."
            );
        }

        Usuario usuario = recuperacao.getUsuario();

        String novaSenhaProtegida =
                passwordEncoder.encode(
                        request.novaSenha()
                );

        usuario.alterarSenha(novaSenhaProtegida);
        recuperacao.marcarComoUtilizado();

        usuarioRepository.save(usuario);
        tokenRepository.save(recuperacao);
    }

    private String gerarTokenSeguro() {

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String gerarHash(String token) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of()
                    .formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "Não foi possível gerar o hash do token.",
                    e
            );
        }
    }
}
