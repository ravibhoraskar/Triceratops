package byteinstrumentation;

import java.util.ArrayList;
import java.util.List;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class InstrumentHelper extends MethodVisitor implements Opcodes {
    
    private static final int[] reg = {0, 1};
    
    private List<ValidationAdapter> adapters;
    
    private char returnType;
    private int numParams;
    private int numRegs;
    private int additionalNeeded;
    
    private MethodVisitor mv;
    private String desc;
    private int maxStack;
    private int maxLocals;
    
    public InstrumentHelper(int api, MethodVisitor mv, String desc) {
        super(api, mv);
        
        this.mv = mv;
        this.desc = desc;
        
        returnType = desc.charAt(0);
        numParams = Descriptors.numParams(desc);
        additionalNeeded = 0;
        
        adapters = new ArrayList<>();
    }
    
    public void addAdapter(ValidationAdapter adapter) {
        adapters.add(adapter);
    }
    
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.numRegs = maxStack - numParams;
        
        Label lreturn = new Label();
        Label lcode = new Label();
        
        if (!adapters.isEmpty()) {
            // We need at least two registers for our instrumentation
            requireTwoRegisters();
            
            // Apply all the adapters
            mv.visitFieldInsn(INSN_SGET, "Lsparta/triceratops/TriceratopsApplication;", "triceratopsState", "I", reg[0], 0);
            for (ValidationAdapter adapter : adapters) {
                Label lnext = new Label();
                adapter.doValidation(mv, lreturn, lnext);
                mv.visitLabel(lnext);
            }
            // Skip over the "return zero"
            mv.visitJumpInsn(INSN_GOTO, lcode, 0, 0);
            
            mv.visitLabel(lreturn);
            returnZero();
            
            // Shift the parameters over and continue to the existing code
            mv.visitLabel(lcode);
            shiftParameters();
        } else {
            mv.visitMaxs(maxStack, maxLocals);
        }
    }
    
    private void requireTwoRegisters() {
        int regNeeded = (returnType == 'J' || returnType == 'D') ? 3 : 2; // need an extra register for 'wide' types
        if (numRegs < regNeeded) // We need more registers
        {
            additionalNeeded = regNeeded - numRegs;
            mv.visitMaxs(maxStack + additionalNeeded, maxLocals);
        } else {
            mv.visitMaxs(maxStack, maxLocals);
        }
    }
    
    private void returnZero() {
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
    }
    
    private void shiftParameters() {
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
    }

}
