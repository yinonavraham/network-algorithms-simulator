package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.Command;
import ynn.tech.algorithms.network.CompositeCommand;
import ynn.tech.algorithms.network.SetNodeAttributeCommand;

public class ResetRequestVarsCommand implements Command
{
	private CompositeCommand _command;
	private Node _node;
	
	public ResetRequestVarsCommand(Node node)
	{
		_node = node;
		_command = new CompositeCommand();
		Aky90NodeAttributes attr = new Aky90NodeAttributes();
		_command.add(new SetNodeAttributeCommand(
			node, attr.getByName(Aky90NodeAttributes.REQUEST), Aky90NodeAttributes.UNDEFINED_REQUEST));
		_command.add(new SetNodeAttributeCommand(
			node, attr.getByName(Aky90NodeAttributes.FROM), Aky90NodeAttributes.UNDEFINED_FROM));
		_command.add(new SetNodeAttributeCommand(
			node, attr.getByName(Aky90NodeAttributes.TO), Aky90NodeAttributes.UNDEFINED_TO));
		_command.add(new SetNodeAttributeCommand(
			node, attr.getByName(Aky90NodeAttributes.DIRECTION), Aky90NodeAttributes.UNDEFINED_DIRECTION));
	}

	@Override
	public void execute()
	{
		_command.execute();
	}

	@Override
	public void undo()
	{
		_command.undo();
	}

	@Override
	public boolean canUndo()
	{
		return _command.canUndo();
	}

	@Override
	public String toString()
	{
		return String.format("%s: Reset Request varialbes", _node);
	}
}
