/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views.administrar_inventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import posglagerman.ConexionDB;


public class EditarProducto extends javax.swing.JFrame {
    
    //------------------------------------
    //  CONSTRUCTOR VACÍO (NetBeans)
    // ------------------------------------
    public EditarProducto() {
        initComponents();
        cargarCombos();
    }

    // ------------------------------------
    //  CONSTRUCTOR PARA EDITAR PRODUCTO
    // ------------------------------------
    public EditarProducto(String cod, String nombre, String precio,
                          String unidad, String stock,
                          String stockMin, String categoria) {

        initComponents();
        cargarCombos();

        txtCodigo.setText(cod);
        txtNombre.setText(nombre);
        txtPrecio.setText(precio);
        txtStockActual.setText(stock);
        txtStockMinimo.setText(stockMin);

        cmbUnidadMedida.setSelectedItem(unidad);
        cmbCategoria.setSelectedItem(categoria);
    }

    // ------------------------------------
    //   CARGA DE COMBO BOXES
    // ------------------------------------
    private void cargarCombos() {

        cmbUnidadMedida.removeAllItems();
        cmbUnidadMedida.addItem("Unidad");
        cmbUnidadMedida.addItem("Gramo");
        cmbUnidadMedida.addItem("Kilogramo");

        cmbCategoria.removeAllItems();
        cmbCategoria.addItem("1");
        cmbCategoria.addItem("2");
        cmbCategoria.addItem("3");
    }

    // ----------------------------------------------------------------------
    //                      AQUI EMPIEZA initComponents()
    // ----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    
    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdAgregarProducto = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmdEditar = new javax.swing.JButton();
        cmdSalir = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        txtCodigo = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtStockActual = new javax.swing.JTextField();
        txtStockMinimo = new javax.swing.JTextField();
        cmbUnidadMedida = new javax.swing.JComboBox<>();
        cmbCategoria = new javax.swing.JComboBox<>();

        cmdAgregarProducto.setBackground(new java.awt.Color(168, 197, 227));
        cmdAgregarProducto.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregarProducto.setText("Agregar Producto");
        cmdAgregarProducto.setName(""); // NOI18N
        cmdAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarProductoActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel39.setText("Editar producto");
        jLabel39.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setText("Código del producto:");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setText("Nombre del producto:");

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setText("Precio de venta:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("Unidad de medida:");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("Stock actual:");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("Stock mínimo:");

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Categoria");

        cmdEditar.setBackground(new java.awt.Color(168, 197, 227));
        cmdEditar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdEditar.setText("Editar Producto");
        cmdEditar.setName(""); // NOI18N
        cmdEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditarActionPerformed(evt);
            }
        });

        cmdSalir.setBackground(new java.awt.Color(244, 168, 168));
        cmdSalir.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdSalir.setText("Cancelar");
        cmdSalir.setName(""); // NOI18N
        cmdSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSalirActionPerformed(evt);
            }
        });

        txtNombre.setBackground(new java.awt.Color(237, 237, 237));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtCodigo.setBackground(new java.awt.Color(237, 237, 237));
        txtCodigo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        txtPrecio.setBackground(new java.awt.Color(237, 237, 237));
        txtPrecio.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtStockActual.setBackground(new java.awt.Color(237, 237, 237));
        txtStockActual.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtStockMinimo.setBackground(new java.awt.Color(237, 237, 237));
        txtStockMinimo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        cmbUnidadMedida.setBackground(new java.awt.Color(237, 237, 237));
        cmbUnidadMedida.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cmbUnidadMedida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Unidad", "Gramo", "Kilogramo" }));

        cmbCategoria.setBackground(new java.awt.Color(237, 237, 237));
        cmbCategoria.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cmdEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(92, 92, 92)
                                .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(73, 73, 73)
                                .addComponent(txtStockActual))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(67, 67, 67)
                                .addComponent(txtStockMinimo, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCodigo)
                                    .addComponent(txtPrecio)
                                    .addComponent(txtNombre)
                                    .addComponent(cmbUnidadMedida, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(121, 121, 121))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(316, 316, 316)
                .addComponent(jLabel39)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel39)
                .addGap(68, 68, 68)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbUnidadMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtStockActual, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(123, 123, 123)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarProductoActionPerformed
        // TODO: agregar la logica para insertar producto nuevo en la base de datos
      
    }//GEN-LAST:event_cmdAgregarProductoActionPerformed

    private void cmdEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditarActionPerformed
        try {
            Connection con = ConexionDB.getConexion();

            String sql = "UPDATE Producto SET nombre_producto=?, precio_unitario_venta=?, unidad_medida=?, stock_actual=?, stock_minimo=?, id_categoria=? WHERE cod_producto=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtPrecio.getText());
            ps.setString(3, cmbUnidadMedida.getSelectedItem().toString());
            ps.setString(4, txtStockActual.getText());
            ps.setString(5, txtStockMinimo.getText());
            ps.setString(6, cmbCategoria.getSelectedItem().toString());
            ps.setString(7, txtCodigo.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito.");

            dispose();
            new AdminInvPage().setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdEditarActionPerformed

    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
       this.dispose();

    
        new AdminInvPage().setVisible(true);
    }//GEN-LAST:event_cmdSalirActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

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
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new EditarProducto().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbUnidadMedida;
    private javax.swing.JButton cmdAgregarProducto;
    private javax.swing.JButton cmdEditar;
    private javax.swing.JButton cmdSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStockActual;
    private javax.swing.JTextField txtStockMinimo;
    // End of variables declaration//GEN-END:variables
}
