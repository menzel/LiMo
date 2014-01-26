package lichen.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import lichen.controller.ColorStack;

@SuppressWarnings("serial")
public class ChooserColorCellRenderer extends DefaultTableCellRenderer { 

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		Color c = ColorStack.getAllColors()[row];

		if(c !=null){ 
			l.setBackground(c);
		}else{
			l.setBackground(Color.white);
		}

		return l;

	}

}
