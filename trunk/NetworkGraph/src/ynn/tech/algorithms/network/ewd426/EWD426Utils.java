package ynn.tech.algorithms.network.ewd426;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.AlgorithmUtils;
import ynn.tech.algorithms.network.NetworkAlgorithm;

public class EWD426Utils implements AlgorithmUtils
{

	@Override
	public NetworkAlgorithm createAlgorithm()
	{
		return new EWD426Algorithm();
	}

	@Override
	public Node createNode()
	{
		return new EWD426Node();
	}

	@Override
	public void initNodeAttributes(Node node)
	{
		((EWD426Node)node).init();
	}

}
