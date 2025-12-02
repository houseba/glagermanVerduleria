package Utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StockRenderer extends DefaultTableCellRenderer {

    private final int colStockActual;
    private final int colStockMinimo;

    public StockRenderer(int colStockActual, int colStockMinimo) {
        this.colStockActual = colStockActual;
        this.colStockMinimo = colStockMinimo;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus,int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Resetea colores
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }

        // Convertir fila de vista a modelo
        int modelRow = table.convertRowIndexToModel(row);

        Object stockObj = table.getModel().getValueAt(modelRow, colStockActual);
        Object minObj   = table.getModel().getValueAt(modelRow, colStockMinimo);

        double stock = 0;
        double min   = 0;

        try {
            if (stockObj != null) stock = Double.parseDouble(stockObj.toString());
            if (minObj   != null) min   = Double.parseDouble(minObj.toString());
        } catch (NumberFormatException ex) {
            
        }

        // Si el stock actual es menor o igual al mínimo, pintar la fila
        if (!isSelected && stock <= min) {
            c.setBackground(new Color(255, 204, 204)); // rojo clarito
        }

        return c;
    }
}
