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
        for (String s : parameters) {
            System.out.println("    " + s);
        }
        // TODO: numParams should be loaded from the method descriptor just to be safe, 
        // since visitParameters is technically debug information
        numParams = parameters.length;
        mv.visitParameters(parameters);
        return;
    }
    
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        // NOTE: maxLocals is ignored in asmdex
        System.out.println("    " + maxStack);
        mv.visitMaxs(maxStack, maxLocals);
        
        // 'this' is the first parameter, so it is one before the first formal parameter
        int thisReg = maxStack - (numParams + 1);
        
        mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getApplication", "Landroid/app/Application;", new int[] { thisReg });
        mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
        mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "Lsparta/triceratops/TriceratopsApplication;");
        mv.visitVarInsn(INSN_CONST_4, 1, 1);
        mv.visitFieldInsn(INSN_IPUT_BOOLEAN, "Lsparta/triceratops/TriceratopsApplication;", "clickValidate", "Z", 1, 0);
        return;
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, int[] arguments) {
        // These are instructions that call a method
        // "name" is the method to be called
        // TODO: Validate the method and potentially block the call
        mv.visitMethodInsn(opcode, owner, name, desc, arguments);
        return;
    }

}
