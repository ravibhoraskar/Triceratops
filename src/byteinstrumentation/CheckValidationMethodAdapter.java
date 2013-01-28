package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class CheckValidationMethodAdapter extends MethodVisitor implements Opcodes {
    private String desc;
    private Triceratops.Precondition precondition;

    public CheckValidationMethodAdapter(int api, MethodVisitor mv, String desc, Triceratops.Precondition precondition) {
        super(api, mv);
        this.desc = desc;
        this.precondition = precondition;
    }
    
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        char returnType = desc.charAt(0);
        int numParams = Descriptors.numParams(desc);
        int numRegs = maxStack - numParams;
        int regNeeded = (returnType == 'J' || returnType == 'D') ? 3 : 2; // need an extra register for 'wide' types
        int additionalNeeded = 0;
        if (numRegs < regNeeded) // We need more registers
        {
            additionalNeeded = regNeeded - numRegs;
            mv.visitMaxs(maxStack + additionalNeeded, maxLocals);
        }
        
        int[] reg = new int[] {0, 1};
        
        Label l0 = new Label();
        
        mv.visitFieldInsn(INSN_SGET_BOOLEAN, "Lsparta/triceratops/TriceratopsApplication;", precondition.name, "Z", reg[0], 0);
        mv.visitJumpInsn(INSN_IF_NEZ, l0, reg[0], 0);
        
        // default return for method: 0 (or equivalent)
        switch (returnType) {
        case 'J': // long
        case 'D': // double
            mv.visitVarInsn(INSN_CONST_WIDE, reg[1], 0);
            mv.visitIntInsn(INSN_RETURN_WIDE, reg[1]);
            break;
        case '[': // array
        case 'L': // object
            mv.visitVarInsn(INSN_CONST, reg[1], 0);
            mv.visitIntInsn(INSN_RETURN_OBJECT, reg[1]);
        case 'V': // void
            mv.visitInsn(INSN_RETURN_VOID);
            break;
        default:
            mv.visitVarInsn(INSN_CONST, reg[1], 0);
            mv.visitIntInsn(INSN_RETURN, reg[1]);
            break;
        }
        
        mv.visitLabel(l0);
        
        if (additionalNeeded > 0) { // Downshift all the parameter registers so that the existing code will work
            int index = 0;
            index = Descriptors.advanceOne(desc, index); // Skip past the return type
            int pdest = numRegs;
            int psrc = maxStack - numParams;
            
            while (index < desc.length()) {
                char c = desc.charAt(index);
                switch (c) {
                case 'J': // long
                case 'D': // double
                    mv.visitVarInsn(INSN_MOVE_WIDE_16, pdest, psrc);
                    // Extra register for longs and doubles
                    pdest++;
                    psrc++;
                    break;
                case '[': // array
                case 'L': // object
                    mv.visitVarInsn(INSN_MOVE_OBJECT_16, pdest, psrc);
                default:
                    mv.visitVarInsn(INSN_MOVE_16, pdest, psrc);
                    break;
                }
                pdest++;
                psrc++;
                index = Descriptors.advanceOne(desc, index);
            }
        }
        
        // Further visit* calls will write out the existing code
        
        return;
    }

}
