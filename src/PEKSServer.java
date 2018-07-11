
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
	private BigInteger modules, generator;// the public parameters modules
											// received from the client;
	private ObjectInputStream ois;
	private ServerSocket ss;
	private Socket servs;
	//private Vector<String> keyVector1,keyVector2;// an vector to stand for the keywords
											// stored
	// on the server
	private MessageDigest mds;
	private Hashtable<String, String> ht;
	SecretKey key;
	JPanel southPanel;
	JButton openButton;
	//BBGHIBE bbgHIBE;
	//BBGHIBEMasterKey msk;
	//String[] IDV;

	RSAPublicKey publicKey;

	public PEKSSFrame() {// constructor
		/*try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		

        //bbgHIBE = new BBGHIBE();  
        //msk = bbgHIBE.Setup("a.properties");  
        //IDV = new String[1];  
        //IDV[0]="hk94";

        //BBGHIBESecretKey SecretKey = bbgHIBE.KeyGen(msk, IDV);   
		setTitle("PEKS服务器");
		jfc = new JFileChooser("G:\\test\\Data Records");
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
		//keyVector1 = new Vector<String>();// initial the keyVector
		//keyVector2 = new Vector<String>();
		//keyVector3 = new Vector<Element>();
		ht = new Hashtable<String, String>();// initial the hashtable
		//jfc = new JFileChooser("Data Records");
		southPanel = new JPanel();
		openButton = new JButton("打开");
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
					"G:\\test\\server.data"));
			publicKey=(RSAPublicKey) ois.readObject();
			System.out.println("publickey is :"+publicKey.toString());
			//key = (SecretKey) ois.readObject();
			//modules = (BigInteger) ois.readObject();
			//generator = (BigInteger) ois.readObject();
			//keyVector=(Vector<BigInteger>) ois.readObject();
			//ht=(Hashtable<BigInteger, String>) ois.readObject();
			ois.close();
			
			/*ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(
					"G:\\test\\filelist.data"));
			ht=(Hashtable) ois2.readObject();

			ois2.close();*/
			mds = MessageDigest.getInstance("SHA");// initial the
													// messagedigester
			System.out.println("初始化成功！文件夹路径：G:\\test\\\n");
			jta.append("初始化成功！文件夹路径：G:\\test\\\n");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	/*public void getValues(String pathName) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				"F:\\test\\filelist.data"));
		//key = (SecretKey) ois.readObject();
		//modules = (BigInteger) ois.readObject();
		//generator = (BigInteger) ois.readObject();
		for (File tempFile : new File(pathName).listFiles()) {
			encrypter = new DesEncrypter(key);
			String tempString = tempFile.getName();

			System.out.println(tempString);
			int dot = tempString.lastIndexOf(".");
			String tempString1 = tempString.substring(0, dot);
			System.out.println(tempString1);
			/*mds = MessageDigest.getInstance("SHA");
			mds.update(tempString1.getBytes());
			BigInteger tempBigInteger1 = new BigInteger(mds.digest()).abs()
					.nextProbablePrime();
			mds.reset();
			BigInteger tempBigInteger2 = generator.modPow(tempBigInteger1,
					modules); 
					
			//BBGHIBECiphertext ciphertext = bbgHIBE.Encrypt(IDV,tempString1);  
			//keyVector1.add(ciphertext.C_0);
			//keyVector2.add(ciphertext.C_1);
			//keyVector3.add(ciphertext.C_2);
			//BigDecimal big=new BigDecimal();
			File outputFile = new File("G:\\test\\Data Records\\"
					+ System.currentTimeMillis()*Math.random() + ".txt");
			try {
				outputFile.createNewFile();//如果目录下没有myfile.txt这个文件则新建一个。
				 } catch (IOException e) {		
				  // TODO Auto-generated catch block
				  e.printStackTrace();}
			encrypter.encrypt(new FileInputStream(tempFile),
					new FileOutputStream(outputFile));
			ht.put( outputFile.getAbsolutePath(),ciphertext.C_0.toString());
		}

		ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(
				"F:\\test\\server.data"));
		o.writeObject(key);
		o.writeObject(modules);
		o.writeObject(generator);
		o.writeObject(keyVector1);
		o.writeObject(keyVector2);
		o.writeObject(keyVector3);
		o.writeObject(ht);
		o.close();
	}

	public void genKeyVector() {// generate the vector list of the keywords of
								// awd1990

		try {
			getValues("F:\\test\\123");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jmi1) {
			
			//genKeyVector();
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
						+ "连接成功！");
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
				//jpb.setBorder(BorderFactory.createTitledBorder("当前目录搜索完成度"));
				jpb.setValue(0);
				jpb.setStringPainted(true);
				jdg.setTitle("搜索");
				jdg.add(jpb,BorderLayout.NORTH);
				jpb.setMinimum(0);
				jpb.setMaximum(256);//
				showArea = new JTextArea(10, 10);
				showArea.setLineWrap(true);
				showArea.setBorder(BorderFactory.createTitledBorder("当前搜索文件"));
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
						String temps=t.getStr();
						//BBGHIBECiphertext ciphertextt = bbgHIBE.Encrypt(IDV,temps);
						//Element tempe=ciphertextt.C_0;
						jta.append("\ncomplete reading the trapdoor!\nStarting to seach!");
						i = 0;
						jpb.setValue(i);
						jdg.setVisible(true);
						System.out.println("s1");
						for (String tempk : ht.keySet()) {
							//System.out.println(tempb.modPow(temp1, modules)+"??="+temp2+"\n");
							//System.out.println("s2");
							String tempb = ht.get(tempk);
							if (temps.equals(tempb)) {
								/*Iterator<Entry<String, String>> it2 = ht.entrySet().iterator();
								Entry<String, String> entry;
						        while(it2.hasNext()){
						        	entry=it2.next();

									//System.out.println(entry.getValue()+"?="+tempb.toString()+"\n");
						        	//System.out.println("now is :"+entry.getKey());
						        	if (entry.getValue().equals(tempb.toString())){
*/
										oos1.writeInt(1);
						        		fileName = tempk;
										tempFile = new File(fileName);
										oos1.writeObject(fileName);

										//System.out.println(fileName);
										oos1.flush();
										jpb.setValue(jpb.getMaximum());
										showArea.setText(tempb.toString()
												+ "match success!");
										//Thread.sleep(1000);
										jdg.setVisible(false);	
										i++;
										jpb.setValue(i);
										showArea.setText("当前匹配:"+tempb.toString());
						        	
						        	   
						        
								//break;
								
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
							//Thread.sleep(2000);
							System.out.println("s6");
							jdg.setVisible(false);
						}
					}
					else
					{
						String str = t.getStr();
						System.out.println(str);

						//keyVector1.add(str);
						//keyVector2.add(t.getpath());
						ht.put(t.getpath(),str);
						
						//mds = MessageDigest.getInstance("SHA");
						

				        //BBGHIBECiphertext ciphertext = bbgHIBE.Encrypt(IDV,str1);  
						
						//ciphertext.mds.update(str1.getBytes());
						//BigInteger tempBigInteger1 = new BigInteger(mds.digest()).abs().nextProbablePrime();
						//mds.reset();
						//BigInteger tempBigInteger2 = generator.modPow(tempBigInteger1,modules);
						//keyVector3.add(ciphertext.C_2);
						//BigDecimal big=new BigDecimal();
						
						ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(
								"G:\\test\\filelist.data"));
						o.writeObject(ht);
						//o.writeObject(keyVector2);
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

// 实例化服务器
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
