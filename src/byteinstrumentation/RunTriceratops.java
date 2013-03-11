package byteinstrumentation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RunTriceratops {
    public static void main(String args[]) throws IOException {
        // TODO: Validate these
        FileReader policyFile = new FileReader(args[0]);
        File inFile = new File(args[1]);
        File outFile = new File(args[2]);
        
        TriceratopsPolicy tripolicy = TriceratopsPolicy.readJson(policyFile);
        
        Triceratops.instrumentDex(inFile, outFile, tripolicy);
        System.out.println("Done!");
    }
}
