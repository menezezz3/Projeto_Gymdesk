# GymDesk (Refatorado)

Projeto Java Swing + SQLite com arquitetura em camadas (MVC/DAO) para gestão de academia.

## Como compilar e executar

Pré-requisitos: **Java 17+** e **Maven** instalados.

```bash
mvn clean package
java -jar target/gymdesk-1.0.0-jar-with-dependencies.jar
```

O banco `gymdesk.db` será criado automaticamente na mesma pasta do executável.

## Melhorias principais
- Separação de camadas: `model`, `dao`, `service`, `ui`, `db`.
- Tabelas com ordenação, filtro de alunos por nome e diálogos de edição.
- Look&Feel moderno com FlatLaf.
- Relatórios simples (total de alunos, receita do mês).
- Código pronto para evoluir (exportações, alertas, permissões etc.).