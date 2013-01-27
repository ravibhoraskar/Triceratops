package byteinstrumentation;

import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.FieldVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class InstrumentCallsAdapter extends ApplicationVisitor implements Opcodes {
    public InstrumentCallsAdapter(int api, ApplicationVisitor av) {
        super(api, av);
    }
    
    @Override
    public ClassVisitor visitClass(int access, String name, String[] signature, String superName, String[] interfaces) {
        ClassVisitor cv = av.visitClass(access, name, signature, superName, interfaces);
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
        // cv.visit(0, ACC_PUBLIC, "Lsparta/triceratops/TriceratopsApplication;", null, "Landroid/app/Application;", null);
        {
            fv = cv.visitField(ACC_PUBLIC + ACC_STATIC, "clickValidate", "Z", null, null);
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
        
        cv = av.visitClass(ACC_PUBLIC, "Lsparta/triceratops/TriceratopsWrappers;", null, "Landroid/app/Application;", null);
        // cv.visit(0, ACC_PUBLIC, "Lsparta/triceratops/TriceratopsWrappers;", null, "Landroid/app/Application;", null);
        {
            mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC, "i", "ILjava/lang/String;Ljava/lang/String;", null, null);
            mv.visitCode();
            
            int regs = 4;
            int r0 = 0;
            int r1 = 1;
            int p0 = regs - 2;
            int p1 = regs - 1;
            mv.visitMaxs(regs, 0);
            
            mv.visitFieldInsn(INSN_SGET_BOOLEAN, "Lmy/android/mouse/TriceratopsApplication;", "clickValidate", "Z", r0, 0);
            mv.visitVarInsn(INSN_CONST, r1, 0); // default return for int method: 0
            
            Label l0 = new Label();
            mv.visitJumpInsn(INSN_IF_EQZ, l0, r0, 0);
            
            mv.visitMethodInsn(INSN_INVOKE_STATIC, "Landroid/util/Log;", "i", "ILjava/lang/String;Ljava/lang/String;", new int[] { p0, p1 });
            mv.visitIntInsn(INSN_MOVE_RESULT, r1);
            
            mv.visitLabel(l0);
            mv.visitIntInsn(INSN_RETURN, r1);
            mv.visitEnd();
        }
        cv.visitEnd();
        
        av.visitEnd();
    }

}
