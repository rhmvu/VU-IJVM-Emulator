package pad.ijvm;

import java.nio.ByteBuffer;

public class Word {
	byte[] bytes = new byte[4];	

	Word(byte [] byteArray){
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(bytesToInt(byteArray));
		bytes = b.array();
	}

	Word (int value){
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(value);
		bytes = b.array();
	}
	
	int giveInt() {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	int bytesToInt(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	Word add(Word newWord){
		int sum = bytesToInt(bytes)+bytesToInt(newWord.bytes);
		Word sumWord = intToWord(sum);
		return sumWord;
	}
	
	Word subtract(Word newWord){
		int subtraction = bytesToInt(newWord.bytes)-bytesToInt(bytes);
		Word totalWord = intToWord(subtraction);
		return totalWord;
	}
	
	Word intToWord(int number){
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(number);
		byte[] result = b.array();
		Word resultWord = new Word(result);
		return resultWord;
	}
}

