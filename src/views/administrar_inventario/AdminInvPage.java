package views.administrar_inventario;
import views.InicioPage;
import posglagerman.ConexionDB;
import Utils.StockRenderer;

import java.sql.*;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class AdminInvPage extends javax.swing.JFrame {
    
        private String productoSeleccionado = null;
        private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminInvPage.class.getName());

        public AdminInvPage() {
            initComponents();
            setLocationRelativeTo(null);
            setResizable(false);
            setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/gg.png")).getImage());
            
            cargarProductos();
            StockRenderer renderer = new StockRenderer(4, 5); // col 4 = stock actual, col 5 = stock mínimo
            tblCompra.setDefaultRenderer(Object.class, renderer);
            seleccionarProductoTabla();
        }

        private void cargarProductos() {

            DefaultTableModel model = (DefaultTableModel) tblCompra.getModel();
            model.setRowCount(0);

            String sql =  
                "SELECT p.cod_producto, " +
                "p.nombre_producto, " +
                "COALESCE(c.nombre_categoria, '(Sin categoría)') AS categoria, " + // ← nombre de la categoría
                "p.unidad_medida, " +
                "p.precio_unitario_venta, " +
                "p.stock_actual, " +
                "p.stock_minimo " +
                "FROM Producto p " +
                "LEFT JOIN Categoria c ON c.id_categoria = p.id_categoria " + // JOIN para obtener el nombre de la categoria
                "ORDER BY p.nombre_producto";

            try (Connection conn = ConexionDB.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String cod = rs.getString("cod_producto");
                    String nombre = rs.getString("nombre_producto");
                    int precio = rs.getInt("precio_unitario_venta");
                    String unidad = rs.getString("unidad_medida");
                    double stockAct = rs.getDouble("stock_actual");
                    double stockMin = rs.getDouble("stock_minimo");
                    String categoria = rs.getString("categoria");
                    model.addRow(new Object[]{
                        cod,
                        nombre,
                        precio,
                        unidad,
                        stockAct,
                        stockMin,
                        categoria
                    });
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar inventario: " + e.getMessage());
            }
        }

        private void seleccionarProductoTabla() {
            tblCompra.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = tblCompra.getSelectedRow();
                    if (fila != -1) {
                        productoSeleccionado = tblCompra.getValueAt(fila, 0).toString();
                    }
                }
            });
        }

        private void buscarProducto() {

            String busqueda = jTextField1.getText().trim();

            DefaultTableModel model = (DefaultTableModel) tblCompra.getModel();
            model.setRowCount(0);

            String sql =
                "SELECT p.cod_producto, " +
                "p.nombre_producto, " +
                "p.precio_unitario_venta, " +
                "p.unidad_medida, " +
                "p.stock_actual, " +
                "p.stock_minimo, " +
                "COALESCE(c.nombre_categoria, '(Sin categoría)') AS categoria " +
                "FROM Producto p " +
                "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                "WHERE p.nombre_producto LIKE ? OR p.cod_producto LIKE ? " +
                "ORDER BY p.nombre_producto";

            try (Connection conn = ConexionDB.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, "%" + busqueda + "%");
                ps.setString(2, "%" + busqueda + "%");

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        String cod       = rs.getString("cod_producto");
                        String nombre    = rs.getString("nombre_producto");
                        int precio       = rs.getInt("precio_unitario_venta");
                        String unidad    = rs.getString("unidad_medida");
                        double stockAct  = rs.getDouble("stock_actual");
                        double stockMin  = rs.getDouble("stock_minimo");
                        String categoria = rs.getString("categoria"); // ← nombre, no id

                        model.addRow(new Object[]{
                            cod,
                            nombre,
                            precio,
                            unidad,
                            stockAct,
                            stockMin,
                            categoria
                        });
                    }
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());
            }
        }


        private void eliminarProducto() {

            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto primero.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que deseas eliminar el producto " + productoSeleccionado + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            String sql = "DELETE FROM Producto WHERE cod_producto = ?";

            try (Connection conn = ConexionDB.getConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, productoSeleccionado);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                productoSeleccionado = null;

                cargarProductos();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdSalir1 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblCompra = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cmdSalir = new javax.swing.JButton();
        cmdEditar = new javax.swing.JButton();
        cmdEliminar = new javax.swing.JButton();
        cmdAgregar = new javax.swing.JButton();
        cmdHistorial = new javax.swing.JButton();
        cmdCategorias = new javax.swing.JButton();

        cmdSalir1.setBackground(new java.awt.Color(244, 168, 168));
        cmdSalir1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdSalir1.setText("Salir");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administrar inventario");
        setBackground(new java.awt.Color(250, 250, 250));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel39.setText("Administrar Inventario");
        jLabel39.setToolTipText("");

        tblCompra.setBackground(new java.awt.Color(237, 237, 237));
        tblCompra.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Precio unitario venta", "Unidad de medida", "Stock actual", "Stock mínimo", "Categoría"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCompra.setShowGrid(true);
        jScrollPane7.setViewportView(tblCompra);

        jLabel1.setText("Búsqueda de productos por nombre:");

        jTextField1.setBackground(new java.awt.Color(237, 237, 237));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        cmdEditar.setBackground(new java.awt.Color(168, 197, 227));
        cmdEditar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdEditar.setText("Editar Producto");
        cmdEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditarActionPerformed(evt);
            }
        });

        cmdEliminar.setBackground(new java.awt.Color(168, 197, 227));
        cmdEliminar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdEliminar.setText("Eliminar");
        cmdEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEliminarActionPerformed(evt);
            }
        });

        cmdAgregar.setBackground(new java.awt.Color(168, 197, 227));
        cmdAgregar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdAgregar.setText("Agregar Producto");
        cmdAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarActionPerformed(evt);
            }
        });

        cmdHistorial.setBackground(new java.awt.Color(168, 197, 227));
        cmdHistorial.setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        cmdHistorial.setText("Historial de modificaciones de Stock");
        cmdHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHistorialActionPerformed(evt);
            }
        });

        cmdCategorias.setBackground(new java.awt.Color(168, 197, 227));
        cmdCategorias.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdCategorias.setText("Administrar categorias");
        cmdCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCategoriasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel39)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(cmdAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cmdCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmdHistorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmdHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmdSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmdEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        buscarProducto();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose(); // Cerrar el sistema
    }//GEN-LAST:event_cmdSalirActionPerformed

    private void cmdEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditarActionPerformed
     int fila = tblCompra.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un producto primero");
        return;
    }

    String cod = tblCompra.getValueAt(fila, 0).toString();
    String nombre = tblCompra.getValueAt(fila, 1).toString();
    double precio = Double.parseDouble(tblCompra.getValueAt(fila, 2).toString());
    String unidad = tblCompra.getValueAt(fila, 3).toString();
    double stockAct = Double.parseDouble(tblCompra.getValueAt(fila, 4).toString());
    double stockMin = Double.parseDouble(tblCompra.getValueAt(fila, 5).toString());
    String categoria = tblCompra.getValueAt(fila, 6).toString();

    EditarProducto editar = new EditarProducto(
        cod,
        nombre,
        String.valueOf(precio),
        unidad,
        String.valueOf(stockAct),
        String.valueOf(stockMin),
        String.valueOf(categoria)
    );

    editar.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_cmdEditarActionPerformed

    private void cmdEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEliminarActionPerformed
        eliminarProducto();        // TODO add your handling code here:
    }//GEN-LAST:event_cmdEliminarActionPerformed

    private void cmdAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarActionPerformed
        AgregarProductoPage agregarProductoPage = new AgregarProductoPage();
        agregarProductoPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cmdAgregarActionPerformed

    private void cmdHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHistorialActionPerformed
        HistorialStockPage historialStockPage = new HistorialStockPage();
        historialStockPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cmdHistorialActionPerformed

    private void cmdCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCategoriasActionPerformed
        AdministrarCategoriasPage administrarCategoriasPage = new AdministrarCategoriasPage();
        administrarCategoriasPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cmdCategoriasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new AdminInvPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAgregar;
    private javax.swing.JButton cmdCategorias;
    private javax.swing.JButton cmdEditar;
    private javax.swing.JButton cmdEliminar;
    private javax.swing.JButton cmdHistorial;
    private javax.swing.JButton cmdSalir;
    private javax.swing.JButton cmdSalir1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tblCompra;
    // End of variables declaration//GEN-END:variables
}
