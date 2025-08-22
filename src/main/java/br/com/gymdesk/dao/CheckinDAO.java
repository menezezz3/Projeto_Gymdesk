package br.com.gymdesk.dao;

import br.com.gymdesk.db.Database;
import br.com.gymdesk.model.Checkin;

import java.sql.*;
import java.util.*;

public class CheckinDAO {

    public void registrar(int alunoId, String dataHora) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("INSERT INTO checkins (aluno_id, data_hora) VALUES (?, ?)")) {
            ps.setInt(1, alunoId);
            ps.setString(2, dataHora);
            ps.executeUpdate();
        }
    }

    public List<Checkin> listarRecentes(int limit) throws SQLException {
        List<Checkin> out = new ArrayList<>();
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT id, aluno_id, data_hora FROM checkins ORDER BY data_hora DESC LIMIT ?")) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Checkin ch = new Checkin();
                ch.id = rs.getInt("id");
                ch.alunoId = rs.getInt("aluno_id");
                ch.dataHora = rs.getString("data_hora");
                out.add(ch);
            }
        }
        return out;
    }
}