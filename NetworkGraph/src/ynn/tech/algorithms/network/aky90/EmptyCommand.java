package ynn.tech.algorithms.network.aky90;

import ynn.tech.algorithms.network.Command;

public class EmptyCommand implements Command
{

	@Override
	public void execute()
	{
	}

	@Override
	public void undo()
	{
	}

	@Override
	public boolean canUndo()
	{
		return true;
	}
	
	@Override
	public String toString()
	{
		return "";
	}

}
