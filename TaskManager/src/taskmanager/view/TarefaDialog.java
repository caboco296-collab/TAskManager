package taskmanager.view;

import taskmanager.model.Prioridade;
import taskmanager.model.Tarefa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Diálogo modal para criar ou editar uma Tarefa.
 * Contém validações com try-catch — TRATAMENTO DE ERROS.
 */
public class TarefaDialog extends JDialog {

    private boolean confirmado = false;

    private JTextField txtTitulo;
    private JTextArea  txtDescricao;
    private JTextField txtPrazo;
    private JComboBox<Prioridade> comboPrioridade;
    private JTextField txtCategoria;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TarefaDialog(Frame parent, String titulo, Tarefa tarefaExistente) {
        super(parent, titulo, true);
        setSize(480, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        construirUI(tarefaExistente);
    }

    private void construirUI(Tarefa t) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(30, 30, 46));
        painel.setBorder(new EmptyBorder(16, 20, 16, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Título
        addLabel(painel, gbc, "Título *", 0);
        txtTitulo = new JTextField(t != null ? t.getTitulo() : "");
        estilizarCampo(txtTitulo);
        addField(painel, gbc, txtTitulo, 0);

        // Descrição
        addLabel(painel, gbc, "Descrição", 1);
        txtDescricao = new JTextArea(t != null ? t.getDescricao() : "", 3, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescricao.setBackground(new Color(49, 50, 68));
        txtDescricao.setForeground(new Color(205, 214, 244));
        txtDescricao.setCaretColor(Color.WHITE);
        JScrollPane scroll = new JScrollPane(txtDescricao);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(88, 91, 112)));
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1;
        painel.add(scroll, gbc);

        // Prazo
        addLabel(painel, gbc, "Prazo (dd/MM/yyyy)", 2);
        txtPrazo = new JTextField(t != null && t.getPrazo() != null ? t.getPrazo().format(FMT) : "");
        estilizarCampo(txtPrazo);
        addField(painel, gbc, txtPrazo, 2);

        // Prioridade
        addLabel(painel, gbc, "Prioridade", 3);
        comboPrioridade = new JComboBox<>(Prioridade.values());
        comboPrioridade.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboPrioridade.setBackground(new Color(49, 50, 68));
        comboPrioridade.setForeground(new Color(205, 214, 244));
        if (t != null) comboPrioridade.setSelectedItem(t.getPrioridade());
        gbc.gridx = 1; gbc.gridy = 3;
        painel.add(comboPrioridade, gbc);

        // Categoria
        addLabel(painel, gbc, "Categoria", 4);
        txtCategoria = new JTextField(t != null ? t.getCategoria() : "Geral");
        estilizarCampo(txtCategoria);
        addField(painel, gbc, txtCategoria, 4);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setBackground(new Color(30, 30, 46));

        JButton btnCancelar = new JButton("Cancelar");
        estilizarBotao(btnCancelar, new Color(88, 91, 112));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnConfirmar = new JButton("Salvar");
        estilizarBotao(btnConfirmar, new Color(137, 180, 250));
        btnConfirmar.addActionListener(e -> validarESalvar());

        botoes.add(btnCancelar);
        botoes.add(btnConfirmar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        painel.add(botoes, gbc);

        setContentPane(painel);
        getRootPane().setDefaultButton(btnConfirmar);
    }

    private void validarESalvar() {
        // TRATAMENTO DE ERROS: validação de campos obrigatórios
        try {
            String titulo = txtTitulo.getText().trim();
            if (titulo.isEmpty()) {
                throw new IllegalArgumentException("O título da tarefa é obrigatório.");
            }
            String prazoTexto = txtPrazo.getText().trim();
            if (!prazoTexto.isEmpty()) {
                LocalDate.parse(prazoTexto, FMT); // lança DateTimeParseException se inválido
            }
            confirmado = true;
            dispose();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Data inválida! Use o formato dd/MM/yyyy.\nExemplo: 31/12/2025",
                    "Erro de validação", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getters para o Controller lê os valores
    public boolean isConfirmado()   { return confirmado; }
    public String  getTituloValor() { return txtTitulo.getText().trim(); }
    public String  getDescricaoValor(){ return txtDescricao.getText().trim(); }
    public Prioridade getPrioridadeValor(){ return (Prioridade) comboPrioridade.getSelectedItem(); }
    public String  getCategoriaValor(){ return txtCategoria.getText().trim(); }

    public LocalDate getPrazoValor() {
        String texto = txtPrazo.getText().trim();
        if (texto.isEmpty()) return null;
        try { return LocalDate.parse(texto, FMT); }
        catch (DateTimeParseException e) { return null; }
    }

    // Utilitários de estilo
    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(new Color(49, 50, 68));
        campo.setForeground(new Color(205, 214, 244));
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(88, 91, 112)),
                new EmptyBorder(4, 6, 4, 6)));
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(30, 30, 46));
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void addLabel(JPanel p, GridBagConstraints gbc, String texto, int row) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(166, 173, 200));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        p.add(lbl, gbc);
    }

    private void addField(JPanel p, GridBagConstraints gbc, JComponent comp, int row) {
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 1;
        p.add(comp, gbc);
    }
}
