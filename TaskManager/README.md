# ✅ TaskManager — Gerenciador de Tarefas e Metas Pessoais

## 📌 Descrição do Problema

No dia a dia, é comum esquecer tarefas importantes, perder prazos e não acompanhar o progresso das metas pessoais. Anotar tudo em papel é ineficiente, e aplicativos online exigem cadastro e internet. O **TaskManager** resolve isso com uma solução desktop simples, visual e offline.

---

## 🎯 Objetivo do Sistema

Oferecer uma ferramenta desktop para:
- Criar, editar e remover **tarefas** com prazo, prioridade e categoria
- Acompanhar **metas** de longo prazo com barra de progresso
- Visualizar um **dashboard** com estatísticas em tempo real
- Alertar sobre tarefas **atrasadas** ao abrir o sistema
- **Persistir dados** localmente em arquivo de texto (sem banco de dados)

---

## ✅ Requisitos Funcionais

| # | Requisito |
|---|-----------|
| RF01 | Adicionar tarefa com título, descrição, prazo, prioridade e categoria |
| RF02 | Adicionar meta com título, descrição, motivação e progresso percentual |
| RF03 | Editar qualquer item existente |
| RF04 | Marcar itens como concluídos |
| RF05 | Remover itens |
| RF06 | Pesquisar itens por título ou descrição |
| RF07 | Filtrar por tipo, status ou atraso |
| RF08 | Exibir dashboard com total, concluídos, pendentes e atrasadas |
| RF09 | Alertar automaticamente sobre tarefas com prazo vencido |
| RF10 | Salvar e carregar dados em arquivo local |

---

## 🔒 Requisitos Não Funcionais

| # | Requisito |
|---|-----------|
| RNF01 | Interface gráfica com Java Swing |
| RNF02 | Sem dependências externas (apenas JDK) |
| RNF03 | Compatível com Java 17 ou superior |
| RNF04 | Tempo de resposta imediato para todas as operações |
| RNF05 | Dados persistidos em arquivo `.txt` local |
| RNF06 | Código organizado em pacotes (MVC) |

---

## 🛠️ Tecnologias Utilizadas

- **Java 17+** — Linguagem principal
- **Java Swing** — Interface gráfica (GUI)
- **Java I/O (NIO)** — Persistência em arquivo local
- **Java Collections & Streams** — Manipulação de listas

---

## 📂 Estrutura do Projeto

```
TaskManager/
└── src/
    └── taskmanager/
        ├── Main.java                        ← Ponto de entrada
        ├── model/
        │   ├── Item.java                    ← Classe abstrata base (HERANÇA)
        │   ├── Tarefa.java                  ← Estende Item (HERANÇA + POLIMORFISMO)
        │   ├── Meta.java                    ← Estende Item (HERANÇA + POLIMORFISMO)
        │   └── Prioridade.java              ← Enum de prioridades
        ├── controller/
        │   └── TaskController.java          ← Lógica de negócio (ENCAPSULAMENTO)
        ├── view/
        │   ├── MainWindow.java              ← Janela principal
        │   ├── DashboardPanel.java          ← Painel de estatísticas
        │   ├── ListaPanel.java              ← Tabela de itens com filtros
        │   ├── TarefaDialog.java            ← Diálogo criar/editar tarefa
        │   └── MetaDialog.java              ← Diálogo criar/editar meta
        └── util/
            └── ArquivoUtil.java             ← Persistência em arquivo
```

---

## 🧱 Diagrama de Classes (simplificado)

```
         ┌─────────────────────────────────┐
         │          <<abstract>>           │
         │             Item                │
         │─────────────────────────────────│
         │ - id: String                    │
         │ - titulo: String                │
         │ - descricao: String             │
         │ - dataCriacao: LocalDate        │
         │ - concluido: boolean            │
         │─────────────────────────────────│
         │ + getTipo(): String  (abstract) │
         │ + getResumo(): String (abstract)│
         └──────────────┬──────────────────┘
                        │ (herança)
          ┌─────────────┴──────────────┐
          │                            │
┌─────────▼──────────┐    ┌───────────▼────────┐
│       Tarefa        │    │        Meta         │
│────────────────────│    │────────────────────│
│ - prazo: LocalDate  │    │ - progressoAtual:int│
│ - prioridade        │    │ - motivacao: String │
│ - categoria: String │    │────────────────────│
│────────────────────│    │ + avancarProgresso()│
│ + isAtrasada()      │    │ + getTipo()         │
│ + getTipo()         │    │ + getResumo()       │
│ + getResumo()       │    └────────────────────┘
└────────────────────┘

         ┌────────────────────────────────┐
         │        TaskController           │
         │────────────────────────────────│
         │ - itens: List<Item>            │
         │────────────────────────────────│
         │ + adicionarTarefa(...)         │
         │ + adicionarMeta(...)           │
         │ + concluirItem(id)             │
         │ + removerItem(id)              │
         │ + pesquisar(termo)             │
         └────────────────────────────────┘
```

---

## 🟢 Conceitos de POO aplicados

### 🔵 Herança
- `Tarefa` e `Meta` herdam de `Item` (classe abstrata)
- Reutilizam atributos: `id`, `titulo`, `descricao`, `dataCriacao`, `concluido`

### 🟡 Encapsulamento
- Todos os atributos são `private`
- Acesso apenas via getters/setters com validação
- A lista de itens no `TaskController` é `private final`

### 🟠 Polimorfismo
- `getTipo()` e `getResumo()` são `abstract` em `Item`
- Cada subclasse implementa de forma diferente
- `ListaPanel` itera a lista de `Item` e chama `getTipo()`/`getResumo()` polimorficamente

### 🔴 Tratamento de Erros
- `TarefaDialog` valida título obrigatório e formato de data (try-catch com `DateTimeParseException`)
- `MetaDialog` valida título vazio
- `TaskController` valida IDs inexistentes e lança `IllegalArgumentException`
- `ArquivoUtil` trata linhas corrompidas silenciosamente
- `Main` trata falha no LookAndFeel

---

## ▶️ Como Executar

### Pré-requisito
- **Java JDK 17 ou superior** instalado
- Verificar: `java -version`

### Via Terminal (linha de comando)

```bash
# 1. Entrar na pasta do projeto
cd TaskManager

# 2. Compilar todos os arquivos
javac -d out -sourcepath src src/taskmanager/Main.java

# 3. Executar
java -cp out taskmanager.Main
```

### Via IntelliJ IDEA
1. `File → Open` → selecione a pasta `TaskManager`
2. Marque `src` como Sources Root (clique direito → Mark Directory as → Sources Root)
3. Abra `Main.java` e clique no ▶️ verde

### Via Eclipse
1. `File → New → Java Project`
2. Desmarque "Use default location" e aponte para a pasta `TaskManager`
3. Clique em Run (`Ctrl+F11`)

### Via NetBeans
1. `File → New Project → Java with Existing Sources`
2. Aponte para a pasta `TaskManager`
3. `Run Project (F6)`

---

## 📁 Arquivo de dados

O sistema salva automaticamente em `taskmanager_data.txt` na pasta de execução.  
Os dados persistem entre sessões. Para resetar, basta apagar o arquivo.

---

## 👨‍💻 Autor

Projeto educacional — Programação Orientada a Objetos com Java Swing  
Desenvolvido como trabalho acadêmico
