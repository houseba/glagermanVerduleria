package views;

import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import posglagerman.ConexionDB;

public class VentasPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentasPage.class.getName());

    /**
     * Creates new form Ventas
     */
    public VentasPage() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void agregarProducto(){
        String cod = txtProducto.getText().toUpperCase().trim();
        String cantidadStr = txtCantidad.getText().trim();
        double cantidad;

        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el código del producto.");
            return;
        }
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa la cantidad.");
            return;
        }

        try {
            cantidad = Double.parseDouble(cantidadStr.replace(',', '.')); // permitir , o .
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número.");
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
            return;
        }

        String nombre;
        String unidadMedida;
        int precioUnitario;
        double stockActual;

        // Buscar producto
        try (Connection conex = ConexionDB.getConexion()) {
            
            if (conex == null) {
                JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            String sql = "SELECT nombre_producto, precio_unitario_venta, stock_actual, unidad_medida " +
                "FROM Producto WHERE UPPER(cod_producto) = ?";
            
            try (PreparedStatement ps = conex.prepareStatement(sql)) {
            ps.setString(1, cod);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "No existe un producto con ese código.");
                        return;
                    }
                    nombre = rs.getString("nombre_producto");
                    precioUnitario = rs.getInt("precio_unitario_venta");
                    stockActual = rs.getDouble("stock_actual");
                    unidadMedida = rs.getString("unidad_medida");
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar producto: " + e.getMessage());
            return;
        }

        // Solo Kilogramos puede tener decimales
        boolean esKg = "Kilogramo".equalsIgnoreCase(unidadMedida);
        if (!esKg) {
            if (Math.abs(cantidad - Math.round(cantidad)) > 1e-9) {
                JOptionPane.showMessageDialog(this,
                    """
                    Solo los productos vendidos en Kilogramo pueden tener decimales.
                    El producto """ + nombre +  " está en " + unidadMedida +
                    ", por lo que la cantidad debe ser un número entero.");
                return;
            }
            cantidad = Math.round(cantidad);
        }

        DefaultTableModel model = (DefaultTableModel) tblVenta.getModel();

        // Ver si el producto ya está en la tabla para sumar cantidad
        boolean encontrada = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String nombreFila = model.getValueAt(i, 0).toString();
            if (nombreFila.equals(nombre)) {
                double cantFila = Double.parseDouble(model.getValueAt(i, 1).toString());
                double nuevaCantidad = cantFila + cantidad;

                // validar stock con la nueva cantidad total
                if (nuevaCantidad > stockActual) {
                    JOptionPane.showMessageDialog(this,
                        "Stock insuficiente.\nDisponible: " + stockActual +
                        " " + unidadMedida + "\nYa tienes " + cantFila +
                        " agregados a la venta.");
                    return;
                }

                double nuevoSubtotal = nuevaCantidad * precioUnitario;

                Object cantidadTabla = esKg ? nuevaCantidad : (int) Math.round(nuevaCantidad);

                model.setValueAt(cantidadTabla, i, 1); // cantidad
                model.setValueAt(precioUnitario, i, 2); // precio unitario (por si cambia)
                model.setValueAt(nuevoSubtotal, i, 3); // subtotal
                encontrada = true;
                break;
            }
        }

        // Si no estaba en la tabla, agregar una fila nueva
        if (!encontrada) {
            if (cantidad > stockActual) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuficiente. Disponible: " + stockActual + " " + unidadMedida);
                return;
            }

            double subtotal = cantidad * precioUnitario;
            Object cantidadTabla = esKg ? cantidad : (int) Math.round(cantidad);

            model.addRow(new Object[]{
                nombre,
                cantidadTabla,
                precioUnitario,
                subtotal
            });
        }

        // Recalcular total (double) y redondear a entero para mostrar
        double total = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object v = model.getValueAt(i, 3); // subtotal
            if (v != null) {
                total += Double.parseDouble(v.toString());
            }
        }
        long totalRedondeado = Math.round(total); // sin decimales
        txtTotal.setText(String.valueOf(totalRedondeado));

        // Limpiar campos
        txtProducto.setText("");
        txtCantidad.setText("");
        }

        private void confirmarVenta(){
            DefaultTableModel model = (DefaultTableModel) tblVenta.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agrega al menos un producto.");
            return;
        }

        // Total en double y se redondea al final
        double total = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object v = model.getValueAt(i, 3); // Subtotal
            if (v != null) {
                total += Double.parseDouble(v.toString());
            }
        }
        long totalRedondeado = Math.round(total); // Chile sin decimales

        try (Connection conex = ConexionDB.getConexion()) {
            if (conex == null) {
                JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                conex.setAutoCommit(false);

                int idVenta;

                // insertar en tabla venta
                try (PreparedStatement psVenta = conex.prepareStatement(
                        "INSERT INTO venta (total_venta, fecha_venta) VALUES (?, datetime('now'))",
                        Statement.RETURN_GENERATED_KEYS)) {

                    psVenta.setLong(1, totalRedondeado);
                    psVenta.executeUpdate();

                    try (ResultSet rs = psVenta.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("No se obtuvo id venta.");
                        }
                        idVenta = rs.getInt(1);
                    }
                }

                // Preparar búsquedas, actualización de stock y detalle_venta
                try (PreparedStatement psBusca = conex.prepareStatement(
                            "SELECT cod_producto, stock_actual, unidad_medida " +
                            "FROM Producto WHERE nombre_producto = ?");
                     PreparedStatement psStock = conex.prepareStatement(
                            "UPDATE Producto " +
                            "SET stock_actual = stock_actual - ? " +
                            "WHERE cod_producto = ? AND stock_actual >= ?");
                     PreparedStatement psDet = conex.prepareStatement(
                            "INSERT INTO detalle_venta " +
                            "(id_venta, cod_producto, cantidad_venta, precio_unitario_venta) " +
                            "VALUES (?, ?, ?, ?)")) {

                    for (int i = 0; i < model.getRowCount(); i++) {
                        String nombre = model.getValueAt(i, 0).toString();
                        double cantidadTabla = Double.parseDouble(model.getValueAt(i, 1).toString());
                        int precioUnit = Integer.parseInt(model.getValueAt(i, 2).toString());

                        String cod;
                        double stock;
                        String unidadMedida;

                        // Buscar producto en BD
                        psBusca.setString(1, nombre);
                        try (ResultSet rs = psBusca.executeQuery()) {
                            if (!rs.next()) {
                                throw new SQLException("Producto no encontrado: " + nombre);
                            }
                            cod = rs.getString("cod_producto");
                            stock = rs.getDouble("stock_actual");
                            unidadMedida = rs.getString("unidad_medida");
                        }

                        boolean esKg = "Kilogramo".equalsIgnoreCase(unidadMedida);

                        // si no es kg, cantidad entera
                        double cantidadReal = esKg ? cantidadTabla : Math.round(cantidadTabla);

                        if (cantidadReal > stock + 1e-9) {
                            throw new SQLException(
                                "Stock insuficiente para " + nombre +
                                ". Disponible: " + stock
                            );
                        }

                        // Descontar stock
                        psStock.setDouble(1, cantidadReal);
                        psStock.setString(2, cod);
                        psStock.setDouble(3, cantidadReal); // condición stock_actual >= cantidadReal
                        int ok = psStock.executeUpdate();
                        if (ok == 0) {
                            throw new SQLException(
                                "Stock insuficiente para " + nombre +
                                " al confirmar la venta. Stock actual: " + stock
                            );
                        }

                        // Insertar detalle de la venta
                        psDet.setInt(1, idVenta);
                        psDet.setString(2, cod);
                        psDet.setDouble(3, cantidadReal); // campo debe ser REAL en BD
                        psDet.setInt(4, precioUnit);
                        psDet.addBatch();
                    }

                    psDet.executeBatch();
                }

                conex.commit();
                JOptionPane.showMessageDialog(this, "Venta registrada. Total: $ " + totalRedondeado);

                // Limpiar
                model.setRowCount(0);
                txtProducto.setText("");
                txtCantidad.setText("");
                txtTotal.setText("");

            } catch (SQLException e) {
                try {
                    conex.rollback();
                } catch (SQLException ignore) {}
                JOptionPane.showMessageDialog(this, "No se pudo registrar la venta: " + e.getMessage());
            } finally {
                try {
                    conex.setAutoCommit(true);
                } catch (SQLException ignore) {}
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión al registrar la venta: " + e.getMessage());
        }
    }
    
    private void limpiarTabla(){
        DefaultTableModel m = (DefaultTableModel) tblVenta.getModel();
        
        int ok = JOptionPane.showConfirmDialog(this, "¿Vaciar la lista?", "Vaciar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            m.setRowCount(0);
            txtTotal.setText("");
            txtProducto.setText("");
            txtCantidad.setText("");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        cmdAgregarProducto = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVenta = new javax.swing.JTable();
        cmdConfirmarVenta = new javax.swing.JButton();
        cmdVolver = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmdLimpiarTabla = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ventas");

        jPanel4.setBackground(new java.awt.Color(250, 250, 250));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel3.setText("REGISTRO DE VENTA");
        jLabel3.setToolTipText("");

        txtProducto.setBackground(new java.awt.Color(237, 237, 237));

        cmdAgregarProducto.setBackground(new java.awt.Color(211, 211, 211));
        cmdAgregarProducto.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregarProducto.setText("Agregar producto");
        cmdAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarProductoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("Código de producto:");

        tblVenta.setBackground(new java.awt.Color(237, 237, 237));
        tblVenta.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio Unitario", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVenta.setShowGrid(true);
        jScrollPane1.setViewportView(tblVenta);

        cmdConfirmarVenta.setBackground(new java.awt.Color(168, 197, 227));
        cmdConfirmarVenta.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdConfirmarVenta.setText("Confirmar venta");
        cmdConfirmarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConfirmarVentaActionPerformed(evt);
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

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("Total:");

        txtTotal.setEditable(false);

        txtCantidad.setBackground(new java.awt.Color(237, 237, 237));

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("Cantidad:");

        cmdLimpiarTabla.setBackground(new java.awt.Color(168, 197, 227));
        cmdLimpiarTabla.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdLimpiarTabla.setText("Limpiar tabla");
        cmdLimpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLimpiarTablaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(jLabel3))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(cmdConfirmarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(80, 80, 80)
                                    .addComponent(cmdLimpiarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmdVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel6))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmdAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 34, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmdAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdConfirmarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdLimpiarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarProductoActionPerformed
        agregarProducto();
    }//GEN-LAST:event_cmdAgregarProductoActionPerformed

    private void cmdConfirmarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdConfirmarVentaActionPerformed
        confirmarVenta();
    }//GEN-LAST:event_cmdConfirmarVentaActionPerformed

    private void cmdVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdVolverActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_cmdVolverActionPerformed

    private void cmdLimpiarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLimpiarTablaActionPerformed
        limpiarTabla();
    }//GEN-LAST:event_cmdLimpiarTablaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VentasPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdAgregarProducto;
    private javax.swing.JButton cmdConfirmarVenta;
    private javax.swing.JButton cmdLimpiarTabla;
    private javax.swing.JButton cmdVolver;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblVenta;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
