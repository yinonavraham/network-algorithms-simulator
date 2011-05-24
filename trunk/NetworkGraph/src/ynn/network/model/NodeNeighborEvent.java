package ynn.network.model;

public class NodeNeighborEvent
{
	public static final int ADDED = 0;
	public static final int REMOVED = 1;
	
	private Node _node;
	private Node _neighbor;
	private int _type;
	
	public NodeNeighborEvent(int type, Node node, Node neighbor)
	{
		_type = type;
		_node = node;
		_neighbor = neighbor;
	}
	
	public int getType()
	{
		return _type;
	}
	
	public Node getNode()
	{
		return _node;
	}
	
	public Node getNeighbor()
	{
		return _neighbor;
	}
}
