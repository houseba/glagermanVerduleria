package views;

import Utils.ValidarRut;
import java.awt.HeadlessException;
import posglagerman.ConexionDB;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AdminProPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminProPage.class.getName());

    /**
     * Creates new form AdminProPage
     */
    public AdminProPage() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        ListarProveedores();
    }
    
    private void ListarProveedores() {
        DefaultTableModel model = (DefaultTableModel)tblProveedores.getModel();
        model.setRowCount(0);
        
        String sql = "SELECT rut_proveedor, nombre_proveedor, " +
                 "IFNULL(direccion_proveedor,''), IFNULL(contacto_proveedor,'') " +
                 "FROM proveedor";
        
        try {
            Connection conex = ConexionDB.getConexion();
            PreparedStatement ps = conex.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString(1), // RUT
                rs.getString(2), // Nombre
                rs.getString(4), // Contacto
                rs.getString(3)  // Direccion
            });
        }
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al listar proveedores: " + e.getMessage());
    }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        cmdEliminarProveedor = new javax.swing.JButton();
        cmdAgregarProveedor = new javax.swing.JButton();
        cmdSalir = new javax.swing.JButton();
        txtRut = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProveedores = new javax.swing.JTable();
        jLabel44 = new javax.swing.JLabel();
        cmdActualizarProveedor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administrar proveedores");
        setBackground(new java.awt.Color(250, 250, 250));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel39.setText("Administrar Proveedores");
        jLabel39.setToolTipText("");

        jLabel40.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel40.setText("Nombre:");

        cmdEliminarProveedor.setBackground(new java.awt.Color(168, 197, 227));
        cmdEliminarProveedor.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdEliminarProveedor.setText("Eliminar");
        cmdEliminarProveedor.setActionCommand("");
        cmdEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEliminarProveedorActionPerformed(evt);
            }
        });

        cmdAgregarProveedor.setBackground(new java.awt.Color(168, 197, 227));
        cmdAgregarProveedor.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregarProveedor.setText("Agregar");
        cmdAgregarProveedor.setActionCommand("");
        cmdAgregarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarProveedorActionPerformed(evt);
            }
        });

        cmdSalir.setBackground(new java.awt.Color(244, 168, 168));
        cmdSalir.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdSalir.setText("Salir");
        cmdSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSalirActionPerformed(evt);
            }
        });

        txtRut.setEditable(false);
        txtRut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRutActionPerformed(evt);
            }
        });

        txtNombre.setToolTipText("");

        jLabel41.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel41.setText("Teléfono:");

        jLabel42.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel42.setText("Dirección:");

        tblProveedores.setAutoCreateRowSorter(true);
        tblProveedores.setBackground(new java.awt.Color(237, 237, 237));
        tblProveedores.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rut", "Nombre", "Contacto", "Dirección"
            }
        ));
        tblProveedores.setShowGrid(true);
        tblProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProveedoresMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblProveedores);

        jLabel44.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel44.setText("Rut:");

        cmdActualizarProveedor.setBackground(new java.awt.Color(168, 197, 227));
        cmdActualizarProveedor.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdActualizarProveedor.setText("Actualizar");
        cmdActualizarProveedor.setActionCommand("");
        cmdActualizarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdActualizarProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane7)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(txtTelefono)
                            .addComponent(txtNombre)
                            .addComponent(txtRut))))
                .addGap(18, 18, 18)
                .addComponent(cmdActualizarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmdAgregarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cmdEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                        .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(256, 256, 256))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(txtRut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdActualizarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(cmdAgregarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEliminarProveedorActionPerformed
        int rowSeleccionado = tblProveedores.getSelectedRow();
        
        if (rowSeleccionado == -1){
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor en la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel)tblProveedores.getModel();
        String rut = String.valueOf(model.getValueAt(rowSeleccionado, 0)); // col 0
        String nombre = String.valueOf(model.getValueAt(rowSeleccionado, 1)); // col 1
        
        int ok = JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar proveedor?\n" + nombre + " (" + rut + ")",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );
    if (ok == JOptionPane.NO_OPTION) return;
    
    //Connection conex = null;
    try{
        Connection conex = ConexionDB.getConexion();
        conex.setAutoCommit(false);
        
        try (Statement stm = conex.createStatement()) {
            stm.execute("PRAGMA foreign_keys = ON"); // activar las claves foraneas
        }
        try (PreparedStatement ps = conex.prepareStatement(
                "DELETE FROM proveedor WHERE rut_proveedor = ?")) {
            ps.setString(1, rut);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                conex.rollback();
                JOptionPane.showMessageDialog(this, "El proveedor no existe.");
                return;
            }
        } 
        JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar el proveedor:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
        
        ListarProveedores();
    }//GEN-LAST:event_cmdEliminarProveedorActionPerformed

    private void cmdAgregarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarProveedorActionPerformed
        String rut = txtRut.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        // Quitar puntos y guiones del RUT
        rut = rut.replace(".", "").replace("-", "");

        // Validación del RUT
        if (!ValidarRut.esValido(rut)) {
            JOptionPane.showMessageDialog(this, "El RUT ingresado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que los campos obligatorios estén completos
        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = ConexionDB.getConexion();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO proveedor (rut_proveedor, nombre_proveedor, contacto_proveedor, direccion_proveedor) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rut);
            ps.setString(2, nombre);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.executeUpdate();
            
            

            JOptionPane.showMessageDialog(this, "Proveedor agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos
            txtRut.setText("");
            txtTelefono.setText("");
            txtNombre.setText("");
            txtDireccion.setText("");

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el proveedor:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        ListarProveedores();
    }//GEN-LAST:event_cmdAgregarProveedorActionPerformed
    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cmdSalirActionPerformed

    private void txtRutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutActionPerformed

    private void tblProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProveedoresMouseClicked
        int rowSeleccionada = tblProveedores.getSelectedRow();
        
        DefaultTableModel model = (DefaultTableModel) tblProveedores.getModel();
        txtRut.setText(String.valueOf(model.getValueAt(rowSeleccionada, 0)));
        txtNombre.setText(String.valueOf(model.getValueAt(rowSeleccionada, 1)));
        txtTelefono.setText(String.valueOf(model.getValueAt(rowSeleccionada, 2)));
        txtDireccion.setText(String.valueOf(model.getValueAt(rowSeleccionada, 3)));
    }//GEN-LAST:event_tblProveedoresMouseClicked

    private void cmdActualizarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdActualizarProveedorActionPerformed
         int rowSeleccionada = tblProveedores.getSelectedRow();
        if (rowSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor en la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String rut = txtRut.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        
        if (rut.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "RUT y Nombre son obligatorios.");
            return;
        }
        
        DefaultTableModel m = (DefaultTableModel) tblProveedores.getModel();
        String rutOriginal = String.valueOf(m.getValueAt(rowSeleccionada, 0));
        
        try{
            Connection conex = ConexionDB.getConexion();
            conex.setAutoCommit(false);
            try (Statement stm = conex.createStatement()) {
                stm.execute("PRAGMA foreign_keys = ON");
            }
            
            String sql = "UPDATE proveedor " +
                     "SET rut_proveedor = ?, nombre_proveedor = ?, contacto_proveedor = ?, direccion_proveedor = ? " +
                     "WHERE rut_proveedor = ?";
        
            PreparedStatement ps = conex.prepareStatement(sql);
            ps.setString(1, rut);
            ps.setString(2, nombre);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.setString(5, rutOriginal);
                
            int rows = ps.executeUpdate();
            if (rows == 0) {
                conex.rollback();
                JOptionPane.showMessageDialog(this, "No se encontró el proveedor a actualizar.");
                return;
            }
            conex.commit();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el proveedor:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        ListarProveedores();    
    }//GEN-LAST:event_cmdActualizarProveedorActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new AdminProPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdActualizarProveedor;
    private javax.swing.JButton cmdAgregarProveedor;
    private javax.swing.JButton cmdEliminarProveedor;
    private javax.swing.JButton cmdSalir;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable tblProveedores;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRut;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

}
