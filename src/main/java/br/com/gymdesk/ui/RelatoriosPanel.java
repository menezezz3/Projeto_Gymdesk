package br.com.gymdesk.ui;

import br.com.gymdesk.service.RelatorioService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class RelatoriosPanel extends JPanel {
    private final RelatorioService svc = new RelatorioService();
    private final JLabel lblAlunos = new JLabel("0");
    private final JLabel lblReceita = new JLabel("R$ 0,00");

    public RelatoriosPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JPanel card1 = makeCard("Total de alunos", lblAlunos);
        JPanel card2 = makeCard("Receita do mês", lblReceita);

        c.gridx=0; c.gridy=0; add(card1, c);
        c.gridx=1; c.gridy=0; add(card2, c);

        JButton btAtualizar = new JButton("Atualizar");
        btAtualizar.addActionListener(e -> load());
        c.gridx=0; c.gridy=1; c.gridwidth=2;
        add(btAtualizar, c);

        load();
    }

    private JPanel makeCard(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setFont(value.getFont().deriveFont(24f));
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private void load() {
        try {
            lblAlunos.setText(String.valueOf(svc.totalAlunos()));
            String ym = LocalDate.now().toString().substring(0,7);
            lblReceita.setText(String.format("R$ %.2f", svc.receitaMes(ym)));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}