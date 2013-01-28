package byteinstrumentation;

import java.util.List;

import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.FieldVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class TriceratopsApplicationAdapter extends ApplicationVisitor implements Opcodes {
    private List<Triceratops.Precondition> conditions;
    
    public TriceratopsApplicationAdapter(int api, ApplicationVisitor av, List<Triceratops.Precondition> conditions) {
        super(api, av);
        this.conditions = conditions;
    }
    
    @Override
    public ClassVisitor visitClass(int access, String name, String[] signature, String superName, String[] interfaces) {
        ClassVisitor cv = av.visitClass(access, name, signature, superName, interfaces);
        TriceratopsClassAdapter ica = new TriceratopsClassAdapter(api, cv, name, conditions);
        return ica;
    }
    
    @Override
    public void visitEnd()
    {
        ClassVisitor tapp, twrap;
        FieldVisitor fv;
        MethodVisitor mv;

        tapp = av.visitClass(ACC_PUBLIC, "Lsparta/triceratops/TriceratopsApplication;", null, "Landroid/app/Application;", null);
        for (Triceratops.Precondition precondition : conditions) {
            // Add field to TriceratopsApplication
            fv = tapp.visitField(ACC_PUBLIC + ACC_STATIC, precondition.name, "Z", null, null);
            fv.visitEnd();
        }
        {
            mv = tapp.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
            mv.visitCode();
            mv.visitMaxs(1, 0);
            mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Application;", "<init>", "V", new int[] { 0 });
            mv.visitInsn(INSN_RETURN_VOID);
            mv.visitEnd();
        }
        tapp.visitEnd();
        
        // Add method(s) to TriceratopsWrappers
        twrap = av.visitClass(ACC_PUBLIC, "Lsparta/triceratops/TriceratopsWrappers;", null, "Ljava/lang/Object;", null);
        for (Triceratops.Precondition precondition : conditions) {
            for (Triceratops.Function function : precondition.restrictedFunctions) {
                String desc = function.desc;
                if (function.instancefn) {
                    // Insert parameter for 'this'
                    desc = Descriptors.insertParamAtStart(desc, function.owner);
                }
                
                char returnType = function.desc.charAt(0);
                
                mv = twrap.visitMethod(ACC_PUBLIC + ACC_STATIC, function.name, desc, null, null);
                mv.visitCode();
                
                int numparams = Descriptors.numParams(desc);
                int[] reg = new int[] {0, 1};
                mv.visitMaxs(numparams + reg.length, 0);
                
                int[] param = new int[numparams];
                for (int i = 0; i < param.length; i++) {
                    param[i] = reg.length + i;
                }
                
                mv.visitFieldInsn(INSN_SGET_BOOLEAN, "Lsparta/triceratops/TriceratopsApplication;", precondition.name, "Z", reg[0], 0);
                
                // default return for method: 0 (or equivalent)
                switch (returnType) {
                case 'J': // long
                case 'D': // double
                    mv.visitVarInsn(INSN_CONST_WIDE, reg[1], 0);
                    break;
                case 'V': // void
                    break;
                default:
                    mv.visitVarInsn(INSN_CONST, reg[1], 0);
                    break;
                }
                
                Label l0 = new Label();
                mv.visitJumpInsn(INSN_IF_EQZ, l0, reg[0], 0);
                
                mv.visitMethodInsn(function.instancefn ? INSN_INVOKE_DIRECT : INSN_INVOKE_STATIC, function.owner, function.name, function.desc, param);
                switch (returnType) {
                case 'J': // long
                case 'D': // double
                    mv.visitIntInsn(INSN_MOVE_RESULT_WIDE, reg[1]);
                    break;
                case '[': // array
                case 'L': // object
                    mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, reg[1]);
                    break;
                case 'V': // void
                    break;
                default:
                    mv.visitIntInsn(INSN_MOVE_RESULT, reg[1]);
                    break;
                }
                
                mv.visitLabel(l0);
                switch (returnType) {
                case 'J': // long
                case 'D': // double
                    mv.visitIntInsn(INSN_RETURN_WIDE, reg[1]);
                    break;
                case '[': // array
                case 'L': // object
                    mv.visitIntInsn(INSN_RETURN_OBJECT, reg[1]);
                    break;
                case 'V': // void
                    mv.visitInsn(INSN_RETURN_VOID);
                    break;
                default:
                    mv.visitIntInsn(INSN_RETURN, reg[1]);
                    break;
                }
                mv.visitEnd();
            }
        }
        twrap.visitEnd();
        
        av.visitEnd();
    }

}
