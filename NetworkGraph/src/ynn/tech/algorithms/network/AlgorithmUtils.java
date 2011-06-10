package ynn.tech.algorithms.network;

import ynn.network.model.INodeFactory;
import ynn.network.model.Node;

public interface AlgorithmUtils extends INodeFactory
{ 
    void initNodeAttributes(Node node); 
	NetworkAlgorithm createAlgorithm();
}
