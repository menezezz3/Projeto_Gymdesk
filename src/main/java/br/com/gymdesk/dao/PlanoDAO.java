package br.com.gymdesk.dao;

import br.com.gymdesk.db.Database;
import br.com.gymdesk.model.Plano;

import java.sql.*;
import java.util.*;

public class PlanoDAO {

    public List<Plano> listar() throws SQLException {
        List<Plano> out = new ArrayList<>();
        try (Connection c = Database.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nome, mensalidade FROM planos ORDER BY nome")) {
            while (rs.next()) {
                out.add(new Plano(rs.getInt("id"), rs.getString("nome"), rs.getDouble("mensalidade")));
            }
        }
        return out;
    }

    public int inserir(Plano p) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO planos (nome, mensalidade) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.nome);
            ps.setDouble(2, p.mensalidade);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        }
    }

    public void atualizar(Plano p) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE planos SET nome=?, mensalidade=? WHERE id=?")) {
            ps.setString(1, p.nome);
            ps.setDouble(2, p.mensalidade);
            ps.setInt(3, p.id);
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM planos WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}