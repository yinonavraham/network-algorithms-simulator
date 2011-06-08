package ynn.tech.algorithms.network.ui.attributes;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import ynn.network.model.Attribute;

public class AttributeValueCellEditor implements TableCellEditor, CellEditorListener
{
	private DefaultCellEditor _textCellEditor;
	private DefaultCellEditor _checkBoxCellEditor;
	private DefaultCellEditor _comboBoxCellEditor;
	private DefaultCellEditor _currentCellEditor;
	private List<CellEditorListener> _listeners;
	
	public AttributeValueCellEditor()
	{
		_listeners = new ArrayList<CellEditorListener>();
		_textCellEditor = new DefaultCellEditor(new JTextField());
		_textCellEditor.addCellEditorListener(this);
		_checkBoxCellEditor = new DefaultCellEditor(new JCheckBox());
		_checkBoxCellEditor.addCellEditorListener(this);
		_comboBoxCellEditor = new DefaultCellEditor(new JComboBox());
		_comboBoxCellEditor.addCellEditorListener(this);
		_currentCellEditor = null;
	}

	@Override
	public void addCellEditorListener(CellEditorListener l)
	{
		_listeners.add(l);
	}

	@Override
	public void cancelCellEditing()
	{
		if (_currentCellEditor != null)
		{
			_currentCellEditor.cancelCellEditing();
		}
	}

	@Override
	public Object getCellEditorValue()
	{
		if (_currentCellEditor != null)
			return _currentCellEditor.getCellEditorValue();
		else return null;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l)
	{
		_listeners.remove(l);
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		if (_currentCellEditor != null)
			return _currentCellEditor.shouldSelectCell(anEvent);
		else return false;
	}

	@Override
	public boolean stopCellEditing()
	{
		if (_currentCellEditor != null)
		{
			return _currentCellEditor.stopCellEditing();
		}
		return true;
	}

	@Override
	public Component getTableCellEditorComponent(
		JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column)
	{
		if (column == 0) return null;
		NodeAttributesTableModel model = 
			table.getModel() != null && table.getModel() instanceof NodeAttributesTableModel ?
			(NodeAttributesTableModel)table.getModel() : null;
		if (model != null)
		{
			Object objAttr = model.getValueAt(row, 0);
			Object objValue = model.getValueAt(row, column);
			if (objAttr instanceof Attribute)
			{
				Attribute attr = (Attribute)objAttr;
				Class<?> type = attr.getType();
				if (Boolean.class.equals(type))
				{
					_currentCellEditor = _checkBoxCellEditor;
				}
				else if (type.isEnum())
				{
					_currentCellEditor = _comboBoxCellEditor;
					JComboBox comboBox = (JComboBox)_comboBoxCellEditor.getComponent();
					comboBox.removeAllItems();
					try
					{
						Method mValues = type.getMethod("values");
						Object res = mValues.invoke(null);
						if (res != null && res.getClass().isArray())
						{
							Object[] values = (Object[])res;
							for (Object val : values) comboBox.addItem(val); 
						}
//						DirectionEnum[] values = DirectionEnum.values();
//						for (DirectionEnum dir : values) comboBox.addItem(dir);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					_currentCellEditor = _textCellEditor;
				}
				return _currentCellEditor.getTableCellEditorComponent(
					table, objValue, isSelected, row, column);
			}
			else return null;
		}
		else return null;
	}

	@Override
	public void editingCanceled(ChangeEvent e)
	{
		ChangeEvent newE = new ChangeEvent(this);
		fireEditingCanceled(newE);
	}

	@Override
	public void editingStopped(ChangeEvent e)
	{
		ChangeEvent newE = new ChangeEvent(this);
		fireEditingStopped(newE);
	}
	
	private void fireEditingCanceled(ChangeEvent e)
	{
		for (int i = 0; i < _listeners.size(); i++)
		{
			_listeners.get(i).editingCanceled(e);
		}
	}
	
	private void fireEditingStopped(ChangeEvent e)
	{
		for (int i = 0; i < _listeners.size(); i++)
		{
			_listeners.get(i).editingStopped(e);
		}
	}
}
