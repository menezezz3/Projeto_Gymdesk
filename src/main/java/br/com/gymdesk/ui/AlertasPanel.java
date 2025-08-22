package br.com.gymdesk.ui;

import br.com.gymdesk.db.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class AlertasPanel extends JPanel {
    private final JTable tbSemPagamento = new JTable();
    private final JTable tbVencidos = new JTable();

    public AlertasPanel() {
        setLayout(new GridLayout(1,2,8,8));
        add(makeCard("Sem pagamento no mês corrente", tbSemPagamento));
        add(makeCard("Com vencimento vencido", tbVencidos));
        load();
    }

    private JPanel makeCard(String title, JTable table) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        table.setFillsViewportHeight(true);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton refresh = new JButton("Atualizar");
        refresh.addActionListener(e -> load());
        p.add(refresh, BorderLayout.SOUTH);
        return p;
    }

    private void load() {
        try (Connection c = Database.get()) {
            String ym = LocalDate.now().toString().substring(0,7);

            String sqlSemMes =
                "SELECT a.id, a.nome " +
                "  FROM alunos a " +
                " WHERE NOT EXISTS ( " +
                "    SELECT 1 FROM pagamentos p " +
                "     WHERE p.aluno_id = a.id " +
                "       AND substr(p.data_pagamento,1,7) = ? " +
                " ) " +
                " ORDER BY a.nome";

            PreparedStatement ps = c.prepareStatement(sqlSemMes);
            ps.setString(1, ym);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m1 = new DefaultTableModel(new String[]{"ID","Aluno"},0){
                @Override public boolean isCellEditable(int r,int c){return false;}
            };
            while (rs.next()) m1.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
            tbSemPagamento.setModel(m1);
            tbSemPagamento.getColumnModel().getColumn(0).setMaxWidth(60);

            String sqlVenc =
                "SELECT a.id, a.nome, MAX(p.vencimento) AS ultimo_venc " +
                "  FROM alunos a " +
                "  LEFT JOIN pagamentos p ON p.aluno_id = a.id " +
                "GROUP BY a.id, a.nome " +
                "HAVING ultimo_venc IS NOT NULL AND ultimo_venc < date('now') " +
                "ORDER BY ultimo_venc ASC";

            Statement st = c.createStatement();
            ResultSet rs2 = st.executeQuery(sqlVenc);
            DefaultTableModel m2 = new DefaultTableModel(new String[]{"ID","Aluno","Último vencimento"},0){
                @Override public boolean isCellEditable(int r,int c){return false;}
            };
            while (rs2.next()) m2.addRow(new Object[]{rs2.getInt(1), rs2.getString(2), rs2.getString(3)});
            tbVencidos.setModel(m2);
            tbVencidos.getColumnModel().getColumn(0).setMaxWidth(60);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao carregar alertas", JOptionPane.ERROR_MESSAGE);
        }
    }
}