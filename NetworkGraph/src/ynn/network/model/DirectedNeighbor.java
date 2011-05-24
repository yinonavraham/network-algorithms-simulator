package ynn.network.model;

public class DirectedNeighbor
{
	private Node _neighbor;
	private boolean _isDirected;
	
	public DirectedNeighbor(Node neighbor, boolean directed)
	{
		_neighbor = neighbor;
		_isDirected = directed;
	}
	
	public DirectedNeighbor(Node neighbor)
	{
		this(neighbor,false);
	}
	
	public void setDirected(boolean directed)
	{
		_isDirected = directed;
	}
	
	public boolean isDirected()
	{
		return _isDirected;
	}
	
	public Node getNeighbor()
	{
		return _neighbor;
	}
}
