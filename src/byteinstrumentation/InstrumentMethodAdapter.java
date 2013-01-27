package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class InstrumentMethodAdapter extends MethodVisitor implements Opcodes {
    private int numParams;

    public InstrumentMethodAdapter(int api, MethodVisitor mv) {
        super(api, mv);
    }
    
    @Override
    public void visitParameters(String[] parameters) {
        // TODO: numParams should be loaded from the method descriptor just to be safe, 
        // since visitParameters is technically debug information
        numParams = parameters.length;
        mv.visitParameters(parameters);
        return;
    }
    
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        // NOTE: maxLocals is ignored in asmdex
        mv.visitMaxs(maxStack, maxLocals);
        
        mv.visitVarInsn(INSN_CONST_4, 0, 1);
        mv.visitFieldInsn(INSN_SPUT_BOOLEAN, "Lsparta/triceratops/TriceratopsApplication;", "clickValidate", "Z", 0, 0);
        return;
    }

}
