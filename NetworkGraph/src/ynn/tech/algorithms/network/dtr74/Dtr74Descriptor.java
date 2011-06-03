package ynn.tech.algorithms.network.dtr74;

import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.AlgorithmUtils;
import ynn.tech.algorithms.network.NodeAttributes;

public class Dtr74Descriptor implements AlgorithmDescriptor
{

	@Override
	public String getCode()
	{
		return "DTR74 [Dijkstra]";
	}

	@Override
	public String getName()
	{
		return "Self-Stabilizing Token-Ring";
	}

	@Override
	public String getDescription()
	{
		return "Self-stabilizing Systems in Spite of Distributed Control - " +
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
	}

	@Override
	public NodeAttributes getNodeAttributes()
	{
		return new Dtr74NodeAttributes();
	}

	@Override
	public AlgorithmUtils getUtilities()
	{
		return new Dtr74Utils();
	}

}
