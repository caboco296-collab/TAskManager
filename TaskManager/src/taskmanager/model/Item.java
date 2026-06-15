package taskmanager.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Classe abstrata base — HERANÇA
 * Todos os tipos de item (Tarefa, Meta) herdam desta classe.
 */
public abstract class Item {

    // ENCAPSULAMENTO: atributos privados com acesso via getters/setters
    private String id;
    private String titulo;
    private String descricao;
    private LocalDate dataCriacao;
    private boolean concluido;

    public Item(String titulo, String descricao) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser vazio.");
        }
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.titulo = titulo.trim();
        this.descricao = (descricao == null) ? "" : descricao.trim();
        this.dataCriacao = LocalDate.now();
        this.concluido = false;
    }

    // Método abstrato — POLIMORFISMO
    public abstract String getTipo();

    public abstract String getResumo();

    // Getters e Setters — ENCAPSULAMENTO
    public String getId()             { return id; }
    public String getTitulo()         { return titulo; }
    public String getDescricao()      { return descricao; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public boolean isConcluido()      { return concluido; }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser vazio.");
        }
        this.titulo = titulo.trim();
    }

    public void setDescricao(String descricao) {
        this.descricao = (descricao == null) ? "" : descricao.trim();
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    @Override
    public String toString() {
        return "[" + getTipo() + "] " + titulo + (concluido ? " ✓" : "");
    }
}
