package ynn.network.model;

public interface INetworkModelListener
{
	void networkChanged(NetworkEvent e);
	void nodeAttributeChanged(NodeAttributeEvent e);
}
