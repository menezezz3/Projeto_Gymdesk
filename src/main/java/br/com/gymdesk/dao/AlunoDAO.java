package br.com.gymdesk.dao;

import br.com.gymdesk.db.Database;
import br.com.gymdesk.model.Aluno;

import java.sql.*;
import java.util.*;

public class AlunoDAO {

    public List<Aluno> listar(String filtro) throws SQLException {
        List<Aluno> out = new ArrayList<>();
        String sql = "SELECT id, nome, email, telefone, plano_id, ativo FROM alunos " +
                     "WHERE (? IS NULL OR nome LIKE ?) ORDER BY nome";
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (filtro == null || filtro.isBlank()) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
            } else {
                String like = "%" + filtro + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new Aluno(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    (Integer) rs.getObject("plano_id"),
                    rs.getInt("ativo") == 1
                ));
            }
        }
        return out;
    }

    public int inserir(Aluno a) throws SQLException {
        String sql = "INSERT INTO alunos (nome, email, telefone, plano_id, ativo) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.nome);
            ps.setString(2, a.email);
            ps.setString(3, a.telefone);
            if (a.planoId == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, a.planoId);
            ps.setInt(5, a.ativo ? 1 : 0);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        }
    }

    public void atualizar(Aluno a) throws SQLException {
        String sql = "UPDATE alunos SET nome=?, email=?, telefone=?, plano_id=?, ativo=? WHERE id=?";
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.nome);
            ps.setString(2, a.email);
            ps.setString(3, a.telefone);
            if (a.planoId == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, a.planoId);
            ps.setInt(5, a.ativo ? 1 : 0);
            ps.setInt(6, a.id);
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM alunos WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}