package replication;

import java.io.Serializable;
import java.net.MulticastSocket;
import java.util.Iterator;
import java.util.Vector;

import lights.Field;
import lights.interfaces.IField;
import lights.interfaces.ITuple;

public class VersionedTuple implements ITuple {
	public static int DEFAULTID = 0;
	public static int DEFAULTVERSION = 0;
	protected Vector _fields = null;
	protected Vector _forms = null;

	/** Creates an uninitialized tuple. */
	public VersionedTuple() {
		_fields = new Vector();
		_fields.addElement(DEFAULTID);           // Tuple ID
		_fields.addElement(DEFAULTVERSION);	     // Tuple Version
		_forms = new Vector();
		_forms.addElement(Integer.class);        // Tuple ID
		_forms.addElement(Integer.class);        // Tuple Version
	}
	

	@Override
	public ITuple add(IField arg0) {
		_fields.addElement(arg0);
		return null;
	}

	@Override
	public ITuple addActual(Serializable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITuple addFormal(Class formal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IField get(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IField[] getFields() {
		IField[] ret = new Field[_fields.size()];
		_fields.copyInto(ret);
		return ret;
	}

	@Override
	public ITuple insertAt(IField field, int index) {
		
		return null;
	}

	@Override
	public int length() {
		_fields.size();
		return 0;
	}

	@Override
	public boolean matches(ITuple tuple) {
		boolean matching = (_fields.size() == tuple.length());
		int i = 0;
		while (matching && i < _fields.size()) {
			matching = matching
					&& ((IField) _fields.elementAt(i)).matches(tuple.get(i));
			i++;
		}
		return matching;
	}

	@Override
	public ITuple removeAt(int index) {
		_fields.remove(index);
		return null;
	}

	@Override
	public ITuple set(IField arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// for debug use
	public String toString() {
		String result = null;
		for (int i = 0; i < length(); i++)
			result = (result == null) ? (get(i).toString())
					: (result + ", " + get(i).toString());
		return "<" + result + ">";
	}

}
