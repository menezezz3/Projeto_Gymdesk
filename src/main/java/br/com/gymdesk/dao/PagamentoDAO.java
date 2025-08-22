package br.com.gymdesk.dao;

import br.com.gymdesk.db.Database;
import br.com.gymdesk.model.Pagamento;

import java.sql.*;
import java.util.*;

public class PagamentoDAO {

    public List<Pagamento> listarPorAluno(int alunoId) throws SQLException {
        List<Pagamento> out = new ArrayList<>();
        String sql = "SELECT id, aluno_id, valor, data_pagamento, vencimento, status FROM pagamentos WHERE aluno_id=? ORDER BY vencimento DESC";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, alunoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pagamento p = new Pagamento();
                p.id = rs.getInt("id");
                p.alunoId = rs.getInt("aluno_id");
                p.valor = rs.getDouble("valor");
                p.dataPagamento = rs.getString("data_pagamento");
                p.vencimento = rs.getString("vencimento");
                p.status = rs.getString("status");
                out.add(p);
            }
        }
        return out;
    }

    public int inserir(Pagamento p) throws SQLException {
        String sql = "INSERT INTO pagamentos (aluno_id, valor, data_pagamento, vencimento, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.alunoId);
            ps.setDouble(2, p.valor);
            ps.setString(3, p.dataPagamento);
            ps.setString(4, p.vencimento);
            ps.setString(5, p.status);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        }
    }
}