# 🏋️‍♂️ GymDesk

![Java](https://img.shields.io/badge/Java-17+-red?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)
![Swing](https://img.shields.io/badge/GUI-Swing-orange)
![Database](https://img.shields.io/badge/DB-MySQL%2FPostgreSQL-green?logo=databricks)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

Sistema desktop em **Java (Swing + JDBC)** para gerenciamento de academias, permitindo controle de **alunos, planos, pagamentos, check-ins e relatórios** de forma prática e organizada.

---

## 📋 Funcionalidades

- 📌 Cadastro e gerenciamento de **alunos**
- 📝 Controle de **planos** de academia
- 💰 Registro e acompanhamento de **pagamentos**
- 🕒 Sistema de **check-in** para alunos
- 📊 Geração de **relatórios** (financeiros e administrativos)
- ⚠️ Painel de **alertas** para acompanhamento de pendências

---

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Swing** (interface gráfica desktop)
- **JDBC** (acesso a banco de dados)
- **Maven** (gerenciador de dependências)
- **MySQL / PostgreSQL** (pode ser adaptado)
- **SQL Script** (`schema.sql` para criação da base de dados)

---

## 📂 Estrutura do Projeto

```
gymdesk-avancado/
│── pom.xml                 # Configuração Maven
│── src/main/java/br/com/gymdesk/
│   ├── db/                 # Conexão com banco de dados
│   ├── model/              # Modelos (Aluno, Plano, Pagamento, Checkin)
│   ├── dao/                # DAOs (acesso ao banco)
│   ├── service/            # Serviços (ex: Relatórios)
│   └── ui/                 # Interface gráfica Swing
│── src/main/resources/
│   └── schema.sql          # Script para criação das tabelas
```

---

## ⚙️ Como Executar

### 1. Pré-requisitos
- [Java 17+](https://adoptium.net/) instalado  
- [Maven](https://maven.apache.org/) configurado  
- Banco de dados **MySQL** ou **PostgreSQL** rodando  

### 2. Criar Banco de Dados
Execute o script `src/main/resources/schema.sql` no seu banco:

```sql
CREATE DATABASE gymdesk;
USE gymdesk;

-- (tabelas e constraints definidas no schema.sql)
```

### 3. Configurar Conexão
No arquivo `Database.java`, ajuste as credenciais:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gymdesk";
private static final String USER = "seu_usuario";
private static final String PASSWORD = "sua_senha";
```

### 4. Compilar e Rodar
Na raiz do projeto:

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="br.com.gymdesk.ui.App"
```

---

## 🚀 Futuras Melhorias

- Autenticação de usuários (login/admin)  
- Dashboard inicial com métricas  
- Exportação de relatórios em PDF  
- Integração com serviços de pagamento  

---

## 📸 Demonstração

> *(adicione aqui prints da aplicação depois de rodar, exemplo: painel de alunos, tela de planos, etc.)*  

---

## 👨‍💻 Autor

Projeto desenvolvido por **Matheus Menezes** no intuito de praticar e aplicar conceitos de **Java, JDBC e desenvolvimento de sistemas desktop**.

---
