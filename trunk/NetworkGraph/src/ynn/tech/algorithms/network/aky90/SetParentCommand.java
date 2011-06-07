package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.SetNodeAttributeCommand;

public class SetParentCommand extends SetNodeAttributeCommand
{
	private String _newParentId;
	private String _oldParentId;
	
	public SetParentCommand(Node node, Object value)
	{
		super(node, new Aky90NodeAttributes().getByName(Aky90NodeAttributes.PARENT), value);
		_newParentId = (String)value;
	}
	
	@Override
	protected void onExecute()
	{
		_oldParentId = new Aky90Node(_node).getParentId();
		new Aky90Node(_node).setParent(_newParentId);
	}
	
	@Override
	protected void onUndo()
	{
		new Aky90Node(_node).setParent(_oldParentId);
	}
	
	@Override
	public String toString()
	{
		return String.format("%s- Change parent: %s -> %s", _node, _oldParentId, _newParentId);
	}
}
