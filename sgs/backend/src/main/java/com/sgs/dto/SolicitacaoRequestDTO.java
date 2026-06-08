package com.sgs.dto;

import com.sgs.model.StatusSolicitacao;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// ── Entrada: criar solicitação ────────────────────────────────────────────────
@Getter @Setter
public class SolicitacaoRequestDTO {

    @NotNull(message = "solicitanteId é obrigatório")
    private Long solicitanteId;

    @NotNull(message = "categoriaId é obrigatório")
    private Long categoriaId;

    @NotBlank(message = "descricao é obrigatória")
    @Size(max = 2000, message = "descricao deve ter no máximo 2000 caracteres")
    private String descricao;

    @NotNull(message = "valor é obrigatório")
    @DecimalMin(value = "0.01", message = "valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2, message = "valor fora do formato permitido")
    private BigDecimal valor;

    private LocalDate dataSolicitacao;
}
