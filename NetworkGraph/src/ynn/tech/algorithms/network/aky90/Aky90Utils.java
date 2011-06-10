package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.AlgorithmUtils;
import ynn.tech.algorithms.network.NetworkAlgorithm;

public class Aky90Utils implements AlgorithmUtils
{

	@Override
	public NetworkAlgorithm createAlgorithm()
	{
		return new Aky90Algorithm();
	}

	@Override
	public Node createNode()
	{
		return new Aky90Node();
	}

	@Override
	public void initNodeAttributes(Node node)
	{
		((Aky90Node)node).init();
	}

}
