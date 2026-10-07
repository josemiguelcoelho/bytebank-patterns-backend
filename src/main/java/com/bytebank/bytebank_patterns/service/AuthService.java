package com.bytebank.bytebank_patterns.service;

import com.bytebank.bytebank_patterns.dto.CadastroRequest;
import com.bytebank.bytebank_patterns.dto.CadastroResponse;
import com.bytebank.bytebank_patterns.dto.LoginRequest;
import com.bytebank.bytebank_patterns.dto.LoginResponse;
import com.bytebank.bytebank_patterns.model.Conta;
import com.bytebank.bytebank_patterns.model.Usuario;
import com.bytebank.bytebank_patterns.repository.ContaRepository;
import com.bytebank.bytebank_patterns.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            ContaRepository contaRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public CadastroResponse cadastrar(CadastroRequest request) {

        String nome = request.nome().trim();
        String email = request.email().trim().toLowerCase();

        if (!request.senha().equals(request.confirmarSenha())) {
            throw new IllegalArgumentException(
                    "As senhas não coincidem."
            );
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        String senhaProtegida =
                passwordEncoder.encode(request.senha());

        Conta conta = new Conta(nome);
        Conta contaSalva = contaRepository.save(conta);

        Usuario usuario = new Usuario(
                nome,
                email,
                senhaProtegida
        );

        usuario.vincularConta(contaSalva);

        Usuario usuarioSalvo =
                usuarioRepository.save(usuario);

        return CadastroResponse.from(usuarioSalvo);
    }

    public LoginResponse login(LoginRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "E-mail ou senha inválidos."
                        )
                );

        boolean senhaCorreta = passwordEncoder.matches(
                request.senha(),
                usuario.getSenha()
        );

        if (!senhaCorreta) {
            throw new IllegalArgumentException(
                    "E-mail ou senha inválidos."
            );
        }

        String token = tokenService.gerarToken(usuario);

        return LoginResponse.from(usuario, token);
    }
}
