package replication;

import lime.ReactionEvent;
import lime.ReactionListener;

@SuppressWarnings("serial")
public class ReplicationListener implements ReactionListener {
	private int replicationMode;
	private int consistencyMode;

	public ReplicationListener(int repmode, int conmode) {
		this.replicationMode = repmode;
		this.consistencyMode = conmode;
	}

	@Override
	public void reactsTo(ReactionEvent e) {
		ReplicableTuple tuple = (ReplicableTuple) e.getEventTuple();
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

	private void cons(ReplicableTuple tuple) {
		switch (consistencyMode) {
		case ReplicableLimeTupleSpace.CONSISTENCY_MODE_ANY: {
			break;
		}
		case ReplicableLimeTupleSpace.CONSISTENCY_MODE_MASTER: {
			break;
		}
		case ReplicableLimeTupleSpace.CONSISTENCY_MODE_NEVER: {
			break;
		}
		}
	}

}
