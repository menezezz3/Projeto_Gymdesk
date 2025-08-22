package br.com.gymdesk.service;

import br.com.gymdesk.db.Database;

import java.sql.*;
import java.util.*;

public class RelatorioService {

    public int totalAlunos() throws SQLException {
        try (Connection c = Database.get(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) AS n FROM alunos")) {
            return rs.next() ? rs.getInt("n") : 0;
        }
    }

    public double receitaMes(String anoMes) throws SQLException {
        String sql = "SELECT COALESCE(SUM(valor),0) AS total FROM pagamentos WHERE substr(data_pagamento,1,7)=?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, anoMes);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }
}