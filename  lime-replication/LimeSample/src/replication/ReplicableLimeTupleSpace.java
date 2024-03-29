package replication;

import lights.adapters.Tuple;
import lime.AgentLocation;
import lime.HostLocation;
import lime.ILimeAgent;
import lime.IllegalTupleSpaceNameException;
import lime.LimeServer;
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
	private AgentLocation local;
	
	public static final int REPLICATION_MODE_MASTER = 1;
	public static final int REPLICATION_MODE_ANY = 2;
	public static final int CONSISTENCY_MODE_MASTER = 1;
	public static final int CONSISTENCY_MODE_ANY = 2;
	public static final int CONSISTENCY_MODE_NEVER = 3;
	
	private String name;
	private static int maxid = 1;

	public ReplicableLimeTupleSpace(String name) {
		try {
			Thread t = Thread.currentThread();
			lts = new LimeTupleSpace(name);
			this.name = name;
			creator = (ILimeAgent)t;
			local = new AgentLocation(creator.getMgr().getID()); 
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
	public ReplicableTuple inp(HostLocation hostLocation,AgentLocation destination,ReplicableTuple template)
	throws TupleSpaceEngineException{
		return new ReplicableTuple(lts.inp(hostLocation, destination, template.getTuple()));
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
		try {
			Tuple temp = (Tuple) lts.inp(local,AgentLocation.UNSPECIFIED,template.getTuple());
			if(temp != null){
				ReplicableTuple tuple = new ReplicableTuple(temp);
				if (tuple.isMaster()){
					t.setCur(tuple.getCur());
					t.setDest(tuple.getDest());
					t.setID(tuple.getID());
					t.setVersion(tuple.getVersion()+1);
					t.setRepli(ReplicableTuple.IS_MASTER);
					lts.out(t.getTuple());
				}
			}
		} catch (TupleSpaceEngineException e) {
			e.printStackTrace();
		}
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
		private ReplicableTuple template;
		public ReplicationListener(int repmode, int conmode , ReplicableTuple template) {
			this.replicationMode = repmode;
			this.consistencyMode = conmode;
			this.template = template;
		}

		@Override
		public void reactsTo(ReactionEvent e) {
			ReplicableTuple tuple = new ReplicableTuple(e.getEventTuple());
			System.out.println(local.toString() + " : "+e.getEventTuple().toString()+"\n");
			// if is local, do nothing
			if (!tuple.getCur().equals(local)) {
				boolean entercon = false;
				switch (replicationMode) {
				case ReplicableLimeTupleSpace.REPLICATION_MODE_MASTER: {
					if (tuple.isMaster()) {
						entercon =true;
					}
					break;
				}
				case ReplicableLimeTupleSpace.REPLICATION_MODE_ANY: {
					entercon =true;
					break;
				}
				}
				
				if (entercon){
					ReplicableTuple localmatch = null;
					ReplicableTuple t = new ReplicableTuple();
					try {
						for (int i = 0 ; i < template.getFields().length ; i++){
							t.add(template.getFields()[i]);
						}
						t.setID(tuple.getID());
						Tuple temp = (Tuple) lts.rdp(local,
								AgentLocation.UNSPECIFIED, t.getTuple());
						if (temp != null){
							localmatch = new ReplicableTuple(temp);
						}
					}
					catch (TupleSpaceEngineException e1) {
						e1.printStackTrace();
					}
					if (localmatch == null) {
						try {
							tuple.setRepli(ReplicableTuple.IS_REPLICA);
							out(tuple);
							if (LimeServer.getServer().isDebugOn()){
								System.out.println("\n******* replication ********\n");
							}
							
						} catch (TupleSpaceEngineException e1) {
							e1.printStackTrace();
						}
					} else {
						// 如果本地已经有一个备份，检查版本
						// 如果版本号大于本地版本，更新
						if (tuple.getVersion() > localmatch.getVersion()) {
							switch (consistencyMode) {
							case ReplicableLimeTupleSpace.CONSISTENCY_MODE_ANY: {
								try {
									lts.inp(local,
											AgentLocation.UNSPECIFIED, t.getTuple()); // remove the old copy
									tuple.setRepli(ReplicableTuple.IS_REPLICA);
									lts.out(tuple.getTuple());     //  add the new copy
								} catch (TupleSpaceEngineException e1) {
									e1.printStackTrace();
								}
								break;
							}
							case ReplicableLimeTupleSpace.CONSISTENCY_MODE_MASTER: {
								if (tuple.isMaster()){
									try {
										lts.inp(local,
												AgentLocation.UNSPECIFIED, t.getTuple());
										tuple.setRepli(ReplicableTuple.IS_REPLICA);
										lts.out(tuple.getTuple());     //  add the new copy
									} catch (TupleSpaceEngineException e1) {
										e1.printStackTrace();
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
		}
	}

	public void print() {
		lts.print();
	}
}
