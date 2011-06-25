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

import lime.*;
import lights.adapters.*;
import lights.interfaces.*;

/**
 * 
 * This is an extremely simple Lime application which demonstrates how to launch
 * Lime from within an application.  To run this application:
 * 
 * java SimpleLime one_word_message [lime args]
 * 
 * The one word message (a single word) will be put in a tuple and written to
 * the tuple space, the lime args are optional and can be any of the standard
 * Lime arguments (see lime.util.Launcher).  When the tuple is successfully
 * written, a message will be printed to standard out.
 * 
 * Sample output:
 * >java SimpleLime Hi -mcast off
 * Lime:Factory set to lights.adapters.builtin.TupleSpaceFactory
 * Lime:Lime server murphy:1973 activated
 * Lime:Agent SimpleLime loaded and started.
 * I wrote the tuple: <Hi>
 * Lime:Shutting down Lime server...
 * Lime:Done.
 * 
 */

public class SimpleLime extends StationaryAgent {
  static final int NUMLOCALPARAMETERS = 1;
  String msg = null;
	
  // This constructor receives the parameters we passed in server.loadAgent()
  public SimpleLime (String msg) {
    this.msg = msg;
  }
	
  public static void main (String[] args) {
		
    // must be at least one argument 
    if (args.length == 0) {
      System.out.println("Usage: java SimpleLime one_word_message [lime args]\n");
      System.exit(1);
    }

    // Pass Lime arguments (if any) through the Launcher and launch the
    // LimeServer. In this case, NUMLOCALPARAMETERS is the index of the 
    // first Lime parameter (as opposed to the index of the application
    // parameter)
    new lime.util.Launcher().launch(args,NUMLOCALPARAMETERS);
    LimeServer.getServer().setProperty("debug", "true");

		
    // load a SimpleLime, passing the first command line argument as the
    // only paramter
    try{
      LimeServer.getServer().loadAgent("sample.SimpleLime",
                                       new String[]{args[0]});
    } catch (LimeException le) { 
      System.out.println("Trouble Loading the agent");
      le.printStackTrace(); 
    }
  }

  public void run () { 
    LimeTupleSpace lts = null;
    ITuple myTuple = new Tuple().addActual(msg);
    // create the new tuple space (default name)
    try {
      lts = new LimeTupleSpace();
      lts.out(myTuple);
      lts.print();
    } catch(LimeException le) {
      System.out.println("Trouble creating tuple space and writing to it");
      le.printStackTrace();
      System.exit(1);
    }
   
    System.out.println("I wrote the tuple: "+myTuple);
    
		
    // shut down Lime gracefully
    LimeServer.getServer().shutdown(true);
  }
}
