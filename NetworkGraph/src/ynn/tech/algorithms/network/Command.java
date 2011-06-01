package ynn.tech.algorithms.network;

public interface Command
{
	void execute();
	void undo();
	boolean canUndo();
}
