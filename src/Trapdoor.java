import java.io.Serializable;
import java.math.BigInteger;

class Trapdoor implements Serializable {
	int type;
	String str;
	String path;

	public Trapdoor(int type,String str, String path) {
		this.type = type;
		this.str = str;
		this.path = path;
	}

	public int getType() {
		return type;
	}
	
	public String getStr() {
		return str;
	}
	public String getpath() {
		return path;
	}
}