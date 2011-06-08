package ynn.network.ui;

public class SelectionChangedEvent
{
	private AbstractShape[] _oldSelection = null;
	private AbstractShape[] _newSelection = null;
	
	public SelectionChangedEvent(AbstractShape oldSelection, AbstractShape newSelection)
	{
		if (oldSelection != null) _oldSelection = new AbstractShape[] { oldSelection };
		if (newSelection != null) _newSelection = new AbstractShape[] { newSelection };
	}
	
	public SelectionChangedEvent(AbstractShape[] oldSelection, AbstractShape[] newSelection)
	{
		if (oldSelection != null) _oldSelection = oldSelection;
		if (newSelection != null) _newSelection = newSelection;
	}
	
	public AbstractShape[] getOldSelection()
	{
		return _oldSelection;
	}
	
	public AbstractShape[] getNewSelection()
	{
		return _newSelection;
	}
	
	public AbstractShape getFirstOldSelection()
	{
		return _oldSelection == null ? null : _oldSelection[0];
	}
	
	public AbstractShape getFirstNewSelection()
	{
		return _newSelection == null ? null : _newSelection[0];
	}
}
