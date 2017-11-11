package pad.ijvm;

public class Frame {
IJVMStack stack;
Word[] variableFrame;
int programCounter;
int amountOfVariables;

	Frame(int pc){
		stack = new IJVMStack();
		variableFrame= new Word[1000000]; 
		programCounter = pc;
		amountOfVariables = 0;
	}
	
	void addVariable(Word newVar){
		variableFrame[amountOfVariables]= newVar;
		amountOfVariables+=1;
	}
	
	Word getVariable(int index){
		return variableFrame[index]; 
	}
	
	void storeVariable(int index, Word newVar){
		variableFrame[index] = newVar;
	}
}
