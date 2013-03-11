package byteinstrumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.FieldVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class TriceratopsApplicationAdapter extends ApplicationVisitor implements Opcodes {
    private TriceratopsPolicy tripolicy;
    
    public TriceratopsApplicationAdapter(int api, ApplicationVisitor av, TriceratopsPolicy tripolicy) {
        super(api, av);
        this.tripolicy = tripolicy;
    }
    
    @Override
    public ClassVisitor visitClass(int access, String name, String[] signature, String superName, String[] interfaces) {
        ClassVisitor cv = av.visitClass(access, name, signature, superName, interfaces);
        TriceratopsClassAdapter ica = new TriceratopsClassAdapter(api, cv, name, tripolicy);
        return ica;
    }
    
    @Override
    public void visitEnd()
    {
        ClassVisitor tapp, twrap;
        FieldVisitor fv;
        MethodVisitor mv;
        Set<TriceratopsPolicy.Function> fnSet = new HashSet<>();

        tapp = av.visitClass(ACC_PUBLIC, "Lsparta/triceratops/TriceratopsApplication;", null, "Landroid/app/Application;", null);
        {
            // Add field to TriceratopsApplication
            fv = tapp.visitField(ACC_PUBLIC + ACC_STATIC, "triceratopsState", "I", null, null);
            fv.visitEnd();
        }
        {
            mv = tapp.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
            mv.visitCode();
            mv.visitMaxs(1, 0);
            mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Application;", "<init>", "V", new int[] { 0 });
            mv.visitInsn(INSN_RETURN_VOID);
            mv.visitEnd();
        }
        tapp.visitEnd();
        
        // Add method(s) to TriceratopsWrappers
        twrap = av.visitClass(ACC_PUBLIC, "Lsparta/triceratops/TriceratopsWrappers;", null, "Ljava/lang/Object;", null);
        
        // Make a set of all functions that need to be instrumented
        fnSet.addAll(tripolicy.restrictedFunctions);
        fnSet.addAll(tripolicy.protectedFunctions);
        fnSet.addAll(tripolicy.transitionFunctions);
        
        for (TriceratopsPolicy.Function fn : fnSet) {
            if (!fn.canInstrument()) { // Only make wrappers for methods that we couldn't instrument
                int fntype = fn.getType();
                String desc = fn.getDesc();
                if (fntype != INSN_INVOKE_STATIC && fntype != INSN_INVOKE_STATIC_RANGE) {
                    // Insert parameter for 'this'
                    desc = Descriptors.insertParamAtStart(desc, fn.getOwner());
                }
                
                char returnType = desc.charAt(0);
                
                mv = twrap.visitMethod(ACC_PUBLIC + ACC_STATIC, fn.getWrapperName(), desc, null, null);
                mv.visitCode();
                
                int numparams = Descriptors.numParams(desc);
                int[] reg = new int[] {0, 1};
                mv.visitMaxs(numparams + reg.length, 0);
                
                int[] params = new int[numparams];
                for (int i = 0; i < params.length; i++) {
                    params[i] = reg.length + i;
                }
                
                mv.visitFieldInsn(INSN_SGET, "Lsparta/triceratops/TriceratopsApplication;", "triceratopsState", "I", reg[0], 0);
                
                // store default return for method into our return register: 0 (or equivalent)
                switch (returnType) {
                case 'J': // long
                case 'D': // double
                    mv.visitVarInsn(INSN_CONST_WIDE, reg[1], 0);
                    break;
                case 'V': // void
                    break;
                default:
                    mv.visitVarInsn(INSN_CONST, reg[1], 0);
                    break;
                }
                
                Label lreturn = new Label();
                
                // Get the proper validation adapters
                
                List<Integer> states;
                Map<Integer, Integer> stateChanges = new HashMap<>();
                List<ValidationAdapter> adapters = new ArrayList<>();
                
                // if this function is default-denied
                if (tripolicy.restrictedFunctions.contains(fn)) {
                    states = new ArrayList<>();
                    for (int state : tripolicy.permitted.keySet()) {
                        if (tripolicy.permitted.get(state).contains(fn)) {
                            states.add(state);
                        }
                    }
                    if (!states.isEmpty())
                        adapters.add(new CheckValidationMethodAdapter(states, CheckValidationMethodAdapter.ValidateType.ALLOW_BY_STATE));
                }
                
                // if this function is denied by state (but default-allowed)
                states = new ArrayList<>();
                for (int state : tripolicy.forbidden.keySet()) {
                    if (tripolicy.forbidden.get(state).contains(fn)) {
                        states.add(state);
                    }
                }
                if (!states.isEmpty())
                    adapters.add(new CheckValidationMethodAdapter(states, CheckValidationMethodAdapter.ValidateType.DENY_BY_STATE));
                
                // apply any state transitions
                for (int stateFrom : tripolicy.transitions.keySet()) {
                    Map<Integer, List<TriceratopsPolicy.Function>> changeMap =  tripolicy.transitions.get(stateFrom);
                    for (int stateTo : changeMap.keySet()) {
                        if (changeMap.get(stateTo).contains(fn)) {
                            stateChanges.put(stateFrom, stateTo);
                        }
                    }
                }
                if (!stateChanges.isEmpty())
                    adapters.add(new AddValidationMethodAdapter(stateChanges));
                
                // Execute the proper validation adapters
                for (ValidationAdapter adapter : adapters) {
                    Label lnext = new Label();
                    adapter.doValidation(mv, lreturn, lnext);
                    mv.visitLabel(lnext);
                }
                
                // Call the wrapped method
                mv.visitMethodInsn(fntype, fn.getOwner(), fn.getName(), fn.getDesc(), params);
                
                // Move return value (if any) into our return register
                switch (returnType) {
                case 'J': // long
                case 'D': // double
                    mv.visitIntInsn(INSN_MOVE_RESULT_WIDE, reg[1]);
                    break;
                case '[': // array
                case 'L': // object
                    mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, reg[1]);
                    break;
                case 'V': // void
                    break;
                default:
                    mv.visitIntInsn(INSN_MOVE_RESULT, reg[1]);
                    break;
                }
                
                // Return the value in our return register
                mv.visitLabel(lreturn);
                switch (returnType) {
                case 'J': // long
                case 'D': // double
                    mv.visitIntInsn(INSN_RETURN_WIDE, reg[1]);
                    break;
                case '[': // array
                case 'L': // object
                    mv.visitIntInsn(INSN_RETURN_OBJECT, reg[1]);
                    break;
                case 'V': // void
                    mv.visitInsn(INSN_RETURN_VOID);
                    break;
                default:
                    mv.visitIntInsn(INSN_RETURN, reg[1]);
                    break;
                }
                mv.visitEnd();
            }
        }
        twrap.visitEnd();
        
        av.visitEnd();
    }

}
