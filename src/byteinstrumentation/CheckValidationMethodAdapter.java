package byteinstrumentation;

import java.util.List;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class CheckValidationMethodAdapter implements Opcodes, ValidationAdapter {
    
    public static enum ValidateType {ALLOW_BY_STATE, DENY_BY_STATE};
    
    private List<Integer> states;
    private ValidateType vtype;

    public CheckValidationMethodAdapter(List<Integer> states, ValidateType vtype) {
        this.states = states;
        this.vtype = vtype;
    }

    @Override
    public void doValidation(MethodVisitor mv, Label lreturn, Label lnext) {
        int[] reg = {0, 1};
        
        Label lhitstate;
        Label lnotfound;
        
        if (vtype == ValidateType.DENY_BY_STATE) {
            lhitstate = lreturn;
            lnotfound = lnext;
        }
        else { // if (vtype = ValidateType.ALLOW_BY_STATE)
            lhitstate = lnext;
            lnotfound = lreturn;
        }
        
        // Check against current state and jump to the proper place if found
        mv.visitFieldInsn(INSN_SGET, "Lsparta/triceratops/TriceratopsApplication;", "triceratopsState", "I", reg[0], 0);
        for (int state : states) {
            mv.visitVarInsn(INSN_CONST, reg[1], state);
            mv.visitJumpInsn(INSN_IF_EQ, lhitstate, reg[0], reg[1]);
        }
        
        mv.visitJumpInsn(INSN_GOTO, lnotfound, 0, 0);
    }

}
