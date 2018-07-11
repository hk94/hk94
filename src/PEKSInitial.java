

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class PEKSInitial {
	private BigInteger primeP;
	private BigInteger primeQ;
	private BigInteger primeM;
	private BigInteger generator;
	private BigInteger fainM;
	PEKSInitial() {
		primeP = BigInteger.probablePrime(512, new Random());
		primeQ = BigInteger.probablePrime(512, new Random());
		generator = new BigInteger(512, new Random());
		primeM = primeP.multiply(primeQ);
		fainM = (primeP.subtract(BigInteger.ONE)).multiply(primeQ
				.subtract(BigInteger.ONE));
	}	
	
	public BigInteger getGenerator(){
		return this.generator;
	}
	
	public BigInteger getModules(){
		return this.primeM;
	}

	public BigInteger getEulerFunction(){
		return this.fainM;
	}
	public static void main(String[] args){
		//PEKSInitial initial=new PEKSInitial();
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("G:\\test\\server.data"));
			oos.writeObject(publicKey);
			oos.close();
			ObjectOutputStream oos2=new ObjectOutputStream(new FileOutputStream("G:\\test\\client.data"));
			oos2.writeObject(publicKey);
			oos2.writeObject(privateKey);
			oos2.close();
			ObjectOutputStream oos3=new ObjectOutputStream(new FileOutputStream("G:\\test\\client1.data"));
			oos3.writeObject(privateKey);
			oos3.close();
			/*SecretKey key=KeyGenerator.getInstance("DES").generateKey();
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("G:\\test\\initial.data"));
			oos.writeObject(key);
			oos.writeObject(initial.getModules());
			oos.writeObject(initial.getGenerator());
			oos.writeObject(initial.getEulerFunction());
			ObjectOutputStream oos2=new ObjectOutputStream(new FileOutputStream("G:\\test\\server0.data"));
			oos2.writeObject(key);
			oos2.writeObject(initial.getModules());
			oos2.writeObject(initial.getGenerator());
			oos2.close();
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream("G:\\test\\initial.data"));
			for(int i=0;i<4;i++){
				System.out.println(ois.readObject().toString());
			}
			*/
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException eio){
			eio.printStackTrace();
		}
	}
}
