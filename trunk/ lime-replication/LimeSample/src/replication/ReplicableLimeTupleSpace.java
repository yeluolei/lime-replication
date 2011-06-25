package replication;

import lights.interfaces.ITuple;
import lime.AgentLocation;
import lime.IllegalTupleSpaceNameException;
import lime.LimeTupleSpace;
import lime.NoSuchReactionException;
import lime.Reaction;
import lime.ReactionEvent;
import lime.ReactionListener;
import lime.RegisteredReaction;
import lime.TupleSpaceEngineException;
import lime.UbiquitousReaction;

public class ReplicableLimeTupleSpace{
	private LimeTupleSpace lts;
	
	public static final int REPLICATION_MODE_MASTER = 1;
	public static final int REPLICATION_MODE_ANY = 2;
	public static final int CONSISTENCY_MODE_MASTER = 1;
	public static final int CONSISTENCY_MODE_ANY = 2;
	public static final int CONSISTENCY_MODE_NEVER = 3;
	
	private String name;
	private int maxid;

	public ReplicableLimeTupleSpace(String name) {
		try {
			maxid = 1;
			lts = new LimeTupleSpace(name);
			this.name = name;
			initReaction();
		} catch (IllegalTupleSpaceNameException e) {
			e.printStackTrace();
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
	}
	
	public ReplicableTuple createReplicableTuple(){
		return new ReplicableTuple(name, maxid);
	}
	
	private void initReaction(){
		
	}
	public boolean setShared(boolean isShared) {
		return lts.setShared(isShared);
	}
	public void out(ReplicableTuple t) {
		try {
			lts.out(t);
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
	}
	public void out(AgentLocation destination, ReplicableTuple t) {
		try {
			lts.out(destination, t);
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
	}
	public ReplicableTuple in(ReplicableTuple template) {
		try {
			return (ReplicableTuple) lts.in(template);
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ReplicableTuple inp(AgentLocation current,AgentLocation destination,ReplicableTuple template){
		try {
			return (ReplicableTuple) lts.inp(current, destination, template);
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ReplicableTuple[] ing(ReplicableTuple p) {
		return null;
	}
	public ReplicableTuple rd(ReplicableTuple template) {
		try {
			return (ReplicableTuple) lts.rd(template);
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ReplicableTuple rdp(AgentLocation current,AgentLocation destination,ReplicableTuple template) {
		try {
			return (ReplicableTuple) lts.rdp(current, destination, template);
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ReplicableTuple[] rdg(AgentLocation current, AgentLocation destination ,ReplicableTuple template) {
		try {
			return (ReplicableTuple[]) lts.rdg(current, destination, template);
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ReplicableRegisteredReaction[] addStrongReaction(ReplicableLocalizedReaction[] rlr) {
		return null;
	}
	public ReplicableRegisteredReaction[] addWeakReaction(ReplicableReaction[] rr) {
		return null;
	}
	public void removeStrongReaction(ReplicableRegisteredReaction[] rrr) {
	}
	public void removeWeakReaction(ReplicableRegisteredReaction[] rrr) {
	}
	// REPLICATION-SPECIFIC OPERATIONS
	public ReplicableTuple change(ReplicableTuple template, ReplicableTuple t) {
		return null;
	}
	
	
	public RegisteredReaction addReplicaRequest(ReplicableTuple template,
			int replicationMode,
			int consistencyMode) {
		try {
			Reaction reaction = new UbiquitousReaction(template,
					new ReplicationListener(replicationMode, consistencyMode),
					Reaction.ONCEPERTUPLE);
			return lts.addWeakReaction(new Reaction[]{reaction})[0];
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void removeReplicaRequest(RegisteredReaction r) {
		try {
			lts.removeWeakReaction(new RegisteredReaction[]{r});
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		} catch (NoSuchReactionException e) {
			e.printStackTrace();
		}
	}
}
