package taskmanager.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * HERANÇA: Tarefa estende Item.
 * Representa uma tarefa com prazo e prioridade.
 */
public class Tarefa extends Item {

    private LocalDate prazo;
    private Prioridade prioridade;
    private String categoria;

    public Tarefa(String titulo, String descricao, LocalDate prazo, Prioridade prioridade, String categoria) {
        super(titulo, descricao);
        this.prazo = prazo;
        this.prioridade = (prioridade == null) ? Prioridade.MEDIA : prioridade;
        this.categoria = (categoria == null || categoria.trim().isEmpty()) ? "Geral" : categoria.trim();
    }

    // POLIMORFISMO: sobrescrita de métodos abstratos
    @Override
    public String getTipo() {
        return "TAREFA";
    }

    @Override
    public String getResumo() {
        String fmt = (prazo != null) ? prazo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Sem prazo";
        return getTitulo() + " | " + prioridade.getLabel() + " | Prazo: " + fmt + " | " + categoria;
    }

    public boolean isAtrasada() {
        return !isConcluido() && prazo != null && prazo.isBefore(LocalDate.now());
    }

    // Getters e Setters — ENCAPSULAMENTO
    public LocalDate getPrazo()       { return prazo; }
    public Prioridade getPrioridade() { return prioridade; }
    public String getCategoria()      { return categoria; }

    public void setPrazo(LocalDate prazo)           { this.prazo = prazo; }
    public void setPrioridade(Prioridade prioridade){ this.prioridade = (prioridade == null) ? Prioridade.MEDIA : prioridade; }
    public void setCategoria(String categoria)      { this.categoria = (categoria == null || categoria.trim().isEmpty()) ? "Geral" : categoria.trim(); }
}
