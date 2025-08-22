package br.com.gymdesk.ui;

import br.com.gymdesk.dao.AlunoDAO;
import br.com.gymdesk.dao.PlanoDAO;
import br.com.gymdesk.model.Aluno;
import br.com.gymdesk.model.Plano;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class AlunosPanel extends JPanel {
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final PlanoDAO planoDAO = new PlanoDAO();

    private final JTable table = new JTable();
    private final JTextField txtBusca = new JTextField();
    private final JButton btNovo = new JButton("Novo");
    private final JButton btEditar = new JButton("Editar");
    private final JButton btExcluir = new JButton("Excluir");

    public AlunosPanel() {
        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new BorderLayout(8,8));
        top.add(new JLabel("Buscar por nome:"), BorderLayout.WEST);
        top.add(txtBusca, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btNovo);
        actions.add(btEditar);
        actions.add(btExcluir);
        add(actions, BorderLayout.SOUTH);

        txtBusca.getDocument().addDocumentListener((SimpleDocumentListener) e -> load());
        btNovo.addActionListener(e -> novo());
        btEditar.addActionListener(e -> editar());
        btExcluir.addActionListener(e -> excluir());

        load();
    }

    private void load() {
        try {
            String filtro = txtBusca.getText();
            List<Aluno> alunos = alunoDAO.listar(filtro);
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Nome","Email","Telefone","Plano","Ativo"},0) {
                @Override public boolean isCellEditable(int r,int c){ return false; }
            };
            var planos = planoDAO.listar();
            for (Aluno a : alunos) {
                String nomePlano = planos.stream().filter(p -> p.id != null && p.id.equals(a.planoId)).map(p -> p.nome).findFirst().orElse("");
                model.addRow(new Object[]{a.id, a.nome, a.email, a.telefone, nomePlano, a.ativo ? "Sim":"Não"});
            }
            table.setModel(model);
            table.getColumnModel().getColumn(0).setMaxWidth(60);
            table.getColumnModel().getColumn(5).setMaxWidth(80);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar alunos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer selectedId() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int modelRow = table.convertRowIndexToModel(row);
        return (Integer) table.getModel().getValueAt(modelRow, 0);
        }

    private void novo() {
        AlunoForm form = new AlunoForm(null);
        if (form.showDialog(this)) {
            try {
                alunoDAO.inserir(form.aluno);
                load();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao inserir", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editar() {
        Integer id = selectedId();
        if (id == null) { JOptionPane.showMessageDialog(this, "Selecione um aluno."); return; }
        // Carrega o registro para edição
        try {
            List<Aluno> lista = alunoDAO.listar("");
            Aluno a = lista.stream().filter(x -> x.id.equals(id)).findFirst().orElse(null);
            if (a == null) return;
            AlunoForm form = new AlunoForm(a);
            if (form.showDialog(this)) {
                alunoDAO.atualizar(form.aluno);
                load();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        Integer id = selectedId();
        if (id == null) { JOptionPane.showMessageDialog(this, "Selecione um aluno."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir o aluno selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                alunoDAO.excluir(id);
                load();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao excluir", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Document listener simplificado
    private interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
        void update(javax.swing.event.DocumentEvent e);
        @Override default void insertUpdate(javax.swing.event.DocumentEvent e){ update(e); }
        @Override default void removeUpdate(javax.swing.event.DocumentEvent e){ update(e); }
        @Override default void changedUpdate(javax.swing.event.DocumentEvent e){ update(e); }
    }
}