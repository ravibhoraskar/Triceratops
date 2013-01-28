package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class WrapMethodAdapter extends MethodVisitor implements Opcodes {
    private Triceratops.Precondition precondition;
    
    public WrapMethodAdapter(int api, MethodVisitor mv, Triceratops.Precondition precondition) {
        super(api, mv);
        this.precondition = precondition;
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, int[] arguments) {
        boolean flag = false;
        for (Triceratops.Function function : precondition.restrictedFunctions) {
            if (function.owner.equals(owner) && function.name.equals(name) && function.desc.equals(desc))
                flag = true;
        }
        if (flag)
            mv.visitMethodInsn(INSN_INVOKE_STATIC, "Lsparta/triceratops/TriceratopsWrappers;", name, desc, arguments);
        else
            mv.visitMethodInsn(opcode, owner, name, desc, arguments);
        
        return;
    }

}
