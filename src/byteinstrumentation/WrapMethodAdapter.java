package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class WrapMethodAdapter extends MethodVisitor implements Opcodes {
    public WrapMethodAdapter(int api, MethodVisitor mv) {
        super(api, mv);
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, int[] arguments) {
        if (owner.equals("Landroid/util/Log;") && name.equals("i") && desc.equals("ILjava/lang/String;Ljava/lang/String;")) {
            mv.visitMethodInsn(INSN_INVOKE_STATIC, "Lsparta/triceratops/TriceratopsApplication;", name, desc, arguments);
        }
        else
            mv.visitMethodInsn(opcode, owner, name, desc, arguments);
        return;
    }

}
