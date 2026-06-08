package com.sgs.dto;

import com.sgs.model.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor
public class SolicitacaoResponseDTO {

    private Long id;
    private String nomeSolicitante;
    private String cpfCnpjSolicitante;
    private Long solicitanteId;
    private String nomeCategoria;
    private Long categoriaId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataSolicitacao;
    private StatusSolicitacao status;
}
