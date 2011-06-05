package ynn.tech.algorithms.network.ewd426;

import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.AlgorithmUtils;
import ynn.tech.algorithms.network.NodeAttributes;

public class EWD426Descriptor implements AlgorithmDescriptor
{

	@Override
	public String getCode()
	{
		return "EWD426 [ACM]";
	}

	@Override
	public String getName()
	{
		return "Self-stabilizing Systems in Spite of Distributed Control";
	}

	@Override
	public String getDescription()
	{
		return "Dijkstra's paper, which introduces the concept of self-stabilization, " +
				"presents an example in the context of a \"token ring\" — a network of " +
				"computers ordered in a circle, such that exactly one of them is supposed " +
				"to \"hold a token\" at any given time. " +
				"Not holding a token is a correct state for each computer in this network, " +
				"since the token can be held by another computer. However, if every computer " +
				"is in the state of \"not holding a token\" then the network altogether is " +
				"not in a correct state. " +
				"Similarly, if more than one computer \"holds a token\" then this is not a " +
				"correct state for the network, although it cannot be observed to be incorrect " +
				"by viewing any computer individually. Since every computer can \"observe\" " +
				"only the states of its two neighbors, it is hard for the computers to decide " +
				"whether the network altogether is in a correct state.";
		/*
		 * "Dijkstra's Self-stabilizing Token Ring - " +
				"The synchronization task between loosely coupled " +
				"cyclic sequential processes (as can be distinguished in, " +
				"for instance, operating systems) can be viewed as keeping " +
				"the relation \"the system is in a legitimate state\" invariant. " +
				"As a result, each individual process step that " +
				"could possibly cause violation of that relation has to " +
				"be preceded by a test deciding whether the process in " +
				"question is allowed to proceed or has to be delayed. " +
				"The resulting design is readily--and quite systematically- " +
				"implemented if the different processes can be " +
				"granted mutually exclusive access to a common store " +
				"in which \"the current system state\" is recorded."; 
		 */
	}

	@Override
	public NodeAttributes getNodeAttributes()
	{
		return new EWD426NodeAttributes();
	}

	@Override
	public AlgorithmUtils getUtilities()
	{
		return new EWD426Utils();
	}

}
