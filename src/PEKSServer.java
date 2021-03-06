import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


class PEKSSFrame extends JFrame implements ActionListener {

	private JFileChooser jfc;
	private JScrollPane jsp;
	private JTextArea jta;
	private JMenuBar jmb;
	private JMenu jm1;
	private JMenuItem jmi1, jmi2;// jmi1 initial, jmi2 exit
	private ObjectInputStream ois;
	private ServerSocket ss;
	private Socket servs;
	private MessageDigest mds;
	private Hashtable<String, Integer> ht;
	SecretKey key;
	JPanel southPanel;
	JButton openButton;

	RSAPublicKey publicKey;

	RSAdemo rsa = new RSAdemo();
	Base64.Decoder decoder = Base64.getDecoder();
	Base64.Encoder encoder = Base64.getEncoder();
	public PEKSSFrame() {
		
		

		setTitle("PEKS Server");
		jfc = new JFileChooser("test\\DataRecords");
		jta = new JTextArea(10, 10);
		jsp = new JScrollPane(jta);
		jta.setEditable(false);
		jta.setLineWrap(true);

		jmi1 = new JMenuItem("initial");
		jmi2 = new JMenuItem("exit");
		jm1 = new JMenu("System");
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.addSeparator();	
		jmb = new JMenuBar();
		jmb.add(jm1);// add the menu bar
		jmi1.addActionListener(this);
		jmi2.addActionListener(this);// add action listener
		setJMenuBar(jmb);
		add(jsp, BorderLayout.EAST);
		ht = new Hashtable<String, Integer>();// initial the hashtable
		
		southPanel = new JPanel();
		openButton = new JButton("Open");
		openButton.addActionListener(this);
		southPanel.add(openButton);
		add(southPanel, BorderLayout.SOUTH);
		jfc.setControlButtonsAreShown(false);
		add(jfc);
		
		

        
		pack();
		setLocation(300, 100);
		setResizable(false);

	}

	public void loadParameters() {// if the system has not been initialed ,load
									// the system parameters
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					"test\\server.data"));
			publicKey=(RSAPublicKey) ois.readObject();
			System.out.println("publickey is :"+publicKey.toString());
			
			ois.close();
			
			mds = MessageDigest.getInstance("SHA");// initial the
													// messagedigester
			System.out.println("Initial Success, path is: test\\\n");
			jta.append("Initial Success, path is: test\\\n");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jmi1) {
			
			loadParameters();
			socketThread st = new socketThread();
			Thread socketT = new Thread(st);
			socketT.start();
		}

		if (e.getSource() == jmi2) {
			System.exit(0);
		}
		if (e.getSource() == openButton) {
			if (jfc.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(null,
						"No File Choosed! please try again!");
			} else {
				String[] command = { "notepad",
						jfc.getSelectedFile().getAbsolutePath() };
				try {
					Runtime.getRuntime().exec(command);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class socketThread implements Runnable {// this thread is to initial
													// the socket to listen to
													// the port, the
													// mainframe can respond the
													// close operation at the
													// same time.
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ss = new ServerSocket(8901);

				servs = ss.accept();

				jta.append("\n" + servs.getLocalSocketAddress()
						+ "Connect Success.");
			} catch (Exception es) {
				JOptionPane.showMessageDialog(null,
						"socket error has taken place!");
			}
			serverThread st = new serverThread(servs);
			Thread t = new Thread(st);
			t.start();
		}

	}

	private class serverThread implements Runnable {// create a thread for each
													// client
		Socket s;
		ObjectInputStream ois1;
		ObjectOutputStream oos1;
		FileInputStream fis;
		FileOutputStream fos;
		String fileName;
		File tempFile;
		int i;
		JDialog jdg;
		JProgressBar jpb;

		JTextArea showArea;
		JScrollPane jscp;

		public serverThread(Socket s) {
			this.s = s;
			try {
				ois1 = new ObjectInputStream(s.getInputStream());
				oos1 = new ObjectOutputStream(s.getOutputStream());
				jdg = new JDialog();
				jpb = new JProgressBar();
				jpb.setBorderPainted(true);
				jpb.setValue(0);
				jpb.setStringPainted(true);
				jdg.setTitle("Search");
				jdg.add(jpb,BorderLayout.NORTH);
				jpb.setMinimum(0);
				jpb.setMaximum(256);//
				showArea = new JTextArea(10, 10);
				showArea.setLineWrap(true);
				showArea.setBorder(BorderFactory.createTitledBorder("Now Searching:"));
				jscp = new JScrollPane(showArea); 
				jdg.add(jscp, BorderLayout.SOUTH);
				jdg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				jdg.pack();
				jdg.setLocationRelativeTo(getParent());
				jdg.setDefaultLookAndFeelDecorated(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (!s.isClosed()) {
				try {
					Trapdoor t = (Trapdoor) ois1.readObject();
					int type = t.getType();
					if (type==1)
					{
						String tempt=t.getStr();
						//System.out.println("tempt is:"+tempt);
						byte[] sign=decoder.decode(tempt);
						jta.append("\ncomplete reading the trapdoor!\nStarting to seach!");
						i = 0;
						jpb.setValue(i);
						jdg.setVisible(true);
						for (String tempk : ht.keySet()) {
							int tempb = ht.get(tempk);
							//System.out.println("tempb is:"+tempb);
							if (rsa.verify(publicKey,(tempb+"").getBytes(),sign)) {
						
										oos1.writeInt(1);
						        		fileName = tempk;
										tempFile = new File(fileName);
										oos1.writeObject(fileName);

										oos1.flush();
										jpb.setValue(jpb.getMaximum());
										showArea.setText(tempb
												+ "match success!");
										jdg.setVisible(false);	
										i++;
										jpb.setValue(i);
										showArea.setText("Now Matching:"+tempb);
						        	
							}
						}
						if (i==0)
						{
							oos1.writeInt(-1);
						}
						else
						{
							oos1.writeInt(0);
						}
						oos1.flush();
						if (i==0) {
							jpb.setValue(256);
							showArea.setText("complete searching! No file matches!");
							jdg.setVisible(false);
						}
					}
					else
					{
						String str = t.getStr();
						int strt=Integer.parseInt(str);
						//System.out.println("hashtable put:"+strt);

						ht.put(t.getpath(),strt);
						
						ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(
								"test\\filelist.data"));
						o.writeObject(ht);
						o.close();
						jta.append("\nUpdate success!");
						oos1.writeBoolean(true);
						oos1.flush();
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

public class PEKSServer {
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				PEKSSFrame pekss = new PEKSSFrame();
				pekss.setVisible(true);
				pekss.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}.start();
	}
}
