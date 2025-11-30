/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views.administrar_inventario;

import javax.swing.table.DefaultTableModel;
import posglagerman.ConexionDB;
import views.InicioPage;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author conta
 */
public class AdministrarCategoriasPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdministrarCategoriasPage.class.getName());

    /**
     * Creates new form AdministrarCategoriasPage
     */
    public AdministrarCategoriasPage() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        configurarTablaCategorias();
        cargarCategorias();
    }

    private void configurarTablaCategorias() {
        // Modelo con ID oculto + nombre + cantidad de productos
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Cantidad de productos"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla solo lectura
            }
        };

        tblCategorias.setModel(model);

        // Ocultar columna ID (columna 0)
        tblCategorias.getColumnModel().getColumn(0).setMinWidth(0);
        tblCategorias.getColumnModel().getColumn(0).setMaxWidth(0);
        tblCategorias.getColumnModel().getColumn(0).setWidth(0);
    }
    private void cargarCategorias() {
        DefaultTableModel model = (DefaultTableModel) tblCategorias.getModel();
        model.setRowCount(0); // limpiar

        String sql =
            "SELECT c.id_categoria, c.nombre_categoria, " +
            "COUNT(p.cod_producto) AS cantidad_productos " +
            "FROM categoria c " +
            "LEFT JOIN Producto p ON p.id_categoria = c.id_categoria " +
            "GROUP BY c.id_categoria, c.nombre_categoria " +
            "ORDER BY c.nombre_categoria";

        try (Connection conex = ConexionDB.getConexion();
            PreparedStatement ps = conex.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idCategoria = rs.getInt("id_categoria");
                String nombre = rs.getString("nombre_categoria");
                int cantidadProd = rs.getInt("cantidad_productos");

                model.addRow(new Object[]{idCategoria, nombre, cantidadProd});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar categorías: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void agregarCategoria() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un nombre de categoría.");
            return;
        }

        // Validar que no exista la categoría
        String sqlCheck = "SELECT COUNT(*) FROM categoria WHERE nombre_categoria = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {

            psCheck.setString(1, nombre);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Ya existe una categoría con ese nombre.");
                    return;
                }
            }

            String sqlInsert = "INSERT INTO categoria (nombre_categoria) VALUES (?)";
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setString(1, nombre);
                psInsert.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Categoría agregada correctamente.");
            txtNombre.setText("");
            cargarCategorias();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void renombrarCategoria() {
        int fila = tblCategorias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una categoría de la tabla.");
            return;
        }

        String nuevoNombre = txtNombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar el nuevo nombre de la categoría.");
            return;
        }

        // ID oculto está en la columna 0
        int modelRow = tblCategorias.convertRowIndexToModel(fila);
        int idCategoria = (int) tblCategorias.getModel().getValueAt(modelRow, 0);

        // Validar que no exista otra categoría con ese nombre
        String sqlCheck = "SELECT COUNT(*) FROM categoria " +
            "WHERE nombre_categoria = ? AND id_categoria <> ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {

            psCheck.setString(1, nuevoNombre);
            psCheck.setInt(2, idCategoria);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Ya existe otra categoría con ese nombre.");
                    return;
                }
            }

            String sqlUpdate = "UPDATE categoria SET nombre_categoria = ? WHERE id_categoria = ?";

            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                psUpdate.setString(1, nuevoNombre);
                psUpdate.setInt(2, idCategoria);
                psUpdate.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Categoría actualizada correctamente.");
            cargarCategorias();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al renombrar categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void eliminarCategoria() {
        int fila = tblCategorias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una categoría de la tabla.");
            return;
        }

        int modelRow = tblCategorias.convertRowIndexToModel(fila);
        int idCategoria = (int) tblCategorias.getModel().getValueAt(modelRow, 0); // col 0 = ID
        String nombre = tblCategorias.getModel().getValueAt(modelRow, 1).toString(); // col 1 = Nombre

        try (Connection con = ConexionDB.getConexion()) {

            // Validar que no tenga productos
            String sqlCheck = "SELECT COUNT(*) FROM Producto WHERE id_categoria = ?";
            int cantidadProductos = 0;

            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idCategoria);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        cantidadProductos = rs.getInt(1);
                    }
                }
            }

            if (cantidadProductos > 0) {
                JOptionPane.showMessageDialog(this,
                    "No se puede eliminar la categoría '" + nombre +
                    "' porque tiene " + cantidadProductos + " producto(s) asociado(s).");
                return;
            }

            // Confirmar eliminación
            int resp = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar la categoría '" + nombre + "'?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (resp != JOptionPane.YES_OPTION) {
                return; // usuario canceló
            }

            // Eliminar categoría
            String sqlDelete = "DELETE FROM categoria WHERE id_categoria = ?";

            try (PreparedStatement psDelete = con.prepareStatement(sqlDelete)) {
                psDelete.setInt(1, idCategoria);
                psDelete.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Categoría eliminada correctamente.");
            txtNombre.setText("");
            cargarCategorias(); // refrescar tabla

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategorias = new javax.swing.JTable();
        cmdAgregar = new javax.swing.JButton();
        cmdRenombrar = new javax.swing.JButton();
        cmdVolver = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Categorias");

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel3.setText("Administrar categorias");
        jLabel3.setToolTipText("");

        tblCategorias.setAutoCreateRowSorter(true);
        tblCategorias.setBackground(new java.awt.Color(237, 237, 237));
        tblCategorias.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Cantidad de productos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCategorias.setShowGrid(true);
        jScrollPane1.setViewportView(tblCategorias);

        cmdAgregar.setBackground(new java.awt.Color(168, 197, 227));
        cmdAgregar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregar.setText("Agregar");
        cmdAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarActionPerformed(evt);
            }
        });

        cmdRenombrar.setBackground(new java.awt.Color(168, 197, 227));
        cmdRenombrar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdRenombrar.setText("Renombrar");
        cmdRenombrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRenombrarActionPerformed(evt);
            }
        });

        cmdVolver.setBackground(new java.awt.Color(244, 168, 168));
        cmdVolver.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdVolver.setText("Volver");
        cmdVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdVolverActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre:");

        cmdEliminar.setBackground(new java.awt.Color(244, 168, 168));
        cmdEliminar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdEliminar.setText("Eliminar");
        cmdEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(273, 273, 273)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmdEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cmdAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(98, 98, 98)
                                .addComponent(cmdRenombrar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                        .addComponent(cmdVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 178, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmdEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdRenombrar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 7, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarActionPerformed
        agregarCategoria();
        cargarCategorias();
    }//GEN-LAST:event_cmdAgregarActionPerformed

    private void cmdRenombrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRenombrarActionPerformed
        renombrarCategoria();
        cargarCategorias();
    }//GEN-LAST:event_cmdRenombrarActionPerformed

    private void cmdVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdVolverActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cmdVolverActionPerformed

    private void cmdEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEliminarActionPerformed
        eliminarCategoria();
    }//GEN-LAST:event_cmdEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AdministrarCategoriasPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAgregar;
    private javax.swing.JButton cmdEliminar;
    private javax.swing.JButton cmdRenombrar;
    private javax.swing.JButton cmdVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCategorias;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
