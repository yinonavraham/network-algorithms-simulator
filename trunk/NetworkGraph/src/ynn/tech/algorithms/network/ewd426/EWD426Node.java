package ynn.tech.algorithms.network.ewd426;

import ynn.network.model.Node;

public class EWD426Node extends Node
{
	private EWD426NodeAttributes _attributes = new EWD426NodeAttributes();
	
	public EWD426Node()
	{
		super();
	}
	
	public void init()
	{
		setIsRoot(false);
		setTokenValue(0);
		setPrevNode(null);
		setHasToken(false);
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
}