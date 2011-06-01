package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Attribute;
import ynn.tech.algorithms.network.NodeAttributes;

/**
 * <PRE>
 * v.Id - (Name) - 
 * 	The read-only identity of node v
 * v.Edge_list - (Neighbors) - 
 * 	List of links incident to node v that are operational and such that 
 * 	the processor on the other side is up.
 * v.Root - 
 * 	Supposed to hold the identity of the root of the tree to which node v belongs
 * v.Parent - 
 *	The identity of a neighbor of v which is the parent of v in the tree
 * v.Distance - 
 *	The distance from node v to its root
 * v.Request - 
 * 	Either an Id of a node that is currently requesting to join the tree to which v
 * 	belongs, or equal to v.ld if v itself is trying to join another tree
 * v.From - 
 *	Either the Id of the neighbor from which v copied the value of v.Request, or v.ld if v
 *	has initiated a request in an attempt to join a new tree
 * v.To - 
 *	Name of a neighbor of v through which it is trying to propagate the request message
 * v.Direction - 
 *	Either 'Ask', to signify that the node whose Id is in v.Request wishes to join the
 * 	tree, or 'Grant' to signify that this request has been granted
 * </PRE>
 * Based on the paper: <A href='http://iew3.technion.ac.il/~kutten/dblp_13.pdf'>
 * Memory-Efficient Self Stabilizing Protocols for General Networks</A>
 * @author Yinon Avraham
 */
public class Aky90NodeAttributes implements NodeAttributes
{
	public static final String ROOT = "Root";
	public static final String PARENT = "Parent";
	public static final String DISTANCE = "Distance";
	public static final String REQUEST = "Request";
	public static final String FROM = "From";
	public static final String TO = "To";
	public static final String DIRECTION = "Direction";
	
	public static final String UNDEFINED_REQUEST = "";
	public static final String UNDEFINED_TO = "";
	public static final String UNDEFINED_FROM = "";
	public static final DirectionEnum UNDEFINED_DIRECTION = DirectionEnum.Undefined;
	
	private static Attribute[] __attributes = {
		new Attribute(ROOT, String.class),
		new Attribute(PARENT, String.class),
		new Attribute(DISTANCE, Integer.class),
		new Attribute(REQUEST, String.class),
		new Attribute(FROM, String.class),
		new Attribute(TO, String.class),
		new Attribute(DIRECTION, DirectionEnum.class)
	};
	
	public Attribute getByName(String name)
	{
		for (Attribute attr : __attributes)
		{
			if (attr.getName().equals(name)) return attr;
		}
		return null;
	}
	
	public Attribute[] values()
	{
		return __attributes;
	}
}
