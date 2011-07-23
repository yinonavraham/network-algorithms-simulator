package ynn.tech.algorithms.network.ui;

import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.aky90.Aky90Descriptor;
import ynn.tech.algorithms.network.ewd426.EWD426Descriptor;

public class Algorithms
{
	private static Algorithms __instance = null;
	
	private AlgorithmDescriptor[] _algorithms;
	
	private Algorithms()
	{
		_algorithms = new AlgorithmDescriptor[] {
	    	new Aky90Descriptor(),
	    	new EWD426Descriptor()
	    };
	}
	
	public static Algorithms getInstance()
	{
		if (__instance == null)
		{
			__instance = new Algorithms();
		}
		return __instance;
	}
	
	public AlgorithmDescriptor[] getSupportedAlgorithms()
	{
		return _algorithms;
	}
}
