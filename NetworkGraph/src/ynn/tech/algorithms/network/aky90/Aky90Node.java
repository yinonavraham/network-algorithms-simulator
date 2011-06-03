package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Direction;
import ynn.network.model.Node;

public class Aky90Node
{
	private Node _node;
	
	public Aky90Node(Node node)
	{
		_node = node;
	}
	
	public void init()
	{
		setRoot(getId());
		setParent(getId());
		setDistance(0);
		setRequest(Aky90NodeAttributes.UNDEFINED_REQUEST);
		setTo(Aky90NodeAttributes.UNDEFINED_TO);
		setFrom(Aky90NodeAttributes.UNDEFINED_FROM);
		setDirection(Aky90NodeAttributes.UNDEFINED_DIRECTION);
	}
	
	public Node getNode()
	{
		return _node; 
	}
	
	public void setParent(Node parent)
	{
		Aky90Node akyParent = new Aky90Node(parent);
		setParent(akyParent.getId());
	}
	
	public void setParent(String parentId)
	{
		Direction parentDir = _node.getNeighborDirection(getParent());
		if (parentDir != null)
		{
			if (parentDir.equals(Direction.Default))
			{
				_node.setNeighborDirection(getParent(), Direction.None);
			}
			else if (parentDir.equals(Direction.Both))
			{
				_node.setNeighborDirection(getParent(), Direction.Other);
			}
		}
		_node.putAttribute(Aky90NodeAttributes.PARENT, parentId);
		parentDir = _node.getNeighborDirection(getParent());
		if (parentDir != null)
		{
			if (parentDir.equals(Direction.None))
			{
				_node.setNeighborDirection(getParent(), Direction.Default);
			}
			else if (parentDir.equals(Direction.Other))
			{
				_node.setNeighborDirection(getParent(), Direction.Both);
			}
		}
	}
	
	public String getParentId()
	{
		return (String)_node.getAttributeValue(Aky90NodeAttributes.PARENT);
	}
	
	public String getRootId()
	{
		return (String)_node.getAttributeValue(Aky90NodeAttributes.ROOT);
	}
	
	public Node getParent()
	{
		if (this.getId().equals(this.getParentId())) return _node;
		else return _node.getFirstNeighborByName(getParentId());
	}
	
	public Node getRoot()
	{
		if (this.getId().equals(this.getRootId())) return _node;
		else return _node.getFirstNeighborByName(getRootId());
	}
	
	public Integer getDistance()
	{
		return (Integer)_node.getAttributeValue(Aky90NodeAttributes.DISTANCE);
	}
	
	public String getId()
	{
		return _node.getName();
	}

	public String getMaxNeghiborRoot()
	{
		String max = null;
		for (Node neighbor : _node.getNeighbors())
		{
			Aky90Node akyNeighbor = new Aky90Node(neighbor);
			String root = akyNeighbor.getRootId();
			if (max == null) max = root;
			else max = max.compareToIgnoreCase(root) > 0 ? max : root;
		}
		return max;
	}

	public Aky90Node getNeghiborWithMaxRoot()
	{
		String max = null;
		Aky90Node result = null;
		for (Node neighbor : _node.getNeighbors())
		{
			Aky90Node akyNeighbor = new Aky90Node(neighbor);
			String root = akyNeighbor.getRootId();
			if (max == null || max.compareToIgnoreCase(root) > 0)
			{
				max = root;
				result = akyNeighbor;
			}
		}
		return result;
	}

	/**
	 * <P>
	 * Check whether the given node is the parent of this node.
	 * </P><P>
	 * <B>Definition 3.1</B><BR> 
	 * We say that <code>v</code> is a child of <code>w</code> 
	 * and that <code>w</code> is the parent of <code>v</code> if:<BR>
	 * <code>(w.Id in v.Edge_list) AND (v.Parent = w.Id) AND (v.Root = w.Root) AND (v.Distance = w.Distance + 1)</code>
	 * </P>
	 * @param parent
	 * @return
	 */
	public boolean isChildOf(Aky90Node parent)
	{
		return (
			isNeighborOf(parent) &&
			this.getParentId().equals(parent.getId()) &&
			this.getRootId().equals(parent.getRootId()) &&
			this.getDistance() == parent.getDistance() + 1);
	}
	
	public boolean isParentOf(Aky90Node child)
	{
		return child.isChildOf(this);
	}
	
	public boolean isNeighborOf(Node node)
	{
		return _node.isNeighborOf(node);
	}
	
	public boolean isNeighborOf(Aky90Node node)
	{
		return _node.isNeighborOf(node.getNode());
	}
	
	public void setRoot(String rootId)
	{
		_node.putAttribute(Aky90NodeAttributes.ROOT, rootId);
	}
	
	public void setDistance(int distance)
	{
		_node.putAttribute(Aky90NodeAttributes.DISTANCE, distance);
	}
	
	public void setRequest(String nodeId)
	{
		_node.putAttribute(Aky90NodeAttributes.REQUEST, nodeId);
	}
	
	public String getRequest()
	{
		return (String)_node.getAttributeValue(Aky90NodeAttributes.REQUEST);
	}
	
	public void setFrom(String nodeId)
	{
		_node.putAttribute(Aky90NodeAttributes.FROM, nodeId);
	}
	
	public String getFrom()
	{
		return (String)_node.getAttributeValue(Aky90NodeAttributes.FROM);
	}
	
	public void setTo(String nodeId)
	{
		_node.putAttribute(Aky90NodeAttributes.TO, nodeId);
	}
	
	public String getTo()
	{
		return (String)_node.getAttributeValue(Aky90NodeAttributes.TO);
	}
	
	public void setDirection(DirectionEnum direction)
	{
		_node.putAttribute(Aky90NodeAttributes.DIRECTION, direction);
	}
	
	public DirectionEnum getDirection()
	{
		return (DirectionEnum)_node.getAttributeValue(Aky90NodeAttributes.DIRECTION);
	}
	
	public void setAsRoot()
	{
		setRoot(getId());
		setParent(getId());
		setDistance(0);
	}
	
	/**
	 * <B>Definition 3.2</B></BR>
	 * Node <code>v</code> is a root if:<BR> 
	 * <code>(v.Root = v.Id) AND (v.Parent = Id) AND (v.Distance = 0)</code>
	 *
	 * @return
	 */
	public boolean isRoot()
	{
		return getRootId().equals(getId()) && getParentId().equals(getId()) && getDistance() == 0;
	}
}