
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class PEKSCFrame extends JFrame implements ActionListener {
	JTextArea jta;
	JScrollPane jsp;
	JPanel eastPanel;
	
	JTextArea jta1;
	JScrollPane jsp1;
	JButton connect;
	JButton plainText;
	JButton updateFile;
	JButton cipherText;
	JPanel buttonPanel;
	JPanel buttonPanel1;
	BigInteger modules, eulermodules, generator;// system parameters
	ObjectInputStream ois;
	ObjectOutputStream oos;
	SecretKey key;
	Socket clientS;
	InputStream is;
	InetSocketAddress inetsa;
	String keyword;// keyword for search
	String filePath;
	MessageDigest mdc;// hash function ( SHA1)
	boolean tempB;
	int tempC;
	JPanel centerPanel;
	JPanel displayPanel;
	JPanel updatePanel;
	JPanel searchPanel;
	JPanel resultPanel;
	JTabbedPane jtp;
	JFileChooser result;
	JFileChooser update;
	JButton search;
	JTextField keywordField;
	boolean connected = true;
	RSAPublicKey publicKey;
	RSAPrivateKey privateKey;

	RSAdemo rsa = new RSAdemo();

	PEKSCFrame() {// constructor, initial the graphic user interface
		setTitle("PEKSClient");
		/*try {

			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
			SwingUtilities.updateComponentTreeUI(rootPane);
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

		jta = new JTextArea(10, 10);
		jsp = new JScrollPane(jta1);
		jta.setLineWrap(true);
		jta.setEditable(true);
		jta.append("success!");

		jta1 = new JTextArea(10, 10);
		jsp1 = new JScrollPane(jta1);
		jta1.setLineWrap(true);
		displayPanel = new JPanel();
		displayPanel.setLayout(new BorderLayout());
		displayPanel.add(jsp1, BorderLayout.CENTER);
		connect = new JButton("连接");
		displayPanel.add(connect, BorderLayout.SOUTH);
		connect.addActionListener(this);

		updatePanel = new JPanel(new BorderLayout());
		update = new JFileChooser("G:\\test");
		update.setControlButtonsAreShown(false);
		updatePanel.add(update, BorderLayout.CENTER);
		buttonPanel1 = new JPanel();
		updateFile = new JButton("上传");
		buttonPanel1.add(updateFile);
		updateFile.addActionListener(this);
		updatePanel.add(buttonPanel1, BorderLayout.SOUTH);

		searchPanel = new JPanel();
		keywordField = new JTextField(20);
		searchPanel.add(keywordField);
		search = new JButton("搜索");
		searchPanel.add(search);
		search.addActionListener(this);

		resultPanel = new JPanel(new BorderLayout());
		result = new JFileChooser("G:\\test\\Search Result");
		result.setControlButtonsAreShown(false);
		resultPanel.add(result, BorderLayout.CENTER);
		buttonPanel = new JPanel();
		cipherText = new JButton("直接打开");
		plainText = new JButton("解密后打开");
		buttonPanel.add(cipherText);
		cipherText.addActionListener(this);
		buttonPanel.add(plainText);
		plainText.addActionListener(this);
		resultPanel.add(buttonPanel, BorderLayout.SOUTH);

		jtp = new JTabbedPane();
		jtp.add("客户端信息", displayPanel);
		jtp.add("上传文件", updatePanel);
		jtp.add("搜索文件", searchPanel);
		jtp.add("结果查看", resultPanel);

		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(jtp, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
		inetsa = new InetSocketAddress("127.0.0.1", 8901);
		

        
        
		setLocation(300, 10);
		pack();
		setResizable(false);

		tempB = false;
		loadParameters();
		displayInitialInformation();
	}

	public void loadParameters() {
		try {
			/*KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(512);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();*/
			ois = new ObjectInputStream(new FileInputStream("G:\\test\\client.data"));
			publicKey=(RSAPublicKey) ois.readObject();
			privateKey=(RSAPrivateKey) ois.readObject();
			
			ois.close();
			/*System.out.println("publickey is :"+publicKey.toString());
			System.out.println("privatekey is :"+privateKey.toString());


			String test="make a test";
			System.out.println(new String(test.getBytes()));
			
			byte[] test2=rsa.encrypt(publicKey, test.getBytes());
			System.out.println(new String(test2));
			byte[] test3=rsa.decrypt(privateKey, test2);
			System.out.println(new String(test3));*/
			
			
			
			/*key = (SecretKey) ois.readObject();
			modules = (BigInteger) ois.readObject();
			generator = (BigInteger) ois.readObject();// read in the system//
														// parameters
			eulermodules = (BigInteger) ois.readObject();
			*/
			mdc = MessageDigest.getInstance("SHA");

			//System.out.println("c0");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == connect) {// response of initial
	
				clientS = new Socket();
				try {
					clientS.connect(inetsa, 5000);

					oos = new ObjectOutputStream(clientS.getOutputStream());
					ois = new ObjectInputStream(clientS.getInputStream());

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				connect.setEnabled(false);
			
		}
		if (e.getSource() == search) {// generate token for search

			if (keywordField.getText().length() == 0) {
				JOptionPane
						.showMessageDialog(null,
								"Sorry, you have not input a valid keyword! Please try again!");
			}

			else {
				keyword = keywordField.getText().trim();
				String str=""+keyword.hashCode();
				//BigInteger temp1 = new BigInteger(mdc.digest()).abs().nextProbablePrime();// h(w)
				/*
				BigInteger temp2 = temp1.modInverse(eulermodules);// generate
																	// -h(w)
				BigInteger temp3 = new BigInteger(512, new Random());// generate
																		// r
				BigInteger temp4 = temp2.multiply(temp3);// -r*h(w)
				BigInteger temp5 = generator.modPow(temp3, modules);// generate
																	// g^r
				*/					
																	 
				Trapdoor t = new Trapdoor(1,str,null);

				try {
					oos.writeObject(t);
					oos.flush();

					//System.out.println("c1");
					tempC = ois.readInt();
					String fileName;
					File tempFile;
					FileInputStream fis;
					FileOutputStream fos;
					while (tempC==1) {

						//System.out.println("c2");
						fileName = (String) ois.readObject();
						tempFile = new File(fileName);
						//System.out.println("c2="+fileName);
						fis = new FileInputStream(tempFile);
						fos = new FileOutputStream(
								"G:\\test\\search result\\"
										+ tempFile.getName());
						System.out.println("file is :"+tempFile.getName());
						int i = 0;
						while ((i = fis.read()) != -1) {
							fos.write(i);
						}
						fis.close();
						fos.close();
						tempC = ois.readInt();
					}

					//System.out.println("c3");
					if  (tempC==-1){
						JOptionPane.showMessageDialog(jtp,
								"没有匹配的密文！");
					}
					else
					{
						JOptionPane
								.showMessageDialog(jtp, "搜索完成！");
					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
		if (e.getSource() == cipherText) {// open file button clicked
			if (result.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(jtp,
						"No Files selected, Please try again!");
			} else {
				String[] command = { "notepad",
						result.getSelectedFile().getAbsolutePath() };
				try {
					Runtime.getRuntime().exec(command);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		if (e.getSource() == plainText) {
			if (result.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(jtp,
						"No Files selected, Please try again!");
			} else {
				try {
					filePath = result.getSelectedFile().getAbsolutePath();
					File tempFile = new File(filePath);
					System.out.println("try to decrypt:"+filePath);
					File outputFile = new File("G:\\test\\Data Records\\temp");

					outputFile.delete();
					outputFile.createNewFile();

					rsa.decryptFile(privateKey, tempFile, outputFile);
					/*encrypter = new DesEncrypter(key);
					encrypter.decrypt(
							new FileInputStream(result.getSelectedFile()),
							new FileOutputStream("decrypt"
									+ result.getSelectedFile().getName()));*/
					
					String[] command = { "notepad","G:\\test\\Data Records\\temp"};
					Runtime.getRuntime().exec(command);
					result.rescanCurrentDirectory();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		if (e.getSource() == updateFile) {
			if (update.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(jtp,
						"No Files selected, Please try again!");
			} 
			else {
					filePath = update.getSelectedFile().getAbsolutePath();
					int rand=(int)((100000000+Math.random()*899999999)+(100000000+Math.random()*899999999)+(100000000+Math.random()*899999999));
					String strp="file"+rand;
					

					File tempFile = new File(filePath);
					File outputFile = new File("G:\\test\\Data Records\\"+strp);
					
					int dot = filePath.lastIndexOf(".");
					String str1 = filePath.substring(0, dot);
					dot = str1.lastIndexOf("\\");
					str1 = str1.substring(dot+1, str1.length());
					String strs=""+str1.hashCode();
					System.out.println(str1);
					try {
						outputFile.createNewFile();//如果目录下没有myfile.txt这个文件则新建一个。

						rsa.encryptFile(publicKey, tempFile, outputFile);
						 } catch (IOException e1) {
						  // TODO Auto-generated catch block
						  e1.printStackTrace();}
					
					
					
					Trapdoor t = new Trapdoor(2,strs,"G:\\test\\Data Records\\"+strp);
					try 
					{
						oos.writeObject(t);
						oos.flush();
						tempB = ois.readBoolean();
						if (tempB) {
							JOptionPane
									.showMessageDialog(jtp, "上传成功");
						} 
						else 
						{
							JOptionPane.showMessageDialog(jtp,
									"Update failed!");
						}

					}
					catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		}
	}

	private void displayInitialInformation() {// display the encryption key and
		// generator,modules,eulermodules;
		jta1.append("文件夹位置：F\\test\\Client\\\n\n");
		jta1.append("连接端口号：8901\n\n");
		jta1.append("初始化完成！点击连接按钮后可以开始正常使用。");
	}


}

public class PEKSClient {
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				PEKSCFrame peksc = new PEKSCFrame();
				peksc.setVisible(true);
				peksc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}.start();
	}
}