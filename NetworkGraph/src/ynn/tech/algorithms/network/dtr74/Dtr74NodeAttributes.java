package ynn.tech.algorithms.network.dtr74;

import ynn.network.model.Attribute;
import ynn.tech.algorithms.network.NodeAttributes;

public class Dtr74NodeAttributes implements NodeAttributes
{
	private static Attribute[] __attributes = new Attribute[0]; 

	@Override
	public Attribute getByName(String name)
	{
		for (Attribute attr : __attributes)
		{
			if (attr.getName().equals(name)) return attr;
		}
		return null;
	}

	@Override
	public Attribute[] values()
	{
		return __attributes;
	}

}
