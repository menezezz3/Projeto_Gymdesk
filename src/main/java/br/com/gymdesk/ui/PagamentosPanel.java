package br.com.gymdesk.ui;

import br.com.gymdesk.dao.AlunoDAO;
import br.com.gymdesk.dao.PagamentoDAO;
import br.com.gymdesk.model.Aluno;
import br.com.gymdesk.model.Pagamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.sql.SQLException;

public class PagamentosPanel extends JPanel {
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final PagamentoDAO pagDAO = new PagamentoDAO();

    private final JComboBox<AlunoItem> cbAluno = new JComboBox<>();
    private final JTable table = new JTable();
    private final JButton btAdicionar = new JButton("Registrar Pagamento");

    public PagamentosPanel() {
        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Aluno:"));
        top.add(cbAluno);
        JButton btCarregar = new JButton("Carregar");
        top.add(btCarregar);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btAdicionar, BorderLayout.SOUTH);

        btCarregar.addActionListener(e -> load());
        btAdicionar.addActionListener(e -> registrar());

        carregarAlunos();
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

    private void load() {
        AlunoItem item = (AlunoItem) cbAluno.getSelectedItem();
        if (item == null) return;
        try {
            var lista = pagDAO.listarPorAluno(item.id());
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Valor","Pagamento","Vencimento","Status"},0) {
                @Override public boolean isCellEditable(int r,int c){ return false; }
            };
            for (Pagamento p : lista) model.addRow(new Object[]{p.id, String.format("R$ %.2f", p.valor), p.dataPagamento, p.vencimento, p.status});
            table.setModel(model);
            table.getColumnModel().getColumn(0).setMaxWidth(60);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrar() {
        AlunoItem item = (AlunoItem) cbAluno.getSelectedItem();
        if (item == null) { JOptionPane.showMessageDialog(this, "Selecione um aluno."); return; }
        String valorStr = JOptionPane.showInputDialog(this, "Valor pago (R$):", "0.00");
        if (valorStr == null) return;
        double valor;
        try { valor = Double.parseDouble(valorStr.replace(",", ".")); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Valor inválido."); return; }
        String venc = LocalDate.now().plusMonths(1).toString();
        Pagamento p = new Pagamento();
        p.alunoId = item.id();
        p.valor = valor;
        p.dataPagamento = LocalDate.now().toString();
        p.vencimento = venc;
        p.status = "pago";
        try {
            pagDAO.inserir(p);
            load();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private record AlunoItem(int id, String nome) {
        @Override public String toString() { return nome; }
    }
}