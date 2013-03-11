package byteinstrumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;

public class TriceratopsClassAdapter extends ClassVisitor {
    private String name;
    private TriceratopsPolicy tripolicy;

    public TriceratopsClassAdapter(int api, ClassVisitor cv, String name, TriceratopsPolicy tripolicy) {
        super(api, cv);
        this.name = name;
        this.tripolicy = tripolicy;
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String[] signatures, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signatures, exceptions);
        mv = new WrapMethodAdapter(api, mv);
        
        InstrumentHelper helper = new InstrumentHelper(api, mv, desc);
        
        TriceratopsPolicy.Function thisfunction = TriceratopsPolicy.function(this.name, name, desc);
        List<Integer> states;
        Map<Integer, Integer> stateChanges = new HashMap<>();
        
        // if this function is default-denied
        if (tripolicy.restrictedFunctions.contains(thisfunction)) {
            states = new ArrayList<>();
            for (int state : tripolicy.permitted.keySet()) {
                if (tripolicy.permitted.get(state).contains(thisfunction)) {
                    states.add(state);
                }
            }
            if (!states.isEmpty())
                helper.addAdapter(new CheckValidationMethodAdapter(states, CheckValidationMethodAdapter.ValidateType.ALLOW_BY_STATE));
        }
        
        // if this function is denied by state (but default-allowed)
        states = new ArrayList<>();
        for (int state : tripolicy.forbidden.keySet()) {
            if (tripolicy.forbidden.get(state).contains(thisfunction)) {
                states.add(state);
            }
        }
        if (!states.isEmpty())
            helper.addAdapter(new CheckValidationMethodAdapter(states, CheckValidationMethodAdapter.ValidateType.DENY_BY_STATE));
        
        // apply any state transitions
        for (int stateFrom : tripolicy.transitions.keySet()) {
            Map<Integer, List<TriceratopsPolicy.Function>> changeMap =  tripolicy.transitions.get(stateFrom);
            for (int stateTo : changeMap.keySet()) {
                if (changeMap.get(stateTo).contains(thisfunction)) {
                    stateChanges.put(stateFrom, stateTo);
                }
            }
        }
        if (!stateChanges.isEmpty())
            helper.addAdapter(new AddValidationMethodAdapter(stateChanges));
        
        return helper;
    }

}
