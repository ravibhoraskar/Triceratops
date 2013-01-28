package byteinstrumentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.Opcodes;

public class Triceratops {
    
    // Package private
    static class Function {
        public String owner;
        public String name;
        public String desc;
        public boolean instancefn; // invoke-direct vs invoke-static; does not yet support other invocations (abstract, interface, etc.)
        
        public Function(String owner, boolean instancefn, String name, String desc) {
            this.owner = owner;
            this.name = name;
            this.desc = desc;
            this.instancefn = instancefn;
        }
    }
    
    // Package private
    static class Precondition {
        public List<Function> restrictedFunctions;
        public List<Function> validateFunctions;
        public String name;
        
        public Precondition(List<Function> restrictedFunctions, List<Function> validateFunctions, String name) {
            this.restrictedFunctions = restrictedFunctions;
            this.validateFunctions = validateFunctions;
            this.name = name;
        }
        
        public Precondition(List<Function> restrictedFunctions, List<Function> validateFunctions) {
            this(restrictedFunctions, validateFunctions, "var" + (new Random()).nextInt());
        }
    }
    
    public static void instrumentDex(File inFile, File outFile, List<Precondition> conditions) throws IOException {
        int api = Opcodes.ASM4;
        FileOutputStream os;
        
        ApplicationReader ar = new ApplicationReader(api, inFile);
        ApplicationWriter aw = new ApplicationWriter(ar);
        ApplicationVisitor aa = new TriceratopsApplicationAdapter(api, aw, conditions);
        ar.accept(aa,  0);
        
        byte[] b = aw.toByteArray();
        os = new FileOutputStream(outFile);
        os.write(b);
        os.close();
    }
}
