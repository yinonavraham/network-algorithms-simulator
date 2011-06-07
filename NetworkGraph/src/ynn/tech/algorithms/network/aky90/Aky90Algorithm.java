package ynn.tech.algorithms.network.aky90;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.NetworkAlgorithm;
import ynn.tech.algorithms.network.SetNodeAttributeCommand;
import ynn.tech.algorithms.network.StepContext;

public class Aky90Algorithm implements NetworkAlgorithm
{
	private Aky90NodeAttributes _attr;
	
	public Aky90Algorithm()
	{
		_attr = new Aky90NodeAttributes();
	}

	@Override
	public void performStep(StepContext context)
	{
		for (Node node : context.getNetwork().getNodes())
		{
			Aky90Node v = new Aky90Node(node);
			action1(context,v); 
			action2(context,v);
			action3(context,v);
			action4(context,v);
			action5(context,v);
			action6(context,v);
			action7(context,v);
			action8(context,v);
		}
	}
	
	private boolean action1(StepContext context, Aky90Node v)
	{
		boolean guard = 
			!condition1(v) && 
			!condition1Prime(v);
		if (guard == true)
		{
			context.getCommands().add(new SetAsRootCommand(v.getNode()));
		}
		return guard;
	}
	
	private boolean action2(StepContext context, Aky90Node v)
	{
		Aky90Node u = v.getNeghiborWithMaxRoot();
		boolean guard = 
			condition1Prime(v) &&
			u.getRootId().compareToIgnoreCase(v.getRootId()) > 0;
		if (guard == true)
		{
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.REQUEST), v.getId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.FROM), v.getId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.TO), u.getId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Ask));
		}
		return guard;
	}
	
	private boolean action3(StepContext context, Aky90Node v)
	{
		boolean guard = 
			condition1(v) &&
			!condition2(v);
		if (guard == true)
		{
			context.getCommands().add(new ResetRequestVarsCommand(v.getNode()));
		}
		return guard;
	}
	
	private boolean action4(StepContext context, Aky90Node v)
	{
		Aky90Node w = null;
		for (Node node : v.getNode().getNeighbors())
		{
			w = new Aky90Node(node);
			if (w.getDirection().equals(DirectionEnum.Ask) &&
				w.getTo().equals(v.getId()) &&
				w.getRequest().equals(w.getId()) &&
				w.getRequest().equals(w.getRootId()) &&
				w.getRequest().equals(w.getFrom()))
			{
				break;
			}
			else w = null;
		}
		boolean guard = 
			w != null &&
			condition1(v) &&
			condition2(v) &&
			!condition2Prime(v);
		if (guard == true)
		{
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.REQUEST), w.getId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.FROM), w.getId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.TO), v.getParentId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Ask));
		}
		return guard;
	}
	
	private boolean action5(StepContext context, Aky90Node v)
	{
		Aky90Node w = null;
		for (Node node : v.getNode().getNeighbors())
		{
			w = new Aky90Node(node);
			if (w.getDirection().equals(DirectionEnum.Ask) &&
				w.getTo().equals(v.getId()) &&
				w.isChildOf(v))
			{
				break;
			}
			else w = null;
		}
		boolean guard = 
			w != null &&
			condition1(v) &&
			condition2(v) &&
			!condition2Prime(v);
		if (guard == true)
		{
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.REQUEST), w.getRequest()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.FROM), w.getId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.TO), v.getParentId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Ask));
		}
		return guard;
	}
	
	private boolean action6(StepContext context, Aky90Node v)
	{
		boolean guard = 
			condition1(v) &&
			condition2Prime(v) &&
			v.isRoot() &&
			v.getDirection().equals(DirectionEnum.Ask);
		if (guard == true)
		{
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Grant));
		}
		return guard;
	}
	
	private boolean action7(StepContext context, Aky90Node v)
	{
		Aky90Node u = new Aky90Node(v.getParent());
		boolean guard = 
			condition1(v) &&
			condition2Prime(v) &&
			v.getTo().equals(u.getId()) &&
			v.getParentId().equals(u.getId()) &&
			u.getDirection().equals(DirectionEnum.Grant) &&
			v.getDirection().equals(DirectionEnum.Ask) &&
			u.getRequest().equals(v.getRequest()) &&
			u.getFrom().equals(v.getId());
		if (guard == true)
		{
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Grant));
		}
		return guard;
	}
	
	private boolean action8(StepContext context, Aky90Node v)
	{
		Node uNode = v.getNode();
		if (!v.getId().equals(v.getTo())) uNode = v.getNode().getFirstNeighborByName(v.getTo());
		Aky90Node u = new Aky90Node(uNode);
		boolean guard = 
			condition1Prime(v) &&
			!condition1(v) &&
			v.getDirection().equals(DirectionEnum.Ask) &&
			v.getRequest().equals(u.getRequest()) &&
			v.getRequest().equals(v.getFrom()) &&
			v.getFrom().equals(v.getRootId()) &&
			v.getRootId().equals(v.getId()) &&
			u.getFrom().equals(v.getId()) &&
			u.getDirection().equals(DirectionEnum.Grant) &&
			v.getTo().equals(u.getId());
		if (guard == true)
		{
			context.getCommands().add(new SetParentCommand(v.getNode(), u.getId()));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.DISTANCE), u.getDistance()+1));
			context.getCommands().add(new SetNodeAttributeCommand(
				v.getNode(), _attr.getByName(Aky90NodeAttributes.ROOT), u.getRootId()));
			context.getCommands().add(new ResetRequestVarsCommand(v.getNode()));
		}
		return guard;
	}
	

	/**
	 * <P>
	 * <B>Condition C1:</B><BR>
	 * <code>{[(Root = Id) AND (Parent = Id) AND (Distance = 0)] OR
	 * [(Root > Id) AND (Parent in Edge_list) AND (Root = Parent.Root) AND (Distance = Parent.Distance + 1 > 0)]}
	 * AND (Root >= max{x.Root} of x in Edge_List)</code>
	 * </P>
	 *
	 * @param node
	 * @return
	 */
	private boolean condition1(Aky90Node v)
	{
		Aky90Node parent = new Aky90Node(v.getParent());
		return (
			(v.getRootId().equals(v.getId()) && parent.getId().equals(v.getId()) && v.getDistance() == 0) ||
			v.getRootId().compareToIgnoreCase(v.getId()) > 0 && v.isNeighborOf(parent) && 
			v.getRootId().equals(parent.getRootId()) && v.getDistance() == parent.getDistance() + 1) &&
			v.getRootId().compareToIgnoreCase(v.getMaxNeghiborRoot()) > 0;
	}
	
	/**
	 * <P>
	 * <B>Condition C1':</B><BR>
	 * <code>{[(Root = Id) AND (Parent = Id) AND (Distance = 0)] OR
	 * [(Root > Id) AND (Parent in Edge_list) AND (Root = Parent.Root) AND (Distance = Parent.Distance + 1 > 0)]}</code>
	 * </P> 
	 *
	 * @param context
	 * @param node
	 * @return
	 */
	private boolean condition1Prime(Aky90Node v)
	{
		Aky90Node parent = new Aky90Node(v.getParent());
		return (
			(v.getRootId().equals(v.getId()) && parent.getId().equals(v.getId()) && v.getDistance() == 0) ||
			v.getRootId().compareToIgnoreCase(v.getId()) > 0 && v.isNeighborOf(parent) && 
			v.getRootId().equals(parent.getRootId()) && v.getDistance() == parent.getDistance() + 1);
	}
	
	/**
	 * <P>
	 * <B>Definition 3.3</B><BR> 
	 * We say that node v is forwarding a request from node w if the following condition holds:
	 * </P><P>
	 * <B>Condition C2':</B><BR>
	 * <code>((w.Id in v.Edge_list) AND
	 * (w.Root = w.Id = w.Request = w.From = v.Request = v.From AND
	 * (w.To = v.Id) AND (w.Direction = Ask))<BR>
	 * OR<BR>
	 * ((w.Id in v.Edge_list) AND (v parent of w) AND (w.Request = v.Request != w.Id) AND
	 * (v.From =w.Id) AND (v.To = v.Parent) AND (w.To = v.Id))</code><P>
	 *
	 * @param node
	 * @return
	 */
	private boolean condition2Prime(Aky90Node v)
	{
		for (Node neighbor : v.getNode().getNeighbors())
		{
			Aky90Node w = new Aky90Node(neighbor);
			if ((w.getId().equals(w.getRootId()) &&
				w.getId().equals(w.getRequest()) &&
				w.getId().equals(w.getFrom()) &&
				w.getId().equals(v.getRequest()) &&
				w.getId().equals(v.getFrom()) &&
				v.getId().equals(w.getTo()) &&
				DirectionEnum.Ask.equals(w.getDirection())) ||
				v.isParentOf(w) &&
				w.getRequest().equals(v.getRequest()) &&
				!w.getId().equals(v.getRequest()) &&
				w.getId().equals(v.getFrom()) &&
				v.getTo().equals(v.getParentId()) &&
				w.getTo().equals(v.getId())) return true;
		}
		return false;
	}
	
	/**
	 * <B>Condition C2:</B><BR>
	 * <code>
	 * C2' OR<BR>
	 * (v.Request, v.To, v.From, and v.Direction are undefined)
	 * </code>
	 *
	 * @param node
	 * @return
	 * @see #condition2Prime(Node)
	 */
	private boolean condition2(Aky90Node v)
	{
		return condition2Prime(v) || (
			v.getRequest().equals(Aky90NodeAttributes.UNDEFINED_REQUEST) &&
			v.getTo().equals(Aky90NodeAttributes.UNDEFINED_TO) &&
			v.getFrom().equals(Aky90NodeAttributes.UNDEFINED_FROM) &&
			v.getDirection().equals(Aky90NodeAttributes.UNDEFINED_DIRECTION));
	}

}
