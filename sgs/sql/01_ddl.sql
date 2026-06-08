-- ============================================================
-- SGS - Sistema de Gestão de Solicitações
-- 01_ddl.sql - Criação das tabelas
-- ============================================================

CREATE TABLE solicitante (
    id      BIGSERIAL PRIMARY KEY,
    nome    VARCHAR(150) NOT NULL,
    cpf_cnpj VARCHAR(18) NOT NULL UNIQUE
);

CREATE TABLE categoria (
    id   BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE solicitacao (
    id               BIGSERIAL PRIMARY KEY,
    solicitante_id   BIGINT       NOT NULL REFERENCES solicitante(id),
    categoria_id     BIGINT       NOT NULL REFERENCES categoria(id),
    descricao        TEXT         NOT NULL,
    valor            NUMERIC(15,2) NOT NULL,
    data_solicitacao DATE         NOT NULL DEFAULT CURRENT_DATE,
    status           VARCHAR(20)  NOT NULL DEFAULT 'SOLICITADO',
    CONSTRAINT chk_status CHECK (status IN ('SOLICITADO','LIBERADO','APROVADO','REJEITADO','CANCELADO')),
    CONSTRAINT chk_valor   CHECK (valor > 0)
);
