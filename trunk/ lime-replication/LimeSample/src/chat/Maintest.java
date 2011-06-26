package chat;

import java.io.StreamCorruptedException;

import lime.AgentCreationException;
import lime.LimeServer;
import lime.util.Launcher;

public class Maintest {
	public static void main(String[] args){
		new Launcher().launch(args, 0);
		LimeServer.getServer().setProperty("debug","true");
		try {
			LimeServer.getServer().loadAgent("chat.LChat",new String[]{});
			LimeServer.getServer().loadAgent("chat.LChat",new String[]{});
		} catch (AgentCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
