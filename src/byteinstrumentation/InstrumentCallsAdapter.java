package byteinstrumentation;

import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.FieldVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

public class InstrumentCallsAdapter extends ApplicationVisitor implements Opcodes {
    public InstrumentCallsAdapter(int api, ApplicationVisitor av) {
        super(api, av);
    }
    
    @Override
    public ClassVisitor visitClass(int access, String name, String[] signature, String superName, String[] interfaces) {
        ClassVisitor cv = av.visitClass(access, name, signature, superName, interfaces);
        System.out.println(name);
        InstrumentClassAdapter ica = new InstrumentClassAdapter(api, cv);
        return ica;
    }
    
    @Override
    public void visitEnd()
    {
        ClassVisitor cv;
        FieldVisitor fv;
        MethodVisitor mv;

        cv = av.visitClass(ACC_PUBLIC, "Lsparta/triceratops/TriceratopsApplication;", null, "Landroid/app/Application;", null);
        cv.visit(0, ACC_PUBLIC, "Lsparta/triceratops/TriceratopsApplication;", null, "Landroid/app/Application;", null);
        cv.visitSource("TriceratopsApplication.java", null);
        {
            fv = cv.visitField(ACC_PUBLIC, "clickValidate", "Z", null, null);
            fv.visitEnd();
        }
        {
            mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
            mv.visitCode();
            mv.visitMaxs(1, 0);
            mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Application;", "<init>", "V", new int[] { 0 });
            mv.visitInsn(INSN_RETURN_VOID);
            mv.visitEnd();
        }
        cv.visitEnd();
        
        av.visitEnd();
    }

}
