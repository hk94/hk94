import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Random;
import java.nio.ByteBuffer;

import javax.crypto.SecretKey;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;	

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
	RSAPublicKey spublicKey;
	RSAPrivateKey sprivateKey;

	RSAdemo rsa = new RSAdemo();
	Base64.Decoder decoder = Base64.getDecoder();
	Base64.Encoder encoder = Base64.getEncoder();
	
	
	PEKSCFrame() {// constructor, initial the graphic user interface
		setTitle("PEKS Client");
		
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
		connect = new JButton("Connect");
		displayPanel.add(connect, BorderLayout.SOUTH);
		connect.addActionListener(this);

		updatePanel = new JPanel(new BorderLayout());
		update = new JFileChooser("test");
		update.setControlButtonsAreShown(false);
		updatePanel.add(update, BorderLayout.CENTER);
		buttonPanel1 = new JPanel();
		updateFile = new JButton("Upload");
		buttonPanel1.add(updateFile);
		updateFile.addActionListener(this);
		updatePanel.add(buttonPanel1, BorderLayout.SOUTH);

		searchPanel = new JPanel();
		keywordField = new JTextField(20);
		searchPanel.add(keywordField);
		search = new JButton("Search");
		searchPanel.add(search);
		search.addActionListener(this);

		resultPanel = new JPanel(new BorderLayout());
		result = new JFileChooser("test/client/SearchResult");
		result.setControlButtonsAreShown(false);
		resultPanel.add(result, BorderLayout.CENTER);
		buttonPanel = new JPanel();
		cipherText = new JButton("Open");
		plainText = new JButton("Decrypt and Open");
		buttonPanel.add(cipherText);
		cipherText.addActionListener(this);
		buttonPanel.add(plainText);
		plainText.addActionListener(this);
		resultPanel.add(buttonPanel, BorderLayout.SOUTH);

		jtp = new JTabbedPane();
		jtp.add("Server Information", displayPanel);
		jtp.add("Upload Files", updatePanel);
		jtp.add("Search Files", searchPanel);
		jtp.add("Search Result", resultPanel);

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
			ois = new ObjectInputStream(new FileInputStream("test/client/client.data"));
			publicKey=(RSAPublicKey) ois.readObject();
			privateKey=(RSAPrivateKey) ois.readObject();
			spublicKey = (RSAPublicKey) ois.readObject();
			sprivateKey = (RSAPrivateKey) ois.readObject();
			
			ois.close();
			
			mdc = MessageDigest.getInstance("SHA");

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
				JOptionPane.showMessageDialog(null,	"Sorry, you have not input a valid keyword! Please try again!");
			} else {
				keyword = keywordField.getText().trim();
				byte[] strb = rsa.encryptk(spublicKey, keyword.getBytes());
				String str = encoder.encodeToString(strb);
				try {
					strb = rsa.signature(privateKey, (str.hashCode()+"").getBytes());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				String[] strt = new String[1];
				strt[0] = encoder.encodeToString(strb);
																	 
				Trapdoor t = new Trapdoor(1,strt,null);

				try {
					oos.writeObject(t);
					oos.flush();

					tempC = ois.readInt();
					String fileName;
					File tempFile;
					FileInputStream fis;
					FileOutputStream fos;
					while (tempC == 1) {
						fileName = (String) ois.readObject();
						tempFile = new File(fileName);
						fis = new FileInputStream(tempFile);
						fos = new FileOutputStream("test/client/SearchResult/" + tempFile.getName());
						int i = 0;
						while ((i = fis.read()) != -1) {
							fos.write(i);
						}
						fis.close();
						fos.close();
						tempC = ois.readInt();
					}

					if (tempC == -1){
						JOptionPane.showMessageDialog(jtp, "No File Matching");
					}else{
						JOptionPane.showMessageDialog(jtp, "Searching Completed");
					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

		if (e.getSource() == cipherText) {  // open file button clicked
			if (result.getSelectedFile() == null) {
				JOptionPane.showMessageDialog(jtp, "No Files selected, Please try again!");
			} else {
				String[] command = { "open", result.getSelectedFile().getAbsolutePath() };
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
				JOptionPane.showMessageDialog(jtp, "No Files selected, Please try again!");
			} else {
				try {
					filePath = result.getSelectedFile().getAbsolutePath();
					File tempFile = new File(filePath);
					File outputFile = new File("test/server/DataRecords/temp");

					outputFile.delete();
					outputFile.createNewFile();

					rsa.decryptFile(privateKey, tempFile, outputFile);
					
					String[] command = { "open","test/server/DataRecords/temp"};
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
				JOptionPane.showMessageDialog(jtp, "No Files selected, Please try again!");
			} 
			else {
				filePath = update.getSelectedFile().getAbsolutePath();
				int rand = (int)((100000000+Math.random()*899999999)+(100000000+Math.random()*899999999)+(100000000						  + Math.random() * 899999999));
				String strp = "file" + rand;

				File tempFile = new File(filePath);
				File outputFile = new File("test/server/DataRecords/" + strp);	

				int dot = filePath.lastIndexOf(".");
				String str1 = filePath.substring(0, dot);
				dot = str1.lastIndexOf("/");
				str1 = str1.substring(dot+1, str1.length());  // str1 is file name(not including file extension, and directiory informtion)
				String[] strs = str1.split("__");
				String[] enc_strs = new String[strs.length];
				
				for(int j=0;j<strs.length;j++){
					String str2 = strs[j];
					byte[] strb = rsa.encryptk(spublicKey, str2.getBytes());
					String str = encoder.encodeToString(strb);
					enc_strs[j] = str.hashCode()+"";
				}
				try {
					outputFile.createNewFile();
					rsa.encryptFile(spublicKey, tempFile, outputFile);  // Contents of the file is uploaded after encoded
				} catch (IOException e1) {
					  // TODO Auto-generated catch block
					  e1.printStackTrace();
				}

				Trapdoor t = new Trapdoor(2, enc_strs, "test/server/DataRecords/"+strp);
				try {
					oos.writeObject(t);
					oos.flush();
					tempB = ois.readBoolean();
					if (tempB) {
						JOptionPane.showMessageDialog(jtp, "Upload Success!");
					} else {
						JOptionPane.showMessageDialog(jtp, "Update failed!");
					}
				}catch (Exception e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private void displayInitialInformation() {
		jta1.append("Path: test/\n\n");
		jta1.append("Port Number: 8901\n\n");
		jta1.append("Initial success, click the connect button to use.");
		
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