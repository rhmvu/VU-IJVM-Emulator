package pad.ijvm;

public class IJVMStack {
	int stackPointer;
	Word[] word;

	IJVMStack(){
		word = new Word[1000000];
		stackPointer = -1;
	}
	
	void push(int value){
		stackPointer+=1;
		word[stackPointer]= new Word(value);
	}
	
	void push(Word newWord) {
		stackPointer+=1;
		word[stackPointer]= newWord;
	}
	
	Word pop(){
		Word popWord = word[stackPointer];
		stackPointer-=1;
		return popWord;
	}
	void DeleteTOS(){
		word[stackPointer] = null;
		stackPointer-=1;
		return;
	}
	
	Word peek(){
		return word[stackPointer];
	}
	
	int size(){
		return stackPointer+1;
	}
	
	int[] toIntArray(){
		int[] result = new int[stackPointer];
		for(int i = 0; i<stackPointer; i++){
			result[i]=word[i].giveInt();
		}
		return result;
	}	
}