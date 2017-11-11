package pad.ijvm;

import java.nio.ByteBuffer;

public class Short {
	byte[] bytes = new byte[2];

	Short(byte[] shortBytes){
		this.bytes = shortBytes;
	}
	
	Short(int value){
		ByteBuffer b = ByteBuffer.allocate(2);
		b.putInt(value);
		bytes = b.array();
	}

	int toSignedInt() {
		return (bytes[0] << 8) | (bytes[1] & 0xFF);
	}
	
	int toUnsignedInt(){
		return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
	}
}
