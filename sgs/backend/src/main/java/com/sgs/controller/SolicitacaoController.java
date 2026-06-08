package com.sgs.controller;

import com.sgs.dto.*;
import com.sgs.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService service;

    /** GET /api/solicitacoes — listagem com filtros opcionais */
    @GetMapping
    public ResponseEntity<List<SolicitacaoResponseDTO>> listar(
            @ModelAttribute FiltroSolicitacaoDTO filtro) {
        return ResponseEntity.ok(service.listar(filtro));
    }

    /** GET /api/solicitacoes/{id} — detalhe */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /** POST /api/solicitacoes — cadastro */
    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(
            @RequestBody @Valid SolicitacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    /** PATCH /api/solicitacoes/{id}/status — atualização de status */
    @PatchMapping("/{id}/status")
    public ResponseEntity<SolicitacaoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizacaoStatusDTO dto) {
        return ResponseEntity.ok(service.atualizarStatus(id, dto));
    }
}
