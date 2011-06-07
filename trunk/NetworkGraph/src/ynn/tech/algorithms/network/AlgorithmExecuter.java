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
    private int _time;
	
	public AlgorithmExecuter(AlgorithmDescriptor descriptor, NetworkModel network)
	{
		_descriptor = descriptor;
		_algorithm = _descriptor.getUtilities().createAlgorithm();
		_network = network;
		_commands = new CommandStack();
		_time = 0;
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
			_time--;
		}
	}
	
	public String[] stepForward()
	{
		StepContext context = new StepContext(_network);
		_algorithm.performStep(context);
		_commands.executeCommand(context.getCommands());
		List<String> strings = new ArrayList<String>(10);
		commandToStrings(context.getCommands(),strings);
		_time++;
		return strings.toArray(new String[strings.size()]);
	}
	
	private void commandToStrings(Command command, List<String> strings)
	{
		if (command instanceof CompositeCommand)
		{
			for (Command cmd : ((CompositeCommand)command).getCommands())
				commandToStrings(cmd, strings);
		}
		else
		{
			strings.add(command.toString());
		}
	}

	public void stepBack()
	{
		if (_commands.canUndo()) 
		{
			_commands.undo();
			_time--;
		}
	}
	
	public int getTime()
	{
		return _time;
	}
}
