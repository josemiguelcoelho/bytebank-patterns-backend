package com.bytebank.bytebank_patterns.service;

import com.bytebank.bytebank_patterns.model.Conta;
import com.bytebank.bytebank_patterns.model.Movimentacao;
import com.bytebank.bytebank_patterns.repository.ContaRepository;
import com.bytebank.bytebank_patterns.repository.MovimentacaoRepository;
import com.bytebank.bytebank_patterns.strategy.OperacaoStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytebank.bytebank_patterns.dto.MovimentacaoResponse;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final Map<String, OperacaoStrategy> estrategias;

    public ContaService(
            ContaRepository contaRepository,
            MovimentacaoRepository movimentacaoRepository,
            Map<String, OperacaoStrategy> estrategias
    ) {
        this.contaRepository = contaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.estrategias = estrategias;
    }

    public Conta criar(String titular) {
        Conta conta = new Conta(titular);
        return contaRepository.save(conta);
    }

    public List<Conta> listar() {
        return contaRepository.findAll();
    }

    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Conta não encontrada."));
    }

    @Transactional
    public Conta executarOperacao(
            Long contaId,
            String operacao,
            BigDecimal valor
    ) {
        Conta conta = buscarPorId(contaId);

        String tipoOperacao = operacao.toUpperCase();

        OperacaoStrategy estrategia =
                estrategias.get(tipoOperacao);

        if (estrategia == null) {
            throw new IllegalArgumentException("Operação inválida.");
        }

        estrategia.executar(conta, valor);

        Conta contaAtualizada = contaRepository.save(conta);

        Movimentacao movimentacao =
                new Movimentacao(tipoOperacao, valor, contaAtualizada);

        movimentacaoRepository.save(movimentacao);

        return contaAtualizada;
    }
   public List<MovimentacaoResponse> listarMovimentacoes(Long contaId) {
    buscarPorId(contaId);

    return movimentacaoRepository
            .findByContaIdOrderByDataHoraDesc(contaId)
            .stream()
            .map(MovimentacaoResponse::from)
            .toList();
}


}
