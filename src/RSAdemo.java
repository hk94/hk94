
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  

import javax.crypto.Cipher;

public class RSAdemo {  
  
	protected byte[] encrypt(RSAPublicKey publicKey, byte[] obj) {
		if (publicKey != null) {
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				return cipher.doFinal(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/** */
	/**
	 * * Basic decrypt method *
	 * 
	 * @return byte[]
	 */
	protected byte[] decrypt(RSAPrivateKey privateKey, byte[] obj) {
		if (privateKey != null) {
			try {
				//System.out.println("de pvk:"+privateKey.toString());
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				return cipher.doFinal(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


public void encryptFile(RSAPublicKey publicKey, File file, File newFile) {
	try {
		//System.out.println("enF puk:"+publicKey.toString());
		InputStream is = new FileInputStream(file);
		OutputStream os = new FileOutputStream(newFile);
		byte[] bytes = new byte[117];
		while (is.read(bytes) > 0) {

			//System.out.println(new String(bytes));
			byte[] e = encrypt(publicKey, bytes);
			bytes = new byte[117];
			os.write(e, 0, e.length);
		}
		os.close();
		is.close();
		System.out.println("write success");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public void decryptFile(RSAPrivateKey privateKey, File file, File newFile) {
	try {

		//System.out.println("deF pvk:"+privateKey.toString());
		InputStream is = new FileInputStream(file);
		OutputStream os = new FileOutputStream(newFile);
		byte[] bytes1 = new byte[128];
		while (is.read(bytes1) > 0) {
			//System.out.println("decry:"+new String(bytes1));
			byte[] de = decrypt(privateKey, bytes1);
			//System.out.println("success decry:"+new String(de));
			bytes1 = new byte[128];
			os.write(de, 0, de.length);
		}
		os.close();
		is.close();
		System.out.println("write success");
	} catch (Exception e) {
		e.printStackTrace();
	}
}

protected byte[] encbypri(RSAPrivateKey privateKey, byte[] obj) {
	if (privateKey != null) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return cipher.doFinal(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return null;
}

protected int sign2cypher(RSAPublicKey publicKey, byte[] obj) {
	if (publicKey != null) {
		try {
			Cipher cipher1 = Cipher.getInstance("RSA");
			cipher1.init(Cipher.DECRYPT_MODE, publicKey);
			Cipher cipher2 = Cipher.getInstance("RSA");
			cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] temp=cipher1.doFinal(obj);
			String temps=new String(temp);
			System.out.println("keyword is:"+temps+" length:"+temps.length());
			byte[] strs=encrypt(publicKey, temps.getBytes());
			return strs.hashCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return 0;
}


}  

