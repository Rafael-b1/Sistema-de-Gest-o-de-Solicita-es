# SGS — Sistema de Gestão de Solicitações

Sistema web para registro, consulta e acompanhamento de solicitações de pagamento realizadas por diferentes áreas de uma organização.

---

## Tecnologias utilizadas

| Camada    | Tecnologia |
|-----------|------------|
| Backend   | Java 17 · Spring Boot 3.2 · Spring Data JPA · Bean Validation |
| Banco     | PostgreSQL 15+ |
| Frontend  | HTML5 / CSS3 / JavaScript puro (sem frameworks) |
| Build     | Maven 3.9+ |

---

## Pré-requisitos

- JDK 17+
- Maven 3.9+
- PostgreSQL 15+ em execução local (porta padrão `5432`)
- Qualquer servidor HTTP estático para servir o frontend (ou simplesmente abrir o `index.html` no browser via Live Server, por exemplo)

---

## Configuração do banco de dados

### 1. Criar banco e usuário

```sql
CREATE USER sgs_user WITH PASSWORD 'sgs_pass';
CREATE DATABASE sgs_db OWNER sgs_user;
GRANT ALL PRIVILEGES ON DATABASE sgs_db TO sgs_user;
```

### 2. Executar os scripts SQL (na ordem)

```bash
psql -U sgs_user -d sgs_db -f sql/01_ddl.sql
psql -U sgs_user -d sgs_db -f sql/02_dml.sql
```

O script `01_ddl.sql` cria as tabelas com seus vínculos de chaves estrangeiras e constraints de integridade.  
O script `02_dml.sql` popula **7 solicitantes**, **5 categorias** e **10 solicitações** de exemplo em diferentes status.

---

## Executando o backend

```bash
cd backend
mvn spring-boot:run
```

O servidor sobe na porta **8080**.  
Caso queira alterar banco, usuário ou senha, edite `src/main/resources/application.properties`.

---

## Executando o frontend

Abra o arquivo `frontend/index.html` em qualquer servidor estático.  
A maneira mais simples é com a extensão **Live Server** do VS Code, ou:

```bash
# Python 3
cd frontend
python3 -m http.server 3000
# acesse http://localhost:3000
```

O frontend consome a API em `http://localhost:8080/api` (configurável na variável `API` no topo do `index.html`).

---

## Endpoints da API

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET`  | `/api/solicitacoes` | Listar com filtros opcionais |
| `GET`  | `/api/solicitacoes/{id}` | Detalhe de uma solicitação |
| `POST` | `/api/solicitacoes` | Cadastrar nova solicitação |
| `PATCH`| `/api/solicitacoes/{id}/status` | Atualizar status |
| `GET`  | `/api/solicitantes` | Listar solicitantes |
| `GET`  | `/api/categorias` | Listar categorias |

### Parâmetros de filtro (`GET /api/solicitacoes`)

| Parâmetro | Tipo | Exemplo |
|-----------|------|---------|
| `status` | string | `SOLICITADO` |
| `categoriaId` | long | `2` |
| `dataInicio` | ISO date | `2025-06-01` |
| `dataFim` | ISO date | `2025-06-30` |

### Corpo para criação (`POST /api/solicitacoes`)

```json
{
  "solicitanteId": 1,
  "categoriaId": 2,
  "descricao": "Compra de material de escritório",
  "valor": 1500.00,
  "dataSolicitacao": "2025-06-08"
}
```

### Corpo para atualização de status (`PATCH /api/solicitacoes/{id}/status`)

```json
{ "novoStatus": "LIBERADO" }
```

---

## Regras de transição de status

```
SOLICITADO ──► LIBERADO
SOLICITADO ──► REJEITADO
LIBERADO   ──► APROVADO
LIBERADO   ──► REJEITADO
APROVADO   ──► CANCELADO
REJEITADO  ──► (estado final)
CANCELADO  ──► (estado final)
```

Tentativas de transições inválidas retornam `HTTP 422 Unprocessable Entity` com mensagem descritiva.

---

## Estrutura do projeto

```
sgs/
├── sql/
│   ├── 01_ddl.sql          # Criação das tabelas
│   └── 02_dml.sql          # Dados iniciais
├── frontend/
│   └── index.html          # SPA completo (HTML + CSS + JS)
└── backend/
    ├── pom.xml
    └── src/main/java/com/sgs/
        ├── SgsApplication.java
        ├── config/
        │   └── CorsConfig.java
        ├── controller/
        │   ├── SolicitacaoController.java
        │   ├── SolicitanteController.java
        │   └── CategoriaController.java
        ├── service/
        │   ├── SolicitacaoService.java
        │   ├── SolicitanteService.java
        │   └── CategoriaService.java
        ├── repository/
        │   ├── SolicitacaoRepository.java
        │   ├── SolicitanteRepository.java
        │   └── CategoriaRepository.java
        ├── model/
        │   ├── Solicitacao.java
        │   ├── Solicitante.java
        │   ├── Categoria.java
        │   └── StatusSolicitacao.java
        ├── dto/
        │   ├── SolicitacaoRequestDTO.java
        │   ├── SolicitacaoResponseDTO.java
        │   ├── AtualizacaoStatusDTO.java
        │   ├── FiltroSolicitacaoDTO.java
        │   ├── SolicitanteDTO.java
        │   └── CategoriaDTO.java
        └── exception/
            ├── GlobalExceptionHandler.java
            ├── RecursoNaoEncontradoException.java
            └── TransicaoStatusInvalidaException.java
```

---

## Decisões técnicas

### SQL nativo com filtros dinâmicos
A listagem principal usa `@Query(nativeQuery = true)` com a técnica de filtros opcionais via `COALESCE`/`IS NULL`:

```sql
WHERE (:status IS NULL OR s.status = :status)
  AND (:categoriaId IS NULL OR s.categoria_id = :categoriaId)
  ...
```

Isso evita concatenação de SQL em tempo de execução (que introduziria risco de SQL injection e tornaria o código ilegível), mantendo uma única query legível e segura com parâmetros tipados.

### Regras de transição encapsuladas no enum
O enum `StatusSolicitacao` possui um método `transicionarPara(novoStatus)` que centraliza toda a lógica de validação de fluxo. Isso segue o princípio _Tell, Don't Ask_: o próprio status sabe para onde pode ir, em vez de o service ou o controller precisarem fazer `if/switch` espalhados.

### Arquitetura em camadas
- **Controller**: recebe HTTP, valida entrada com Bean Validation, delega ao service.
- **Service**: contém a lógica de negócio (transição de status, construção de entidades, mapeamento DTO).
- **Repository**: acesso ao banco — SQL nativo para listagem, JPA para CRUD simples.

### Frontend sem framework
Optei por HTML/CSS/JS puro para evitar dependências de build e simplificar a execução. A lógica de transições é espelhada no frontend para exibir apenas os botões cabíveis por status, sem consultas extras ao backend.

---

## Versionamento

O repositório deve manter um histórico de commits claro, com mensagens seguindo o padrão:

```
feat: adiciona listagem com filtros dinâmicos
feat: implementa regras de transição de status no enum
fix: corrige mapeamento de colunas no SQL nativo
docs: adiciona README com instruções de execução
```
