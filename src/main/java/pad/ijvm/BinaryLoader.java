package pad.ijvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BinaryLoader {
	
	static final int IJVMMAGICNUMBER = 501931949;
	static final int CONSTANTVALUEOFFSET = 8;
	static final int WORDINBYTES = 4;
	File binary;
	ByteBuffer bytes;

	BinaryLoader(File binaryData){
		binary = binaryData;
		bytes = ByteBuffer.wrap(dumpBytes());
	}

	byte[] dumpBytes(){
		byte[] bytes = new byte[(int)binary.length()];
		try{
			FileInputStream FileInputStream = new FileInputStream(binary);
			FileInputStream.read(bytes);
			FileInputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return bytes;
	}


	Pool readPool(){
		bytes.limit(bytes.capacity());
		bytes.position(CONSTANTVALUEOFFSET);
		byte[] constantLength = new byte[WORDINBYTES];
		bytes.get(constantLength,0,constantLength.length);
		int amountOfConstants = readInt32(constantLength)/4;
		Word[] constantsArray = new Word[amountOfConstants];
		byte[] constant = new byte[4];
		for(int i = 0; i<amountOfConstants; i++){
			bytes.get(constant, 0,4);
			constantsArray[i]= new Word(constant);
		}
		Pool newPool = new Pool(amountOfConstants, constantsArray);
		return newPool;
	}
	Text readText(){
		bytes.position(bytes.position()+4);//skipping the origin
		byte[] textLength = new byte[WORDINBYTES];
		bytes.get(textLength,0,textLength.length);
		int amountOfTextBytes = readInt32(textLength);
		byte[] textArray = new byte[amountOfTextBytes];
		bytes.get(textArray, 0, amountOfTextBytes);
		Text newText = new Text(amountOfTextBytes, textArray);
		return newText;
	}

	int readInt32(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}

	boolean correctMagicNumber(){
		ByteBuffer magicNumber = bytes;
		magicNumber.position(0);
		magicNumber.limit(4);
		byte[] magicBytes = new byte[magicNumber.remaining()];
		magicNumber.get(magicBytes, 0, magicBytes.length);
		if(readInt32(magicBytes) == IJVMMAGICNUMBER){
			return true;
		}
		return false;
	}
}
