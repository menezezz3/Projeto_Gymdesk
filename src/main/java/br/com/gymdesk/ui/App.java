package br.com.gymdesk.ui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public App() {
        super("GymDesk — Academia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Alunos", new AlunosPanel());
        tabs.addTab("Planos", new PlanosPanel());
        tabs.addTab("Pagamentos", new PagamentosPanel());
        tabs.addTab("Check-in", new CheckinPanel());
        tabs.addTab("Relatórios", new RelatoriosPanel());

        setContentPane(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new App().setVisible(true);
        });
    }
}