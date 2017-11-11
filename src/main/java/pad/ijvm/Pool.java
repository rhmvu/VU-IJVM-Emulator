package pad.ijvm;

public class Pool {
	int amountOfConstants;
	Word[] constant;
	
	Pool(int amountOfConstants, Word[] Constants){
		this.amountOfConstants = amountOfConstants;
		this.constant = Constants;
	}
	
	Word getConstant(int index){
		return constant[index];
	}
}
