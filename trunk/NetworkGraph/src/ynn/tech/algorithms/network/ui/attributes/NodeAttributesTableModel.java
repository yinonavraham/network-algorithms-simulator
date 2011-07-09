package ynn.tech.algorithms.network.ui.attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import ynn.network.model.Attribute;
import ynn.network.model.INodeListener;
import ynn.network.model.Node;
import ynn.network.model.NodeAttributeEvent;
import ynn.network.model.NodeNeighborEvent;


public class NodeAttributesTableModel implements TableModel
{
	private Node _node;
	private String[] _headers;
	private List<TableModelListener> _listeners;
	private Attribute[] _orderedAttributes;
	
	public NodeAttributesTableModel(Node node, String[] headers)
	{
		_node = node;
		_headers = headers;
		_listeners = new ArrayList<TableModelListener>();
		_orderedAttributes = node.getAttributes().toArray(new Attribute[node.getAttributes().size()]);
		_node.addListener(new INodeListener()
		{	
			@Override
			public void nodeNeighborsChanged(NodeNeighborEvent e) {}
			@Override
			public void nodeAttributeChanged(NodeAttributeEvent e)
			{
				handleNodeAttributeChanged(e);
			}
		});
	}

	protected void handleNodeAttributeChanged(NodeAttributeEvent e)
	{
		TableModelEvent tme = new TableModelEvent(this, getAttributeRow(e.getAttribute()));
		fireTableModelEvent(tme);
	}
	
	private int getAttributeRow(Attribute attribute)
	{
		int index = -1;
		for (int i = 0; i < _orderedAttributes.length; i++)
		{
			if (_orderedAttributes[i].equals(attribute))
			{
				index = i;
				break;
			}
		}
		return index;
	}

	private void fireTableModelEvent(TableModelEvent e)
	{
		for (TableModelListener l : _listeners)
		{
			l.tableChanged(e);
		}
	}

	private void fireValueParseErrorEvent(NodeAttributesTableModelEvent e)
	{
		for (TableModelListener l : _listeners)
		{
			if (l instanceof NodeAttributesTableModelListener)
				((NodeAttributesTableModelListener)l).valueParseError(e);
		}
	}

	private void fireValueSetEvent(NodeAttributesTableModelEvent e)
	{
		for (TableModelListener l : _listeners)
		{
			if (l instanceof NodeAttributesTableModelListener)
				((NodeAttributesTableModelListener)l).valueSet(e);
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l)
	{
		_listeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == 0) return Attribute.class;
		else if (columnIndex == 1) return Object.class;
		else return null;
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		if (columnIndex >= 0 && columnIndex < _headers.length) return _headers[columnIndex];
		else return null;
	}

	@Override
	public int getRowCount()
	{
		return _orderedAttributes.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		if (colIndex == 0) return _orderedAttributes[rowIndex];
		else if (colIndex == 1) return _node.getAttributeValue(_orderedAttributes[rowIndex]);
		else return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int colIndex)
	{
		return true;
	}

	@Override
	public void removeTableModelListener(TableModelListener l)
	{
		_listeners.remove(l);
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex)
	{
		if (colIndex == 1)
		{
			Class<?> type = _orderedAttributes[rowIndex].getType();
			Object oldValue = _node.getAttributeValue(_orderedAttributes[rowIndex]);
			if (type.isInstance(value))
			{
				if (oldValue != value && value != null && !value.equals(oldValue))
				{
					_node.putAttribute(_orderedAttributes[rowIndex],value);
					NodeAttributesTableModelEvent e = 
						new NodeAttributesTableModelEvent(
							this, rowIndex, colIndex, null, String.format(
								"Value was set to ", ""+value));
					fireValueSetEvent(e);
				}
			}
			else
			{
				try
				{
					Method mValueOf = type.getMethod("valueOf",String.class);
					Object res = mValueOf.invoke(null, value);
					if (res != null && type.isInstance(res) && !res.equals(oldValue))
					{
						_node.putAttribute(_orderedAttributes[rowIndex],res);
						NodeAttributesTableModelEvent e = 
							new NodeAttributesTableModelEvent(
								this, rowIndex, colIndex, null, String.format(
									"Value was set to ", ""+res));
						fireValueSetEvent(e);
					}
					else
					{
						NodeAttributesTableModelEvent e = 
							new NodeAttributesTableModelEvent(
								this, rowIndex, colIndex, null, String.format(
									"Failed to parse '%s' to %s", ""+value, type.getSimpleName()));
						fireValueParseErrorEvent(e);
					}
				}
				catch (Throwable ex)
				{
					if (InvocationTargetException.class.equals(ex.getClass())) ex = ex.getCause();
					NodeAttributesTableModelEvent e = 
						new NodeAttributesTableModelEvent(
							this, rowIndex, colIndex, ex, String.format(
								"Failed to parse '%s' to %s", ""+value, type.getSimpleName()));
					fireValueParseErrorEvent(e);
				}
			}
		}
	}

}
