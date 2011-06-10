package ynn.network.model;

public class DefaultNodeFactory implements INodeFactory
{

	@Override
	public Node createNode()
	{
		return new Node();
	}

}
