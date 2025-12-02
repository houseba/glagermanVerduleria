package views;
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
        cmbProveedor.removeAllItems();
        String sql = "SELECT nombre_proveedor FROM Proveedor";
        try (Connection conex = ConexionDB.getConexion();
            Statement stm = conex.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                cmbProveedor.addItem(rs.getString("nombre_proveedor"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    
    private void RegistrarCompra() {
        String nombreProveedor = String.valueOf(cmbProveedor.getSelectedItem());
        if (nombreProveedor == null || nombreProveedor.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un proveedor.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tblCompra.getModel();
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debes agregar al menos 1 producto.");
            return;
        }

        // Calcular el total desde la tabla (por si txtTotal está desactualizado)
        double totalCompraDouble = 0.0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object v = modelo.getValueAt(i, 3); // Subtotal
            if (v != null) {
                totalCompraDouble += Double.parseDouble(v.toString());
            }
        }
        long totalCompra = Math.round(totalCompraDouble); // Se inserta entero en pesos

        Connection conex = null;

        try {
            conex = ConexionDB.getConexion();
            conex.setAutoCommit(false);

            // Obtener RUT del proveedor
            String rutProveedor;
            try (PreparedStatement psProveedor = conex.prepareStatement(
                     "SELECT rut_proveedor FROM Proveedor WHERE nombre_proveedor = ?")) {

                psProveedor.setString(1, nombreProveedor);

                try (ResultSet rsProv = psProveedor.executeQuery()) {
                    if (rsProv.next()) {
                        rutProveedor = rsProv.getString("rut_proveedor");
                    } else {
                        JOptionPane.showMessageDialog(this, "Proveedor no encontrado.");
                        conex.rollback();
                        return;
                    }
                }
            }

            // Insertar la compra (incluyendo total_compra)
            int idCompra;
            try (PreparedStatement psCompra = conex.prepareStatement(
                     "INSERT INTO Compra (fecha_compra, rut_proveedor, total_compra) " +
                     "VALUES (datetime('now','localtime'), ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

                psCompra.setString(1, rutProveedor);
                psCompra.setLong(2, totalCompra);
                psCompra.executeUpdate();

                try (ResultSet rsCompraID = psCompra.getGeneratedKeys()) {
                    if (rsCompraID.next()) {
                        idCompra = rsCompraID.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la compra.");
                    }
                }
            }

            // Sentencias para detalle_compra y actualización de stock
            try (PreparedStatement psBuscaProducto = conex.prepareStatement(
                     "SELECT cod_producto FROM Producto WHERE nombre_producto = ?");
                 PreparedStatement psDetalle = conex.prepareStatement(
                     "INSERT INTO detalle_compra (id_compra, cod_producto, cantidad_compra, precio_unitario_compra) " +
                     "VALUES (?, ?, ?, ?)");
                 PreparedStatement psActualizaStock = conex.prepareStatement(
                     "UPDATE Producto SET stock_actual = stock_actual + ? WHERE cod_producto = ?")) {

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    String nombreProducto = modelo.getValueAt(i, 0).toString();
                    double cantidad = Double.parseDouble(modelo.getValueAt(i, 1).toString());
                    int precioUnitarioCompra = Integer.parseInt(modelo.getValueAt(i, 2).toString());

                    // Buscar código del producto
                    String codProducto;
                    psBuscaProducto.setString(1, nombreProducto);
                    try (ResultSet rsProd = psBuscaProducto.executeQuery()) {
                        if (rsProd.next()) {
                            codProducto = rsProd.getString("cod_producto");
                        } else {
                            throw new SQLException("Producto no encontrado: " + nombreProducto);
                        }
                    }

                    // Insertar detalle de compra
                    psDetalle.setInt(1, idCompra);
                    psDetalle.setString(2, codProducto);
                    psDetalle.setDouble(3, cantidad);        // cantidad_compra como REAL en BD
                    psDetalle.setInt(4, precioUnitarioCompra);
                    psDetalle.addBatch();

                    // Actualizar stock_actual (sumar la cantidad comprada)
                    psActualizaStock.setDouble(1, cantidad);
                    psActualizaStock.setString(2, codProducto);
                    psActualizaStock.addBatch();
                }

                psDetalle.executeBatch();
                psActualizaStock.executeBatch();
            }

            conex.commit();
            JOptionPane.showMessageDialog(this,
                "Compra registrada satisfactoriamente.\nTotal: $ " + totalCompra);

            // Limpiar tabla y total
            modelo.setRowCount(0);
            txtTotal.setText("");

        } catch (Exception e) {
            try { if (conex != null) conex.rollback(); } catch (SQLException ignore) {}
            JOptionPane.showMessageDialog(this, "Error al registrar compra: " + e.getMessage());
        } finally {
            if (conex != null) {
                try { conex.setAutoCommit(true); } catch (SQLException ignore) {}
                try { conex.close(); } catch (SQLException ignore) {}
            }
        }
    }
    

    public CompraAProveedoresPage() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
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
        jLabel43 = new javax.swing.JLabel();
        txtPrecioCompra = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("compra a proveedores");
        setBackground(new java.awt.Color(250, 250, 250));
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel39.setText("Compra a proveedores");
        jLabel39.setToolTipText("");

        jLabel40.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel40.setText("Proveedor");

        cmbProveedor.setBackground(new java.awt.Color(237, 237, 237));
        cmbProveedor.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cmbProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProveedorActionPerformed(evt);
            }
        });

        txtProductoProveedor.setBackground(new java.awt.Color(237, 237, 237));
        txtProductoProveedor.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        jLabel41.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel41.setText("Búsqueda de producto:");

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
                "Producto", "Cantidad", "Precio compra", "Subtotal"
            }
        ));
        tblCompra.setFocusable(false);
        tblCompra.setShowGrid(true);
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

        jLabel43.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel43.setText("Precio de compra: $");

        txtPrecioCompra.setBackground(new java.awt.Color(237, 237, 237));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(jLabel39)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel40))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbProveedor, 0, 305, Short.MAX_VALUE)
                                    .addComponent(txtProductoProveedor))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel42))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtCantProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(106, 106, 106))
                                    .addComponent(txtPrecioCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmdCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cmdAgregarProductoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel40)
                        .addComponent(txtCantProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtProductoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(cmdAgregarProductoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAgregarProductoCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarProductoCompraActionPerformed
        String codProducto = txtProductoProveedor.getText().trim().toUpperCase();
        String cantidadTexto = txtCantProveedor.getText().trim();
        String precioTexto = txtPrecioCompra.getText().trim();

        if (codProducto.isEmpty() || cantidadTexto.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar código, cantidad y precio de compra.");
            return;
        }

        double cantidad;
        int precioCompra;
        try {
            cantidad = Double.parseDouble(cantidadTexto.replace(',', '.')); // permite 1,5 o 1.5
            precioCompra = Integer.parseInt(precioTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad o precio inválido.");
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
            return;
        }
        if (precioCompra <= 0) {
            JOptionPane.showMessageDialog(this, "El precio de compra debe ser mayor a 0.");
            return;
        }

        // Buscar nombre de producto y u medida a partir del código
        String nombreProducto;
        String unidadMedida;
        try (Connection conex = ConexionDB.getConexion();
             PreparedStatement psProducto = conex.prepareStatement(
                 "SELECT nombre_producto, unidad_medida FROM Producto WHERE cod_producto = ?")) {

            psProducto.setString(1, codProducto);

            try (ResultSet rsProducto = psProducto.executeQuery()) {
                if (rsProducto.next()) {
                    nombreProducto = rsProducto.getString("nombre_producto");
                    unidadMedida = rsProducto.getString("unidad_medida");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Producto no encontrado para código: " + codProducto);
                    return;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar producto: " + ex.getMessage());
            return;
        }
        
        boolean esKg = "Kilogramo".equalsIgnoreCase(unidadMedida);
        if (!esKg) {
        // si no es Kilogramo, la cantidad debe ser int
            if (Math.abs(cantidad - Math.round(cantidad)) > 1e-9) {
                JOptionPane.showMessageDialog(this,
                    "Solo los productos en Kilogramo pueden tener decimales.\n" +
                    "El producto " + nombreProducto + " está en " + unidadMedida +
                    ", por lo que la cantidad debe ser un número entero.");
                return;
            }
            // forzar a valor entero limpio
            cantidad = Math.round(cantidad);
        }

        DefaultTableModel model = (DefaultTableModel) tblCompra.getModel();

        // Si el producto ya está en la tabla, sumar cantidades y actualizar subtotal
        boolean encontrada = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String nombreFila = model.getValueAt(i, 0).toString();
            if (nombreFila.equals(nombreProducto)) {
                double cantFila = Double.parseDouble(model.getValueAt(i, 1).toString());
                double nuevaCantidad = cantFila + cantidad;
                double nuevoSubtotal = nuevaCantidad * precioCompra;

                model.setValueAt(nuevaCantidad, i, 1);
                model.setValueAt(precioCompra, i, 2); // por si cambia el precio
                model.setValueAt(nuevoSubtotal, i, 3);
                encontrada = true;
                break;
            }
        }

        // Si no estaba, agregar una fila nueva
        if (!encontrada) {
            double subtotal = cantidad * precioCompra;
            model.addRow(new Object[]{ nombreProducto, cantidad, precioCompra, subtotal });
        }

        // Recalcular total de la compra
        double total = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object valor = model.getValueAt(i, 3); // columna 3 = Subtotal
            if (valor != null) {
                total += Double.parseDouble(valor.toString());
            }
        }

        long totalRedondeado = Math.round(total); // pesos Chilenos sin decimales
        txtTotal.setText(String.valueOf(totalRedondeado));

        // Limpiar campos
        txtProductoProveedor.setText("");
        txtCantProveedor.setText("");
        txtPrecioCompra.setText("");
    }//GEN-LAST:event_cmdAgregarProductoCompraActionPerformed

    private void cmdCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCompraActionPerformed
        RegistrarCompra();
    }//GEN-LAST:event_cmdCompraActionPerformed

    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_cmdSalirActionPerformed

    private void cmbProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbProveedorActionPerformed

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
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable tblCompra;
    private javax.swing.JTextField txtCantProveedor;
    private javax.swing.JTextField txtPrecioCompra;
    private javax.swing.JTextField txtProductoProveedor;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
