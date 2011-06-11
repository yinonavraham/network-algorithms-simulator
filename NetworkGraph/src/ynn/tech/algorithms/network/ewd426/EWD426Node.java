package ynn.tech.algorithms.network.ewd426;

import ynn.network.model.Direction;
import ynn.network.model.INodeListener;
import ynn.network.model.Node;
import ynn.network.model.NodeAttributeEvent;
import ynn.network.model.NodeNeighborEvent;
import ynn.network.ui.IAnnotationProvider;

public class EWD426Node extends Node implements IAnnotationProvider
{
	private EWD426NodeAttributes _attributes = new EWD426NodeAttributes();
	
	public EWD426Node()
	{
		super();
		addListener(new INodeListener()
		{	
			@Override
			public void nodeNeighborsChanged(NodeNeighborEvent e) {}
			@Override
			public void nodeAttributeChanged(NodeAttributeEvent e)
			{
				if (EWD426NodeAttributes.PREV_NODE.equals(e.getAttribute().getName()))
				{
					EWD426Node oldPrevNode = (EWD426Node)getFirstNeighborByName((String)e.getOldValue());
					EWD426Node newPrevNode = (EWD426Node)getFirstNeighborByName((String)e.getNewValue());
					updatePrevNodeDirection(oldPrevNode,newPrevNode);
				}
			}
		});
	}
	
	public void init()
	{
		setIsRoot(false);
		setTokenValue(0);
		setPrevNode(null);
		setHasToken(false);
	}
	
	private void updatePrevNodeDirection(EWD426Node oldPrevNode, EWD426Node newPrevNode)
	{
		Direction parentDir = getNeighborDirection(oldPrevNode);
		if (parentDir != null)
		{
			if (parentDir.equals(Direction.Other))
			{
				setNeighborDirection(oldPrevNode, Direction.None);
			}
			else if (parentDir.equals(Direction.Both))
			{
				setNeighborDirection(oldPrevNode, Direction.Default);
			}
		}
		parentDir = getNeighborDirection(newPrevNode);
		if (parentDir != null)
		{
			if (parentDir.equals(Direction.None))
			{
				setNeighborDirection(newPrevNode, Direction.Other);
			}
			else if (parentDir.equals(Direction.Default))
			{
				setNeighborDirection(newPrevNode, Direction.Both);
			}
		}
	}
	
	public void setPrevNode(String nodeName)
	{
		Node neighbor = getFirstNeighborByName(nodeName);
		if (neighbor != null)
		{
			putAttribute(_attributes.getByName(EWD426NodeAttributes.PREV_NODE), nodeName);
		}
		else 
		{
			putAttribute(_attributes.getByName(EWD426NodeAttributes.PREV_NODE), null);
		}
	}
	
	public EWD426Node getPrevNode()
	{
		Node neighbor = null;
		Object nodeName = getAttributeValue(_attributes.getByName(EWD426NodeAttributes.PREV_NODE));
		if (nodeName != null && nodeName instanceof String)
		{
			neighbor = getFirstNeighborByName((String)nodeName);
		}
		return neighbor != null ? (EWD426Node)neighbor : null;
	}
	
	public void setTokenValue(int value)
	{
		putAttribute(_attributes.getByName(EWD426NodeAttributes.TOKEN_VALUE), value);
	}
	
	public Integer getTokenValue()
	{
		Object value = getAttributeValue(_attributes.getByName(EWD426NodeAttributes.TOKEN_VALUE));
		return value != null && value instanceof Integer ? (Integer)value : null;
	}
	
	public void increaseTokenValue()
	{
		Integer value = getTokenValue();
		if (value != null)
		{
			setTokenValue(value++);
		}
	}
	
	public void decreaseTokenValue()
	{
		Integer value = getTokenValue();
		if (value != null)
		{
			setTokenValue(value--);
		}
	}
	
	public void setIsRoot(boolean isRoot)
	{
		putAttribute(_attributes.getByName(EWD426NodeAttributes.IS_ROOT), isRoot);
	}
	
	public Boolean getIsRoot()
	{
		Object value = getAttributeValue(_attributes.getByName(EWD426NodeAttributes.IS_ROOT));
		return value != null && value instanceof Boolean ? (Boolean)value : null;
	}
	
	public void setHasToken(boolean hasToken)
	{
		putAttribute(_attributes.getByName(EWD426NodeAttributes.HAS_TOKEN), hasToken);
	}
	
	public Boolean getHasToken()
	{
		Object value = getAttributeValue(_attributes.getByName(EWD426NodeAttributes.HAS_TOKEN));
		return value != null && value instanceof Boolean ? (Boolean)value : null;
	}
	
	public void calcHasToken()
	{
		EWD426Node prevNode = getPrevNode();
		if (prevNode != null)
		{
			Integer prevValue = prevNode.getTokenValue();
			Integer value = getTokenValue();
			if (value != null && prevValue != null)
			{
				if (getIsRoot())
				{
					setHasToken(value == prevValue);
				}
				else
				{
					setHasToken(value == prevValue+1);
				}
			}
			else setHasToken(false);
		}
	}

	@Override
	public String getAnnotationText()
	{
		Integer value = getTokenValue();
		Boolean hasToken = getHasToken();
		return value != null ? String.valueOf(value) +
			(hasToken ? "T" : "")
			: null;
	}
}
