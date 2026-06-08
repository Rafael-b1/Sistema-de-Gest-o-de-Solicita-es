package com.sgs.service;

import com.sgs.dto.*;
import com.sgs.exception.RecursoNaoEncontradoException;
import com.sgs.model.*;
import com.sgs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitanteRepository solicitanteRepository;
    private final CategoriaRepository   categoriaRepository;

    // ── Listagem com filtros dinâmicos (SQL nativo) ───────────────────────────

    @Transactional(readOnly = true)
    public List<SolicitacaoResponseDTO> listar(FiltroSolicitacaoDTO filtro) {
        String status      = filtro.getStatus()      != null ? filtro.getStatus().name() : null;
        Long   categoriaId = filtro.getCategoriaId();
        String dataInicio  = filtro.getDataInicio()  != null ? filtro.getDataInicio().toString()  : null;
        String dataFim     = filtro.getDataFim()     != null ? filtro.getDataFim().toString()     : null;

        List<Object[]> rows = solicitacaoRepository.listarComFiltros(status, categoriaId, dataInicio, dataFim);
        return rows.stream().map(this::mapearLinha).toList();
    }

    // ── Detalhe por ID (via JPA) ──────────────────────────────────────────────

    @Transactional(readOnly = true)
    public SolicitacaoResponseDTO buscarPorId(Long id) {
        Solicitacao s = buscarOuLancar(id);
        return toDTO(s);
    }

    // ── Cadastro ──────────────────────────────────────────────────────────────

    @Transactional
    public SolicitacaoResponseDTO criar(SolicitacaoRequestDTO dto) {
        Solicitante solicitante = solicitanteRepository.findById(dto.getSolicitanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Solicitante não encontrado: id=" + dto.getSolicitanteId()));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada: id=" + dto.getCategoriaId()));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setSolicitante(solicitante);
        solicitacao.setCategoria(categoria);
        solicitacao.setDescricao(dto.getDescricao());
        solicitacao.setValor(dto.getValor());
        solicitacao.setDataSolicitacao(
                dto.getDataSolicitacao() != null ? dto.getDataSolicitacao() : LocalDate.now());
        // status inicial sempre SOLICITADO, conforme regra de negócio

        return toDTO(solicitacaoRepository.save(solicitacao));
    }

    // ── Atualização de status ─────────────────────────────────────────────────

    @Transactional
    public SolicitacaoResponseDTO atualizarStatus(Long id, AtualizacaoStatusDTO dto) {
        Solicitacao solicitacao = buscarOuLancar(id);

        // a validação da transição está encapsulada no próprio enum
        StatusSolicitacao novoStatus = solicitacao.getStatus().transicionarPara(dto.getNovoStatus());
        solicitacao.setStatus(novoStatus);

        return toDTO(solicitacaoRepository.save(solicitacao));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Solicitacao buscarOuLancar(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Solicitação não encontrada: id=" + id));
    }

    /** Converte result row do SQL nativo para DTO. */
    private SolicitacaoResponseDTO mapearLinha(Object[] row) {
        return new SolicitacaoResponseDTO(
                ((Number) row[0]).longValue(),          // id
                (String)  row[1],                       // nome_solicitante
                (String)  row[2],                       // cpf_cnpj_solicitante
                ((Number) row[3]).longValue(),           // solicitante_id
                (String)  row[4],                       // nome_categoria
                ((Number) row[5]).longValue(),           // categoria_id
                (String)  row[6],                       // descricao
                new BigDecimal(row[7].toString()),       // valor
                LocalDate.parse(row[8].toString()),      // data_solicitacao
                StatusSolicitacao.valueOf((String) row[9]) // status
        );
    }

    /** Converte entidade JPA para DTO. */
    private SolicitacaoResponseDTO toDTO(Solicitacao s) {
        return new SolicitacaoResponseDTO(
                s.getId(),
                s.getSolicitante().getNome(),
                s.getSolicitante().getCpfCnpj(),
                s.getSolicitante().getId(),
                s.getCategoria().getNome(),
                s.getCategoria().getId(),
                s.getDescricao(),
                s.getValor(),
                s.getDataSolicitacao(),
                s.getStatus()
        );
    }
}
