package ynn.tech.algorithms.network.ui.attributes;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class NodeAttributesTableModelEvent extends TableModelEvent
{
	private static final long serialVersionUID = 1L;
	
	private Throwable _e = null;
	private String _message = null;

	public NodeAttributesTableModelEvent(TableModel source, int firstRow, int lastRow, int column,
		int type)
	{
		super(source, firstRow, lastRow, column, type);
	}

	public NodeAttributesTableModelEvent(TableModel source, int firstRow, int lastRow, int column)
	{
		super(source, firstRow, lastRow, column);
	}

	public NodeAttributesTableModelEvent(TableModel source, int firstRow, int lastRow)
	{
		super(source, firstRow, lastRow);
	}

	public NodeAttributesTableModelEvent(TableModel source, int row)
	{
		super(source, row);
	}

	public NodeAttributesTableModelEvent(TableModel source)
	{
		super(source);
	}

	public NodeAttributesTableModelEvent(TableModel source, int row, int column, Throwable e, String message)
	{
		super(source, row);
		this.column = column;
		_e = e;
		_message = message;
	}
	
	public Throwable getException()
	{
		return _e;
	}
	
	public String getMessage()
	{
		return _message;
	}
}
