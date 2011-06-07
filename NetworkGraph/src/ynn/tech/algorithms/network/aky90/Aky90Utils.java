package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.AlgorithmUtils;
import ynn.tech.algorithms.network.NetworkAlgorithm;

public class Aky90Utils implements AlgorithmUtils
{

	@Override
	public void initNodeAttributes(Node node)
	{
		Aky90Node akyNode = new Aky90Node(node);
		akyNode.init();
	}

	@Override
	public NetworkAlgorithm createAlgorithm()
	{
		return new Aky90Algorithm();
	}

}
