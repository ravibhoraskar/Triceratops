package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class WrapMethodAdapter extends MethodVisitor implements Opcodes {
    
    public WrapMethodAdapter(int api, MethodVisitor mv) {
        super(api, mv);
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, int[] arguments) {
        TriceratopsPolicy.Function thisfunction = TriceratopsPolicy.function(owner, name, desc);
        int callcode;
        
        // Only wrap if we can't instrument "inside" the method
        //  and we've already seen it (know the call type) from pass one
        if (!thisfunction.canInstrument() && !(thisfunction.getType() == 0)) {
            thisfunction.setType(opcode);
            
            // Replace the call to the to-be-wrapped method with a static call to our wrapper
            switch (opcode) {
            case INSN_INVOKE_VIRTUAL:
            case INSN_INVOKE_SUPER:
            case INSN_INVOKE_DIRECT:
            case INSN_INVOKE_INTERFACE:
                callcode = INSN_INVOKE_STATIC;
                break;
            case INSN_INVOKE_VIRTUAL_RANGE:
            case INSN_INVOKE_SUPER_RANGE:
            case INSN_INVOKE_DIRECT_RANGE:
            case INSN_INVOKE_INTERFACE_RANGE:
                callcode = INSN_INVOKE_STATIC_RANGE;
                break;
            default:
                callcode = opcode; // INVOKE_STATIC, INVOKE_STATIC_RANGE
            }
            mv.visitMethodInsn(callcode, "Lsparta/triceratops/TriceratopsWrappers;", thisfunction.getWrapperName(), desc, arguments);
        } else { // No need to wrap; pass on the original call
            mv.visitMethodInsn(opcode, owner, name, desc, arguments);
        }
    }

}
