package pad.ijvm;

import pad.ijvm.interfaces.IJVMInterface;

import java.io.File;
import java.io.IOException;

public class MachineFactory {

    public static IJVMInterface createIJVMInstance(File binaryFile) throws IOException {
    	BinaryLoader binary = new BinaryLoader(binaryFile);
    	if(!binary.correctMagicNumber()){
    		System.out.println("ERROR: Input file does not contain proper magic number");
    		System.exit(1);
    	}
    	IJVM newIJVMInstance= new IJVM(binary.readPool(), binary.readText());
        return newIJVMInstance;
    }

}
