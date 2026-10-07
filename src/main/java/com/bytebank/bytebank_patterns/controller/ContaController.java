package com.bytebank.bytebank_patterns.controller;

import com.bytebank.bytebank_patterns.dto.MovimentacaoResponse;
import com.bytebank.bytebank_patterns.dto.OperacaoRequest;
import com.bytebank.bytebank_patterns.model.Conta;
import com.bytebank.bytebank_patterns.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping("/{id}")
    public Conta buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        validarAcessoConta(id, jwt);

        return contaService.buscarPorId(id);
    }

    @PostMapping("/{id}/operacoes")
    public Conta executarOperacao(
            @PathVariable Long id,
            @Valid @RequestBody OperacaoRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        validarAcessoConta(id, jwt);

        return contaService.executarOperacao(
                id,
                request.operacao(),
                request.valor()
        );
    }

    @GetMapping("/{id}/movimentacoes")
    public List<MovimentacaoResponse> listarMovimentacoes(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        validarAcessoConta(id, jwt);

        return contaService.listarMovimentacoes(id);
    }

    private void validarAcessoConta(Long contaId, Jwt jwt) {

        Number contaIdToken = jwt.getClaim("contaId");

        if (contaIdToken == null ||
                contaIdToken.longValue() != contaId.longValue()) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para acessar esta conta."
            );
        }
    }
}
