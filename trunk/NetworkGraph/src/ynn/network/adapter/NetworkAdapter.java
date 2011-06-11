package ynn.network.adapter;

import ynn.network.model.DefaultNodeFactory;
import ynn.network.model.Direction;
import ynn.network.model.INetworkModelListener;
import ynn.network.model.INodeFactory;
import ynn.network.model.NetworkEvent;
import ynn.network.model.NetworkModel;
import ynn.network.model.Node;
import ynn.network.model.NodeAttributeEvent;
import ynn.network.ui.ConnectorShape;
import ynn.network.ui.ConnectorsEvent;
import ynn.network.ui.INetworkViewListener;
import ynn.network.ui.NetworkView;
import ynn.network.ui.NodeShape;
import ynn.network.ui.NodesEvent;
import ynn.network.ui.SelectionChangedEvent;

public class NetworkAdapter
{
	private INetworkModelListener _modelListener;
	private NetworkModel _model = null;
	private INetworkViewListener _viewListener;
	private NetworkView _view = null;
	private INodeFactory _nodeFactory = null;

	public NetworkAdapter()
	{
		_modelListener = new INetworkModelListener()
		{
			@Override
			public void nodeAttributeChanged(NodeAttributeEvent e)
			{
				handleModelNodeAttributeChanged(e);
			}

			@Override
			public void networkChanged(NetworkEvent e)
			{
				handleModelNetworkChanged(e);
			}
		};
		_viewListener = new INetworkViewListener()
		{
			@Override
			public void nodesChanged(NodesEvent e)
			{
				handleViewNodesChanged(e);
			}

			@Override
			public void connectorsChanged(ConnectorsEvent e)
			{
				handleViewConnectorsChanged(e);
			}

			@Override
			public void selectionChanged(SelectionChangedEvent e)
			{
				handleViewSelectionChanged(e);
			}
		};
	}
	
	public void setNodeFactory(INodeFactory factory)
	{
		_nodeFactory = factory;
	}
	
	public INodeFactory getNodeFactory()
	{
		if (_nodeFactory == null) _nodeFactory = new DefaultNodeFactory();
		return _nodeFactory; 
	}

	public void attach(NetworkModel model)
	{
		model.addListener(_modelListener);
		_model = model;
	}

	public void attach(NetworkView view)
	{
		view.addListener(_viewListener);
		_view = view;
	}

	public void detach(NetworkModel model)
	{
		model.removeListener(_modelListener);
		_model = null;
	}

	public void detach(NetworkView view)
	{
		view.removeListener(_viewListener);
		_view = null;
	}

	protected void handleViewConnectorsChanged(ConnectorsEvent e)
	{
		switch (e.getType())
		{
			case ConnectorsEvent.ADDED:
				for (ConnectorShape connector : e.getConnectors())
				{
					if (connector.getVertex1().getData() instanceof Node &&
						connector.getVertex2().getData() instanceof Node)
					{
						Node node1 = (Node)connector.getVertex1().getData();
						Node node2 = (Node)connector.getVertex2().getData();
						Direction direction = connectorShapeDirectionToDirection(connector.getDirection());
						_model.connectNodes(node1, node2);
						_model.setNeighborsDirection(node1, node2, direction);
					}
				}
				break;
			case ConnectorsEvent.REMOVED:
				for (ConnectorShape connector : e.getConnectors())
				{
					if (connector.getVertex1().getData() instanceof Node &&
						connector.getVertex2().getData() instanceof Node)
					{
						Node node1 = (Node)connector.getVertex1().getData();
						Node node2 = (Node)connector.getVertex2().getData();
						_model.disconnectNodes(node1, node2);
					}
				}
				break;
			case ConnectorsEvent.DIRECTION_CHANGED:
				for (ConnectorShape connector : e.getConnectors())
				{
					if (connector.getVertex1().getData() instanceof Node &&
						connector.getVertex2().getData() instanceof Node)
					{
						Node node1 = (Node)connector.getVertex1().getData();
						Node node2 = (Node)connector.getVertex2().getData();
						ConnectorShape.Direction csDirection = connector.getDirection();
						Direction direction = connectorShapeDirectionToDirection(csDirection);
						_model.setNeighborsDirection(node1, node2, direction);
					}
				}
				break;
		}
	}

	protected void handleViewNodesChanged(NodesEvent e)
	{
		switch (e.getType())
		{
		case NodesEvent.ADDED:
			for (NodeShape nodeShape : e.getNodes())
			{
				Object nodeObj = nodeShape.getData();
				Node node;
				if (nodeObj == null)
				{
					node = getNodeFactory().createNode();
					nodeShape.setData(node);
					node.setName(nodeShape.getText());
				}
				else node = (Node)nodeObj;
				_model.addNode(node);
			}
			break;
		case NodesEvent.REMOVED:
			for (NodeShape nodeShape : e.getNodes())
			{
				if (nodeShape.getData() instanceof Node) 
				{
					Node node = (Node)nodeShape.getData();
					_model.removeNode(node);
				}
			}
			break;
		}
	}

	protected void handleViewSelectionChanged(SelectionChangedEvent e)
	{
		// TODO
	}

	protected void handleModelNetworkChanged(NetworkEvent e)
	{
		ConnectorShape connector;
		switch (e.getType())
		{
		case NetworkEvent.NODES_ADDED:
			break;
		case NetworkEvent.NODES_REMOVED:
			break;
		case NetworkEvent.NODES_CONNECTED:
			if (findConnectorShapeOf(e.getNodes().get(0), e.getNodes().get(1)) != null) break;
			connector = new ConnectorShape();
			NodeShape node1 = findNodeShapeOf(e.getNodes().get(0));
			NodeShape node2 = findNodeShapeOf(e.getNodes().get(1));
			connector.setVertex1(node1);
			connector.setVertex2(node2);
			_view.addShape(connector);
			break;
		case NetworkEvent.NODES_DISCONNECTED:
			connector = findConnectorShapeOf(e.getNodes().get(0), e.getNodes().get(1));
			_view.removeShape(connector);
			break;
		case NetworkEvent.NODES_CONNECTION_DIRECTION_CHANGED:
			Node node = e.getNodes().get(0);
			Node neighbor = e.getNodes().get(1);
			connector = findConnectorShapeOf(node, neighbor);
			connector.setDirection(directionToConnectorShapeDirection(connector,node,neighbor));
			break;
		}
	}

	private ConnectorShape.Direction directionToConnectorShapeDirection(
		ConnectorShape connector, Node node, Node neighbor)
	{
		Direction direction = node.getNeighborDirection(neighbor);
		if (direction == null) return null;
		Object data1 = connector.getVertex1().getData();
		switch (direction)
		{
			case Both: return ConnectorShape.Direction.Both;
			case Default:
				if (node.equals(data1)) return ConnectorShape.Direction.Default;
				else return ConnectorShape.Direction.Other;
			case Other: 
				if (node.equals(data1)) return ConnectorShape.Direction.Other;
				else return ConnectorShape.Direction.Default;
			case None: return ConnectorShape.Direction.None;
		}
		return null;
	}

	private Direction connectorShapeDirectionToDirection(ConnectorShape.Direction direction)
	{
		if (direction == null) return null;
		switch (direction)
		{
			case Both: return Direction.Both;
			case Default: return Direction.Default;
			case Other: return Direction.Other;
			case None: return Direction.None;
		}
		return null;
	}

	private void handleModelNodeAttributeChanged(NodeAttributeEvent e)
	{
		switch (e.getType())
		{
		case NodeAttributeEvent.ADDED:
			break;
		case NodeAttributeEvent.REMOVED:
			break;
		case NodeAttributeEvent.CHANGED:
			_view.repaint();
			break;
		}	
	}
	
	private NodeShape findNodeShapeOf(Node node)
	{
		for (NodeShape shape : _view.getNodes())
		{
			if (shape.getData() != null && shape.getData().equals(node))
				return shape;
		}
		return null;
	}
	
	private ConnectorShape findConnectorShapeOf(Node node1, Node node2)
	{
		NodeShape nodeShape1 = findNodeShapeOf(node1);
		NodeShape nodeShape2 = findNodeShapeOf(node2);
		if (nodeShape1 == null || nodeShape2 == null) return null;
		for (ConnectorShape connector : _view.getConnectors())
		{
			if (nodeShape1.equals(connector.getVertex1()) &&
				nodeShape2.equals(connector.getVertex2()) ||
				nodeShape1.equals(connector.getVertex2()) &&
				nodeShape2.equals(connector.getVertex1()))
				return connector;
		}
		return null;
	}
}
