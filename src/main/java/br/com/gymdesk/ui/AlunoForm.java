package br.com.gymdesk.ui;

import br.com.gymdesk.dao.PlanoDAO;
import br.com.gymdesk.model.Aluno;
import br.com.gymdesk.model.Plano;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AlunoForm extends JDialog {
    public Aluno aluno;
    private final JTextField txtNome = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtTelefone = new JTextField();
    private final JComboBox<Plano> cbPlano = new JComboBox<>();
    private final JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    public AlunoForm(Aluno existente) {
        setModal(true);
        setTitle(existente == null ? "Novo Aluno" : "Editar Aluno");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        int y=0;
        c.gridx=0; c.gridy=y; add(new JLabel("Nome*"), c);
        c.gridx=1; c.gridy=y++; add(txtNome, c);

        c.gridx=0; c.gridy=y; add(new JLabel("Email"), c);
        c.gridx=1; c.gridy=y++; add(txtEmail, c);

        c.gridx=0; c.gridy=y; add(new JLabel("Telefone"), c);
        c.gridx=1; c.gridy=y++; add(txtTelefone, c);

        c.gridx=0; c.gridy=y; add(new JLabel("Plano"), c);
        c.gridx=1; c.gridy=y++; add(cbPlano, c);

        c.gridx=1; c.gridy=y++; add(chkAtivo, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton salvar = new JButton("Salvar");
        JButton cancelar = new JButton("Cancelar");
        buttons.add(salvar); buttons.add(cancelar);

        c.gridx=0; c.gridy=y; c.gridwidth=2; add(buttons, c);

        carregarPlanos();

        if (existente != null) {
            this.aluno = existente;
            txtNome.setText(existente.nome);
            txtEmail.setText(existente.email);
            txtTelefone.setText(existente.telefone);
            chkAtivo.setSelected(existente.ativo);
            // selecionar plano
            if (existente.planoId != null) {
                for (int i=0;i<cbPlano.getItemCount();i++) {
                    Plano p = cbPlano.getItemAt(i);
                    if (p.id != null && p.id.equals(existente.planoId)) {
                        cbPlano.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } else {
            this.aluno = new Aluno();
            this.aluno.ativo = true;
        }

        salvar.addActionListener(e -> salvar());
        cancelar.addActionListener(e -> dispose());
    }

    private void carregarPlanos() {
        try {
            cbPlano.removeAllItems();
            List<Plano> planos = new PlanoDAO().listar();
            cbPlano.addItem(new Plano(null, "<sem plano>", 0));
            for (Plano p : planos) cbPlano.addItem(p);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar planos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) { JOptionPane.showMessageDialog(this, "Nome é obrigatório."); return; }
        aluno.nome = nome;
        aluno.email = txtEmail.getText().trim();
        aluno.telefone = txtTelefone.getText().trim();
        Plano p = (Plano) cbPlano.getSelectedItem();
        aluno.planoId = (p==null || p.id==null) ? null : p.id;
        aluno.ativo = chkAtivo.isSelected();
        dispose();
    }

    public boolean showDialog(Component parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
        return aluno != null && aluno.nome != null && !aluno.nome.isEmpty();
    }
}