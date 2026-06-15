package taskmanager.model;

/**
 * HERANÇA: Meta estende Item.
 * Representa uma meta de longo prazo com progresso percentual.
 */
public class Meta extends Item {

    private int progressoAtual;  // 0 a 100
    private String motivacao;

    public Meta(String titulo, String descricao, String motivacao) {
        super(titulo, descricao);
        this.progressoAtual = 0;
        this.motivacao = (motivacao == null) ? "" : motivacao.trim();
    }

    // POLIMORFISMO: sobrescrita de métodos abstratos
    @Override
    public String getTipo() {
        return "META";
    }

    @Override
    public String getResumo() {
        return getTitulo() + " | Progresso: " + progressoAtual + "% | " + motivacao;
    }

    public void avancarProgresso(int incremento) {
        if (incremento < 0) {
            throw new IllegalArgumentException("O incremento de progresso não pode ser negativo.");
        }
        this.progressoAtual = Math.min(100, this.progressoAtual + incremento);
        if (this.progressoAtual == 100) {
            setConcluido(true);
        }
    }

    // Getters e Setters — ENCAPSULAMENTO
    public int getProgressoAtual()  { return progressoAtual; }
    public String getMotivacao()    { return motivacao; }

    public void setProgressoAtual(int progresso) {
        if (progresso < 0 || progresso > 100) {
            throw new IllegalArgumentException("O progresso deve estar entre 0 e 100.");
        }
        this.progressoAtual = progresso;
        if (progresso == 100) setConcluido(true);
    }

    public void setMotivacao(String motivacao) {
        this.motivacao = (motivacao == null) ? "" : motivacao.trim();
    }
}
