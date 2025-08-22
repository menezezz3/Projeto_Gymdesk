package br.com.gymdesk.db;

import java.sql.*;
import java.nio.file.*;
import java.io.*;
import java.util.stream.Collectors;

public class Database {
    private static final String DB_FILE = "gymdesk.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_FILE;

    static {
        try {
            ensureSchema();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao inicializar o banco: " + e.getMessage(), e);
        }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    private static void ensureSchema() throws Exception {
        // Carrega schema.sql do resources
        InputStream in = Database.class.getClassLoader().getResourceAsStream("schema.sql");
        if (in == null) throw new FileNotFoundException("schema.sql não encontrado");
        String sql;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            sql = br.lines().collect(Collectors.joining("\n"));
        }
        try (Connection c = get(); Statement st = c.createStatement()) {
            for (String s : sql.split(";")) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) st.execute(trimmed);
            }
        }
    }
}