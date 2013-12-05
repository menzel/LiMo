package lichen.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import lichen.model.Measurement;
import lichen.model.MeasurementsFactory;
/**
 * 
 */
@SuppressWarnings("serial")
public class ResultsColorCellRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		List<Measurement> lichen = MeasurementsFactory.getInstance().returnAll();

		Color c = lichen.get(row).getColor();
		if(c !=null){ 
			l.setBackground(c);
		}else{
			l.setBackground(Color.white);
		}

		return l;

	}
}