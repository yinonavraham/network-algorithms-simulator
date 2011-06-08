package ynn.network.ui;


public interface INetworkViewListener
{
	void nodesChanged(NodesEvent e);
	void connectorsChanged(ConnectorsEvent e);
	void selectionChanged(SelectionChangedEvent e);
}
