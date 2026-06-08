package com.sgs.dto;

import com.sgs.model.StatusSolicitacao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
public class FiltroSolicitacaoDTO {

    private StatusSolicitacao status;

    private Long categoriaId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;
}
