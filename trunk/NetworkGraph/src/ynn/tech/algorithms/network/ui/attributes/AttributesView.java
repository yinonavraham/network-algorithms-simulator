package ynn.tech.algorithms.network.ui.attributes;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ynn.network.model.Node;

public class AttributesView extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JTable _table;
	private TableColumn _colName;
	private TableColumn _colValue;
	
	public AttributesView()
	{
		super();
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
	
	public void setNodeAttributes(Node node)
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
					StringBuilder sb = new StringBuilder();
					sb.append(e.getMessage());
					if (e.getException() != null)
					{
						Throwable ex = e.getException();
						while (ex != null)
						{
							sb.append("\nCaused by: ");
							sb.append(ex);
							ex = ex.getCause();
						}
					}
					JOptionPane.showMessageDialog(
						AttributesView.this, sb.toString(), "Error occurred", JOptionPane.ERROR_MESSAGE);
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
}
