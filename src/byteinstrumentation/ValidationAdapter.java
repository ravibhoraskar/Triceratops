package byteinstrumentation;

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.structureCommon.Label;

public interface ValidationAdapter {

    public void doValidation(MethodVisitor mv, Label lreturn, Label lnext);
}
