import java.io.Serializable;
import java.math.BigInteger;

class Trapdoor implements Serializable {
	int type;
	byte[][] str;
	String path;

	public Trapdoor(int type, byte[][] str, String path) {
		this.type = type;
		this.str = str;
		this.path = path;
	}

	public int getType() {
		return type;
	}
	
	public byte[][] getStr() {
		return str;
	}
	public String getpath() {
		return path;
	}
}