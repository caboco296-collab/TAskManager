package taskmanager.view;

import taskmanager.controller.TaskController;
import taskmanager.model.Item;
import taskmanager.model.Meta;
import taskmanager.model.Tarefa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel com tabela de itens e barra de pesquisa.
 */
public class ListaPanel extends JPanel {

    private final TaskController controller;
    private final MainWindow mainWindow;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtPesquisa;
    private JComboBox<String> comboFiltro;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String[] COLUNAS = {
            "ID", "Tipo", "Título", "Status", "Detalhe", "Prazo/Progresso"
    };

    public ListaPanel(TaskController controller, MainWindow mainWindow) {
        this.controller = controller;
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(0, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(30, 30, 46));
        construirUI();
    }

    private void construirUI() {
        // Barra de filtros
        JPanel barraFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barraFiltros.setBackground(new Color(30, 30, 46));

        JLabel lblPesquisa = new JLabel("🔍 Pesquisar:");
        lblPesquisa.setForeground(new Color(205, 214, 244));
        lblPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtPesquisa = new JTextField(20);
        estilizarCampo(txtPesquisa);
        txtPesquisa.addActionListener(e -> atualizar());

        JButton btnPesquisar = criarBotao("Buscar", new Color(137, 180, 250));
        btnPesquisar.addActionListener(e -> atualizar());

        comboFiltro = new JComboBox<>(new String[]{
                "Todos", "Apenas Tarefas", "Apenas Metas", "Pendentes", "Concluídos", "Atrasadas"
        });
        comboFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboFiltro.addActionListener(e -> atualizar());

        barraFiltros.add(lblPesquisa);
        barraFiltros.add(txtPesquisa);
        barraFiltros.add(btnPesquisar);
        barraFiltros.add(Box.createHorizontalStrut(20));
        barraFiltros.add(new JLabel("Filtro:") {{
            setForeground(new Color(205, 214, 244));
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }});
        barraFiltros.add(comboFiltro);

        add(barraFiltros, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        configurarTabela();

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBackground(new Color(30, 30, 46));
        scroll.getViewport().setBackground(new Color(30, 30, 46));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(88, 91, 112)));
        add(scroll, BorderLayout.CENTER);

        // Barra de ações
        JPanel barraAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraAcoes.setBackground(new Color(30, 30, 46));

        JButton btnEditar   = criarBotao("✏️ Editar",    new Color(250, 179, 135));
        JButton btnConcluir = criarBotao("✅ Concluir",  new Color(166, 227, 161));
        JButton btnRemover  = criarBotao("🗑️ Remover",  new Color(243, 139, 168));

        btnEditar.addActionListener(e -> editarSelecionado());
        btnConcluir.addActionListener(e -> concluirSelecionado());
        btnRemover.addActionListener(e -> removerSelecionado());

        barraAcoes.add(btnEditar);
        barraAcoes.add(btnConcluir);
        barraAcoes.add(btnRemover);
        add(barraAcoes, BorderLayout.SOUTH);
    }

    private void configurarTabela() {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setBackground(new Color(36, 36, 54));
        tabela.setForeground(new Color(205, 214, 244));
        tabela.setSelectionBackground(new Color(88, 91, 112));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(49, 50, 68));
        tabela.setRowHeight(28);
        tabela.setShowVerticalLines(false);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(new Color(49, 50, 68));
        tabela.getTableHeader().setForeground(new Color(205, 214, 244));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Renderizador com cores por status/tipo
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(isSelected ? new Color(88, 91, 112) : new Color(36, 36, 54));
                setForeground(new Color(205, 214, 244));

                if (!isSelected) {
                    String status = (String) table.getValueAt(row, 3);
                    if ("✅ Concluído".equals(status)) {
                        setForeground(new Color(166, 227, 161));
                    } else if ("🚨 Atrasada".equals(status)) {
                        setForeground(new Color(243, 139, 168));
                    }
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
        for (int i = 0; i < COLUNAS.length; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Larguras das colunas
        int[] larguras = {80, 80, 200, 100, 150, 110};
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    // POLIMORFISMO: usa getTipo() e getResumo() de Item para preencher a tabela
    public void atualizar() {
        modeloTabela.setRowCount(0);
        String pesquisa = txtPesquisa.getText().trim();
        String filtro   = (String) comboFiltro.getSelectedItem();

        List<Item> lista = controller.pesquisar(pesquisa);

        for (Item item : lista) {
            if (!passaFiltro(item, filtro)) continue;

            String status;
            if (item.isConcluido()) {
                status = "✅ Concluído";
            } else if (item instanceof Tarefa t && t.isAtrasada()) {
                status = "🚨 Atrasada";
            } else {
                status = "⏳ Pendente";
            }

            String detalhe = "";
            String prazoOuProgresso = "";

            if (item instanceof Tarefa t) {
                detalhe = t.getPrioridade().getLabel() + " | " + t.getCategoria();
                prazoOuProgresso = t.getPrazo() != null ? t.getPrazo().format(FMT) : "Sem prazo";
            } else if (item instanceof Meta m) {
                detalhe = "🎯 " + (m.getMotivacao().isEmpty() ? "—" : m.getMotivacao());
                prazoOuProgresso = m.getProgressoAtual() + "%";
            }

            modeloTabela.addRow(new Object[]{
                    item.getId(),
                    item.getTipo(),
                    item.getTitulo(),
                    status,
                    detalhe,
                    prazoOuProgresso
            });
        }
    }

    private boolean passaFiltro(Item item, String filtro) {
        return switch (filtro) {
            case "Apenas Tarefas" -> item instanceof Tarefa;
            case "Apenas Metas"   -> item instanceof Meta;
            case "Pendentes"      -> !item.isConcluido();
            case "Concluídos"     -> item.isConcluido();
            case "Atrasadas"      -> item instanceof Tarefa t && t.isAtrasada();
            default -> true;
        };
    }

    private String getIdSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) return null;
        return (String) modeloTabela.getValueAt(row, 0);
    }

    private void editarSelecionado() {
        String id = getIdSelecionado();
        if (id == null) {
            mostrarAviso("Selecione um item na tabela para editar.");
            return;
        }
        mainWindow.editarItem(id);
    }

    private void concluirSelecionado() {
        String id = getIdSelecionado();
        if (id == null) {
            mostrarAviso("Selecione um item na tabela para concluir.");
            return;
        }
        try {
            controller.concluirItem(id);
            mainWindow.atualizarTudo();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerSelecionado() {
        String id = getIdSelecionado();
        if (id == null) {
            mostrarAviso("Selecione um item na tabela para remover.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover este item?", "Confirmação",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp != JOptionPane.YES_OPTION) return;
        try {
            controller.removerItem(id);
            mainWindow.atualizarTudo();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarAviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(new Color(30, 30, 46));
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(new Color(49, 50, 68));
        campo.setForeground(new Color(205, 214, 244));
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(88, 91, 112)),
                new EmptyBorder(4, 6, 4, 6)));
    }
}
