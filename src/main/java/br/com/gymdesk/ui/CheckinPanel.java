package br.com.gymdesk.ui;

import br.com.gymdesk.dao.AlunoDAO;
import br.com.gymdesk.dao.CheckinDAO;
import br.com.gymdesk.model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.SQLException;

public class CheckinPanel extends JPanel {
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final CheckinDAO checkinDAO = new CheckinDAO();
    private final JComboBox<AlunoItem> cbAluno = new JComboBox<>();
    private final JTable table = new JTable();

    public CheckinPanel() {
        setLayout(new BorderLayout(8,8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Aluno:"));
        top.add(cbAluno);
        JButton btReg = new JButton("Registrar Check-in");
        top.add(btReg);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btReg.addActionListener(e -> registrar());

        carregarAlunos();
        carregarRecentes();
    }

    private void carregarAlunos() {
        try {
            cbAluno.removeAllItems();
            List<Aluno> alunos = alunoDAO.listar("");
            for (Aluno a : alunos) cbAluno.addItem(new AlunoItem(a.id, a.nome));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarRecentes() {
        try {
            var lista = checkinDAO.listarRecentes(50);
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID","AlunoID","Data/Hora"},0) {
                @Override public boolean isCellEditable(int r,int c){ return false; }
            };
            for (var ch : lista) model.addRow(new Object[]{ch.id, ch.alunoId, ch.dataHora});
            table.setModel(model);
            table.getColumnModel().getColumn(0).setMaxWidth(60);
            table.getColumnModel().getColumn(1).setMaxWidth(80);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrar() {
        AlunoItem item = (AlunoItem) cbAluno.getSelectedItem();
        if (item == null) { JOptionPane.showMessageDialog(this, "Selecione um aluno."); return; }
        String now = LocalDateTime.now().toString();
        try {
            checkinDAO.registrar(item.id(), now);
            carregarRecentes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private record AlunoItem(int id, String nome) {
        @Override public String toString() { return nome; }
    }
}