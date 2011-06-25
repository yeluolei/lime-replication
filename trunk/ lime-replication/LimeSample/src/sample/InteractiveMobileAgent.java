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
import lime.mobileagent.mucode.*;
import lime.*;

/**
 * An interactive agent used for testing the tuple spaces.  A user interface
 * appears where the user can directly perform tuple space operations on a
 * default tuple space.
 */

public class InteractiveMobileAgent extends MobileAgent
  implements IConsoleProvider {
	
  LimeTupleSpace lts = null;
  transient private LimeConsole c;
  boolean started = false;

  public InteractiveMobileAgent() throws LimeException {
    super();
  }
	
  public void setConsole(LimeConsole c) { this.c = c; }
  public LimeConsole getConsole() { return c; }

  public void doRun() {
    try { 
      if (!started) {
        lts = new LimeTupleSpace();
        started = true;
      }
      c = new LimeConsole(getMgr().getID(), lts, this);
      lts.print();
      while(true) {
        String dest = c.performQueuedOp(); 
        if (dest != null) {
          c.quit();
          // migrate(dest, MuServer.FULL, null, false);
          //migrate(dest);
          String[] classes = new String[1];
          classes[0] = "InteractiveMobileAgent";
          migrate(dest, classes, null, false);
        }
      }
    } catch (Exception e) { e.printStackTrace(); }
  }
}
