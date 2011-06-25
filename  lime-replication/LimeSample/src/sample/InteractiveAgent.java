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
import devutil.awt.*;
import java.awt.*;

/**
 * An interactive agent used for testing the tuple spaces.  A user interface
 * appears where the user can directly perform tuple space operations on a
 * default tuple space.
 */

public class InteractiveAgent extends StationaryAgent 
  implements IConsoleProvider {
  LimeTupleSpace lts = null;
  LimeConsole c;
	
  public InteractiveAgent() throws LimeException {
    super("InteractiveAgent");
  }
  
  public void setConsole(LimeConsole c) { this.c = c; }
  public LimeConsole getConsole() { return c; }
	
  public void run() { 
    try { 
      lts = new LimeTupleSpace(); 
    } catch (LimeException le) { le.printStackTrace(); }
    c = new LimeConsole(getMgr().getID(), lts, this);
    while(true) {
      String s = c.performQueuedOp();
      if (s != null)
        new MsgDialog(new Frame(), "Error!", "This agent is stationary, it cannot migrate.");
    }
  }
}
