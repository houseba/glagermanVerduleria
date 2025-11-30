package posglagerman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import javax.swing.JOptionPane;

public class ConexionDB {

    private static final String DB_PATH = "base_datos/bd_pos_glagerman.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConexion() {

        // 1. Verificar que el archivo exista
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            JOptionPane.showMessageDialog(null,
                "❌ Error: La base de datos no existe.\nRuta: " + DB_PATH);
            return null;
        }

        try {
            // 2. Cargar driver SQLite (seguro)
            Class.forName("org.sqlite.JDBC");

            // 3. Crear la conexión SIEMPRE NUEVA (lo correcto en SQLite)
            Connection cn = DriverManager.getConnection(DB_URL);

            // 4. Configuración recomendada para SQLite
            try (Statement st = cn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
                st.execute("PRAGMA busy_timeout = 5000");
                st.execute("PRAGMA journal_mode = WAL");
                st.execute("PRAGMA synchronous = NORMAL");
            }

            //JOptionPane.showMessageDialog(null, "Conectado correctamente"); // si lo quieres mostrar
            return cn;

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error: Falta el driver SQLite JDBC.\n" + e.getMessage());
            return null;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error al conectar con la base de datos:\n" + e.getMessage());
            return null;
        }
    }

    public static Connection connect() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}