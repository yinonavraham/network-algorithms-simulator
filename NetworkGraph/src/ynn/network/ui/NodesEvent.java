package ynn.network.ui;

public class NodesEvent
{
	public static final int ADDED = 0;
	public static final int REMOVED = 1;
	public static final int TEXT_CHANGED = 2;

	private int _type;
	private NodeShape[] _nodes;

	public NodesEvent(int type, NodeShape[] nodes)
	{
		_type = type;
		_nodes = nodes;
	}

	public NodesEvent(int type, NodeShape node)
	{
		this(type, new NodeShape[]
		{
			node
		});
	}

	public NodeShape[] getNodes()
	{
		return _nodes;
	}

	public int getType()
	{
		return _type;
	}
}
