package lichen.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import lichen.controller.ManualAnalyzer;
import lichen.model.Measurement;
import lichen.model.MeasurementsFactory;
/**
 * 
 */
@SuppressWarnings("serial")
public class ManualColorCellRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		if(ManualAnalyzer.getActive()){ 

			List<Measurement> lichen = MeasurementsFactory.getInstance().returnAll();

			try{
				Color c = lichen.get(row).getColor();
				l.setBackground(c);
				return l;

			}catch (IndexOutOfBoundsException e){
				l.setBackground(Color.white);
				return l;
			}
		}

		return l;

	}
}