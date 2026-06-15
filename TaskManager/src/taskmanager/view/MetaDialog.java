package taskmanager.view;

import taskmanager.model.Meta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Diálogo modal para criar ou editar uma Meta.
 * TRATAMENTO DE ERROS: validações com try-catch.
 */
public class MetaDialog extends JDialog {

    private boolean confirmado = false;
    private JTextField txtTitulo;
    private JTextArea  txtDescricao;
    private JTextField txtMotivacao;
    private JSpinner   spinnerProgresso;

    public MetaDialog(Frame parent, String tituloJanela, Meta metaExistente) {
        super(parent, tituloJanela, true);
        setSize(460, 380);
        setLocationRelativeTo(parent);
        setResizable(false);
        construirUI(metaExistente);
    }

    private void construirUI(Meta m) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(30, 30, 46));
        painel.setBorder(new EmptyBorder(16, 20, 16, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Título
        addLabel(painel, gbc, "Título *", 0);
        txtTitulo = new JTextField(m != null ? m.getTitulo() : "");
        estilizarCampo(txtTitulo);
        addField(painel, gbc, txtTitulo, 0);

        // Descrição
        addLabel(painel, gbc, "Descrição", 1);
        txtDescricao = new JTextArea(m != null ? m.getDescricao() : "", 3, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescricao.setBackground(new Color(49, 50, 68));
        txtDescricao.setForeground(new Color(205, 214, 244));
        txtDescricao.setCaretColor(Color.WHITE);
        JScrollPane scroll = new JScrollPane(txtDescricao);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(88, 91, 112)));
        gbc.gridx = 1; gbc.gridy = 1;
        painel.add(scroll, gbc);

        // Motivação
        addLabel(painel, gbc, "Motivação / Por quê?", 2);
        txtMotivacao = new JTextField(m != null ? m.getMotivacao() : "");
        estilizarCampo(txtMotivacao);
        addField(painel, gbc, txtMotivacao, 2);

        // Progresso
        addLabel(painel, gbc, "Progresso atual (%)", 3);
        SpinnerNumberModel spinModel = new SpinnerNumberModel(
                m != null ? m.getProgressoAtual() : 0, 0, 100, 5);
        spinnerProgresso = new JSpinner(spinModel);
        spinnerProgresso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 3;
        painel.add(spinnerProgresso, gbc);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setBackground(new Color(30, 30, 46));

        JButton btnCancelar = new JButton("Cancelar");
        estilizarBotao(btnCancelar, new Color(88, 91, 112));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnConfirmar = new JButton("Salvar");
        estilizarBotao(btnConfirmar, new Color(166, 227, 161));
        btnConfirmar.addActionListener(e -> validarESalvar());

        botoes.add(btnCancelar);
        botoes.add(btnConfirmar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        painel.add(botoes, gbc);

        setContentPane(painel);
        getRootPane().setDefaultButton(btnConfirmar);
    }

    private void validarESalvar() {
        try {
            String titulo = txtTitulo.getText().trim();
            if (titulo.isEmpty()) {
                throw new IllegalArgumentException("O título da meta é obrigatório.");
            }
            confirmado = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isConfirmado()      { return confirmado; }
    public String  getTituloValor()    { return txtTitulo.getText().trim(); }
    public String  getDescricaoValor() { return txtDescricao.getText().trim(); }
    public String  getMotivacaoValor() { return txtMotivacao.getText().trim(); }
    public int     getProgressoValor() { return (Integer) spinnerProgresso.getValue(); }

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
