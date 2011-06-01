package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.Command;
import ynn.tech.algorithms.network.CompositeCommand;
import ynn.tech.algorithms.network.SetNodeAttributeCommand;

public class SetAsRootCommand implements Command
{
	private CompositeCommand _command;
	
	public SetAsRootCommand(Node node)
	{
		_command = new CompositeCommand();
		Aky90Node akyNode = new Aky90Node(node);
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

}
