import posglagerman.ConexionDB;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author seba
 */
public class CompraAProveedoresPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CompraAProveedoresPage.class.getName());
    
    private void cargarProveedores() {
        try {
            Connection conex = ConexionDB.getConexion();
            Statement stm = conex.createStatement();
            ResultSet rs = stm.executeQuery("SELECT nombre_proveedor FROM proveedor");

            cmbProveedor.removeAllItems(); // Limpia el combo por si acaso

            while (rs.next()) {
                String nombre = rs.getString("nombre_proveedor");
                cmbProveedor.addItem(nombre);
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar proveedores: " + e.getMessage());
        }
    }
    
    private void RegistrarCompra() {
        String nombreProveedor = String.valueOf(cmbProveedor.getSelectedItem());
        String rutProveedor;
        
        try{
            if (tblCompra.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Debes agregar al menos 1 producto.");
                return;
            }
            Connection conex = ConexionDB.getConexion();
            conex.setAutoCommit(false); // no confirmar automaticamente
            
            // Obtener el rut
            PreparedStatement psProveedor = conex.prepareStatement(
            "SELECT rut_proveedor FROM proveedor WHERE nombre_proveedor = ?"
            );
            
            psProveedor.setString(1, nombreProveedor);
            
            ResultSet rsProv = psProveedor.executeQuery();
            if (rsProv.next()) {
            rutProveedor = rsProv.getString("rut_proveedor");
            } else {
                JOptionPane.showMessageDialog(this, "Proveedor no encontrado en BD.");
                return;
            }
            rsProv.close();
            psProveedor.close();
            
            // Insertar en la tabla compra
            PreparedStatement psCompra = conex.prepareStatement(
            "INSERT INTO compra (fecha_compra, rut_proveedor) VALUES (datetime('now'), ?)",
            Statement.RETURN_GENERATED_KEYS
            );
            
            psCompra.setString(1, rutProveedor);
            psCompra.executeUpdate();
            
            ResultSet rsCompraID = psCompra.getGeneratedKeys();
            int idCompra = -1;
            
            if (rsCompraID.next()) {
            idCompra = rsCompraID.getInt(1);
            }
            rsCompraID.close();
            psCompra.close();

            if (idCompra == -1) {
                throw new SQLException("No se pudo obtener el ID de la compra.");
            }
            
            // Insertar en tabla detalle_compra
            PreparedStatement psDetalle = conex.prepareStatement(
                "INSERT INTO detalle_compra (id_compra, id_producto, cantidad_compra, precio_unitario_compra) VALUES (?, ?, ?, ?)"
            );
            DefaultTableModel modelo = (DefaultTableModel) tblCompra.getModel();
            
            for (int i=0;i<modelo.getRowCount(); i++){
                String nombreProducto = modelo.getValueAt(i,0).toString();
                int cantidad = Integer.parseInt(modelo.getValueAt(i, 1).toString());
                int precioUnitario = Integer.parseInt(modelo.getValueAt(i, 2).toString());
                
                // obtener id_producto
                int idProducto = -1;
                PreparedStatement psProducto = conex.prepareStatement(
                    "SELECT id_producto FROM producto WHERE nombre_producto = ?"
                );
                psProducto.setString(1,nombreProducto);
                ResultSet rsProducto = psProducto.executeQuery();
                if (rsProducto.next()) {
                    idProducto = rsProducto.getInt("id_producto");
                } else {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado: " + nombreProducto);
                    continue; // Salta el procucto y pasa al siguiente
                }
                rsProducto.close();
                psProducto.close();

                // Insertar el detalle
                psDetalle.setInt(1, idCompra);
                psDetalle.setInt(2, idProducto);
                psDetalle.setInt(3, cantidad);
                psDetalle.setInt(4, precioUnitario);
                psDetalle.executeUpdate();
            }
            psDetalle.close();
            conex.commit(); // Confirmar el SQL
            JOptionPane.showMessageDialog(null, "Compra registrada satisfactoriamente.");
            
            modelo.setRowCount(0);
            txtTotal.setText("");
            
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error al registrar compra: " + e);
            }
    }
    
    private int obtenerValorUnitario(String nombreProducto) {
    int valor = -1;
    try {
        Connection conex = ConexionDB.getConexion();
        PreparedStatement ps = conex.prepareStatement(
            "SELECT precio_unitario_venta FROM producto WHERE nombre_producto = ?"
        );
        ps.setString(1, nombreProducto);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            valor = rs.getInt("precio_unitario_venta");
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al obtener precio: " + e.getMessage());
    }
    return valor;
}

    public CompraAProveedoresPage() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        Connection conex = ConexionDB.getConexion();
        cargarProveedores();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        txtProductoProveedor = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtCantProveedor = new javax.swing.JTextField();
        cmdAgregarProductoCompra = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblCompra = new javax.swing.JTable();
        jLabel44 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        cmdCompra = new javax.swing.JButton();
        cmdSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel39.setText("Compra a proveedores");
        jLabel39.setToolTipText("");

        jLabel40.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel40.setText("Proveedor");

        cmbProveedor.setBackground(new java.awt.Color(237, 237, 237));

        txtProductoProveedor.setBackground(new java.awt.Color(237, 237, 237));

        jLabel41.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel41.setText("búsqueda de producto:");

        jLabel42.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel42.setText("Cantidad:");

        txtCantProveedor.setBackground(new java.awt.Color(237, 237, 237));

        cmdAgregarProductoCompra.setBackground(new java.awt.Color(211, 211, 211));
        cmdAgregarProductoCompra.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregarProductoCompra.setText("Agregar producto");
        cmdAgregarProductoCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarProductoCompraActionPerformed(evt);
            }
        });

        tblCompra.setAutoCreateRowSorter(true);
        tblCompra.setBackground(new java.awt.Color(237, 237, 237));
        tblCompra.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio Unitario", "Subtotal"
            }
        ));
        jScrollPane7.setViewportView(tblCompra);

        jLabel44.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel44.setText("Total:      $");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(237, 237, 237));

        cmdCompra.setBackground(new java.awt.Color(168, 197, 227));
        cmdCompra.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdCompra.setText("Registrar Compra");
        cmdCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCompraActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(cmdCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cmdAgregarProductoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(258, 258, 258)
                                        .addComponent(jLabel39))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel41)
                                            .addComponent(jLabel40))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtProductoProveedor)
                                            .addComponent(cmbProveedor, 0, 368, Short.MAX_VALUE))))
                                .addGap(78, 78, 78)
                                .addComponent(jLabel42)
                                .addGap(18, 18, 18)
                                .addComponent(txtCantProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)))
                .addGap(24, 24, 24))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42)
                    .addComponent(txtCantProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(cmdAgregarProductoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmdCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addComponent(cmdSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAgregarProductoCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarProductoCompraActionPerformed
    String nombreProducto = txtProductoProveedor.getText().trim();
    String cantidadTexto = txtCantProveedor.getText().trim();

    if (nombreProducto.isEmpty() || cantidadTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debes ingresar nombre y cantidad");
        return;
    }

    int cantidad;
    try {
        cantidad = Integer.parseInt(cantidadTexto);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Cantidad inválida");
        return;
    }

    // Obtener precio desde BD
    int precioUnitario = obtenerValorUnitario(nombreProducto);

    if (precioUnitario == -1) {
        JOptionPane.showMessageDialog(this, "Producto no encontrado");
        return;
    }

    int subtotal = cantidad * precioUnitario;

    // Agregar fila a la tabla
    DefaultTableModel model = (DefaultTableModel) tblCompra.getModel();
    model.addRow(new Object[]{
        nombreProducto,
        cantidad,
        precioUnitario,
        subtotal
    });

    // Actualizar el total (suma de todos los subtotales en la tabla)
    int total = 0;
    for (int i = 0; i < model.getRowCount(); i++) {
        Object valor = model.getValueAt(i, 3); // columna 3 = subtotal
        if (valor != null) {
            total += Integer.parseInt(valor.toString());
        }
    }

    txtTotal.setText(String.valueOf(total));

    // Limpiar campos
    txtProductoProveedor.setText("");
    txtCantProveedor.setText("");
    }//GEN-LAST:event_cmdAgregarProductoCompraActionPerformed

    private void cmdCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCompraActionPerformed
        RegistrarCompra();
    }//GEN-LAST:event_cmdCompraActionPerformed

    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose(); 
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
        java.awt.EventQueue.invokeLater(() -> new CompraAProveedoresPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JButton cmdAgregarProductoCompra;
    private javax.swing.JButton cmdCompra;
    private javax.swing.JButton cmdSalir;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable tblCompra;
    private javax.swing.JTextField txtCantProveedor;
    private javax.swing.JTextField txtProductoProveedor;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
