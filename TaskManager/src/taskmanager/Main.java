package taskmanager;

import taskmanager.controller.TaskController;
import taskmanager.view.MainWindow;

import javax.swing.*;

/**
 * Ponto de entrada da aplicação TaskManager.
 */
public class Main {

    public static void main(String[] args) {
        // Configura o Look and Feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Não foi possível aplicar LookAndFeel: " + e.getMessage());
        }

        // Executa na thread de eventos do Swing (boas práticas)
        SwingUtilities.invokeLater(() -> {
            TaskController controller = new TaskController();
            new MainWindow(controller);
        });
    }
}
