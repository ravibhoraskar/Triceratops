package byteinstrumentation;

import java.util.HashMap;
import java.util.Map;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class AddValidationMethodAdapter implements Opcodes, ValidationAdapter {
    private Map<Integer, Integer> stateChanges;

    public AddValidationMethodAdapter(Map<Integer, Integer> stateChanges) {
        this.stateChanges = stateChanges;
    }
    
    @Override
    public void doValidation(MethodVisitor mv, Label lreturn, Label lnext) {
        int[] reg = {0, 1};
        
        Map<Integer, Label> lchanges = new HashMap<>();
        
        // Check current state and jump to the proper place
        mv.visitFieldInsn(INSN_SGET, "Lsparta/triceratops/TriceratopsApplication;", "triceratopsState", "I", reg[0], 0);
        for (int stateFrom : stateChanges.keySet()) {
            int stateTo = stateChanges.get(stateFrom);
            if (!lchanges.containsKey(stateTo))
                lchanges.put(stateTo, new Label());
            mv.visitVarInsn(INSN_CONST, reg[1], stateFrom);
            mv.visitJumpInsn(INSN_IF_EQ, lchanges.get(stateTo), reg[0], reg[1]);
        }
        mv.visitJumpInsn(INSN_GOTO, lnext, 0, 0);
        
        // Set the new state
        for (int stateTo : stateChanges.values()) {
            Label l = lchanges.get(stateTo);
            mv.visitLabel(l);
            mv.visitVarInsn(INSN_CONST, reg[0], stateTo);
            mv.visitFieldInsn(INSN_SPUT, "Lsparta/triceratops/TriceratopsApplication;", "triceratopsState", "I", reg[0], 0);
            mv.visitJumpInsn(INSN_GOTO, lnext, 0, 0);
        }
    }

}
