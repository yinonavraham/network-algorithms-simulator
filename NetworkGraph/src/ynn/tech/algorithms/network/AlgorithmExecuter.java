package ynn.tech.algorithms.network;

import java.util.ArrayList;
import java.util.List;

import ynn.network.model.NetworkModel;

public class AlgorithmExecuter
{
	private AlgorithmDescriptor _descriptor;
	private NetworkAlgorithm _algorithm;
	private NetworkModel _network;
	private CommandStack _commands;
	
	public AlgorithmExecuter(AlgorithmDescriptor descriptor, NetworkModel network, CommandStack commands)
	{
		_descriptor = descriptor;
		_algorithm = _descriptor.getUtilities().createAlgorithm();
		_network = network;
		_commands = commands;
	}
	
	public AlgorithmDescriptor getDescriptor()
	{
		return _descriptor;
	}
	
	public NetworkModel getNetwork()
	{
		return _network;
	}
	
	public void reset()
	{
		while (_commands.canUndo())
		{
			_commands.undo();
		}
	}
	
	public String[] stepForward()
	{
		StepContext context = new StepContext(_network);
		_algorithm.performStep(context);
		_commands.executeCommand(context.getCommands());
		List<String> strings = new ArrayList<String>(10);
		commandToStrings(context.getCommands(),strings);
		return strings.toArray(new String[strings.size()]);
	}
	
	private void commandToStrings(Command command, List<String> strings)
	{
		String s = command.toString();
		if (s != null & !s.isEmpty()) strings.add(s);
		if (command instanceof CompositeCommand)
		{
			for (Command cmd : ((CompositeCommand)command).getCommands())
				commandToStrings(cmd, strings);
		}
	}

	public void stepBack()
	{
		if (_commands.canUndo()) 
		{
			_commands.undo();
		}
	}
}
