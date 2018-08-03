import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class PEKSInitial {
	public static void main(String[] args){
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			KeyPair skeyPair = keyPairGen.generateKeyPair();
			RSAPrivateKey sprivateKey = (RSAPrivateKey) skeyPair.getPrivate();
			RSAPublicKey spublicKey = (RSAPublicKey) skeyPair.getPublic();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test/server/server.data"));
			oos.writeObject(publicKey);
			oos.close();
			ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("test/client/client.data"));
			oos2.writeObject(publicKey);
			oos2.writeObject(privateKey);
			oos2.writeObject(spublicKey);
			oos2.writeObject(sprivateKey);
			oos2.close();
			ObjectOutputStream oos3 = new ObjectOutputStream(new FileOutputStream("test/client/client1.data"));
			oos3.writeObject(privateKey);
			oos3.close();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException eio){
			eio.printStackTrace();
		}
	}
}
