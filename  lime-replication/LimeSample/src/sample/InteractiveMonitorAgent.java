package sample;

/* LIME 1.1 - A middleware for coordination of mobile agents and mobile hosts
 * Copyright (C) 2003,
 * Chien-Liang Fok, Christine Julien, Radu Handorean, Rohan Sen, Tom Elgin,
 * Amy L. Murphy, Gian Pietro Picco, and Gruia-Catalin Roman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

import lime.util.console.*;
import lime.*;
import lights.adapters.*;
import lights.interfaces.*;
import java.net.*;

/**
 * This is a standard stationary interactive agent which also installs several
 * reactions on the LimeSystemTupleSpace in order to monitor the coming and
 * going of the hosts/agents/tuple spaces.  All detected changes are written
 * to the console.
 */

public class InteractiveMonitorAgent extends StationaryAgent 
  implements IConsoleProvider {
  LimeTupleSpace lts = null;
  LimeSystemTupleSpace slts = null;
  LimeConsole c;

  public void setConsole(LimeConsole c) { this.c = c; }
  public LimeConsole getConsole() { return c; }

  public InteractiveMonitorAgent() throws LimeException {
    super("InteractiveMonitorAgent");
  }
	
  public void run() { 
    try { 
      lts = new LimeTupleSpace(); 
      slts = new LimeSystemTupleSpace();
    } catch (LimeException le) { le.printStackTrace(); }
    c = new LimeConsole(getMgr().getID(), lts, this);
    // add several reactions on the LSTS
    try{
      LimeServerID lsID = null;
      try{
        lsID = new LimeServerID(InetAddress.getLocalHost(),
                                LimeServer.getServer().getPort());
      } catch (UnknownHostException uhe) {
        System.out.println("problem identifying local host, terminating");
        uhe.printStackTrace();
        System.exit(1);
      }
      HostLocation thisHost = new HostLocation(lsID);
			
      // The format of the tuples in the LSTS are:
      // <_host, LimeServerID>
      //     <_host_gone, LimeServerID>
      // <_agent, agentID, LimeServerID>
      //     <_agent_gone, agentID, LimeServerID>
      // <_tuplespace, tsname, agentID>
      //     <_tuplespace_gone, tsname, agentID>

      // AGENT patterns
      // agent arriving
      ITuple newAgentPattern = new Tuple().addActual("_agent")
        .addFormal(AgentID.class)
        .addFormal(LimeServerID.class); 
      // agent departing
      ITuple oldAgentPattern = new Tuple().addActual("_agent_gone")
        .addFormal(AgentID.class)
        .addFormal(LimeServerID.class); 
      // generic agent either arriving or departing
      ITuple genericAgentPattern = new Tuple().addFormal(String.class)
        .addFormal(AgentID.class)
        .addFormal(LimeServerID.class); 

      // HOST patterns
      // host engaging
      ITuple newHostPattern = new Tuple().addActual("_host")
        .addFormal(LimeServerID.class); 
      // host departing
      ITuple oldHostPattern = new Tuple()
        .addActual("_host_gone")
        .addFormal(LimeServerID.class); 

      // TUPLESPACE patterns
      // tuple space sharing
      ITuple newTSPattern = new Tuple().addActual("_tuplespace")
        .addFormal(String.class)
        .addFormal(AgentID.class); 
      // tuple space no longer sharing
      ITuple oldTSPattern = new Tuple().addActual("_tuplespace_gone")
        .addFormal(String.class)
        .addFormal(AgentID.class); 


      // AGENT reactions
      LimeSystemReaction lr1 = 
        new LimeSystemReaction ( newAgentPattern, 
                                 new ConsoleWriterListener(c,"Agent arrived."), 
                                 Reaction.ONCEPERTUPLE);
      LimeSystemReaction lr2 = 
        new LimeSystemReaction ( oldAgentPattern, 
                                 new ConsoleWriterListener(c,"Agent departed."), 
                                 Reaction.ONCEPERTUPLE);

      // HOST reactions
      LimeSystemReaction lr3 = 
        new LimeSystemReaction ( newHostPattern, 
                                 new ConsoleWriterListener(c, "Host arrived."), 
                                 Reaction.ONCEPERTUPLE);
      LimeSystemReaction lr4 = 
        new LimeSystemReaction ( oldHostPattern, 
                                 new ConsoleWriterListener(c, "Host departed."), 
                                 Reaction.ONCEPERTUPLE);

      // TUPLESPACE reactions
      LimeSystemReaction lr5 = 
        new LimeSystemReaction ( newTSPattern, 
                                 new ConsoleWriterListener(c, "Tuple space arrived."), 
                                 Reaction.ONCEPERTUPLE);
      LimeSystemReaction lr6 = 
        new LimeSystemReaction ( oldTSPattern, 
                                 new ConsoleWriterListener(c, "Tuple space departed."), 
                                 Reaction.ONCEPERTUPLE);
			
      slts.addReaction(
                       new LimeSystemReaction[]{lr1, lr2, lr3, lr4, lr5, lr6});
    } catch(LimeException le){ le.printStackTrace(); }
    while(true) { 
      c.performQueuedOp(); 

      try {
        ITuple t = slts.rdp(new Tuple());
      } catch (TupleSpaceEngineException tsee) {
        tsee.printStackTrace();
        System.exit(0);
      }
		
    }
  }
}

class ConsoleWriterListener implements ReactionListener {
  LimeConsole lc;
  String msg;
  public ConsoleWriterListener(LimeConsole c, String msg) {
    lc = c;
    this.msg = msg;
  }
  public void reactsTo(ReactionEvent re) {
    lc.display(msg+":     "+re.getEventTuple().toString()+"\n");
  }
}

