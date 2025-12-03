package views;

import posglagerman.ConexionDB;
import javax.swing.JOptionPane;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public class ReportesPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReportesPage.class.getName());

    /**
     * Creates new form ReportesPage
     */
    public ReportesPage() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        cargarTodo();  
    }
   
    private void cargarTodo() {
        String inicio = "0000-01-01"; // fecha mínima
        String fin = "9999-12-31"; // fecha máxima

        cargarCompras(inicio, fin);
        cargarVentas(inicio, fin);
        cargarGanancias();
    }
    
    
    private int cargarCompras(String inicio, String fin) {
        DefaultTableModel model = (DefaultTableModel) tblCompras.getModel();
        model.setRowCount(0);

        String sql = "SELECT fecha_compra, total_compra, rut_proveedor FROM Compra "
                   + "WHERE DATE(fecha_compra) BETWEEN DATE(?) AND DATE(?)";

        int totalGeneral = 0;

        Connection conn;
        try {
            conn = ConexionDB.getConexion();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtComprasT.setText("0");
            return 0;
        }

        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtComprasT.setText("0");
            return 0;
        }

        try (Connection c = conn;
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, inicio);
            ps.setString(2, fin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String fecha = rs.getString("fecha_compra");
                    int total = rs.getInt("total_compra");
                    String proveedor = rs.getString("rut_proveedor");

                    model.addRow(new Object[]{fecha, proveedor, total});
                    totalGeneral += total;
                }
            }

            txtComprasT.setText(String.valueOf(totalGeneral));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando compras: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtComprasT.setText("0");
            return 0;
        }

        return totalGeneral;
    }


    private int cargarVentas(String inicio, String fin) {
        DefaultTableModel model = (DefaultTableModel) tblVentas.getModel();
        model.setRowCount(0);

        String sql = "SELECT fecha_venta, total_venta FROM Venta "
                   + "WHERE DATE(fecha_venta) BETWEEN DATE(?) AND DATE(?)";

        int totalGeneral = 0;

        Connection conn;
        try {
            conn = ConexionDB.getConexion();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtVentasT.setText("0");
            return 0;
        }

        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtVentasT.setText("0");
            return 0;
        }

        try (Connection c = conn;
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, inicio);
            ps.setString(2, fin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String fecha = rs.getString("fecha_venta");
                    int total = rs.getInt("total_venta");

                    model.addRow(new Object[]{fecha, total});
                    totalGeneral += total;
                }
            }

            txtVentasT.setText(String.valueOf(totalGeneral));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando ventas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtVentasT.setText("0");
            return 0;
        }

        return totalGeneral;
    }

    private void cargarGanancias() {
        long totalVentas;
        long totalCompras;

        try {
            totalVentas = Long.parseLong(txtVentasT.getText().trim());
        } catch (NumberFormatException ignored) {
            JOptionPane.showMessageDialog(this, "Error al cargar ganancias.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            totalCompras = Long.parseLong(txtComprasT.getText().trim());
        } catch (NumberFormatException ignored) {
            JOptionPane.showMessageDialog(this, "Error al cargar ganancias.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long ganancias = totalVentas - totalCompras;
        txtTotal.setText(String.valueOf(ganancias));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        cmdBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmdVolverReportes = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblCompras = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblVentas = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtComprasT = new javax.swing.JTextField();
        txtVentasT = new javax.swing.JTextField();
        jdcFin = new com.toedter.calendar.JDateChooser();
        jdcInicio = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reportes");
        setBackground(new java.awt.Color(250, 250, 250));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel3.setText("REPORTES DE VENTAS Y COMPRAS");
        jLabel3.setToolTipText("");

        cmdBuscar.setBackground(new java.awt.Color(211, 211, 211));
        cmdBuscar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdBuscar.setText("Buscar");
        cmdBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBuscarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("Fecha Inicio:");

        cmdVolverReportes.setBackground(new java.awt.Color(244, 168, 168));
        cmdVolverReportes.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cmdVolverReportes.setText("Volver");
        cmdVolverReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdVolverReportesActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("Fecha fin:");

        tblCompras.setBackground(new java.awt.Color(237, 237, 237));
        tblCompras.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Proveedor", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCompras.setFocusable(false);
        tblCompras.setShowGrid(true);
        jScrollPane8.setViewportView(tblCompras);

        jTabbedPane1.addTab("Compras", jScrollPane8);

        tblVentas.setAutoCreateRowSorter(true);
        tblVentas.setBackground(new java.awt.Color(237, 237, 237));
        tblVentas.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tblVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Total"
            }
        ));
        tblVentas.setFocusable(false);
        tblVentas.setShowGrid(true);
        jScrollPane7.setViewportView(tblVentas);

        jTabbedPane1.addTab("Ventas", jScrollPane7);

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("Ganancias totales:");

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Total Ventas:");

        txtComprasT.setEditable(false);
        txtComprasT.setBackground(new java.awt.Color(237, 237, 237));
        txtComprasT.setFocusable(false);
        txtComprasT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtComprasTActionPerformed(evt);
            }
        });

        txtVentasT.setEditable(false);
        txtVentasT.setBackground(new java.awt.Color(237, 237, 237));
        txtVentasT.setFocusable(false);
        txtVentasT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVentasTActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setText("Total Compras:");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(237, 237, 237));
        txtTotal.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(254, 254, 254)
                        .addComponent(cmdVolverReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(131, 131, 131)
                                .addComponent(jLabel3))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jdcInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jdcFin, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(34, 34, 34)
                                    .addComponent(cmdBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(109, 109, 109)
                                    .addComponent(txtComprasT, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtVentasT, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(jLabel8)
                    .addContainerGap(605, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jdcInicio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jdcFin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cmdBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtComprasT, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtVentasT, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(cmdVolverReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(545, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addGap(125, 125, 125)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBuscarActionPerformed
        Date fechaInicioDate = jdcInicio.getDate();
        Date fechaFinDate = jdcFin.getDate();
        
        if (fechaInicioDate == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una fecha de inicio.", "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SimpleDateFormat fechaFormato = new SimpleDateFormat("yyyy-MM-dd");
        
        String fechaInicio = fechaFormato.format(fechaInicioDate);
        String fechaFin;
        
        if (fechaFinDate == null) {
            fechaFin = "9999-12-31";
        } else {
            // Validar que fin no sea antes que inicio
            if (fechaFinDate.before(fechaInicioDate)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin no puede ser anterior a la fecha de inicio.", "Rango de fechas inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fechaFin = fechaFormato.format(fechaFinDate);
        }
        
        cargarVentas(fechaInicio, fechaFin);
        cargarCompras(fechaInicio, fechaFin);
        cargarGanancias();
    }//GEN-LAST:event_cmdBuscarActionPerformed

    private void cmdVolverReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdVolverReportesActionPerformed
        InicioPage inicioPage = new InicioPage();
        inicioPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cmdVolverReportesActionPerformed

    private void txtComprasTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtComprasTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtComprasTActionPerformed

    private void txtVentasTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVentasTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVentasTActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ReportesPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdBuscar;
    private javax.swing.JButton cmdVolverReportes;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jdcFin;
    private com.toedter.calendar.JDateChooser jdcInicio;
    private javax.swing.JTable tblCompras;
    private javax.swing.JTable tblVentas;
    private javax.swing.JTextField txtComprasT;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtVentasT;
    // End of variables declaration//GEN-END:variables
}
