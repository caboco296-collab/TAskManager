package taskmanager.view;

import taskmanager.controller.TaskController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Painel de resumo/dashboard exibido no topo da janela principal.
 */
public class DashboardPanel extends JPanel {

    private final TaskController controller;

    private JLabel lblTotal;
    private JLabel lblConcluidos;
    private JLabel lblPendentes;
    private JLabel lblAtrasadas;

    public DashboardPanel(TaskController controller) {
        this.controller = controller;
        setLayout(new GridLayout(1, 4, 10, 0));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(30, 30, 46));

        lblTotal      = criarCard("📋 Total",    "0", new Color(137, 180, 250));
        lblConcluidos = criarCard("✅ Concluídos","0", new Color(166, 227, 161));
        lblPendentes  = criarCard("⏳ Pendentes", "0", new Color(250, 179, 135));
        lblAtrasadas  = criarCard("🚨 Atrasadas", "0", new Color(243, 139, 168));

        atualizar();
    }

    private JLabel criarCard(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(new Color(49, 50, 68));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cor, 2, true),
                new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitulo.setForeground(new Color(205, 214, 244));

        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(cor);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        add(card);
        return lblValor;
    }

    public void atualizar() {
        lblTotal.setText(String.valueOf(controller.totalItens()));
        lblConcluidos.setText(String.valueOf(controller.totalConcluidos()));
        lblPendentes.setText(String.valueOf(controller.totalPendentes()));
        lblAtrasadas.setText(String.valueOf(controller.totalAtrasadas()));
    }
}
