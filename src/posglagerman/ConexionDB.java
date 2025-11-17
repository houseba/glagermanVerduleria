package posglagerman;

import java.sql.*;
import java.io.File;
import javax.swing.JOptionPane;




/**
     public class ConexionDB {
    static final String DB_PATH = "base_datos/bd_pos_glagerman.db";
    static final String URL = "jdbc:sqlite:" + DB_PATH;
    static Connection conex;

    public static Connection getConexion() {
        if (conex == null) {
            // Verifica si el archivo existe
            File dbFile = new File(DB_PATH);
            if (!dbFile.exists()) {
                JOptionPane.showMessageDialog(null, "Error: La base de datos no existe.\nRuta: " + DB_PATH);
                return null;
            }
            try {
                conex = DriverManager.getConnection(URL);
                JOptionPane.showMessageDialog(null, "Conectado");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error de conexion");
            }
        }
        return conex;
    }
}
     */
// 

public class ConexionDB {
    
    private static final String DB_PATH = "base_datos/bd_pos_glagerman.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    // MÉTODO CORRECTO: SIEMPRE DEVOLVER UNA CONEXIÓN NUEVA
    public static Connection getConexion() {
        final String DB_URL = "jdbc:sqlite:base_datos/bd_pos_glagerman.db";
        try {
            // Carga explícita por si el auto-registro falla
            Class.forName("org.sqlite.JDBC");

            Connection cn = DriverManager.getConnection(DB_URL);
            try (Statement st = cn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
                st.execute("PRAGMA busy_timeout = 5000");  // espera 5s en lock
                st.execute("PRAGMA journal_mode = WAL");
                st.execute("PRAGMA synchronous = NORMAL");
            }
            return cn;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Falta el driver SQLite JDBC (org.xerial:sqlite-jdbc). Agrégalo al proyecto.", e);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo abrir la BD en: " + DB_URL + " -> " + e.getMessage(), e);
        }
    }
}