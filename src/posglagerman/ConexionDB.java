package posglagerman;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class ConexionDB {

    private static final Logger logger = Logger.getLogger(ConexionDB.class.getName());

    private static final String DB_PATH = "base_datos/bd_pos_glagerman.db";
    private static final String DB_URL_PREFIX = "jdbc:sqlite:";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            // Registrar el error; no lanzar desde el bloque estático para no romper la carga de la clase.
            logger.log(Level.SEVERE, "SQLite JDBC driver no existe en la ruta.", ex);
        }
    }

    /**
     * Obtiene una conexión a la base de datos.
     * Lanza SQLException si algo falla (ruta inexistente, driver faltante o error al conectar).
     *
     * @return Connection lista para usar (el caller debe cerrarla).
     * @throws SQLException en caso de error.
     */
    public static Connection getConexion() throws SQLException {
        Path dbPath = Paths.get(DB_PATH).toAbsolutePath();

        if (!Files.exists(dbPath)) {
            String msg = "La base de datos no existe. Ruta: " + dbPath.toString();
            logger.severe(msg);
            throw new SQLException(msg);
        }

        String dbUrl = DB_URL_PREFIX + dbPath.toString();

        Connection cn = DriverManager.getConnection(dbUrl);

        // Aplicar PRAGMA recomendadas; si fallan, registrar pero devolver la conexión.
        try (Statement st = cn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
            st.execute("PRAGMA busy_timeout = 5000");
            st.execute("PRAGMA journal_mode = WAL");
            st.execute("PRAGMA synchronous = NORMAL");
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error aplicando PRAGMA en SQLite.", e);
        }

        return cn;
    }

    /**
     * Helper para código que espera comportamiento anterior (mostrar JOptionPane y devolver null).
     * Se recomienda migrar consumidores a usar getConexion() y manejar SQLException.
     *
     * @return Connection o null si hay error (y se mostró diálogo).
     */
    public static Connection getConexionOrShowDialog() {
        try {
            return getConexion();
        } catch (SQLException e) {
            // Solo mostrar error aquí
            JOptionPane.showMessageDialog(
                null,
                "Error al conectar con la base de datos:\n" + e.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE
            );
            logger.log(Level.SEVERE, "Error al abrir conexión.", e);
            return null;
        }
    }
}