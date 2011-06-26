package replication;

import lime.AgentID;
import lime.AgentLocation;
import lime.LimeException;
import lime.LimeServer;
import lime.StationaryAgent;

public class test extends StationaryAgent {
	static final int NUMLOCALPARAMETERS = 1;
	String msg = null;

	// This constructor receives the parameters we passed in server.loadAgent()
	public test(String msg) {
		this.msg = msg;
	}

	public static void main(String[] args) {

		// must be at least one argument
		if (args.length == 0) {
			System.out
					.println("Usage: java SimpleLime one_word_message [lime args]\n");
			System.exit(1);
		}

		// Pass Lime arguments (if any) through the Launcher and launch the
		// LimeServer. In this case, NUMLOCALPARAMETERS is the index of the
		// first Lime parameter (as opposed to the index of the application
		// parameter)
		new lime.util.Launcher().launch(args, NUMLOCALPARAMETERS);
		LimeServer.getServer().setProperty("debug", "true");

		// load a SimpleLime, passing the first command line argument as the
		// only paramter
		try {
			LimeServer.getServer().loadAgent("replication.test",
					new String[] { args[0] });
		} catch (LimeException le) {
			System.out.println("Trouble Loading the agent");
			le.printStackTrace();
		}
	}

	public void run() {
		ReplicableLimeTupleSpace rlts = null;
		rlts = new ReplicableLimeTupleSpace("ts");
		AgentID localId= getMgr().getID();
		AgentLocation local = new AgentLocation(localId);
		ReplicableTuple myTuple = (ReplicableTuple) rlts.createReplicableTuple()
									.setCur(new AgentLocation(getMgr().getID())).addActual(msg);
		// create the new tuple space (default name)
		try {
			rlts.out(myTuple);
			rlts.print();
		} catch (LimeException le) {
			System.out
					.println("Trouble creating tuple space and writing to it");
			le.printStackTrace();
			System.exit(1);
		}

		System.out.println("I wrote the tuple: " + myTuple.getTuple());

		// shut down Lime gracefully
		LimeServer.getServer().shutdown(true);
	}
}
