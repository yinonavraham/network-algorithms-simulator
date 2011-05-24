package ynn.network.model;

import java.util.LinkedList;
import java.util.List;

public class NetworkEvent
{
	public static final int NODES_ADDED = 0;
	public static final int NODES_REMOVED = 1;
	public static final int NODES_CONNECTED = 2;
	public static final int NODES_DISCONNECTED = 3;
	
	private int _type;
	private List<Node> _nodes;
	
	public NetworkEvent(int eventType)
	{
		this(eventType, (Node)null);
	}
	
	public NetworkEvent(int eventType, Node node)
	{
		this(eventType, new Node[] { node });
	}
	
	public NetworkEvent(int eventType, Node[] nodes)
	{
		_type = eventType;
		_nodes = new LinkedList<Node>();
		if (nodes != null) addNodes(nodes);
	}
	
	protected void addNode(Node node)
	{
		if (!_nodes.contains(node)) _nodes.add(node);
	}
	
	protected void addNodes(Node[] nodes)
	{
		for (Node node : nodes) addNode(node);
	}
	
	public List<Node> getNodes()
	{
		return _nodes;
	}
	
	public int getType()
	{
		return _type;
	}
}
