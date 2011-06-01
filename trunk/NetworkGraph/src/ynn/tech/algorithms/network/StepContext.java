package ynn.tech.algorithms.network;

import ynn.network.model.NetworkModel;

public class StepContext
{
	private NetworkModel _network;
	private CompositeCommand _commands;
	
	public StepContext(NetworkModel network)
	{
		_network = network;
		_commands = new CompositeCommand();
	}
	
	public NetworkModel getNetwork()
	{
		return _network;
	}
	
	public CompositeCommand getCommands()
	{
		return _commands;
	}
}
