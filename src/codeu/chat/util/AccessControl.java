package codeu.chat.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AccessControl {

	public static final Serializer<AccessControl> SERIALIZER = new Serializer<AccessControl>() {

	    @Override
	    public void write(OutputStream out, AccessControl value) throws IOException {
	    	byte[] acesssLevels = new byte[1]; //create an array of bytes that show levels/statuses
	    	acesssLevels[0] = value.getStatus(); //return byte and set it to the array
	    	Serializers.BYTES.write(out, acesssLevels);
	    }

	    @Override
	    public AccessControl read(InputStream in) throws IOException {
	    	byte[] acesssLevels = Serializers.BYTES.read(in);
	    	return new AccessControl(acesssLevels[0]);
	    }
  };

	private byte level;
	//creators: 00000xxx
	//owners: 000000xx
	//members: 0000000x

	public String toString() {
		String bString = "00000000"; //byte String with intial value
		if (hasCreatorAccess()) {
			bString = bString.substring(0,5) + "1" + bString.substring(6,8); 
		} //substring based on number of places each 'level' has
		if (hasOwnerAccess()) {
			bString = bString.substring(0,6) + "1" + bString.substring(7,8);
		}
		if (hasMemberAccess()) {
			bString = bString.substring(0,7) + "1" + bString.substring(8,8);
		}
		return bString;
	}

	public AccessControl() { //constructor with default value
		level = (byte)0b00000000;
	}

	//various setters and getters for each type 

	public AccessControl(byte status) {
		setStatus(status);
	}

	public void setStatus(byte status) {
		level = status;
	}

	public byte getStatus() {
		return level;
	}

	public void setCreatorStatus() {
		setStatus((byte)0b00000111);
	}

	public boolean hasCreatorAccess() {
		return ((level&(byte)0b00000100) > 0);
	}

	public void setOwnerStatus() {
		setStatus((byte)0b00000011);
	}

	public boolean hasOwnerAccess() {
		return ((level&(byte)0b00000010) > 0);
	}

	public void setMemberStatus() {
		setStatus((byte)0b00000001);
	}

	public boolean hasMemberAccess() {
		return ((level&(byte)0b00000001) > 0);
	}

}