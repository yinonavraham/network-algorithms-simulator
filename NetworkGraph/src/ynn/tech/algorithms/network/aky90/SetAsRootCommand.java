package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.Command;
import ynn.tech.algorithms.network.CompositeCommand;
import ynn.tech.algorithms.network.SetNodeAttributeCommand;

public class SetAsRootCommand implements Command
{
	private CompositeCommand _command;
	private Node _node;
	private String _message;
	
	public SetAsRootCommand(Node node)
	{
		this(node,null);
	}
	
	public SetAsRootCommand(Node node, String message)
	{
		_node = node;
		_message = message;
		_command = new CompositeCommand();
		Aky90Node akyNode = (Aky90Node)node;
		Aky90NodeAttributes attr = new Aky90NodeAttributes();
		_command.add(new SetNodeAttributeCommand(node, attr.getByName(Aky90NodeAttributes.ROOT), akyNode.getId()));
		_command.add(new SetParentCommand(node, akyNode.getId()));
		_command.add(new SetNodeAttributeCommand(node, attr.getByName(Aky90NodeAttributes.DISTANCE), 0));
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
		String suffix = _message == null ? "" : " - " + _message;
		return String.format("%s: Set as root%s", _node, suffix);
	}
}
