package taskmanager.controller;

import taskmanager.model.Item;
import taskmanager.model.Meta;
import taskmanager.model.Prioridade;
import taskmanager.model.Tarefa;
import taskmanager.util.ArquivoUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller principal — gerencia toda a lógica de negócio.
 * ENCAPSULAMENTO: lista privada; acesso apenas por métodos públicos.
 */
public class TaskController {

    private final List<Item> itens;
    private static final String ARQUIVO = "taskmanager_data.txt";

    public TaskController() {
        this.itens = new ArrayList<>();
        carregarDoArquivo();
    }

    // ── Tarefas ──────────────────────────────────────────────

    public Tarefa adicionarTarefa(String titulo, String descricao,
                                  LocalDate prazo, Prioridade prioridade, String categoria) {
        Tarefa t = new Tarefa(titulo, descricao, prazo, prioridade, categoria);
        itens.add(t);
        salvarNoArquivo();
        return t;
    }

    public void editarTarefa(String id, String titulo, String descricao,
                             LocalDate prazo, Prioridade prioridade, String categoria) {
        Tarefa t = (Tarefa) buscarPorId(id);
        if (t == null) throw new IllegalArgumentException("Tarefa não encontrada: " + id);
        t.setTitulo(titulo);
        t.setDescricao(descricao);
        t.setPrazo(prazo);
        t.setPrioridade(prioridade);
        t.setCategoria(categoria);
        salvarNoArquivo();
    }

    // ── Metas ────────────────────────────────────────────────

    public Meta adicionarMeta(String titulo, String descricao, String motivacao) {
        Meta m = new Meta(titulo, descricao, motivacao);
        itens.add(m);
        salvarNoArquivo();
        return m;
    }

    public void atualizarProgressoMeta(String id, int progresso) {
        Meta m = (Meta) buscarPorId(id);
        if (m == null) throw new IllegalArgumentException("Meta não encontrada: " + id);
        m.setProgressoAtual(progresso);
        salvarNoArquivo();
    }

    // ── Operações gerais ─────────────────────────────────────

    public void concluirItem(String id) {
        Item item = buscarPorId(id);
        if (item == null) throw new IllegalArgumentException("Item não encontrado: " + id);
        item.setConcluido(true);
        salvarNoArquivo();
    }

    public void removerItem(String id) {
        boolean removido = itens.removeIf(i -> i.getId().equals(id));
        if (!removido) throw new IllegalArgumentException("Item não encontrado: " + id);
        salvarNoArquivo();
    }

    public Item buscarPorId(String id) {
        return itens.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }

    // ── Consultas — POLIMORFISMO via getTipo() ────────────────

    public List<Item> listarTodos() {
        return new ArrayList<>(itens);
    }

    public List<Tarefa> listarTarefas() {
        return itens.stream()
                .filter(i -> i instanceof Tarefa)
                .map(i -> (Tarefa) i)
                .collect(Collectors.toList());
    }

    public List<Meta> listarMetas() {
        return itens.stream()
                .filter(i -> i instanceof Meta)
                .map(i -> (Meta) i)
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasAtrasadas() {
        return listarTarefas().stream()
                .filter(Tarefa::isAtrasada)
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasPorPrioridade() {
        return listarTarefas().stream()
                .sorted(Comparator.comparing(Tarefa::getPrioridade).reversed())
                .collect(Collectors.toList());
    }

    public List<Item> pesquisar(String termo) {
        if (termo == null || termo.trim().isEmpty()) return listarTodos();
        String t = termo.toLowerCase();
        return itens.stream()
                .filter(i -> i.getTitulo().toLowerCase().contains(t)
                          || i.getDescricao().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    // ── Estatísticas ─────────────────────────────────────────

    public int totalItens()     { return itens.size(); }
    public int totalConcluidos(){ return (int) itens.stream().filter(Item::isConcluido).count(); }
    public int totalPendentes() { return totalItens() - totalConcluidos(); }
    public int totalAtrasadas() { return listarTarefasAtrasadas().size(); }

    // ── Persistência em arquivo ───────────────────────────────

    private void salvarNoArquivo() {
        try {
            ArquivoUtil.salvar(ARQUIVO, itens);
        } catch (Exception e) {
            System.err.println("Aviso: não foi possível salvar dados. " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try {
            List<Item> carregados = ArquivoUtil.carregar(ARQUIVO);
            itens.addAll(carregados);
        } catch (Exception e) {
            System.err.println("Aviso: dados anteriores não encontrados. Iniciando do zero.");
        }
    }
}
