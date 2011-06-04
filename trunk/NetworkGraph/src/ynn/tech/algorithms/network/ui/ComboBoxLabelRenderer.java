package ynn.tech.algorithms.network.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxLabelRenderer extends JLabel implements ListCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	private boolean _displayText;
	
	public ComboBoxLabelRenderer(boolean displayText) 
	{
		setOpaque(true);
		setHorizontalAlignment(LEADING);
		setVerticalAlignment(CENTER);
		_displayText = displayText;
	}
	
	public ComboBoxLabelRenderer()
	{
		this(true);
	}

	@Override
	public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
	{	
		if (isSelected) 
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} 
		else 
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		ComboBoxLabelItemModel item = value instanceof ComboBoxLabelItemModel ?
			(ComboBoxLabelItemModel)value : null;
		setFont(list.getFont());
		if (item != null)
		{
			if (_displayText) setText(item.getText());
			setIcon(item.getIcon());
			setToolTipText(item.getToolTip());
		}
		else
		{
			if (_displayText) setText(value.toString());
		}
		return this;
	}
}
