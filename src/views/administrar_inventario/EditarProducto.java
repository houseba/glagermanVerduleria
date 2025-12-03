/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views.administrar_inventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import posglagerman.ConexionDB;


public class EditarProducto extends javax.swing.JFrame {
    private double stockOriginal;
    
    private void cargarCategoria() {
        cmbCategoria.removeAllItems();
        String sql = "SELECT nombre_categoria FROM categoria ORDER BY nombre_categoria";

        try (Connection conex = ConexionDB.getConexion();
             PreparedStatement ps = conex.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nombreCategoria = rs.getString("nombre_categoria");
                cmbCategoria.addItem(nombreCategoria);
            }

            if (cmbCategoria.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "No hay categorías creadas. Debe crear al menos una categoría antes de editar productos.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorias: " + e.getMessage());
        }
    }

   
    public EditarProducto() {
        initComponents();
        cargarCombo();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // ------------------------------------
    //  CONSTRUCTOR PARA EDITAR PRODUCTO
    // ------------------------------------
    public EditarProducto(String cod, String nombre, String precio,
                          String unidad, String stock,
                          String stockMin, String categoria) {

        initComponents();
        cargarCombo();
        cargarCategoria();
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/gg.png")).getImage());

        txtCodigo.setText(cod);
        txtNombre.setText(nombre);
        txtPrecio.setText(precio);
        txtStockActual.setText(stock);
        txtStockMinimo.setText(stockMin);

        cmbUnidadMedida.setSelectedItem(unidad);
        cmbCategoria.setSelectedItem(categoria);
        // Guardar el stock original
        try {
            stock = stock.replace(',', '.');   // permite , o .
            stockOriginal = Double.parseDouble(stock);
        } catch (NumberFormatException e) {
            stockOriginal = 0.0;
        }
    }
    private void cargarCombo() {

        cmbUnidadMedida.removeAllItems();
        cmbUnidadMedida.addItem("Unidad");
        cmbUnidadMedida.addItem("Gramo");
        cmbUnidadMedida.addItem("Kilogramo");
    }
    @SuppressWarnings("unchecked")
    
    
    private String normalizarNombreProducto(String nombre) {
        if (nombre == null) return "";
        nombre = nombre.trim();
        nombre = nombre.replaceAll("\\s+", " "); // colapsar espacios dobles
        if (nombre.isEmpty()) return "";
        return nombre;
    }
    
    private boolean validarSiExiste(String nombre, String codProducto) {
        String nombreUC = nombre.toUpperCase(java.util.Locale.ROOT).trim();

        String sql = "SELECT 1 FROM Producto " +
                     "WHERE UPPER(nombre_producto) = ? " +
                     "AND UPPER(cod_producto) <> ? " +   // excluir el propio
                     "LIMIT 1";

        try (Connection conex = ConexionDB.getConexion();
             PreparedStatement ps = conex.prepareStatement(sql)) {

            ps.setString(1, nombreUC);
            ps.setString(2, codProducto.toUpperCase(java.util.Locale.ROOT).trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this,
                        "Ya existe otro producto con el nombre " + nombre + ".");
                    return false;
                }
            }

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error validando duplicados: " + e.getMessage());
            return false;
        }
    }

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
        jLabel8 = new javax.swing.JLabel();
        txtMotivoAjuste = new javax.swing.JTextField();
        chkModificarStock = new javax.swing.JCheckBox();

        cmdAgregarProducto.setBackground(new java.awt.Color(168, 197, 227));
        cmdAgregarProducto.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregarProducto.setText("Agregar Producto");
        cmdAgregarProducto.setName(""); // NOI18N
        cmdAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarProductoActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar producto");
        setBackground(new java.awt.Color(250, 250, 250));

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
        jLabel7.setText("Categoría");

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

        txtCodigo.setEditable(false);
        txtCodigo.setBackground(new java.awt.Color(237, 237, 237));
        txtCodigo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        txtCodigo.setFocusable(false);
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        txtPrecio.setBackground(new java.awt.Color(237, 237, 237));
        txtPrecio.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        txtStockActual.setEditable(false);
        txtStockActual.setBackground(new java.awt.Color(237, 237, 237));
        txtStockActual.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        txtStockActual.setEnabled(false);

        txtStockMinimo.setBackground(new java.awt.Color(237, 237, 237));
        txtStockMinimo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        cmbUnidadMedida.setBackground(new java.awt.Color(237, 237, 237));
        cmbUnidadMedida.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cmbUnidadMedida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Unidad", "Gramo", "Kilogramo" }));

        cmbCategoria.setBackground(new java.awt.Color(237, 237, 237));
        cmbCategoria.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setText("Motivo de ajuste de stock:");

        txtMotivoAjuste.setEditable(false);
        txtMotivoAjuste.setBackground(new java.awt.Color(237, 237, 237));
        txtMotivoAjuste.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        txtMotivoAjuste.setEnabled(false);
        txtMotivoAjuste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMotivoAjusteActionPerformed(evt);
            }
        });

        chkModificarStock.setText("¿Modificar stock?");
        chkModificarStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkModificarStockActionPerformed(evt);
            }
        });

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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(45, 45, 45)
                        .addComponent(txtCodigo)
                        .addGap(121, 121, 121))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(316, 316, 316)
                .addComponent(jLabel39)
                .addGap(0, 339, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMotivoAjuste)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtStockActual, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chkModificarStock)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(94, 94, 94)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbUnidadMedida, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtStockMinimo))))
                .addGap(121, 121, 121))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbUnidadMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtStockActual, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkModificarStock))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtMotivoAjuste, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(77, 77, 77)
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
        String codProducto = txtCodigo.getText().trim();
        String nombre = normalizarNombreProducto(txtNombre.getText());
        
        if (codProducto.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Código y nombre del producto son obligatorios.");
            return;
        }
        
        if (cmbUnidadMedida.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                "Debes seleccionar una unidad de medida.");
            return;
        }

        if (cmbCategoria.getItemCount() == 0 || cmbCategoria.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                "Debes seleccionar una categoría válida.");
            return;
        }
        String nombreCategoria = cmbCategoria.getSelectedItem().toString();
        
        // Validar duplicados en bd
        if (!validarSiExiste(nombre, codProducto)) return;
        
        try (Connection con = ConexionDB.getConexion()) {
            try {
                con.setAutoCommit(false);

                int idCategoria;
                String sqlCat = "SELECT id_categoria FROM categoria WHERE nombre_categoria = ?";

                try (PreparedStatement psCategoria = con.prepareStatement(sqlCat)) {
                    psCategoria.setString(1, nombreCategoria);

                    try (ResultSet rs = psCategoria.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            JOptionPane.showMessageDialog(this, "Categoría no encontrada: " + nombreCategoria);
                            return;
                        }
                        idCategoria = rs.getInt(1);
                    }
                }

                // Sacar datos del producto desde el formulario
                String unidadMedida = cmbUnidadMedida.getSelectedItem().toString();

                // Precio
                int precio;
                try {
                    precio = Integer.parseInt(txtPrecio.getText().trim());
                    if (precio <= 0) {
                        JOptionPane.showMessageDialog(this,
                            "El precio de venta debe ser un número entero mayor a $0.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio de venta inválido.");
                    return;
                }

                // Stock mínimo y actual (permite coma o punto)
                double stockMinimo;
                double nuevoStock;
                try {
                    String stockMinStr = txtStockMinimo.getText().trim().replace(',', '.');
                    String stockActStr = txtStockActual.getText().trim().replace(',', '.');

                    stockMinimo = Double.parseDouble(stockMinStr);
                    nuevoStock  = Double.parseDouble(stockActStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Stock mínimo y stock actual deben ser números válidos.");
                    return;
                }

                // solo productos en Kilogramo pueden tener decimales
                boolean esKg = unidadMedida.equalsIgnoreCase("Kilogramo");

                if (!esKg) {
                    boolean stockMinEsEntero = (stockMinimo % 1 == 0);
                    boolean stockActEsEntero = (nuevoStock  % 1 == 0);

                    if (!stockMinEsEntero || !stockActEsEntero) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Solo los productos con unidad 'Kilogramo' pueden tener decimales en el stock.\n" +
                            "Para 'Unidad' o 'Gramo', usa valores enteros.",
                            "Stock inválido",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                }

                if (stockMinimo < 0 || nuevoStock < 0) {
                    JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.");
                    return;
                }

                // 4) Actualizar Producto
                String sqlUpdate =
                    "UPDATE Producto SET nombre_producto=?, precio_unitario_venta=?, " +
                    "unidad_medida=?, stock_actual=?, stock_minimo=?, id_categoria=? " +
                    "WHERE cod_producto=?";

                try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                    ps.setString(1, nombre);
                    ps.setInt(2, precio);
                    ps.setString(3, unidadMedida);
                    ps.setDouble(4, nuevoStock);
                    ps.setDouble(5, stockMinimo);
                    ps.setInt(6, idCategoria);
                    ps.setString(7, codProducto);
                    ps.executeUpdate();
                }

                // Registrar ajuste de stock SOLO si está marcada la opción y realmente cambió
                if (chkModificarStock.isSelected()) {
                    double diferencia = nuevoStock - stockOriginal;

                    if (diferencia != 0) {
                        String motivo = txtMotivoAjuste.getText().trim();
                        if (motivo.isEmpty()) {
                            con.rollback();
                            JOptionPane.showMessageDialog(this,
                                "Debes ingresar un motivo de ajuste de stock.");
                            return;
                        }

                        String sqlAjusteStock =
                            "INSERT INTO ajuste_inventario " +
                            "(cod_producto, cantidad_ajustada, fecha_ajuste, motivo_ajuste) " +
                            "VALUES (?, ?, datetime('now','localtime'), ?)";

                        try (PreparedStatement psAjuste = con.prepareStatement(sqlAjusteStock)) {
                            psAjuste.setString(1, codProducto);
                            psAjuste.setDouble(2, diferencia); // puede ser + o -
                            psAjuste.setString(3, motivo);
                            psAjuste.executeUpdate();
                        }
                    }
                }

                con.commit();
                JOptionPane.showMessageDialog(this, "Producto actualizado con éxito.");
                dispose();
                new AdminInvPage().setVisible(true);

            } catch (Exception eInner) {
                try { con.rollback(); } catch (SQLException ignore) {}
                throw eInner;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdEditarActionPerformed

    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
        AdminInvPage administrarInvPage = new AdminInvPage();
        administrarInvPage.setVisible(true);   
        this.dispose();
    }//GEN-LAST:event_cmdSalirActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void txtMotivoAjusteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMotivoAjusteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMotivoAjusteActionPerformed

    private void chkModificarStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkModificarStockActionPerformed
        boolean habilitar = chkModificarStock.isSelected();

        txtStockActual.setEditable(habilitar);
        txtStockActual.setEnabled(habilitar);
        txtMotivoAjuste.setEditable(habilitar);
        txtMotivoAjuste.setEnabled(habilitar);
        
        if (!habilitar) {
            txtStockActual.setText(String.valueOf(stockOriginal));
            txtMotivoAjuste.setText("");
        }
    }//GEN-LAST:event_chkModificarStockActionPerformed

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
    private javax.swing.JCheckBox chkModificarStock;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtMotivoAjuste;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStockActual;
    private javax.swing.JTextField txtStockMinimo;
    // End of variables declaration//GEN-END:variables
}
