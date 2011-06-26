package replication;

import lights.adapters.Tuple;
import lime.AgentLocation;
import lime.ILimeAgent;
import lime.IllegalTupleSpaceNameException;
import lime.LimeTupleSpace;
import lime.LocalizedReaction;
import lime.NoSuchReactionException;
import lime.Reaction;
import lime.ReactionEvent;
import lime.ReactionListener;
import lime.RegisteredReaction;
import lime.TupleSpaceEngineException;
import lime.UbiquitousReaction;

public class ReplicableLimeTupleSpace{
    ILimeAgent creator = null;
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
			Thread t = Thread.currentThread();
			maxid = 1;
			lts = new LimeTupleSpace(name);
			this.name = name;
			creator = (ILimeAgent)t;
			initReaction();
		} catch (IllegalTupleSpaceNameException e) {
			e.printStackTrace();
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
	}
	
	public ReplicableTuple createReplicableTuple(){
		ReplicableTuple ret =  new ReplicableTuple(name, maxid);
		maxid += 1;
		return ret;
	}
	
	private void initReaction(){
		
	}
	public boolean setShared(boolean isShared) {
		return lts.setShared(isShared);
	}
	public void out(ReplicableTuple t)
	throws TupleSpaceEngineException {
		lts.out(t.getTuple());
	}
	public void out(AgentLocation destination, ReplicableTuple t)
	throws TupleSpaceEngineException {
		lts.out(destination, t.getTuple());
	}
	public ReplicableTuple in(ReplicableTuple template) throws TupleSpaceEngineException {
		return new ReplicableTuple(lts.in(template.getTuple()));
	}
	public ReplicableTuple inp(AgentLocation current,AgentLocation destination,ReplicableTuple template)
	throws TupleSpaceEngineException{
		return new ReplicableTuple(lts.inp(current, destination, template.getTuple()));
	}
	public ReplicableTuple[] ing(AgentLocation current, AgentLocation destination ,ReplicableTuple template)
	throws TupleSpaceEngineException {
		Tuple[] retTuples = (Tuple[]) lts.ing(current, destination, template.getTuple());
		ReplicableTuple[] result = new ReplicableTuple[retTuples.length];
		for (int i = 0 ; i < retTuples.length ; i++){
			result[i] = new ReplicableTuple(retTuples[i]);
		}
		return result;
	}
	
	public ReplicableTuple rd(ReplicableTuple template)
	throws TupleSpaceEngineException {
		return new ReplicableTuple(lts.rd(template.getTuple()));
	}
	public ReplicableTuple rdp(AgentLocation current,AgentLocation destination,ReplicableTuple template)
	throws TupleSpaceEngineException {
		return new ReplicableTuple(lts.rdp(current, destination, template.getTuple()));
	}
	public ReplicableTuple[] rdg(AgentLocation current, AgentLocation destination ,ReplicableTuple template) 
	throws TupleSpaceEngineException {
		Tuple[] retTuples = (Tuple[]) lts.rdg(current, destination, template.getTuple());
		ReplicableTuple[] result = new ReplicableTuple[retTuples.length];
		for (int i = 0 ; i < retTuples.length ; i++){
			result[i] = new ReplicableTuple(retTuples[i]);
		}
		return result;
	}
	public RegisteredReaction[] addStrongReaction(LocalizedReaction[] lr) 
	throws TupleSpaceEngineException {
		return lts.addStrongReaction(lr);
	}
	public RegisteredReaction[] addWeakReaction(Reaction[] r) 
	throws TupleSpaceEngineException {
		return lts.addWeakReaction(r);
	}
	public void removeStrongReaction(RegisteredReaction[] rr)
	throws TupleSpaceEngineException, NoSuchReactionException {
		lts.removeStrongReaction(rr);
	}
	public void removeWeakReaction(RegisteredReaction[] rr)
	throws TupleSpaceEngineException, NoSuchReactionException {
		lts.removeWeakReaction(rr);
	}
	// REPLICATION-SPECIFIC OPERATIONS
	public ReplicableTuple change(ReplicableTuple template, ReplicableTuple t) {
		return null;
	}
	
	
	public RegisteredReaction addReplicaRequest(ReplicableTuple template,
			int replicationMode,
			int consistencyMode) {
		try {
			Reaction reaction = new UbiquitousReaction(template.getTuple(),
					new ReplicationListener(replicationMode, consistencyMode,template),
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
	
	@SuppressWarnings("serial")
	class ReplicationListener implements ReactionListener {
		private int replicationMode;
		private int consistencyMode;
		private AgentLocation local;
		private ReplicableTuple template;
		public ReplicationListener(int repmode, int conmode , ReplicableTuple template) {
			this.replicationMode = repmode;
			this.consistencyMode = conmode;
			this.template = template;
			local = new AgentLocation(creator.getMgr().getID()); 
		}

		@Override
		public void reactsTo(ReactionEvent e) {
			ReplicableTuple tuple = (ReplicableTuple) e.getEventTuple();
			// if is local, do nothing
			if (!tuple.getCur().equals(local)) {
				switch (replicationMode) {
				case ReplicableLimeTupleSpace.REPLICATION_MODE_MASTER: {
					if (tuple.isMaster()) {
						cons(tuple);
					}
					break;
				}
				case ReplicableLimeTupleSpace.REPLICATION_MODE_ANY: {
					cons(tuple);
					break;
				}
				}
			}
		}

		private void cons(ReplicableTuple tuple) {
			// 从自己的空间中找到ID相同的
			// 根据
			ReplicableTuple localmatch = null;
			try {
				template.setID(tuple.getID());
				localmatch = (ReplicableTuple) lts.rdp(local,
						AgentLocation.UNSPECIFIED, template.getTuple());
			} catch (TupleSpaceEngineException e) {
				e.printStackTrace();
			}
			if (localmatch == null) {
				try {
					out(tuple);
				} catch (TupleSpaceEngineException e) {
					e.printStackTrace();
				}
			} else {
				// 如果本地已经有一个备份，检查版本
				// 如果版本号大于本地版本，更新
				if (tuple.getVersion() > localmatch.getVersion()) {
					switch (consistencyMode) {
					case ReplicableLimeTupleSpace.CONSISTENCY_MODE_ANY: {
						try {
							lts.in(localmatch.getTuple()); // remove the old copy
							lts.out(tuple.getTuple());     //  add the new copy
						} catch (TupleSpaceEngineException e) {
							e.printStackTrace();
						}
						break;
					}
					case ReplicableLimeTupleSpace.CONSISTENCY_MODE_MASTER: {
						if (tuple.isMaster()){
							try {
								lts.in(localmatch.getTuple()); // remove the old copy
								lts.out(tuple.getTuple());     //  add the new copy
							} catch (TupleSpaceEngineException e) {
								e.printStackTrace();
							}
						}
						break;
					}
					case ReplicableLimeTupleSpace.CONSISTENCY_MODE_NEVER: {
						break;
					}
					}
				}
			}
		}
	}

	public void print() {
		lts.print();
	}
}
