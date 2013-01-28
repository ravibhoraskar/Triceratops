package byteinstrumentation;

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

}
