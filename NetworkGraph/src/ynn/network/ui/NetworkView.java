package ynn.network.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import ynn.network.model.Node;
import ynn.network.ui.ConnectorShape.Direction;

public class NetworkView extends JPanel
{
	private static final long serialVersionUID = 7327880761001439913L;
	
	public enum Mode { View, Move, Connect }

	private List<AbstractShape> _shapes;
	private AbstractShape _selectedShape;
	private Point _dragSource = null;
	private Point _mouseLocation = null;
	private Mode _mode = Mode.Move;
	private List<INetworkViewListener> _listeners;

	public NetworkView()
	{
		super();
		setToolTipText("");
		_listeners = new LinkedList<INetworkViewListener>();
		_shapes = new LinkedList<AbstractShape>();
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
				setSelectedShape(null);
				for (int i = 0; i < _shapes.size(); i++)
				{
					AbstractShape shape = _shapes.get(i);
					if (shape.contains(e.getPoint()))
					{
						setSelectedShape(shape);
						break;
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				super.mousePressed(e);
				setSelectedShape(null);
				for (int i = 0; i < _shapes.size(); i++)
				{
					AbstractShape shape = _shapes.get(i);
					if (shape.contains(e.getPoint()))
					{
						setSelectedShape(shape);
						if (!(shape instanceof ConnectorShape))
						{
							_shapes.remove(shape);
							_shapes.add(0, shape);
							_dragSource = e.getPoint();
							if (_mode == Mode.Move)
							{
							}
							if (_mode == Mode.Connect)
							{
								ConnectorShape connector = new ConnectorShape();
								setSelectedShape(connector);
								connector.setVertex1(shape);
								connector.setPoint2(e.getPoint().getX(), e.getPoint().getY());
								_shapes.add(connector);
							}
						}
						break;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				if (_dragSource != null)
				{
					if (_mode == Mode.Move)
					{
						_selectedShape.move(e.getPoint().x - _dragSource.x, e.getPoint().y - _dragSource.y);
					}
					else if (_mode == Mode.Connect && _selectedShape != null && _selectedShape instanceof ConnectorShape)
					{
						ConnectorShape connector = (ConnectorShape)_selectedShape;
						int i = 0;
						for (i = 0; i < _shapes.size(); i++)
						{
							AbstractShape shape = _shapes.get(i);
							if (shape.contains(e.getPoint()) && 
								!(shape instanceof ConnectorShape) && 
								!shape.equals(connector.getVertex1()))
							{
								connector.setVertex2(shape);
								setSelectedShape(null);
								break;
							}
						}
						if (i >= _shapes.size()) 
						{
							_shapes.remove(connector);
							connector.setVertex1(null);
						}
						else fireConnectorsAdded(new ConnectorShape[] { connector });
					}
					_dragSource = null;
					NetworkView.this.repaint();
				}
			}
		});
		addMouseMotionListener(new MouseMotionListener()
		{

			@Override
			public void mouseMoved(MouseEvent e)
			{
				_mouseLocation = e.getPoint();
				NetworkView.this.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e)
			{
				_mouseLocation = e.getPoint();
				if (_dragSource != null)
				{
					if (_mode == Mode.Move)
					{
						_selectedShape.move(e.getPoint().x - _dragSource.x, e.getPoint().y - _dragSource.y);
						_dragSource = e.getPoint();
					}
					else if (_mode == Mode.Connect)
					{
						ConnectorShape connector = (ConnectorShape) _selectedShape;
						connector.setPoint2(e.getPoint().getX(), e.getPoint().getY());
					}
					NetworkView.this.repaint();
				}
			}
		});
	}
	
	@Override
	public String getToolTipText(MouseEvent e)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < _shapes.size(); i++)
		{
			AbstractShape shape = _shapes.get(i);
			if (shape.contains(e.getPoint()))
			{
				if (shape instanceof ConnectorShape)
				{
					ConnectorShape connector = (ConnectorShape)shape;
					sb.append(connector.toString());
				}
				else if (shape instanceof NodeShape)
				{
					NodeShape node = (NodeShape)shape;
					sb.append("<html>");
					sb.append("<b><u>" + node.getText() + "</u></b>");
					if (node.getData() != null && node.getData() instanceof Node)
					{
						Node dataNode = (Node)node.getData();
						for (String attr : dataNode.getAttributesNames())
						{
							Object value = dataNode.getAttributeValue(attr);
							String sValue = value != null ? value.toString() : "";
							sb.append(String.format("<br>%s: %s", attr, sValue));
						}
					}
					sb.append("</html>");
				}
				break;
			}
		}
		return sb.toString();
	}

	private void setSelectedShape(AbstractShape shape)
	{
		AbstractShape oldSelection = _selectedShape;
		AbstractShape newSelection = shape;
		if (_selectedShape != null)
		{
			_selectedShape.setSelected(false);
		}
		_selectedShape = shape;
		if (_selectedShape != null)
		{
			_selectedShape.setSelected(true);
		}
		repaint();
		fireSelectionChanged(oldSelection, newSelection);
	}
	
	public AbstractShape[] getSelectedShapes()
	{
		if (_selectedShape != null) return new AbstractShape[] { _selectedShape };
		else return new AbstractShape[0];
	}
	
	public void addShape(AbstractShape shape)
	{
		if (_mode != Mode.View)
		{
			if (shape instanceof NodeShape) 
			{
				_shapes.add(0, shape);
				fireNodesAdded(new NodeShape[] { ( NodeShape)shape });
			}	
			else if (shape instanceof ConnectorShape)
			{
				_shapes.add(shape);
				fireConnectorsAdded(new ConnectorShape[] { ( ConnectorShape)shape });
			}
			repaint();
		}
	}
	
	public void removeSelectedShape()
	{
		removeShape(_selectedShape);
	}
	
	public void removeShape(AbstractShape shape)
	{
		if (shape != null && _mode != Mode.View)
		{
			_shapes.remove(shape);
			IShapeListener[] listeners = new IShapeListener[shape.getShapeListeners().size()];
			shape.getShapeListeners().toArray(listeners);
			for (IShapeListener l : listeners)
			{
				if (l instanceof ConnectorShape)
				{
					ConnectorShape connector = (ConnectorShape) l;
					_shapes.remove(connector);
					fireConnectorsRemoved(new ConnectorShape[] { connector });
					connector.dispose();
				}
			}
			if (shape instanceof NodeShape)
				fireNodesRemoved(new NodeShape[] { (NodeShape)shape });
			else if (shape instanceof ConnectorShape)
				fireConnectorsRemoved(new ConnectorShape[] { (ConnectorShape)shape });
			shape.dispose();
			if (shape.equals(_selectedShape)) setSelectedShape(null);
		}
	}
	
	public void setMode(Mode mode)
	{
		_mode = mode;
	}
	
	public void setConnectorsDirection(ConnectorShape[] connectors, Direction direction)
	{
		if (connectors != null)
		{
			direction = direction == null ? Direction.None : direction;
			for (ConnectorShape connector : connectors)
			{
				connector.setDirection(direction);
			}
			fireConnectorsDirectionChanged(connectors);
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int hoveredIndex = -1;
		if (_mouseLocation != null)
		{
			for (hoveredIndex = 0; hoveredIndex < _shapes.size(); hoveredIndex++)
			{
				if (_shapes.get(hoveredIndex).contains(_mouseLocation)) break;
			}
		}
		for (int i = _shapes.size() - 1; i >= 0; i--)
		{
			AbstractShape shape = _shapes.get(i);
			if (hoveredIndex == i)
			{
				shape.draw(g2, true);
			}
			else shape.draw(g2);
		}
	}
	
	public void addListener(INetworkViewListener l)
	{
		if (!_listeners.contains(l)) _listeners.add(l);
	}
	
	public void removeListener(INetworkViewListener l)
	{
		if (_listeners.contains(l)) _listeners.remove(l);
	}

	private void fireNodesAdded(NodeShape[] nodes)
	{
		NodesEvent e = new NodesEvent(NodesEvent.ADDED, nodes);
		for (INetworkViewListener l : _listeners) l.nodesChanged(e);
	}

	private void fireNodesRemoved(NodeShape[] nodes)
	{
		NodesEvent e = new NodesEvent(NodesEvent.REMOVED, nodes);
		for (INetworkViewListener l : _listeners) l.nodesChanged(e);
	}

	private void fireConnectorsAdded(ConnectorShape[] connectors)
	{
		ConnectorsEvent e = new ConnectorsEvent(ConnectorsEvent.ADDED, connectors);
		for (INetworkViewListener l : _listeners) l.connectorsChanged(e);
	}

	private void fireConnectorsRemoved(ConnectorShape[] connectors)
	{
		ConnectorsEvent e = new ConnectorsEvent(ConnectorsEvent.REMOVED, connectors);
		for (INetworkViewListener l : _listeners) l.connectorsChanged(e);
	}

	private void fireConnectorsDirectionChanged(ConnectorShape[] connectors)
	{
		ConnectorsEvent e = new ConnectorsEvent(ConnectorsEvent.DIRECTION_CHANGED, connectors);
		for (INetworkViewListener l : _listeners) l.connectorsChanged(e);
	}

//	private void fireSelectionChanged(AbstractShape[] oldSelection, AbstractShape[] newSelection)
//	{
//		SelectionChangedEvent e = new SelectionChangedEvent(oldSelection, newSelection); 
//		for (INetworkViewListener l : _listeners) l.selectionChanged(e);
//	}

	private void fireSelectionChanged(AbstractShape oldSelection, AbstractShape newSelection)
	{
		SelectionChangedEvent e = new SelectionChangedEvent(oldSelection, newSelection); 
		for (INetworkViewListener l : _listeners) l.selectionChanged(e);
	}
	
	public List<NodeShape> getNodes()
	{
		List<NodeShape> nodes = new LinkedList<NodeShape>();
		for (AbstractShape shape : _shapes)
		{
			if (shape instanceof NodeShape) nodes.add((NodeShape)shape);
		}
		return nodes;
	}
	
	public List<ConnectorShape> getConnectors()
	{
		List<ConnectorShape> connectors = new LinkedList<ConnectorShape>();
		for (AbstractShape shape : _shapes)
		{
			if (shape instanceof ConnectorShape) connectors.add((ConnectorShape)shape);
		}
		return connectors;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		int width = 0;
		int height = 0;
		int x, y;
		for (AbstractShape shape : _shapes)
		{
			x = (int)shape.getCenter().getX();
			y = (int)shape.getCenter().getY();
			width = Math.max(width, x + shape.getDimension().width / 2);
			height = Math.max(width, y + shape.getDimension().height / 2);
		}
		return new Dimension(width+5, height+5);
	}
}
