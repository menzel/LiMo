package lichen.view;

import java.awt.Color;
import java.awt.Component; 
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer; 
import lichen.model.Genus;

/**
 * http://stackoverflow.com/questions/5673430/java-jtable-change-cell-color
 * @author sbrattla
 *
 */
@SuppressWarnings("serial")
public class ColorCellRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		Object id = table.getValueAt(row, 0);
		if(id !=null){
		int idInt = Integer.parseInt(id.toString()); 

			if (Genus.getInstance().returnAll().get(idInt-1).getResults() != null){ 
				Color c = Genus.getInstance().returnAll().get(idInt-1).getResults().getColor(); 
				l.setBackground(c); 
			}else{
				l.setBackground(Color.white);
			}
		}else{
			
				l.setBackground(Color.white);
		}
		return l;

	}
}