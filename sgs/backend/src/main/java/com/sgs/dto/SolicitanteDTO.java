package com.sgs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class SolicitanteDTO {
    private Long id;
    private String nome;
    private String cpfCnpj;
}
