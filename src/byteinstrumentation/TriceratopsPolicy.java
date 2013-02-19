package byteinstrumentation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class TriceratopsPolicy {

    public String[] STATES;
    public Map<String, List<String>> PERMITTED_METHODS;
    public Map<String, List<String>> FORBIDDEN_METHODS;
    public Map<String, Map<String, List<String>>> TRANSITIONS;
    
    public static void main(String[] args) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader testpolicy = new FileReader("res/testpolicy.json");
        TriceratopsPolicy tripolicy = gson.fromJson(testpolicy, TriceratopsPolicy.class);
        
        System.out.println("STATES");
        for (String state : tripolicy.STATES) {
            System.out.println("  " + state);
        }
        
        System.out.println("PERMITTED_METHODS");
        for (String state : tripolicy.PERMITTED_METHODS.keySet()) {
            System.out.println("  " + state);
            for (String method : tripolicy.PERMITTED_METHODS.get(state)) {
                System.out.println("    " + method);
            }
        }
        
        System.out.println("FORBIDDEN_METHODS");
        for (String state : tripolicy.FORBIDDEN_METHODS.keySet()) {
            System.out.println("  " + state);
            for (String method : tripolicy.FORBIDDEN_METHODS.get(state)) {
                System.out.println("    " + method);
            }
        }
        
        System.out.println("TRANSITIONS");
        for (String s : tripolicy.TRANSITIONS.keySet()) {
            System.out.println("  " + s);
            for (String t : tripolicy.TRANSITIONS.get(s).keySet()) {
                System.out.println("    " + t);
                for (String m : tripolicy.TRANSITIONS.get(s).get(t)) {
                    System.out.println("      " + m);
                }
            }
        }
        
        System.out.println(gson.toJson(tripolicy));
    }
}
