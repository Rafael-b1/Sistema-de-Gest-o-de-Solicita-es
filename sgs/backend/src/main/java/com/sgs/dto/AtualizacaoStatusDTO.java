package com.sgs.dto;

import com.sgs.model.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AtualizacaoStatusDTO {

    @NotNull(message = "O novo status é obrigatório")
    private StatusSolicitacao novoStatus;
}
