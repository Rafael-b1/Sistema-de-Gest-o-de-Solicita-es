-- ============================================================
-- SGS - Sistema de Gestão de Solicitações
-- 02_dml.sql - Dados iniciais
-- ============================================================

INSERT INTO solicitante (nome, cpf_cnpj) VALUES
    ('Ana Paula Ferreira',   '111.222.333-44'),
    ('Carlos Eduardo Lima',  '555.666.777-88'),
    ('Márcia Oliveira Santos','999.000.111-22'),
    ('Tech Solutions Ltda',  '12.345.678/0001-90'),
    ('Distribuidora Norte S/A','98.765.432/0001-10'),
    ('Roberto Alves Costa',  '333.444.555-66'),
    ('Fernanda Duarte',      '777.888.999-00');

INSERT INTO categoria (nome) VALUES
    ('Serviços'),
    ('Material'),
    ('Transporte'),
    ('Tecnologia'),
    ('Infraestrutura');

INSERT INTO solicitacao (solicitante_id, categoria_id, descricao, valor, data_solicitacao, status) VALUES
    (1, 1, 'Contratação de serviço de limpeza para sede',          3500.00, '2025-06-01', 'SOLICITADO'),
    (2, 2, 'Aquisição de material de escritório – resma A4',        850.00, '2025-06-02', 'LIBERADO'),
    (3, 3, 'Frete de equipamentos para filial Recife',             2200.00, '2025-06-03', 'APROVADO'),
    (4, 4, 'Licença anual de software de gestão',                 12000.00, '2025-06-04', 'REJEITADO'),
    (5, 5, 'Reforma elétrica do depósito',                        45000.00, '2025-06-05', 'CANCELADO'),
    (6, 1, 'Consultoria jurídica – elaboração de contratos',        7500.00, '2025-06-06', 'SOLICITADO'),
    (7, 2, 'Compra de cadeiras ergonômicas para TI',               9800.00, '2025-06-07', 'LIBERADO'),
    (1, 3, 'Translado de diretores para congresso SP',             1400.00, '2025-06-08', 'SOLICITADO'),
    (2, 4, 'Aquisição de notebooks Dell para equipe de suporte',  32000.00, '2025-06-09', 'APROVADO'),
    (3, 5, 'Instalação de câmeras de segurança',                  18500.00, '2025-06-10', 'SOLICITADO');
