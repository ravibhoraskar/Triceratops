package byteinstrumentation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ow2.asmdex.Opcodes;

public class Descriptors {
    
    public static String insertParamAtStart(String desc, String param) {
        StringBuilder newDesc = new StringBuilder(desc);
        
        int index = advanceOne(desc, 0);
        
        newDesc.insert(index, param);
        
        return newDesc.toString();
    }
    
    public static int numParams(String desc) {
        int index = 0;
        int params = 0;
        
        while (index < desc.length()) {
            char c = desc.charAt(index);
            params++;
            if (c == 'J' || c == 'D') // Extra register for longs and doubles
                params++;
            index = advanceOne(desc, index);
        }
        
        return params - 1;
    }
    
    public static int advanceOne(String desc, int index) {
        char c = desc.charAt(index);
        
        if (c == 'L') {
            index = desc.indexOf(';', index);
        } else if (c == '[') {
            do {
                index++;
                c = desc.charAt(index);
            } while (c != '[');
        }
        index++;
        
        return index;
    }

    public static TriceratopsPolicy.Function parse(String fname) {
        Pattern fpatt = Pattern.compile("(.+) (.+)->(.+)\\((.*)\\)(.+)");
        Matcher m = fpatt.matcher(fname);
        int fntype;
        if (!m.matches())
            throw new RuntimeException("Bad function name:\n\t" + fname);
        String desc = m.group(5) + m.group(4);
        TriceratopsPolicy.Function fn = TriceratopsPolicy.function(m.group(2), m.group(3), desc);
        
        switch(m.group(1)) {
        case "static":
            fntype = Opcodes.INSN_INVOKE_STATIC;
            break;
        case "virtual":
            fntype = Opcodes.INSN_INVOKE_VIRTUAL;
            break;
        default:
            throw new RuntimeException("Bad function type specified: " + m.group(1));
        }
        fn.setType(fntype);
        return fn;
    }
}
