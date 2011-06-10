package ynn.tech.algorithms.network;

import java.util.ArrayList;
import java.util.List;

public class CommandStack
{
	private List<Command> _commands;
	private int _currentCmd = -1;
	
	public CommandStack()
	{
		_commands = new ArrayList<Command>();
	}
	
	public void reset()
	{
		_commands.clear();
		_currentCmd = -1;
	}
	
	public void executeCommand(Command cmd)
	{
		cmd.execute();
		_currentCmd++;
		if (_currentCmd >= _commands.size())
		{
			_commands.add(cmd);
		}
		else
		{
			_commands.set(_currentCmd, cmd);
			removeDeprecatedCommands();
		}
	}
	
	private void removeDeprecatedCommands()
	{
		for (int index = _commands.size()-1; index > _currentCmd; index--)
		{
			_commands.remove(index);
		}
	}

	public void undo()
	{
		if (canUndo())
		{
			Command command = _commands.get(_currentCmd);
			command.undo();
			_currentCmd--;
		}
	}

	public void redo()
	{
		if (canRedo())
		{
			_currentCmd++;
			Command command = _commands.get(_currentCmd);
			command.execute();
		}
	}

	public boolean canRedo()
	{
		return _currentCmd+1 < _commands.size();
	}

	public boolean canUndo()
	{
		return _currentCmd >= 0 && _commands.get(_currentCmd).canUndo();
	}
}
