package pad.ijvm;

public class Text {
	int amountOfTextBytes;
	int currentBytePosition;
	byte[] textBytes;
	
	Text(int amountOfTextBytes, byte[] textBytes) {
		this.textBytes= textBytes;
		this.amountOfTextBytes = amountOfTextBytes;
		currentBytePosition = 0;
	}
	
	byte fetchByte(){
		currentBytePosition+=1;
		return textBytes[currentBytePosition-1]; //because we just incremented the counter
	}
	
	boolean hasByte(){
		if(currentBytePosition >= textBytes.length || currentBytePosition < 0){
			return false;
		}
		return true;
	}
	
	void skipShort(){
		currentBytePosition +=2;
	}
}
