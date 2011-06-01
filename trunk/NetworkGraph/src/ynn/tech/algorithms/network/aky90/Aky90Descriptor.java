package ynn.tech.algorithms.network.aky90;

import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.NodeAttributes;

public class Aky90Descriptor implements AlgorithmDescriptor
{

	@Override
	public NodeAttributes getNodeAttributes()
	{
		return new Aky90NodeAttributes();
	}

	@Override
	public String getCode()
	{
		return "AKY90 [WDAG]";
	}

	@Override
	public String getName()
	{
		return "Memory-Efficient Self Stabilizing Protocols for General Networks";
	}

	@Override
	public String getDescription()
	{
		return 
			"A self stabilizing protocol for constructing a rooted spanning tree in an arbitrary asynchronous " +
			"network of processors that communicate through sha~ed memory is presented. The " +
			"processors have unique identifiers but are otherwise identical. The network topology is assumed " +
			"to be dynamic, that is, edges can join or leave the computation before it eventually stabilizes.";
	}

}
