package byteinstrumentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.Opcodes;

public class Triceratops {
    
    public static void instrumentDex(File inFile, File outFile, TriceratopsPolicy tripolicy) throws IOException {
        int api = Opcodes.ASM4;
        FileOutputStream os;
        
        ApplicationReader ar = new ApplicationReader(api, inFile);
        
        ApplicationVisitor discover = new PassOneApplicationAdapter(api, tripolicy);
        ar.accept(discover, 0);
        
        ApplicationWriter aw = new ApplicationWriter(ar);
        ApplicationVisitor aa = new TriceratopsApplicationAdapter(api, aw, tripolicy);
        ar.accept(aa,  0);
        
        byte[] b = aw.toByteArray();
        os = new FileOutputStream(outFile);
        os.write(b);
        os.close();
    }
}
