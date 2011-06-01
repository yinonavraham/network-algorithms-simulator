package ynn.tech.algorithms.network;

import ynn.network.model.Attribute;

public interface NodeAttributes
{
	Attribute getByName(String name);
	Attribute[] values();
}
