<div align="center">

<img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
<img src="https://img.shields.io/badge/Java%20Swing-GUI-4A90D9?style=for-the-badge&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/POO-Orientado%20a%20Objetos-6A0DAD?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Status-Conclu%C3%ADdo-27AE60?style=for-the-badge"/>

<br/><br/>

# ✅ TaskManager
### Gerenciador de Tarefas e Metas Pessoais

*Transformando o esquecimento em organização — um projeto Java completo com interface gráfica*

</div>

---

## 🏫 Informações Acadêmicas

| Campo | Informação |
|---|---|
| 🏛️ Instituição | EEEP Paulo Petrola |
| 📚 Curso | Análise e Desenvolvimento de Sistemas |
| 👨‍💻 Autor | Kaique Wesley Oliveira da Silva |
| 🛠️ Disciplina | Programação Orientada a Objetos |
| 📅 Ano | 2025 |

---

## 📌 Sobre o Projeto

O **TaskManager** é um sistema desktop desenvolvido em **Java** com interface gráfica **Swing** que resolve um problema real do cotidiano: o esquecimento de tarefas e a dificuldade de acompanhar metas pessoais.

Com ele é possível criar tarefas com prazo e prioridade, definir metas com progresso percentual, receber alertas de atrasos e pesquisar itens — tudo isso **sem internet e sem banco de dados**, com dados salvos localmente.

---

## 🎯 Problema Resolvido

> *"Quantas vezes você esqueceu uma tarefa importante ou perdeu um prazo por falta de organização?"*

Aplicativos online exigem cadastro, internet e muitas vezes são complexos demais para o uso diário. O TaskManager oferece uma solução **simples, offline e imediata** para organizar o dia a dia.

---

## 🧱 Conceitos de POO Aplicados

| Conceito | Onde foi aplicado |
|---|---|
| 🔵 **Herança** | `Tarefa` e `Meta` herdam da classe abstrata `Item` |
| 🟡 **Encapsulamento** | Todos os atributos são `private` com getters/setters validados |
| 🟠 **Polimorfismo** | `getTipo()` e `getResumo()` são sobrescritos em cada subclasse |
| 🔴 **Tratamento de Erros** | `try-catch` em validações de data, título e operações de arquivo |

---

## 🗂️ Estrutura do Projeto

```
TaskManager/
└── src/taskmanager/
    ├── Main.java                  ← Ponto de entrada
    ├── model/
    │   ├── Item.java              ← Classe abstrata base
    │   ├── Tarefa.java            ← Herda de Item
    │   ├── Meta.java              ← Herda de Item
    │   └── Prioridade.java        ← Enum de prioridades
    ├── controller/
    │   └── TaskController.java    ← Lógica de negócio
    ├── view/
    │   ├── MainWindow.java        ← Janela principal
    │   ├── DashboardPanel.java    ← Painel de estatísticas
    │   ├── ListaPanel.java        ← Tabela com filtros
    │   ├── TarefaDialog.java      ← Formulário de tarefa
    │   └── MetaDialog.java        ← Formulário de meta
    └── util/
        └── ArquivoUtil.java       ← Persistência em arquivo
```

---

## ⚙️ Funcionalidades

- ➕ Criar, editar e remover tarefas e metas
- 📅 Definir prazo e prioridade (Alta / Média / Baixa)
- 📊 Dashboard com total, concluídos, pendentes e atrasados
- 🔍 Pesquisa por título ou descrição
- 🔎 Filtros por tipo, status e atraso
- 🚨 Alerta automático de tarefas com prazo vencido
- 💾 Dados salvos automaticamente em arquivo local

---

## 🛠️ Tecnologias Utilizadas

<div align="center">

<img src="https://img.shields.io/badge/Java%2017-ED8B00?style=flat-square&logo=openjdk&logoColor=white"/>
<img src="https://img.shields.io/badge/Java%20Swing-4A90D9?style=flat-square"/>
<img src="https://img.shields.io/badge/Java%20NIO-File%20I%2FO-lightgrey?style=flat-square"/>
<img src="https://img.shields.io/badge/Padr%C3%A3o%20MVC-arquitectura-blueviolet?style=flat-square"/>

</div>

---

## ▶️ Como Executar

**Pré-requisito:** Java 17 ou superior instalado.

```bash
# 1. Compilar
javac -d out -sourcepath src src/taskmanager/Main.java

# 2. Executar
java -cp out taskmanager.Main
```

Ou use o script incluído no projeto:

```bash
# Linux / Mac
./compilar_e_executar.sh

# Windows
compilar_e_executar.bat
```

---

## 📄 Licença

Projeto desenvolvido para fins acadêmicos — **EEEP Paulo Petrola**.

---

<div align="center">

Desenvolvido por **Kaique Wesley Oliveira da Silva** &nbsp;|&nbsp; EEEP Paulo Petrola

</div>
