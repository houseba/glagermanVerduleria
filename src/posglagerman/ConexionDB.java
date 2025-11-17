package posglagerman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

        try {
            File dbFile = new File(DB_PATH);

            if (!dbFile.exists()) {
                System.err.println("ERROR: Base de datos no encontrada: " + DB_PATH);
                return null;
            }

            // Siempre crea una nueva conexión
            return DriverManager.getConnection(URL);

        } catch (SQLException e) {
            System.err.println("ERROR de conexión: " + e.getMessage());
            return null;
        }
    }
}