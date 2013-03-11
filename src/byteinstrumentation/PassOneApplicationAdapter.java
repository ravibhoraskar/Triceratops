package byteinstrumentation;

import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class PassOneApplicationAdapter extends ApplicationVisitor implements Opcodes {
    private TriceratopsPolicy tripolicy;
    
    public PassOneApplicationAdapter(int api, TriceratopsPolicy tripolicy) {
        super(api);
        this.tripolicy = tripolicy;
    }
    
    @Override
    public ClassVisitor visitClass(int access, String name, String[] signature, String superName, String[] interfaces) {
        ClassVisitor cv = new PassOneClassAdapter(api, name);
        return cv;
    }
    
    private class PassOneClassAdapter extends ClassVisitor {
        String name;

        public PassOneClassAdapter(int api, String name) {
            super(api);
            this.name = name;
        }
        
        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String[] signatures, String[] exceptions) {
            MethodVisitor mv = new CheckMethodAdapter(api);
            TriceratopsPolicy.Function thisfunction = TriceratopsPolicy.function(this.name, name, desc);
            
            // Only bother with functions in our policy file
            boolean flag = tripolicy.transitionFunctions.contains(thisfunction) 
                    || tripolicy.protectedFunctions.contains(thisfunction)
                    || tripolicy.restrictedFunctions.contains(thisfunction);
            
            if (flag) {
                thisfunction.canInstrument(true); // We can instrument "inside" this method
            }
            
            return mv;
        }

    }
    
    private class CheckMethodAdapter extends MethodVisitor {
        
        public CheckMethodAdapter(int api) {
            super(api);
        }
        
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, int[] arguments) {
            TriceratopsPolicy.Function thisfunction = TriceratopsPolicy.function(owner, name, desc);
            
            // Only bother with functions in our policy file
            boolean flag = tripolicy.transitionFunctions.contains(thisfunction) 
                    || tripolicy.protectedFunctions.contains(thisfunction)
                    || tripolicy.restrictedFunctions.contains(thisfunction);
            
            if (flag) {
                thisfunction.setType(opcode); // Set the call type so that our wrapper calls it properly
            }
        }
    }

}
