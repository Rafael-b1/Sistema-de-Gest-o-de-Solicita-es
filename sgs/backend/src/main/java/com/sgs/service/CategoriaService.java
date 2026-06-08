package com.sgs.service;

import com.sgs.dto.CategoriaDTO;
import com.sgs.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(c -> new CategoriaDTO(c.getId(), c.getNome()))
                .toList();
    }
}
