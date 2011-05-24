package ynn.network.model;

public interface INodeListener
{
	void nodeAttributeChanged(NodeAttributeEvent e);
	void nodeNeighborsChanged(NodeNeighborEvent e);
}
