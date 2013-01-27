package byteinstrumentation;

import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;

public class InstrumentClassAdapter extends ClassVisitor {

    public InstrumentClassAdapter(int api, ClassVisitor cv) {
        super(api, cv);
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String[] signatures, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signatures, exceptions);
        if (name.equals("leftClick") || name.equals("rightClick")) {
            mv = new InstrumentMethodAdapter(api, mv);
        }
        mv = new WrapMethodAdapter(api, mv);
        return mv;
    }

}
