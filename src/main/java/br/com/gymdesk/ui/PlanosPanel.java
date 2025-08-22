package br.com.gymdesk.ui;

import br.com.gymdesk.dao.PlanoDAO;
import br.com.gymdesk.model.Plano;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PlanosPanel extends JPanel {
    private final PlanoDAO dao = new PlanoDAO();
    private final JTable table = new JTable();
    private final JButton btNovo = new JButton("Novo");
    private final JButton btEditar = new JButton("Editar");
    private final JButton btExcluir = new JButton("Excluir");

    public PlanosPanel() {
        setLayout(new BorderLayout(8,8));
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(btNovo); actions.add(btEditar); actions.add(btExcluir);
        add(actions, BorderLayout.SOUTH);

        btNovo.addActionListener(e -> novo());
        btEditar.addActionListener(e -> editar());
        btExcluir.addActionListener(e -> excluir());

        load();
    }

    private void load() {
        try {
            List<Plano> planos = dao.listar();
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Nome","Mensalidade"},0) {
                @Override public boolean isCellEditable(int r,int c){ return false; }
            };
            for (Plano p : planos) model.addRow(new Object[]{p.id, p.nome, String.format("R$ %.2f", p.mensalidade)});
            table.setModel(model);
            table.getColumnModel().getColumn(0).setMaxWidth(60);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer selectedId() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int modelRow = table.convertRowIndexToModel(row);
        return (Integer) table.getModel().getValueAt(modelRow, 0);
    }

    private void novo() {
        PlanoForm f = new PlanoForm(null);
        if (f.showDialog(this)) {
            try { 
                dao.inserir(f.plano); 
                load();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editar() {
        Integer id = selectedId();
        if (id == null) { JOptionPane.showMessageDialog(this, "Selecione um plano."); return; }
        try {
            Plano atual = dao.listar().stream().filter(p -> p.id.equals(id)).findFirst().orElse(null);
            if (atual == null) return;
            PlanoForm f = new PlanoForm(atual);
            if (f.showDialog(this)) {
                dao.atualizar(f.plano);
                load();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        Integer id = selectedId();
        if (id == null) { JOptionPane.showMessageDialog(this, "Selecione um plano."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir o plano selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { dao.excluir(id); load(); }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}