package pad.ijvm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import pad.ijvm.interfaces.IJVMInterface;

public class IJVM implements IJVMInterface{
	Pool constants;
	Text text;
	boolean running;
	IJVMStack stack;
	private PrintStream out;
	InputStream in;
	Frame[] frame;
	int current;
	
	IJVM(Pool constants, Text text)  {
		this.text = text;
		text.currentBytePosition = 0;
		this.constants = constants;
		running = true;
		frame = new Frame[1000000];
		current = 0;
		stack = new IJVMStack();
		setOutput(System.out);
		setInput(System.in);
		frame[current]= new Frame(text.currentBytePosition);
	}

	public void run(){
		while(text.hasByte()&&running){
			step();
		}
	}

	public void step() {
		byte instruction = getInstruction();

		switch(instruction){
		case (byte) 0xFD: System.out.println("OUT");
		int tos = frame[current].stack.pop().giveInt();
		out.printf("%c",(char) tos);
		break;
		case (byte) 0xFF: System.out.println("HALT");
		running = false;
		break;
		case 0x10: System.out.println("BIPUSH");
		Word newWord = new Word((int)text.fetchByte()); 
		frame[current].stack.push(newWord);
		break;
		case 0x59:System.out.println("DUP");
		Word topOfStack = frame[current].stack.peek();
		frame[current].stack.push(topOfStack);
		break;
		case (byte) 0xA7: System.out.println("GOTO");
		branch();
		break;
		case 0x60: System.out.println("IADD");
		frame[current].stack.push(frame[current].stack.pop().add(frame[current].stack.pop()));
		break;
		case 0x7E: System.out.println("IAND"); 
		Word ANDResult = new Word(frame[current].stack.pop().giveInt() & frame[current].stack.pop().giveInt());
		frame[current].stack.push(ANDResult); 
		break;
		case (byte) 0x99: System.out.println("IFEQ");
		ifeq();
		break;
		case (byte) 0x9B: System.out.println("IFLT");
		iflt();
		break;
		case (byte) 0x9F: System.out.println("IF_ICMPEQ");
		if_icmpeq();
		break;
		case (byte) 0x84: System.out.println("IINC");
		iinc();
		break;
		case 0x36: System.out.println("ISTORE");
		Word newLV = frame[current].stack.pop();
		frame[current].storeVariable(text.fetchByte(), newLV);
		break;
		case 0x15: System.out.println("ILOAD");
		Word var = frame[current].getVariable(text.fetchByte());
		frame[current].stack.push(var);
		break;
		case (byte) 0xB6: System.out.println("INVOKEVIRTUAL");
		invoke();
		break;
		case (byte) 0xB0: System.out.println("IOR");
		Word ORResult = new Word(frame[current].stack.pop().giveInt() | frame[current].stack.pop().giveInt());
		frame[current].stack.push(ORResult); 
		break;
		case (byte) 0xAC: System.out.println("IRETURN");
		ireturn();
		break;
		case 0x64: System.out.println("ISUB");
		frame[current].stack.push(frame[current].stack.pop().subtract(frame[current].stack.pop()));
		break;
		case 0x13: System.out.println("LDC_W");
		ldc_w();
		break;
		case 0x00: System.out.println("NOP");
		break;
		case 0x57: System.out.println("POP");
		frame[current].stack.DeleteTOS();
		break;
		case 0x5F: System.out.println("SWAP");
		swap();
		break;
		case (byte) 0xC4: System.out.println("WIDE");
		wide();
		break;
		case (byte) 0xFC: System.out.println("IN");
		try {
			frame[current].stack.push(in.read());
		} catch (IOException e) {
			frame[current].stack.push(0);
		}
		break;

		default:System.out.printf("Not recogized byte:0x%02X\n", instruction);
		break;
		}
		
		frame[current].programCounter = text.currentBytePosition;
		return;
	}

	void iflt() {
		if(frame[current].stack.pop().giveInt() < 0){
			branch();
		}else{
			text.skipShort();
		}
	}
	
	void iinc() {
		int varIndex = text.fetchByte();
		Word variable = frame[current].getVariable(varIndex);
		Word con = new Word(text.fetchByte());
		frame[current].storeVariable(varIndex, variable.add(con));
	}

	void if_icmpeq() {
		if(frame[current].stack.pop().giveInt() == frame[current].stack.pop().giveInt()){
			branch();
		}else{
			text.skipShort();
		}	
	}

	void ifeq(){
		int integ = frame[current].stack.pop().giveInt();
		if(integ == 0){
			branch();
		}else{
			text.skipShort();
		}
	}
	
	void ireturn() {
		frame[current-1].stack.push(frame[current].stack.pop());
		frame[current]= null;
		current-=1;
		text.currentBytePosition = frame[current].programCounter;
	}

	void ldc_w() {
		Short index = getShort();
		frame[current].stack.push(constants.getConstant(index.toUnsignedInt()));
	}
	void swap(){
		Word firstWord = frame[current].stack.pop();
		Word secondWord = frame[current].stack.pop();
		frame[current].stack.push(firstWord);
		frame[current].stack.push(secondWord);
	}
	
	void invoke(){
		Short index = getShort();
		frame[current].programCounter = text.currentBytePosition;
		current+=1;
		int newPc = constants.getConstant(index.toUnsignedInt()).giveInt();
		frame[current] = new Frame(newPc);			//check for right programcounter
		text.currentBytePosition = frame[current].programCounter;
		Short nrArgs = getShort();
		int numberOfArguments = nrArgs.toUnsignedInt();
		for(int i = 0; i<numberOfArguments;i++){
			Word argument = frame[current-1].stack.pop();
			frame[current].stack.push(argument);
			frame[current].variableFrame[numberOfArguments-i-1]=argument;
		}
		text.skipShort();
		frame[current].programCounter = text.currentBytePosition;
	}


	void branch(){
		Short offSet = getShort();
		text.currentBytePosition = text.currentBytePosition -3;		
		text.currentBytePosition = text.currentBytePosition + offSet.toSignedInt();
	}

	void wide(){
		byte instruction = getInstruction();
		Short index = getShort();
		int varIndex = index.toUnsignedInt();

		switch(instruction){
		case (byte) 0x84: //IINC
			Word variable = frame[current].getVariable(varIndex);
		Word con = constants.getConstant(text.fetchByte());
		frame[current].storeVariable(varIndex, variable.add(con));
		break;
		case 0x36: //ISTORE
			Word newLV = frame[current].stack.pop();
			frame[current].storeVariable(varIndex, newLV);
		case 0x15: //ILOAD
			Word var = frame[current].getVariable(varIndex);
			frame[current].stack.push(var);
			break;
		}
	}
	
	Short getShort(){
		byte[] shortBytes = new byte[2];
		shortBytes[0] = text.fetchByte();
		shortBytes[1] = text.fetchByte();
		return new Short(shortBytes);
	}
	public int topOfStack() {
		return frame[current].stack.peek().giveInt();
	}

	public int[] getStackContents() {
		return frame[current].stack.toIntArray();
	}

	public byte[] getText() {
		return text.textBytes;
	}

	public int getProgramCounter() {
		return text.currentBytePosition;
	}

	public int getLocalVariable(int i) {
		return frame[current].getVariable(i).giveInt();
	}

	public int getConstant(int i) {
		return constants.getConstant(i).giveInt();
	}
	
	public byte getInstruction() {
		return text.fetchByte();
	}

	public void setOutput(PrintStream output) {
		this.out = output;
	}

	public void setInput(InputStream in) {
		this.in = in;
	}
}
