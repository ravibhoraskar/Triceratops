package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class AddValidationMethodAdapter extends MethodVisitor implements Opcodes {
    private Triceratops.Precondition precondition;

    public AddValidationMethodAdapter(int api, MethodVisitor mv, Triceratops.Precondition precondition) {
        super(api, mv);
        this.precondition = precondition;
    }
    
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        // NOTE: maxLocals parameter is ignored in asmdex
        mv.visitMaxs(maxStack, maxLocals);
        
        mv.visitVarInsn(INSN_CONST_4, 0, 1);
        mv.visitFieldInsn(INSN_SPUT_BOOLEAN, "Lsparta/triceratops/TriceratopsApplication;", precondition.name, "Z", 0, 0);
        return;
    }

}
