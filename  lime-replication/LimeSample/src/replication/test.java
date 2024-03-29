package replication;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import devutil.Queue;

import lime.AgentLocation;
import lime.LimeException;
import lime.LimeServer;
import lime.LimeTupleID;
import lime.RegisteredReaction;
import lime.StationaryAgent;
import lime.TupleSpaceEngineException;

public class test extends StationaryAgent implements ActionListener{
	static final int NUMLOCALPARAMETERS = 0;
	String msg = null;
	ReplicableLimeTupleSpace rlts = null;
	ReplicableTuple myTuple = null;
	private Queue opque ;
	private RegisteredReaction  reaction = null;

	// This constructor receives the parameters we passed in server.loadAgent()
	public test() {
		mainwindow = new JFrame();
		initialize();
	}

	public static void main(String[] args) {

		// must be at least one argument


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
					new String[] {});
			LimeServer.getServer().loadAgent("replication.test",
					new String[] {});
			LimeServer.getServer().loadAgent("replication.test",
					new String[] {});
		} catch (LimeException le) {
			System.out.println("Trouble Loading the agent");
			le.printStackTrace();
		}
	}

	public void run() {
		this.opque = new Queue();
		rlts = new ReplicableLimeTupleSpace("ts");
		mainwindow.show();
		opque.add(new Runnable() {
			
			@Override
			public void run() {
				rlts.setShared(false);
				jButton4.setEnabled(false);
				ReplicableTuple template = new ReplicableTuple().addFormal(String.class);
				reaction = rlts.addReplicaRequest(template,ReplicableLimeTupleSpace.REPLICATION_MODE_ANY,
						ReplicableLimeTupleSpace.CONSISTENCY_MODE_ANY);
			}
		});
		
		// create the new tuple space (default name)
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			Runnable runnable = (Runnable) opque.remove();
			if (runnable == null){
				break;
			}else {
				runnable.run();
			}
		}
//		while (true) {
//			try {
//				String input = br.readLine();
//				if (input.equals("false")){
//					rlts.setShared(false);
//				}
//				else if (input.equals("true")){
//					rlts.setShared(true);
//				}else {
//					AgentLocation local = new AgentLocation(getMgr().getID());
//					ReplicableTuple myTuple = (ReplicableTuple) rlts.createReplicableTuple()
//												.setCur(new AgentLocation(getMgr().getID())).addActual(input);
//					rlts.out(myTuple);
//					System.out.println("\n\n----------\n" + local.toString() + ":");
//					rlts.print();
//					System.out.println("-------------\n\n");
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (TupleSpaceEngineException e) {
//				e.printStackTrace();
//			}
//		}
		// shut down Lime gracefully
	}
	
	private static final long serialVersionUID = 1L;
	private JFrame mainwindow = null;
	private JPanel jContentPane = null;
	private JTextField jTextField = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JPanel jPanel = null;
	private JButton jButton3 = null;
	private JButton jButton4 = null;
		
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("send");
			jButton.addActionListener(this);
			jButton.setActionCommand("send");
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("update");
			jButton1.addActionListener(this);
			jButton1.setActionCommand("update");
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("print");
			jButton2.addActionListener(this);
			jButton2.setActionCommand("print");
		}
		return jButton2;
	}
	
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setText("shared");
			jButton3.addActionListener(this);
			jButton3.setActionCommand("shared");
		}
		return jButton3;
	}
	
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new JButton();
			jButton4.setText("unshared");
			jButton4.addActionListener(this);
			jButton4.setActionCommand("unshared");
		}
		return jButton4;
	}
	

	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		mainwindow.setSize(402, 146);
		mainwindow.setContentPane(getJContentPane());
		mainwindow.setTitle(new AgentLocation(getMgr().getID()).toString());
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJTextField(), BorderLayout.NORTH);
			jContentPane.add(getJButton(), BorderLayout.WEST);
			jContentPane.add(getJButton1(), BorderLayout.EAST);
			jContentPane.add(getJButton2(), BorderLayout.SOUTH);
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJButton3(), BorderLayout.NORTH);
			jPanel.add(getJButton4(), BorderLayout.SOUTH);
		}
		return jPanel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("send")){
			opque.add(new Runnable(){
				@Override
				public void run() {
					AgentLocation local = new AgentLocation(getMgr().getID());
					myTuple = (ReplicableTuple) rlts.createReplicableTuple()
												.setCur(new AgentLocation(getMgr().getID())).addActual(jTextField.getText());
					jTextField.setText("");
					try {
						rlts.out(myTuple);
					} catch (TupleSpaceEngineException e1) {
						e1.printStackTrace();
					}
					System.out.println("\n\n----------\n" + local.toString() + ":");
					rlts.print();
					System.out.println("-------------\n\n");	
				}
			});
		}
		else if (e.getActionCommand().equals("shared")) {
			opque.add(new Runnable(){
				@Override
				public void run() {
					rlts.setShared(true);
					jButton3.setEnabled(false);
					jButton4.setEnabled(true);
					System.out.println("\n\n---------\nshare open\n----------\n\n");
				}
			});
		}else if (e.getActionCommand().equals("unshared")){
			opque.add(new Runnable(){
				@Override
				public void run() {
					rlts.setShared(false);
					jButton3.setEnabled(true);
					jButton4.setEnabled(false);
					//rlts.removeReplicaRequest(reaction);
					System.out.println("\n\n---------\nshare closed\n----------\n\n");
				}
			});
		}else if (e.getActionCommand().equals("print")){
			opque.add(new Runnable() {
				@Override
				public void run() {
					System.out.println("\n\n----------\n" + new AgentLocation(getMgr().getID()).toString() + ":");
					rlts.print();
					System.out.println("-------------\n\n");	
				}
			});
		}else {
			opque.add(new Runnable() {
				@Override
				public void run() {
					ReplicableTuple tuplet = new ReplicableTuple();
					tuplet.addFormal(String.class);
					tuplet.setID(new LimeTupleID(1));
					
					
					ReplicableTuple t = new ReplicableTuple();
					t.addActual("updated");
					rlts.change(tuplet, t);
				}
			});
		}
		
	}
}
