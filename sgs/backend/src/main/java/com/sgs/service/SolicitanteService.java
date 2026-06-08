package com.sgs.service;

import com.sgs.dto.SolicitanteDTO;
import com.sgs.repository.SolicitanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitanteService {

    private final SolicitanteRepository repository;

    @Transactional(readOnly = true)
    public List<SolicitanteDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(s -> new SolicitanteDTO(s.getId(), s.getNome(), s.getCpfCnpj()))
                .toList();
    }
}
