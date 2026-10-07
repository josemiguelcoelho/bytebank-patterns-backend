package com.bytebank.bytebank_patterns.controller;

import com.bytebank.bytebank_patterns.dto.CadastroRequest;
import com.bytebank.bytebank_patterns.dto.CadastroResponse;
import com.bytebank.bytebank_patterns.dto.EsqueciSenhaRequest;
import com.bytebank.bytebank_patterns.dto.LoginRequest;
import com.bytebank.bytebank_patterns.dto.LoginResponse;
import com.bytebank.bytebank_patterns.dto.RedefinirSenhaRequest;
import com.bytebank.bytebank_patterns.service.AuthService;
import com.bytebank.bytebank_patterns.service.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public AuthController(
            AuthService authService,
            RecuperacaoSenhaService recuperacaoSenhaService
    ) {
        this.authService = authService;
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public CadastroResponse cadastrar(
            @Valid @RequestBody CadastroRequest request
    ) {
        return authService.cadastrar(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/esqueci-senha")
    public Map<String, String> esqueciSenha(
            @Valid @RequestBody EsqueciSenhaRequest request
    ) {

        recuperacaoSenhaService.solicitarRecuperacao(request);

        return Map.of(
                "mensagem",
                "Se o e-mail estiver cadastrado, você receberá as instruções para redefinir sua senha."
        );
    }

    @PostMapping("/redefinir-senha")
    public Map<String, String> redefinirSenha(
            @Valid @RequestBody RedefinirSenhaRequest request
    ) {

        recuperacaoSenhaService.redefinirSenha(request);

        return Map.of(
                "mensagem",
                "Senha redefinida com sucesso."
        );
    }
}
