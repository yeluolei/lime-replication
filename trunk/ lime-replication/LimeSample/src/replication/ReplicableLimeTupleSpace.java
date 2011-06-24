package replication;

import lights.interfaces.ITuple;
import lime.AgentLocation;

public class ReplicableLimeTupleSpace {
	public ReplicableLimeTupleSpace(String name) {
	}
	public boolean setShared(boolean isShared) {
		return false;
	}
	public ReplicableTuple out(ITuple t) {
		return null;
	}
	public ReplicableTuple out(AgentLocation destination, ITuple t) {
		return null;
	}
	public ReplicableTuple in(ReplicableTemplate p) {
		return null;
	}
	public ReplicableTuple inp(ReplicableTemplate p) {
		return null;
	}
	public ReplicableTuple[] ing(ReplicableTemplate p) {
		return null;
	}
	public ReplicableTuple rd(ReplicableTemplate p) {
		return null;
	}
	public ReplicableTuple rdp(ReplicableTemplate p) {
		return null;
	}
	public ReplicableTuple[] rdg(ReplicableTemplate p) {
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
	public ReplicableTuple change(ReplicableTemplate p, ITuple t) {
		return null;
	}
	public RegisteredReplicaRequest addReplicaRequest(ITuple p,
			int replicationMode,
			int consistencyMode) {
		return null;
	}
	public void removeReplicaRequest(RegisteredReplicaRequest r) {
	}
}
