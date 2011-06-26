package replication;

import java.io.Serializable;

import lights.adapters.Tuple;
import lights.interfaces.IField;
import lights.interfaces.ITuple;
import lime.AgentLocation;
import lime.LimeTupleID;

public class ReplicableTuple {
	private Tuple tuple;
	public static final int DEFAULT_VERSION = 1;
	public static final int REPLICA_USED_LENTH = 5;
	
	public static final int IS_REPLICA_TAG = 2;
	public static final int IS_REPLICA = 1;
	public static final int IS_MASTER = 0;
	
	
	
	
	public ReplicableTuple(ITuple tuple){
		this.tuple = (Tuple) tuple;
	}
	
	public ReplicableTuple(){
		tuple = new Tuple();
		tuple.addFormal(AgentLocation.class);   // origCur
		tuple.addFormal(AgentLocation.class);   // origDest
		tuple.addFormal(Integer.class);     // isReplica
		tuple.addFormal(LimeTupleID.class); // ID
		tuple.addFormal(Integer.class);		// Version
	}
	public ReplicableTuple(String name , int id){
		tuple = new Tuple();
		tuple.addFormal(AgentLocation.class);   // origCur
		tuple.addFormal(AgentLocation.class);   // origDest
		tuple.addFormal(Integer.class);     // isReplica
		tuple.addFormal(LimeTupleID.class); // ID
		tuple.addFormal(Integer.class);		// Version
		
		
//		tuple.addActual(AgentLocation.UNSPECIFIED);
//		tuple.addActual(AgentLocation.UNSPECIFIED);
//		tuple.addActual(IS_MASTER);
//		tuple.addActual(new LimeTupleID(id));
//		tuple.addActual(DEFAULT_VERSION);
		tuple.get(0).setToActual(AgentLocation.UNSPECIFIED);
		tuple.get(1).setToActual(AgentLocation.UNSPECIFIED);
		tuple.get(2).setToActual(IS_MASTER);
		tuple.get(3).setToActual(new LimeTupleID(id));
		tuple.get(4).setToActual(DEFAULT_VERSION);
	}
	
	public ReplicableTuple updateVersion(){
		tuple.get(4).setToActual((Integer)tuple.get(4).getValue() + 1);
		return this;
	}
	
	
	public ReplicableTuple add(IField field) {
		tuple.add(field);
		return this;
	}

	
	public ReplicableTuple addActual(Serializable arg0) {
		tuple.addActual(arg0);
		return this;
	}

	
	public ReplicableTuple addFormal(Class arg0) {
		tuple.addFormal(arg0);
		return this;
	}

	
	public IField get(int arg0) {
		return tuple.get(arg0 + REPLICA_USED_LENTH);
	}

	
	public IField[] getFields() {
		IField[] repfields = tuple.getFields();
		IField []result = new IField[repfields.length - REPLICA_USED_LENTH];
		for (int i = 0 ; i < repfields.length - REPLICA_USED_LENTH ; i++)
		{
			result[i] = repfields[i+REPLICA_USED_LENTH];
		}
		return result;
	}

	
	public ReplicableTuple insertAt(IField arg0, int arg1) {
		tuple.insertAt(arg0, arg1 + REPLICA_USED_LENTH);
		return this;
	}

	
	public int length() {
		return tuple.length();
	}

	
	public boolean matches(ITuple tuple) {
		return tuple.matches(tuple);
	}

	
	public ReplicableTuple removeAt(int index) {
		tuple.removeAt(index + REPLICA_USED_LENTH);
		return this;
	}

	
	public ReplicableTuple set(IField arg0, int arg1) {
		tuple.set(arg0, arg1+REPLICA_USED_LENTH);
		return this;
	}
	
	
	public boolean isMaster(){
		return tuple.get(IS_REPLICA_TAG).getValue().equals(IS_MASTER);
	}
	
	public ReplicableTuple setCur(AgentLocation cur)
	{
		this.tuple.get(0).setToActual(cur);
		return this;
	}
	
	public ReplicableTuple setDest(AgentLocation dest)
	{
		this.tuple.get(1).setToActual(dest);
		return this;
	}
	
	public ReplicableTuple setRepli(int replica)
	{
		this.tuple.get(2).setToActual(replica);
		return this;
	}
	
	public ReplicableTuple setID(LimeTupleID id){
		this.tuple.get(3).setToActual(id);
		return this;
	}
	
	public ReplicableTuple setVersion(int v){
		this.tuple.get(4).setToActual(v);
		return this;
	}
	
	public AgentLocation getCur()
	{
		return (AgentLocation) this.tuple.get(0).getValue();
	}
	
	public AgentLocation getDest()
	{
		return (AgentLocation) this.tuple.get(1).getValue();
	}
	
	public LimeTupleID getID(){
		return (LimeTupleID) this.tuple.get(3).getValue();
	}
	
	public int getVersion(){
		return (Integer) this.tuple.get(4).getValue();
	}
	
	public Tuple getTuple(){
		return tuple;
	}
}
