package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Direction;
import ynn.network.model.INodeListener;
import ynn.network.model.Node;
import ynn.network.model.NodeAttributeEvent;
import ynn.network.model.NodeNeighborEvent;
import ynn.network.ui.IAnnotationProvider;

public class Aky90Node extends Node implements IAnnotationProvider
{	
	public Aky90Node()
	{
		super();
		addListener(new INodeListener()
		{	
			@Override
			public void nodeNeighborsChanged(NodeNeighborEvent e) {}
			@Override
			public void nodeAttributeChanged(NodeAttributeEvent e)
			{
				if (Aky90NodeAttributes.PARENT.equals(e.getAttribute().getName()))
				{
					Aky90Node oldParent = (Aky90Node)getParentById((String)e.getOldValue());
					Aky90Node newParent = (Aky90Node)getParentById((String)e.getNewValue());
					updateParentDirection(oldParent,newParent);
				}
			}
		});
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
	
	public void setParent(Node parent)
	{
		Aky90Node akyParent = (Aky90Node)parent;
		setParent(akyParent.getId());
	}
	
	private void updateParentDirection(Aky90Node oldParent, Aky90Node newParent)
	{
		Direction parentDir = getNeighborDirection(oldParent);
		if (parentDir != null)
		{
			if (parentDir.equals(Direction.Default))
			{
				setNeighborDirection(oldParent, Direction.None);
			}
			else if (parentDir.equals(Direction.Both))
			{
				setNeighborDirection(oldParent, Direction.Other);
			}
		}
		parentDir = getNeighborDirection(newParent);
		if (parentDir != null)
		{
			if (parentDir.equals(Direction.None))
			{
				setNeighborDirection(newParent, Direction.Default);
			}
			else if (parentDir.equals(Direction.Other))
			{
				setNeighborDirection(newParent, Direction.Both);
			}
		}
	}
	
	public void setParent(String parentId)
	{
		putAttribute(Aky90NodeAttributes.PARENT, parentId);
	}
	
	public String getParentId()
	{
		return (String)getAttributeValue(Aky90NodeAttributes.PARENT);
	}
	
	public String getRootId()
	{
		return (String)getAttributeValue(Aky90NodeAttributes.ROOT);
	}
	
	public Node getParent()
	{
		return getParentById(getParentId());
	}
	
	private Node getParentById(String id)
	{
		if (this.getId().equals(id)) return this;
		else return getFirstNeighborByName(id);	
	}
	
	public Node getRoot()
	{
		if (this.getId().equals(this.getRootId())) return this;
		else return getFirstNeighborByName(getRootId());
	}
	
	public Integer getDistance()
	{
		return (Integer)getAttributeValue(Aky90NodeAttributes.DISTANCE);
	}
	
	public String getId()
	{
		return getName();
	}

	public String getMaxNeghiborRoot()
	{
		String max = null;
		for (Node neighbor : getNeighbors())
		{
			Aky90Node akyNeighbor = (Aky90Node)neighbor;
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
		for (Node neighbor : getNeighbors())
		{
			Aky90Node akyNeighbor = (Aky90Node)neighbor;
			String root = akyNeighbor.getRootId();
			if (max == null || max.compareToIgnoreCase(root) < 0)
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
	
	public void setRoot(String rootId)
	{
		putAttribute(Aky90NodeAttributes.ROOT, rootId);
	}
	
	public void setDistance(int distance)
	{
		putAttribute(Aky90NodeAttributes.DISTANCE, distance);
	}
	
	public void setRequest(String nodeId)
	{
		putAttribute(Aky90NodeAttributes.REQUEST, nodeId);
	}
	
	public String getRequest()
	{
		return (String)getAttributeValue(Aky90NodeAttributes.REQUEST);
	}
	
	public void setFrom(String nodeId)
	{
		putAttribute(Aky90NodeAttributes.FROM, nodeId);
	}
	
	public String getFrom()
	{
		return (String)getAttributeValue(Aky90NodeAttributes.FROM);
	}
	
	public void setTo(String nodeId)
	{
		putAttribute(Aky90NodeAttributes.TO, nodeId);
	}
	
	public String getTo()
	{
		return (String)getAttributeValue(Aky90NodeAttributes.TO);
	}
	
	public void setDirection(DirectionEnum direction)
	{
		putAttribute(Aky90NodeAttributes.DIRECTION, direction);
	}
	
	public DirectionEnum getDirection()
	{
		return (DirectionEnum)getAttributeValue(Aky90NodeAttributes.DIRECTION);
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

	@Override
	public String getAnnotationText()
	{
		if (getDirection() != null)
		{
			switch (getDirection())
			{
			case Ask:
				return String.format("%s (%s) %s %s", getFrom(), getRequest(), getDirection(), getTo());
			case Grant:
				return String.format("%s %s %s (%s)", getTo(), getDirection(), getFrom(), getRequest());
			}	
		}
		return null;
	}
}
