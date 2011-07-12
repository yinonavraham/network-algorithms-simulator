package ynn.tech.algorithms.network.aky90;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ynn.network.model.Node;
import ynn.tech.algorithms.network.Command;
import ynn.tech.algorithms.network.CompositeCommand;
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
		Map<Node, Command> actions = new LinkedHashMap<Node, Command>();
		for (Node vNode : context.getNetwork().getNodes())
		{
			/*
			 * The program at node v:
			 * do forever: read next neighbor information and perform actions 1-8
			 */
			Aky90Node v = (Aky90Node)vNode;
			for (Node uNode : v.getNeighbors())
			{
				Aky90Node u = (Aky90Node)uNode;
				action1(context,actions,v); 
				action2(context,actions,v,u);
				action3(context,actions,v);
				action4(context,actions,v);
				action5(context,actions,v);
				action6(context,actions,v);
				action7(context,actions,v,u);
				action8(context,actions,v,u);
			}
		}
		for (Command cmd : actions.values())
		{
			context.getCommands().add(cmd);
		}
	}
	
	private boolean action1(StepContext context, Map<Node, Command> actions, Aky90Node v)
	{
		boolean guard = 
			!condition1(v) && 
			!condition1Prime(v);
		if (guard == true)
		{
			List<String> reasons = getNotCondition1Reasons(v);
			String message = "Action1: " +
					"A node that notices that conditions C1 and C1' do not hold, must become a root.";
			message += reasonsToString(reasons);
			actions.put(v, new SetAsRootCommand(v, message));
		}
		return guard;
	}
	
	private String reasonsToString(List<String> reasons)
	{
		StringBuilder sb = new StringBuilder();
		if (reasons.size() > 0)
		{
			sb.append(" (Reasons: ");
			for (int i = 0; i < reasons.size(); i++)
			{
				if (i > 0) sb.append("; ");
				sb.append(reasons.get(i));
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	private List<String> getNotCondition1Reasons(Aky90Node v)
	{
		List<String> reasons = new ArrayList<String>();
		Aky90Node parent = (Aky90Node)v.getParent();
		if (!(v.getRootId().equals(v.getId()) && parent.getId().equals(v.getId()) && v.getDistance() == 0))
		{
			reasons.add(v + " is not a root");
		}
		if (parent != null)
		{
			if (!v.isNeighborOf(parent))
			{
				reasons.add(v + " is not a neighbor of its parent");
			}
			if (!v.getRootId().equals(parent.getRootId()))
			{
				reasons.add("The root of " + v + " is different than its parent's");
			}
			if (!(v.getDistance() == parent.getDistance() + 1))
			{
				reasons.add("The distance of " + v + " does not match its parent's");
			}
		}
		else
		{
			reasons.add("The parent of " + v + " is no longer connected");
		}
		if (!(compareIds(v.getRootId(),v.getId()) > 0))
		{
			reasons.add(v + " has an ID higher than its root");
		}
		if (!(compareIds(v.getRootId(),v.getMaxNeghiborRoot()) >= 0))
		{
			reasons.add("The root of " + v + " is not the max available root ID");
		}
		return reasons;
	}
	
	private boolean action2(StepContext context, Map<Node, Command> actions, Aky90Node v, Aky90Node u)
	{
		Aky90Node x = v.getNeghiborWithMaxRoot();
		boolean guard = 
			condition1Prime(v) &&
			u.getRootId().equals(x.getRootId()) &&
			compareIds(u.getRootId(),v.getRootId()) > 0;
		if (guard == true)
		{
			// Check that the variables are indeed different than what it suppose to be
			if (!v.getId().equals(v.getRequest()) ||
				!v.getId().equals(v.getFrom()) ||
				!u.getId().equals(v.getTo()) || 
				!DirectionEnum.Ask.equals(v.getDirection()))
			{
				String message = v + " - Action 2: " +
						"Condition C1' holds at the node and there is a neighbor with root higher " +
						"than the current root of the node - move on to the process of joining " +
						"a tree with a larger root (the tree that " + u + " belongs to)";
				CompositeCommand cmd = new CompositeCommand(message);
				cmd.add(new SetNodeAttributeCommand(
					v, _attr.getByName(Aky90NodeAttributes.REQUEST), v.getId()));
				cmd.add(new SetNodeAttributeCommand(
					v, _attr.getByName(Aky90NodeAttributes.FROM), v.getId()));
				cmd.add(new SetNodeAttributeCommand(
					v, _attr.getByName(Aky90NodeAttributes.TO), u.getId()));
				cmd.add(new SetNodeAttributeCommand(
					v, _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Ask));
				actions.put(v, cmd);
			}
			else actions.put(v, new EmptyCommand());
		}
		return guard;
	}

	private boolean action3(StepContext context, Map<Node, Command> actions, Aky90Node v)
	{
		boolean guard = 
			condition1(v) &&
			!condition2(v);
		if (guard == true)
		{
			String message = "Action 3: " +
					"C1 holds but C2 does not - the node must reset the request variables.";
			message += reasonsToString(getNotCondition2Reasons(v));
			actions.put(v, new ResetRequestVarsCommand(v,message));
		}
		return guard;
	}
	
	private List<String> getNotCondition2Reasons(Aky90Node v)
	{
		List<String> reasons = new ArrayList<String>();
		if (!condition2Prime(v))
		{
			reasons.add(v + " is not forwarding a request, based on its neighbors' request variables");
		}
		else
		{
			if (!v.getRequest().equals(Aky90NodeAttributes.UNDEFINED_REQUEST))
			{
				reasons.add("Request var in " + v + " is not undefined");
			}
			if (!v.getTo().equals(Aky90NodeAttributes.UNDEFINED_TO))
			{
				reasons.add("To var in " + v + " is not undefined");
			}
			if (!v.getFrom().equals(Aky90NodeAttributes.UNDEFINED_FROM))
			{
				reasons.add("From var in " + v + " is not undefined");
			}
			if (!v.getDirection().equals(Aky90NodeAttributes.UNDEFINED_DIRECTION))
			{
				reasons.add("Direction var in " + v + " is not undefined");
			}
		}
		return reasons;
	}
	
	private boolean action4(StepContext context, Map<Node, Command> actions, Aky90Node v)
	{
		Aky90Node w = null;
		for (Node node : v.getNeighbors())
		{
			w = (Aky90Node)node;
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
			String message = v + " - Action 4: " +
					"Forward a request from the requesting node: " + w;
			CompositeCommand cmd = new CompositeCommand(message);
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.REQUEST), w.getId()));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.FROM), w.getId()));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.TO), v.getParentId()));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Ask));
			actions.put(v, cmd);
		}
		return guard;
	}
	
	private boolean action5(StepContext context, Map<Node, Command> actions, Aky90Node v)
	{
		Aky90Node w = null;
		for (Node node : v.getNeighbors())
		{
			w = (Aky90Node)node;
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
			String message = v+ " - Action 5: " +
					"Forward a request from " + w + ", requested by " + w.getRequest();
			CompositeCommand cmd = new CompositeCommand(message);
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.REQUEST), w.getRequest()));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.FROM), w.getId()));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.TO), v.getParentId()));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Ask));
			actions.put(v, cmd);
		}
		return guard;
	}
	
	private boolean action6(StepContext context, Map<Node, Command> actions, Aky90Node v)
	{
		boolean guard = 
			condition1(v) &&
			condition2Prime(v) &&
			v.isRoot() &&
			v.getDirection().equals(DirectionEnum.Ask);
		if (guard == true)
		{
			String message = "Action 6: Grant the request. " +
					v + " is forwarding a request (i.e. C2' holds at " + v + ")" +
					" and it is a root. Hence, it can grant the request.";
			actions.put(v, new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Grant, message));
		}
		return guard;
	}
	
	private boolean action7(StepContext context, Map<Node, Command> actions, Aky90Node v, Aky90Node u)
	{
		boolean guard = 
			condition1(v) &&
			condition2Prime(v) &&
			v.getTo().equals(v.getParentId()) &&
			v.getTo().equals(u.getId()) &&
			u.getDirection().equals(DirectionEnum.Grant) &&
			v.getDirection().equals(DirectionEnum.Ask) &&
			u.getRequest().equals(v.getRequest()) &&
			u.getFrom().equals(v.getId());
		if (guard == true)
		{
			String message = "Action 7: Forward the Grant message from " + u + 
					", requested by " + v.getRequest();
			actions.put(v, new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.DIRECTION), DirectionEnum.Grant, message));
		}
		return guard;
	}
	
	private boolean action8(StepContext context, Map<Node, Command> actions, Aky90Node v, Aky90Node u)
	{
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
			String message = v+ " - Action 8: " +
					"The request of " + v + " has been granted, it joins the tree of " + u.getRootId();
			CompositeCommand cmd = new CompositeCommand(message);
			cmd.add(new SetParentCommand(v, u.getId()));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.DISTANCE), u.getDistance()+1));
			cmd.add(new SetNodeAttributeCommand(
				v, _attr.getByName(Aky90NodeAttributes.ROOT), u.getRootId()));
			cmd.add(new ResetRequestVarsCommand(v));
			actions.put(v, cmd);
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
		Aky90Node parent = (Aky90Node)v.getParent();
		return (
			(v.getRootId().equals(v.getId()) && parent.getId().equals(v.getId()) && v.getDistance() == 0) ||
			compareIds(v.getRootId(),v.getId()) > 0 && v.isNeighborOf(parent) && 
			v.getRootId().equals(parent.getRootId()) && v.getDistance() == parent.getDistance() + 1) &&
			compareIds(v.getRootId(),v.getMaxNeghiborRoot()) >= 0;
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
		Aky90Node parent = (Aky90Node)v.getParent();
		return (
			(v.getRootId().equals(v.getId()) && parent.getId().equals(v.getId()) && v.getDistance() == 0) ||
			compareIds(v.getRootId(),v.getId()) > 0 && v.isNeighborOf(parent) && 
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
		for (Node neighbor : v.getNeighbors())
		{
			Aky90Node w = (Aky90Node)neighbor;
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
	
	private int compareIds(String id1, String id2)
	{
		// Try to compare as integers (numeric)
		try
		{
			int int1 = Integer.parseInt(id1);
			int int2 = Integer.parseInt(id2);
			return 	int1 > int2 ? 1 :
					int1 < int2 ? -1 : 0;
		}
		// Otherwise compare strings (lexicographic)
		catch (NumberFormatException e) 
		{
			return id1.compareToIgnoreCase(id2);
		}
	}

}
