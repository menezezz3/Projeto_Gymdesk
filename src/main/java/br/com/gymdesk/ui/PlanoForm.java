package br.com.gymdesk.ui;

import br.com.gymdesk.model.Plano;

import javax.swing.*;
import java.awt.*;

public class PlanoForm extends JDialog {
    public Plano plano;
    private final JTextField txtNome = new JTextField();
    private final JFormattedTextField txtMensalidade = new JFormattedTextField(java.text.NumberFormat.getNumberInstance());

    public PlanoForm(Plano existente) {
        setModal(true);
        setTitle(existente == null ? "Novo Plano" : "Editar Plano");
        setSize(380, 200);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        int y=0;
        c.gridx=0; c.gridy=y; add(new JLabel("Nome*"), c);
        c.gridx=1; c.gridy=y++; add(txtNome, c);

        c.gridx=0; c.gridy=y; add(new JLabel("Mensalidade*"), c);
        c.gridx=1; c.gridy=y++; add(txtMensalidade, c);

        JButton salvar = new JButton("Salvar");
        JButton cancelar = new JButton("Cancelar");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(salvar); buttons.add(cancelar);
        c.gridx=0; c.gridy=y; c.gridwidth=2; add(buttons, c);

        if (existente != null) {
            this.plano = existente;
            txtNome.setText(existente.nome);
            txtMensalidade.setValue(existente.mensalidade);
        } else {
            this.plano = new Plano();
        }

        salvar.addActionListener(e -> salvar());
        cancelar.addActionListener(e -> dispose());
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) { JOptionPane.showMessageDialog(this, "Nome é obrigatório."); return; }
        Number n = (Number) txtMensalidade.getValue();
        if (n == null) { JOptionPane.showMessageDialog(this, "Mensalidade é obrigatória."); return; }
        plano.nome = nome;
        plano.mensalidade = n.doubleValue();
        dispose();
    }

    public boolean showDialog(Component parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
        return plano != null && plano.nome != null;
    }
}