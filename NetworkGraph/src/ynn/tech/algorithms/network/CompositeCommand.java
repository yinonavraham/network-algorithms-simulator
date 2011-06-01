package ynn.tech.algorithms.network;

import java.util.LinkedList;
import java.util.List;

public class CompositeCommand implements Command
{
	private List<Command> _commands;
	
	public CompositeCommand()
	{
		_commands = new LinkedList<Command>();
	}
	
	public void add(Command cmd)
	{
		_commands.add(cmd);
	}
	
	public void remove(Command cmd)
	{
		_commands.remove(cmd);
	}

	@Override
	public void execute()
	{
		for (Command cmd : _commands)
		{
			cmd.execute();
		}
	}

	@Override
	public void undo()
	{
		for (Command cmd : _commands)
		{
			cmd.undo();
		}
	}

	@Override
	public boolean canUndo()
	{
		for (Command cmd : _commands)
		{
			if (!cmd.canUndo()) return false;
		}
		return true;
	}

}
