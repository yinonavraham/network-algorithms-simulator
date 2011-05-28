package ynn.network.model;

import java.util.LinkedList;
import java.util.List;

public class NetworkModel implements INodeListener
{
	private List<Node> _nodes;
	private List<INetworkModelListener> _listeners;

	public NetworkModel()
	{
		_nodes = new LinkedList<Node>();
		_listeners = new LinkedList<INetworkModelListener>();
	}
	
	public void addListener(INetworkModelListener l)
	{
		if (!_listeners.contains(l)) _listeners.add(l);
	}
	
	public void removeListener(INetworkModelListener l)
	{
		if (_listeners.contains(l)) _listeners.remove(l);
	}
	
	private void fireNetworkChanged(int eventType, Node[] nodes)
	{
		if (nodes != null)
		{
			NetworkEvent e = new NetworkEvent(eventType, nodes);
			for (INetworkModelListener l : _listeners)
			{
				l.networkChanged(e);
			}
		}
	}
	
	public List<Node> getNodes()
	{
		return _nodes;
	}
	
	public void setNodes(List<Node> nodes)
	{
		removeNodes((Node[])getNodes().toArray());
		addNodes((Node[])nodes.toArray());
	}
	
	public void addNode(Node node)
	{
		if (!_nodes.contains(node))
		{
			_nodes.add(node);
			node.addListener(this);
			fireNetworkChanged(NetworkEvent.NODES_ADDED, new Node[] { node });
		}
	}
	
	public void addNodes(Node[] nodes)
	{
		List<Node> addedNodes = new LinkedList<Node>();
		for (Node node : nodes)
		{
			if (!_nodes.contains(node))
			{
				_nodes.add(node);
				node.addListener(this);
				addedNodes.add(node);
			}
		}
		fireNetworkChanged(NetworkEvent.NODES_ADDED, (Node[])addedNodes.toArray());
	}
	
	public void removeNode(Node node)
	{
		if (_nodes.contains(node))
		{
			_nodes.remove(node);
			node.removeListener(this);
			fireNetworkChanged(NetworkEvent.NODES_REMOVED, new Node[] { node });
		}
	}
	
	public void removeNodes(Node[] nodes)
	{
		List<Node> removedNodes = new LinkedList<Node>();
		for (Node node : nodes)
		{
			if (_nodes.contains(node))
			{
				_nodes.remove(node);
				node.removeListener(this);
				removedNodes.add(node);
			}
		}
		fireNetworkChanged(NetworkEvent.NODES_REMOVED, (Node[])removedNodes.toArray());
	}
	
	public void connectNodes(Node node1, Node node2)
	{
		if (node1 != null && node2 != null && !node1.isNeighborOf(node2)) 
		{
			node1.addNeighbor(node2);
			fireNetworkChanged(NetworkEvent.NODES_CONNECTED, new Node[] { node1, node2 });
		}
	}
	
	public void disconnectNodes(Node node1, Node node2)
	{
		if (node1 != null && node2 != null && node1.isNeighborOf(node2)) 
		{
			node1.removeNeighbor(node2);
			fireNetworkChanged(NetworkEvent.NODES_DISCONNECTED, new Node[] { node1, node2 });
		}
	}
	
	private void fireNodeAttributeChanged(NodeAttributeEvent e)
	{
		for (INetworkModelListener l : _listeners) l.nodeAttributeChanged(e);
	}

	@Override
	public void nodeAttributeChanged(NodeAttributeEvent e)
	{
		fireNodeAttributeChanged(e);
	}

	@Override
	public void nodeNeighborsChanged(NodeNeighborEvent e)
	{
		switch (e.getType())
		{
			case NodeNeighborEvent.ADDED:
				fireNetworkChanged(NetworkEvent.NODES_CONNECTED, new Node[] {e.getNode(), e.getNeighbor()});
				break;
			case NodeNeighborEvent.REMOVED:
				fireNetworkChanged(NetworkEvent.NODES_DISCONNECTED, new Node[] {e.getNode(), e.getNeighbor()});
				break;
			case NodeNeighborEvent.DIRECTION_CHANGED:
				fireNetworkChanged(NetworkEvent.NODES_CONNECTION_DIRECTION_CHANGED, new Node[] {e.getNode(), e.getNeighbor()});
				break;
		}
	}

	public void setNeighborsDirection(Node node1, Node node2, Direction direction)
	{
		if (node1 != null && node2 != null && node1.isNeighborOf(node2))
		{
			direction = direction == null ? Direction.None : direction;
			if (!direction.equals(node1.getNeighborDirection(node2)))
			{
				node1.setNeighborDirection(node2, direction);
				// The DIRECTION_CHANGED event will be fired by node1 and propagated as a network event. 
			}
		}
	}
}
