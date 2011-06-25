package replication;

import java.io.Serializable;
import java.util.Vector;

import lights.adapters.Tuple;
import lights.interfaces.IField;
import lights.interfaces.ITuple;
import lime.AgentLocation;
import lime.LimeServer;
import lime.LimeTupleID;
import lime.LimeTupleSpace;

@SuppressWarnings("serial")
public class ReplicableTuple implements ITuple {
	private Tuple tuple;
	public static final int DEFAULT_VERSION = 1;
	public static final int REPLICA_USED_LENTH = 5;
	@SuppressWarnings("rawtypes")
	public ReplicableTuple(String name , int id){
		tuple = new Tuple();
		tuple.addFormal(AgentLocation.class);   // origCur
		tuple.addFormal(AgentLocation.class);   // origDest
		tuple.addFormal(Boolean.class);     // isReplica
		tuple.addFormal(LimeTupleID.class); // ID
		tuple.addFormal(Integer.class);		// Version
		
		
		tuple.addActual(AgentLocation.UNSPECIFIED);
		tuple.addActual(AgentLocation.UNSPECIFIED);
		tuple.addActual(false);
		tuple.addActual(new LimeTupleID(id));
		tuple.addActual(DEFAULT_VERSION);
	}
	
	@Override
	public ITuple add(IField field) {
		tuple.add(field);
		return tuple;
	}

	@Override
	public ITuple addActual(Serializable arg0) {
		tuple.addActual(arg0);
		return tuple;
	}

	@Override
	public ITuple addFormal(Class arg0) {
		tuple.addFormal(arg0);
		return tuple;
	}

	@Override
	public IField get(int arg0) {
		return tuple.get(arg0);
	}

	@Override
	public IField[] getFields() {
		return tuple.getFields();
	}

	@Override
	public ITuple insertAt(IField arg0, int arg1) {
		tuple.insertAt(arg0, arg1);
		return null;
	}

	@Override
	public int length() {
		return tuple.length();
	}

	@Override
	public boolean matches(ITuple tuple) {
		return tuple.matches(tuple);
	}

	@Override
	public ITuple removeAt(int index) {
		return tuple.removeAt(index + REPLICA_USED_LENTH);
	}

	@Override
	public ITuple set(IField arg0, int arg1) {
		return tuple.set(arg0, arg1+REPLICA_USED_LENTH);
	}
	
	
	public boolean isMaster(){
		return !((Boolean) tuple.get(2).getValue());
	}
}
