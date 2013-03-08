package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class WrapMethodAdapter extends MethodVisitor implements Opcodes {
    private TriceratopsPolicy tripolicy;
    
    public WrapMethodAdapter(int api, MethodVisitor mv, TriceratopsPolicy tripolicy) {
        super(api, mv);
        this.tripolicy = tripolicy;
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, int[] arguments) {
        TriceratopsPolicy.Function thisfunction = TriceratopsPolicy.function(owner, name, desc);
        int callcode;
        
        boolean flag = tripolicy.transitionFunctions.contains(thisfunction) 
                || tripolicy.protectedFunctions.contains(thisfunction)
                || tripolicy.restrictedFunctions.contains(thisfunction);
        
        if (flag) {
            thisfunction.setType(opcode);
            
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
        } else {
            mv.visitMethodInsn(opcode, owner, name, desc, arguments);
        }
    }

}
