package com.sgs.controller;

import com.sgs.dto.SolicitanteDTO;
import com.sgs.service.SolicitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitantes")
@RequiredArgsConstructor
public class SolicitanteController {

    private final SolicitanteService service;

    @GetMapping
    public ResponseEntity<List<SolicitanteDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
