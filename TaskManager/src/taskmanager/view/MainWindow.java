package taskmanager.view;

import taskmanager.controller.TaskController;
import taskmanager.model.Item;
import taskmanager.model.Meta;
import taskmanager.model.Tarefa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Janela principal do sistema — orquestra todos os painéis.
 */
public class MainWindow extends JFrame {

    private final TaskController controller;
    private DashboardPanel dashboardPanel;
    private ListaPanel listaPanel;

    public MainWindow(TaskController controller) {
        this.controller = controller;
        setTitle("✅ TaskManager — Gerenciador de Tarefas e Metas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 680);
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(null);
        construirUI();
        setVisible(true);
        verificarAtrasadas();
    }

    private void construirUI() {
        getContentPane().setBackground(new Color(30, 30, 46));
        setLayout(new BorderLayout(0, 0));

        // ── Cabeçalho ─────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(24, 24, 37));
        header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lblTitulo = new JLabel("✅ TaskManager");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(137, 180, 250));

        JLabel lblSub = new JLabel("Gerencie suas tarefas e metas pessoais");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(127, 132, 156));

        JPanel headerTexto = new JPanel(new GridLayout(2, 1));
        headerTexto.setOpaque(false);
        headerTexto.add(lblTitulo);
        headerTexto.add(lblSub);
        header.add(headerTexto, BorderLayout.WEST);

        // Botões de ação principais
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acoes.setOpaque(false);

        JButton btnNovaTarefa = criarBotao("➕ Nova Tarefa", new Color(137, 180, 250));
        JButton btnNovaMeta   = criarBotao("🎯 Nova Meta",   new Color(166, 227, 161));
        JButton btnAtualizar  = criarBotao("🔄 Atualizar",  new Color(180, 190, 254));

        btnNovaTarefa.addActionListener(e -> novaTarefa());
        btnNovaMeta.addActionListener(e -> novaMeta());
        btnAtualizar.addActionListener(e -> atualizarTudo());

        acoes.add(btnNovaTarefa);
        acoes.add(btnNovaMeta);
        acoes.add(btnAtualizar);
        header.add(acoes, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ── Dashboard ────────────────────────────────────────
        dashboardPanel = new DashboardPanel(controller);
        add(dashboardPanel, BorderLayout.CENTER);

        // ── Lista ────────────────────────────────────────────
        listaPanel = new ListaPanel(controller, this);
        listaPanel.setPreferredSize(new Dimension(900, 430));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, dashboardPanel, listaPanel);
        split.setDividerLocation(110);
        split.setDividerSize(4);
        split.setBackground(new Color(30, 30, 46));
        split.setBorder(null);

        add(split, BorderLayout.CENTER);

        // ── Status bar ───────────────────────────────────────
        JLabel statusBar = new JLabel("  Dica: selecione um item na tabela e use os botões Editar, Concluir ou Remover.");
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusBar.setForeground(new Color(108, 112, 134));
        statusBar.setBackground(new Color(24, 24, 37));
        statusBar.setOpaque(true);
        statusBar.setBorder(new EmptyBorder(4, 10, 4, 10));
        add(statusBar, BorderLayout.SOUTH);
    }

    // ── Ações ────────────────────────────────────────────────

    private void novaTarefa() {
        TarefaDialog dlg = new TarefaDialog(this, "Nova Tarefa", null);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) {
            try {
                controller.adicionarTarefa(
                        dlg.getTituloValor(),
                        dlg.getDescricaoValor(),
                        dlg.getPrazoValor(),
                        dlg.getPrioridadeValor(),
                        dlg.getCategoriaValor()
                );
                atualizarTudo();
                JOptionPane.showMessageDialog(this, "Tarefa adicionada com sucesso! 🎉",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void novaMeta() {
        MetaDialog dlg = new MetaDialog(this, "Nova Meta", null);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) {
            try {
                controller.adicionarMeta(
                        dlg.getTituloValor(),
                        dlg.getDescricaoValor(),
                        dlg.getMotivacaoValor()
                );
                atualizarTudo();
                JOptionPane.showMessageDialog(this, "Meta adicionada com sucesso! 🎯",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void editarItem(String id) {
        Item item = controller.buscarPorId(id);
        if (item == null) return;

        if (item instanceof Tarefa t) {
            TarefaDialog dlg = new TarefaDialog(this, "Editar Tarefa", t);
            dlg.setVisible(true);
            if (dlg.isConfirmado()) {
                try {
                    controller.editarTarefa(id,
                            dlg.getTituloValor(),
                            dlg.getDescricaoValor(),
                            dlg.getPrazoValor(),
                            dlg.getPrioridadeValor(),
                            dlg.getCategoriaValor());
                    atualizarTudo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (item instanceof Meta m) {
            MetaDialog dlg = new MetaDialog(this, "Editar Meta", m);
            dlg.setVisible(true);
            if (dlg.isConfirmado()) {
                try {
                    controller.atualizarProgressoMeta(id, dlg.getProgressoValor());
                    m.setTitulo(dlg.getTituloValor());
                    m.setDescricao(dlg.getDescricaoValor());
                    m.setMotivacao(dlg.getMotivacaoValor());
                    atualizarTudo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void atualizarTudo() {
        dashboardPanel.atualizar();
        listaPanel.atualizar();
    }

    private void verificarAtrasadas() {
        int atrasadas = controller.totalAtrasadas();
        if (atrasadas > 0) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Você tem " + atrasadas + " tarefa(s) com prazo vencido!",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(30, 30, 46));
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        return btn;
    }
}
