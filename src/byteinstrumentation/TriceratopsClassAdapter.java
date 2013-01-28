package byteinstrumentation;

import java.util.List;

import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;

public class TriceratopsClassAdapter extends ClassVisitor {
    private String name;
    private List<Triceratops.Precondition> conditions;

    public TriceratopsClassAdapter(int api, ClassVisitor cv, String name, List<Triceratops.Precondition> conditions) {
        super(api, cv);
        this.name = name;
        this.conditions = conditions;
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String[] signatures, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signatures, exceptions);
        for (Triceratops.Precondition precondition : conditions) {
            mv = new WrapMethodAdapter(api, mv, precondition);
            
            for (Triceratops.Function function : precondition.restrictedFunctions) {
                if (function.owner.equals(this.name) && function.name.equals(name) && function.desc.equals(desc)) {
                    mv = new CheckValidationMethodAdapter(api, mv, desc, precondition);
                    break;
                }
            }
            for (Triceratops.Function function : precondition.validateFunctions) {
                if (function.owner.equals(this.name) && function.name.equals(name) && function.desc.equals(desc)) {
                    mv = new AddValidationMethodAdapter(api, mv, precondition);
                    break;
                }
            }
            
        }
        return mv;
    }

}
