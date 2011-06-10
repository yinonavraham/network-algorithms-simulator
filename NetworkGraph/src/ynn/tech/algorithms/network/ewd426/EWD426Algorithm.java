package ynn.tech.algorithms.network.ewd426;

import java.util.HashMap;
import java.util.Map;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.NetworkAlgorithm;
import ynn.tech.algorithms.network.SetNodeAttributeCommand;
import ynn.tech.algorithms.network.StepContext;

public class EWD426Algorithm implements NetworkAlgorithm
{
	private EWD426NodeAttributes attributes = new EWD426NodeAttributes();
	private Map<EWD426Node, Integer> _tokenValues = new HashMap<EWD426Node, Integer>();

	@Override
	public void performStep(StepContext context)
	{
		_tokenValues.clear();
		for (Node node : context.getNetwork().getNodes())
		{
			EWD426Node ewdNode = (EWD426Node)node;
			updateTokenValue(context, ewdNode);
		}
		for (Node node : context.getNetwork().getNodes())
		{
			EWD426Node ewdNode = (EWD426Node)node;
			calcHasToken(context, ewdNode);
		}
	}
	
	private void updateTokenValue(StepContext context, EWD426Node node)
	{
		EWD426Node prevNode = node.getPrevNode();
		if (prevNode != null)
		{
			Integer prevTokenValue = prevNode.getTokenValue();
			Integer tokenValue = node.getTokenValue();
			if (tokenValue != null && prevTokenValue != null)
			{
				if (node.getIsRoot())
				{
					updateTokenValueInRoot(context,node,tokenValue,prevTokenValue);
				}
				else
				{
					updateTokenValueInNode(context,node,tokenValue,prevTokenValue);
				}
			}
		}
	}
	
	private void updateTokenValueInNode(StepContext context, EWD426Node node, int tokenValue, int prevTokenValue)
	{
		if (tokenValue != prevTokenValue)
		{
			tokenValue = prevTokenValue;
			context.getCommands().add(new SetNodeAttributeCommand(
				node, attributes.getByName(EWD426NodeAttributes.TOKEN_VALUE), tokenValue));
		}
		_tokenValues.put(node, tokenValue);
	}
	
	private void updateTokenValueInRoot(StepContext context, EWD426Node node, int tokenValue, int prevTokenValue)
	{
		if (tokenValue == prevTokenValue)
		{
			tokenValue++;
			context.getCommands().add(new SetNodeAttributeCommand(
				node, attributes.getByName(EWD426NodeAttributes.TOKEN_VALUE), tokenValue));
		}
		_tokenValues.put(node, tokenValue);
	}
	
	private void calcHasToken(StepContext context, EWD426Node node)
	{
		EWD426Node prevNode = node.getPrevNode();
		if (prevNode != null)
		{
			Integer prevTokenValue = _tokenValues.get(prevNode);
			Integer tokenValue = _tokenValues.get(node);
			if (tokenValue != null && prevTokenValue != null)
			{
				if (node.getIsRoot())
				{
					calcHasTokenInRoot(context,node,tokenValue,prevTokenValue);
				}
				else
				{
					calcHasTokenInNode(context,node,tokenValue,prevTokenValue);
				}
			}
		}
	}
	
	private void calcHasTokenInNode(StepContext context, EWD426Node node, int tokenValue, int prevTokenValue)
	{
		boolean hasToken = tokenValue != prevTokenValue;
		if (!node.getHasToken().equals(hasToken))
		{
			context.getCommands().add(new SetNodeAttributeCommand(
				node, attributes.getByName(EWD426NodeAttributes.HAS_TOKEN), hasToken));
		}
	}
	
	private void calcHasTokenInRoot(StepContext context, EWD426Node node, int tokenValue, int prevTokenValue)
	{
		boolean hasToken = tokenValue == prevTokenValue;
		if (!node.getHasToken().equals(hasToken))
		{
			context.getCommands().add(new SetNodeAttributeCommand(
				node, attributes.getByName(EWD426NodeAttributes.HAS_TOKEN), hasToken));
		}
	}

}
