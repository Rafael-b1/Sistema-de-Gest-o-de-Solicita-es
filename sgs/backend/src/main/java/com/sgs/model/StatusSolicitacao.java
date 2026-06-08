package com.sgs.model;

import com.sgs.exception.TransicaoStatusInvalidaException;

import java.util.Set;

/**
 * Enum que representa os possíveis status de uma solicitação e encapsula
 * as regras de transição conforme definido nas regras de negócio.
 */
public enum StatusSolicitacao {

    SOLICITADO {
        @Override
        public Set<StatusSolicitacao> transicoesPermitidas() {
            return Set.of(LIBERADO, REJEITADO);
        }
    },
    LIBERADO {
        @Override
        public Set<StatusSolicitacao> transicoesPermitidas() {
            return Set.of(APROVADO, REJEITADO);
        }
    },
    APROVADO {
        @Override
        public Set<StatusSolicitacao> transicoesPermitidas() {
            return Set.of(CANCELADO);
        }
    },
    REJEITADO {
        @Override
        public Set<StatusSolicitacao> transicoesPermitidas() {
            return Set.of(); // estado final
        }
    },
    CANCELADO {
        @Override
        public Set<StatusSolicitacao> transicoesPermitidas() {
            return Set.of(); // estado final
        }
    };

    public abstract Set<StatusSolicitacao> transicoesPermitidas();

    /**
     * Valida e retorna o novo status; lança exceção se a transição não for permitida.
     */
    public StatusSolicitacao transicionarPara(StatusSolicitacao novoStatus) {
        if (!transicoesPermitidas().contains(novoStatus)) {
            throw new TransicaoStatusInvalidaException(
                    String.format("Transição inválida: %s → %s. Transições permitidas a partir de %s: %s",
                            this, novoStatus, this, transicoesPermitidas())
            );
        }
        return novoStatus;
    }
}
