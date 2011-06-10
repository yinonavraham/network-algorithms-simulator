package ynn.tech.algorithms.network.ewd426;

import ynn.network.model.Attribute;
import ynn.tech.algorithms.network.NodeAttributes;

public class EWD426NodeAttributes implements NodeAttributes
{
	public static final String PREV_NODE = "Previous Node";
	public static final String TOKEN_VALUE = "Token Value";
	public static final String HAS_TOKEN = "Has Token";
	public static final String IS_ROOT = "Is Root";
	
	private static Attribute[] __attributes = new Attribute[] {
		new Attribute(PREV_NODE,String.class),
		new Attribute(TOKEN_VALUE,Integer.class),
		new Attribute(HAS_TOKEN,Boolean.class),
		new Attribute(IS_ROOT,Boolean.class)
	};

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
