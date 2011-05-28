package ynn.network.ui;


public class ConnectorsEvent
{
	public static final int ADDED = 0;
	public static final int REMOVED = 1;
	public static final int DIRECTION_CHANGED = 2;
	
	private int _type;
	private ConnectorShape[] _connectors;
	
	public ConnectorsEvent(int type, ConnectorShape[] connectors)
	{
		_type = type;
		_connectors = connectors;
	}
	
	public ConnectorsEvent(int type, ConnectorShape connectors)
	{
		this(type, new ConnectorShape[] { connectors });
	}
	
	public ConnectorShape[] getConnectors()
	{
		return _connectors;
	}
	
	public int getType()
	{
		return _type;
	}
}
