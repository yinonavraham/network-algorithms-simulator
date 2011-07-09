package ynn.tech.algorithms.network.aky90;

import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.AlgorithmUtils;
import ynn.tech.algorithms.network.NodeAttributes;

public class Aky90Descriptor implements AlgorithmDescriptor
{
	private static Aky90Utils __utils = null;

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
			"network of processors that communicate through shared memory is presented. The " +
			"processors have unique identifiers but are otherwise identical. The network topology is assumed " +
			"to be dynamic, that is, edges can join or leave the computation before it eventually stabilizes. \n\n" +
			"Conditions: \n" +
			"Condition C1: \n" +
					"The node is in a legal state. Violations of the condition - " +
					"either because of incorrect initial values of the nodes' variables, " +
					"or because of topological changes. \n" +
			"Condition C1': \n" +
					"By becoming a root condition C1 does not necessarily become true, " +
					"however we define condition C1' which does become true. Moreover, " +
					"if condition C1 is satisfied at all the nodes then (by transitivity) " +
					"the Parent variables in the nodes define a spanning forest in the network. \n" +
			"Condition C2': \n" +
					"C2' is true if node v is forwarding a request from any node w " +
					"(w is a neighbor of v). \n" +
			"Condition C2: \n" +
					"C2 is true if the variables related to the process of forwarding requsts " +
					"are in a legal state. \n";
	}

	@Override
	public AlgorithmUtils getUtilities()
	{
		if (__utils == null) __utils = new Aky90Utils(); 
		return __utils;
	}

}
