package byteinstrumentation;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ow2.asmdex.Opcodes;

import com.google.gson.Gson;

public class TriceratopsPolicy {
    
    private static final String DEFAULT_FORBIDDEN_STATE_NAME = "_default_";
    
    static class Function implements Opcodes {
        
        private String owner;
        private String name;
        private String desc;
        
        private int fnType;
        private boolean canInstrument;
        
        private Function(String owner, String name, String desc) {
            this.owner = owner;
            this.name = name;
            this.desc = desc;
            
            this.fnType = 0;
            canInstrument = false;
        }
        
        public String getOwner() {
            return owner;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public int getType() {
            if (this.fnType == 0)
                throw new RuntimeException("Unknown function type:\n\t" + toString());
            return fnType;
        }
        
        public void setType(int fnType) {
            if (this.fnType != 0 && this.fnType != fnType)
                throw new RuntimeException("Conflicting invocations detected:\n\t"
                        + toString() + "\n\t" + fnType);
            this.fnType = fnType;
        }
        
        public boolean canInstrument() {
            return canInstrument;
        }
        
        public void canInstrument(boolean value) {
            canInstrument = value;
        }
        
        public String getWrapperName() {
            return "Triceratops" + owner.replace("/", "_").replace(";", "__") + name;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;
            if (o == this)
                return true;
            if (o.getClass() != getClass())
                return false;
            
            Function other = (Function)o;
            return owner.equals(other.owner)
                    && name.equals(other.name)
                    && desc.equals(other.desc);
        }
        
        @Override
        public int hashCode() {
            int hash = 13;
            hash = 17 * hash + owner.hashCode();
            hash = 17 * hash + name.hashCode();
            hash = 17 * hash + desc.hashCode();
            return hash;
        }
        
        // Mainly for debugging. Return type is not extracted from description, so it appears
        // as the "first parameter" in this string representation.
        @Override
        public String toString() {
            String type = "";
            switch (fnType) {
            case 0:
                type = "unknown ";
                break;
            case INSN_INVOKE_VIRTUAL:
            case INSN_INVOKE_VIRTUAL_RANGE:
                type = "virtual ";
                break;
            case INSN_INVOKE_SUPER:
            case INSN_INVOKE_SUPER_RANGE:
                type = "super ";
                break;
            case INSN_INVOKE_DIRECT:
            case INSN_INVOKE_DIRECT_RANGE:
                type = "direct ";
                break;
            case INSN_INVOKE_STATIC:
            case INSN_INVOKE_STATIC_RANGE:
                type = "static ";
                break;
            case INSN_INVOKE_INTERFACE:
            case INSN_INVOKE_INTERFACE_RANGE:
                type = "interface ";
                break;
            }
            type += owner + "->" + name + "(" + desc + ")";
            return type;
        }
    }

    private List<String> STATES;
    private Map<String, List<String>> FORBIDDEN_METHODS;
    private Map<String, List<String>> PERMITTED_METHODS;
    private Map<String, Map<String, List<String>>> TRANSITIONS;

    public transient Map<Integer, List<Function>> forbidden; // default-allowed, but forbidden per-state
    public transient Map<Integer, List<Function>> permitted; // default-denied (from FORBIDDEN_METHODS/_default_), but permitted per-state
    public transient Map<Integer, Map<Integer, List<Function>>> transitions;
    
    public transient Set<Function> restrictedFunctions; // default-denied (from FORBIDDEN_METHODS/_default_)
    
    // Convenience sets for WrapMethodAdapter
    public transient Set<Function> protectedFunctions; // set of all functions that are default-allowed, but forbidden per-state
    public transient Set<Function> transitionFunctions; // set of all functions that can change the state
    
    private transient static Gson gson;
    private transient static Map<Function, Function> functions;
    
    // Factory method for creating TriceratopsPolicy.Functions, with interning
    public static Function function(String owner, String name, String desc) {
        if (functions == null) {
            functions = new HashMap<>();
        }
        Function fn = new Function(owner, name, desc);
        if (!functions.containsKey(fn)) {
            functions.put(fn, fn);
        }
        return functions.get(fn);
    }
    
    public static TriceratopsPolicy readJson(Reader jsonData) {
        if (gson == null)
            gson = new Gson();
        TriceratopsPolicy tripolicy = gson.fromJson(jsonData, TriceratopsPolicy.class);
        
        tripolicy.permitted = new HashMap<>();
        tripolicy.forbidden = new HashMap<>();
        tripolicy.transitions = new HashMap<>();
        
        tripolicy.restrictedFunctions = new HashSet<>();
        tripolicy.protectedFunctions = new HashSet<>();
        tripolicy.transitionFunctions = new HashSet<>();
        
        // Extract default forbidden methods
        if (tripolicy.FORBIDDEN_METHODS.containsKey(DEFAULT_FORBIDDEN_STATE_NAME)) {
            for (String fname : tripolicy.FORBIDDEN_METHODS.get(DEFAULT_FORBIDDEN_STATE_NAME)) {
                tripolicy.restrictedFunctions.add(Descriptors.parse(fname));
            }
        }
        
        // Extract per-state data
        for (int i = 0; i < tripolicy.STATES.size(); i++) {
            String state = tripolicy.STATES.get(i);
            
            if (tripolicy.FORBIDDEN_METHODS.containsKey(state)) {
                List<Function> functions = new ArrayList<>();
                for (String fname : tripolicy.FORBIDDEN_METHODS.get(state)) {
                    Function fn = Descriptors.parse(fname);
                    tripolicy.protectedFunctions.add(fn);
                    functions.add(fn);
                }
                tripolicy.forbidden.put(i, functions);
            }
            
            if (tripolicy.PERMITTED_METHODS.containsKey(state)) {
                List<Function> functions = new ArrayList<>();
                for (String fname : tripolicy.PERMITTED_METHODS.get(state)) {
                    functions.add(Descriptors.parse(fname));
                }
                tripolicy.permitted.put(i, functions);
            }
            
            if (tripolicy.TRANSITIONS.containsKey(state)) {
                Map<Integer, List<Function>> transmap = new HashMap<>();
                for (String transstate : tripolicy.TRANSITIONS.get(state).keySet()) {
                    List<Function> functions = new ArrayList<>();
                    for (String fname : tripolicy.TRANSITIONS.get(state).get(transstate)) {
                        Function fn = Descriptors.parse(fname);
                        tripolicy.transitionFunctions.add(fn);
                        functions.add(fn);
                    }
                    transmap.put(tripolicy.STATES.indexOf(transstate), functions);
                }
                tripolicy.transitions.put(i, transmap);
            }
        }
        
        return tripolicy;
    }
}
