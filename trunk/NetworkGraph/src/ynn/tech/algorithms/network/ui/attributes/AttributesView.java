package ynn.tech.algorithms.network.ui.attributes;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.ui.ErrorOccurredListener;

public class AttributesView extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JTable _table;
	private TableColumn _colName;
	private TableColumn _colValue;
	private List<AttributesViewListener> _listeners;
	private List<ErrorOccurredListener> _errorListeners;
	
	public AttributesView()
	{
		super();
		_errorListeners = new LinkedList<ErrorOccurredListener>();
		_listeners = new ArrayList<AttributesViewListener>();
		setLayout(new BorderLayout());
		_table = new JTable();
		initEmptyTable();
		_table.setShowGrid(true);
		_table.setFillsViewportHeight(true);
		add(new JScrollPane(_table),BorderLayout.CENTER);
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		_table.setEnabled(enabled);
		if (!enabled)
		{
			_table.clearSelection();
			_table.editingCanceled(new ChangeEvent(_table));
		}
	}
	
	public void setNodeAttributes(final Node node)
	{
		if (node != null)
		{
			NodeAttributesTableModel model = 
				new NodeAttributesTableModel(node, new String[] {"Name", "Value"});
			model.addTableModelListener(new NodeAttributesTableModelListener()
			{
				@Override
				public void tableChanged(TableModelEvent e) {}
				@Override
				public void valueParseError(NodeAttributesTableModelEvent e)
				{
					fireErrorOccurred(e.getException(), e.getMessage());
				}
				@Override
				public void valueSet(NodeAttributesTableModelEvent e)
				{
					AttributeChangedEvent acme = new AttributeChangedEvent(node, null, null, null);
					fireAttributeChangedManually(acme);
				}
			});
			_table.setModel(model);
			_table.getColumnModel().getColumn(1).setCellEditor(new AttributeValueCellEditor());
		}
		else initEmptyTable();
	}
	
	private void initEmptyTable()
	{
		_table.setModel(new DefaultTableModel());
		_colName = new TableColumn();
		_colName.setHeaderValue("Name");
		_colName.setIdentifier("Name");
		_table.addColumn(_colName);
		_colValue = new TableColumn();
		_colValue.setHeaderValue("Value");
		_colValue.setIdentifier("Value");
		_table.addColumn(_colValue);
	}
	
	public void addErrorListener(ErrorOccurredListener l)
	{
		_errorListeners.add(l);
	}
	
	public void removeErrorListener(ErrorOccurredListener l)
	{
		_errorListeners.remove(l);
	}
	
	protected void fireErrorOccurred(Throwable e, String message)
	{
		for (int i = 0; i < _errorListeners.size(); i++)
		{
			_errorListeners.get(i).errorOccurred(this, e, message);
		}
	}
	
	public void addAttributesViewListener(AttributesViewListener l)
	{
		_listeners.add(l);
	}
	
	public void removeAttributesViewListener(AttributesViewListener l)
	{
		_listeners.remove(l);
	}
	
	protected void fireAttributeChangedManually(AttributeChangedEvent e)
	{
		for (int i = 0; i < _listeners.size(); i++)
		{
			_listeners.get(i).attributeChangedManually(e);
		}
	}
}
