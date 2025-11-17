/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views.administrar_inventario;
import java.sql.*;
import javax.swing.JOptionPane;
import posglagerman.ConexionDB;
import views.InicioPage;

/**
 *
 * @author conta
 */
public class AgregarProductoPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AgregarProductoPage.class.getName());

    private void cargarCategoria(){
        cmbCategoria.removeAllItems();
        try{
            Connection conex = ConexionDB.getConexion();
            Statement stm = conex.createStatement();
            ResultSet rs = stm.executeQuery("SELECT nombre_categoria FROM categoria");
            
            while (rs.next()) {
                String nombreCategoria = rs.getString("nombre_categoria");
                cmbCategoria.addItem(nombreCategoria);
            }
            rs.close();
            stm.close();
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar categorias: " + e.getMessage());
        }
    }
    
    private void insertarProducto(){
        
        //Validar strings
        String codProducto = txtCodProducto.getText().trim();
        String nombreProducto = txtNomProducto.getText().trim();
        String unidadMedida = String.valueOf(cmbUMedida.getSelectedItem());
        String nombreCategoria = String.valueOf(cmbCategoria.getSelectedItem());
        
        Integer precioVenta;
        Integer stockActual;
        Integer stockMinimo;
        Integer idCategoria = null;
        
        if (codProducto.isEmpty() || nombreProducto.isEmpty()
            || unidadMedida == null || unidadMedida.isBlank()
            || nombreCategoria == null || nombreCategoria.isBlank()) {
            
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
            return;
        } 
        
        //Validar números
        try {
            precioVenta = Integer.valueOf(txtPrecioVenta.getText().trim());
            stockActual = Integer.valueOf(txtStockActual.getText().trim());
            stockMinimo = Integer.valueOf(txtStockMinimo.getText().trim());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Precio y stock deben ser números enteros.");
            return;
        }
        if (precioVenta < 0 || stockActual < 0 || stockMinimo < 0) {
            JOptionPane.showMessageDialog(null, "Precio/stock no pueden ser negativos.");
            return;
        }

        try {
            // Conectar a la BD
            Connection conex = ConexionDB.getConexion();
            conex.setAutoCommit(false); // no confirmar automaticamente
            
            Statement stm = conex.createStatement();
            
            // Sacar el id de la categoria
            
            PreparedStatement psCategoria = conex.prepareStatement(
                "SELECT id_categoria FROM categoria WHERE nombre_categoria = ?"
            );
            psCategoria.setString(1, nombreCategoria);
            
            ResultSet rs = psCategoria.executeQuery();
                if (rs.next()){
                    idCategoria = rs.getInt(1);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al insertar categoría");
                    conex.rollback();
                    return;
                }
                
            PreparedStatement ps = conex.prepareStatement(
                "INSERT INTO producto "
                + "(cod_producto, nombre_producto, precio_unitario_venta, unidad_medida, stock_actual, stock_minimo, id_categoria) "
                + "VALUES (?,?,?,?,?,?,?)"
            );
            ps.setString(1, codProducto);
            ps.setString(2, nombreProducto);
            ps.setInt(3, precioVenta);
            ps.setString(4, unidadMedida);
            ps.setInt(5, stockActual);
            ps.setInt(6, stockMinimo);
            ps.setInt(7, idCategoria);
            
            ps.executeUpdate();

            conex.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el producto: " + e.getMessage());
        }
    }
    
    public AgregarProductoPage() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        cargarCategoria();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmdAgregarProducto = new javax.swing.JButton();
        cmdSalir = new javax.swing.JButton();
        txtNomProducto = new javax.swing.JTextField();
        txtCodProducto = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockActual = new javax.swing.JTextField();
        txtStockMinimo = new javax.swing.JTextField();
        cmbUMedida = new javax.swing.JComboBox<>();
        cmbCategoria = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel39.setText("Agregar producto");
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

        cmdAgregarProducto.setBackground(new java.awt.Color(168, 197, 227));
        cmdAgregarProducto.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregarProducto.setText("Agregar Producto");
        cmdAgregarProducto.setName(""); // NOI18N
        cmdAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarProductoActionPerformed(evt);
            }
        });

        cmdSalir.setBackground(new java.awt.Color(244, 168, 168));
        cmdSalir.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdSalir.setText("Salir");
        cmdSalir.setName(""); // NOI18N
        cmdSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSalirActionPerformed(evt);
            }
        });

        txtNomProducto.setBackground(new java.awt.Color(237, 237, 237));
        txtNomProducto.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtCodProducto.setBackground(new java.awt.Color(237, 237, 237));
        txtCodProducto.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtPrecioVenta.setBackground(new java.awt.Color(237, 237, 237));
        txtPrecioVenta.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtStockActual.setBackground(new java.awt.Color(237, 237, 237));
        txtStockActual.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtStockMinimo.setBackground(new java.awt.Color(237, 237, 237));
        txtStockMinimo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        cmbUMedida.setBackground(new java.awt.Color(237, 237, 237));
        cmbUMedida.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cmbUMedida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Unidad", "Gramo", "Kilogramo" }));

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
                        .addComponent(cmdAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(txtStockMinimo, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCodProducto)
                                    .addComponent(txtPrecioVenta)
                                    .addComponent(txtNomProducto)
                                    .addComponent(cmbUMedida, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(121, 121, 121))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(299, 299, 299)
                .addComponent(jLabel39)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNomProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel4))
                    .addComponent(cmbUMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(cmdAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        insertarProducto();
    }//GEN-LAST:event_cmdAgregarProductoActionPerformed

    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose(); // Cerrar el sistema
    }//GEN-LAST:event_cmdSalirActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new AgregarProductoPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbUMedida;
    private javax.swing.JButton cmdAgregarProducto;
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
    private javax.swing.JTextField txtCodProducto;
    private javax.swing.JTextField txtNomProducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtStockActual;
    private javax.swing.JTextField txtStockMinimo;
    // End of variables declaration//GEN-END:variables
}
