package byteinstrumentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.Opcodes;

public class InstrumentCalls {
    public static void main(String args[]) {
        FileOutputStream os = null;
        try {
            int api = Opcodes.ASM4;
            File inFile;
            File outFile;
            
            // TODO: Validate these
            inFile = new File(args[0]);
            outFile = new File(args[1]);
            
            ApplicationReader ar = new ApplicationReader(api, inFile);
            ApplicationWriter aw = new ApplicationWriter(ar);
            ApplicationVisitor aa = new InstrumentCallsAdapter(api, aw);
            ar.accept(aa, 0);
            byte[] b = aw.toByteArray();
            os = new FileOutputStream(outFile);
            os.write(b);
        } catch (IOException e) {
            // TODO
        } finally {
            // TODO
        }
    }
}
