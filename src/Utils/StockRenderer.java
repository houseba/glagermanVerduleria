package Utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class StockRenderer extends DefaultTableCellRenderer {

    // Indices de las columnas
    private final int COL_STOCK_ACTUAL;
    private final int COL_STOCK_MINIMO;

    public StockRenderer(int colStockActual, int colStockMinimo) {
        this.COL_STOCK_ACTUAL = colStockActual;
        this.COL_STOCK_MINIMO = colStockMinimo;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // Pasar de índice de vista a índice del modelo
        int modelRow = table.convertRowIndexToModel(row);
        TableModel model = table.getModel();

        try {
            int stockActual = Integer.parseInt(
                    model.getValueAt(modelRow, COL_STOCK_ACTUAL).toString());
            int stockMinimo = Integer.parseInt(
                    model.getValueAt(modelRow, COL_STOCK_MINIMO).toString());

            if (!isSelected) {
                if (stockActual <= stockMinimo) {
                    // Rojo suave
                    c.setBackground(new Color(255, 180, 180));
                } else {
                    c.setBackground(Color.WHITE);
                }
            } else {
                // Respetar color de selección
                c.setBackground(table.getSelectionBackground());
            }

        } catch (Exception e) {
            // Si algo falla, no se hace nada
            if (!isSelected) {
                c.setBackground(Color.WHITE);
            }
        }

        return c;
    }
}

