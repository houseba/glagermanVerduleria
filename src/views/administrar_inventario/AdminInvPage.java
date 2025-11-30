/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views.administrar_inventario;

import views.CompraAProveedoresPage;
import views.InicioPage;
import views.administrar_inventario.AgregarProductoPage;
import posglagerman.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;





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

            cargarProductos();
            seleccionarProductoTabla();
        }

        // ================================
        //   CARGAR PRODUCTOS
        // ================================
        private void cargarProductos() {

            DefaultTableModel model = (DefaultTableModel) tblCompra.getModel();
            model.setRowCount(0);

            String sql =  
                "SELECT p.cod_producto, " +
                "       p.nombre_producto, " +
                "       COALESCE(c.nombre_categoria, '(Sin categoría)') AS categoria, " + // ← nombre de la categoría
                "       p.unidad_medida, " +
                "       p.precio_unitario_venta, " +
                "       p.stock_actual, " +
                "       p.stock_minimo " +
                "FROM Producto p " +
                "LEFT JOIN Categoria c ON c.id_categoria = p.id_categoria " + // JOIN para obtener el nombre de la categoria
                "ORDER BY p.nombre_producto";

            try (Connection conn = ConexionDB.getConexion();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("cod_producto"),
                        rs.getString("nombre_producto"),
                        rs.getInt("precio_unitario_venta"),
                        rs.getString("unidad_medida"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo"),
                        rs.getString("categoria")
                    });
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar inventario: " + e.getMessage());
            }
        }

        // ================================
        //   SELECCIONAR FILA DE TABLA
        // ================================
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

        // ================================
        //   BUSCAR PRODUCTO
        // ================================
        private void buscarProducto() {

            String busqueda = jTextField1.getText().trim();

            DefaultTableModel model = (DefaultTableModel) tblCompra.getModel();
            model.setRowCount(0);

            String sql = "SELECT * FROM Producto WHERE nombre_producto LIKE ? OR cod_producto LIKE ?";

            try (Connection conn = ConexionDB.getConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, "%" + busqueda + "%");
                ps.setString(2, "%" + busqueda + "%");

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("cod_producto"),
                            rs.getString("nombre_producto"),
                            rs.getInt("precio_unitario_venta"),
                            rs.getString("unidad_medida"),
                            rs.getInt("stock_actual"),
                            rs.getInt("stock_minimo"),
                            rs.getInt("id_categoria")
                        });
                    }
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());
            }
        }

        // ================================
        //   ELIMINAR PRODUCTO
        // ================================
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

        cmdSalir1.setBackground(new java.awt.Color(244, 168, 168));
        cmdSalir1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdSalir1.setText("Salir");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(250, 250, 250));

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel39.setText("Administrar Inventario");
        jLabel39.setToolTipText("");

        tblCompra.setAutoCreateRowSorter(true);
        tblCompra.setBackground(new java.awt.Color(237, 237, 237));
        tblCompra.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Producto", "precio unitario venta", "unidad medida", "stock actual", "stock minimo", "Categoria"
            }
        ));
        jScrollPane7.setViewportView(tblCompra);

        jLabel1.setText("Busqueda de productos por nombre:");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel39)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))
                        .addGap(0, 349, Short.MAX_VALUE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(cmdAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGap(98, 98, 98)
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
    int stockAct = Integer.parseInt(tblCompra.getValueAt(fila, 4).toString());
    int stockMin = Integer.parseInt(tblCompra.getValueAt(fila, 5).toString());
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
        this.setVisible(false);
    }//GEN-LAST:event_cmdAgregarActionPerformed

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
    private javax.swing.JButton cmdEditar;
    private javax.swing.JButton cmdEliminar;
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
