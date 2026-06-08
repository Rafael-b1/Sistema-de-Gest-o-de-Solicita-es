package com.sgs.repository;

import com.sgs.model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    /**
     * Listagem principal via SQL nativo com JOIN entre tabelas e filtros dinâmicos.
     *
     * Os parâmetros opcionais usam a convenção COALESCE/IS NULL para que,
     * quando nulos, o filtro seja ignorado — evitando múltiplas queries ou
     * concatenação de SQL em tempo de execução.
     *
     * @param status      filtro pelo enum de status (ou null para todos)
     * @param categoriaId filtro por categoria (ou null para todas)
     * @param dataInicio  filtro de data inicial (ou null)
     * @param dataFim     filtro de data final (ou null)
     */
    @Query(value = """
            SELECT
                s.id,
                sol.nome             AS nome_solicitante,
                sol.cpf_cnpj         AS cpf_cnpj_solicitante,
                sol.id               AS solicitante_id,
                cat.nome             AS nome_categoria,
                cat.id               AS categoria_id,
                s.descricao,
                s.valor,
                s.data_solicitacao,
                s.status
            FROM solicitacao s
            INNER JOIN solicitante sol ON sol.id = s.solicitante_id
            INNER JOIN categoria   cat ON cat.id = s.categoria_id
            WHERE (:status       IS NULL OR s.status        = :status)
              AND (:categoriaId  IS NULL OR s.categoria_id  = :categoriaId)
              AND (:dataInicio   IS NULL OR s.data_solicitacao >= CAST(:dataInicio AS DATE))
              AND (:dataFim      IS NULL OR s.data_solicitacao <= CAST(:dataFim   AS DATE))
            ORDER BY s.data_solicitacao DESC, s.id DESC
            """,
            nativeQuery = true)
    List<Object[]> listarComFiltros(
            @Param("status")      String status,
            @Param("categoriaId") Long categoriaId,
            @Param("dataInicio")  String dataInicio,
            @Param("dataFim")     String dataFim
    );
}
