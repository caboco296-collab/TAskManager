package taskmanager.model;

/**
 * Enum que representa os níveis de prioridade de uma Tarefa.
 */
public enum Prioridade {
    BAIXA("🟢 Baixa"),
    MEDIA("🟡 Média"),
    ALTA("🔴 Alta");

    private final String label;

    Prioridade(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
