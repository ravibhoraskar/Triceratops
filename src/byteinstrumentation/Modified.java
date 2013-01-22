package byteinstrumentation;

import org.ow2.asmdex.AnnotationVisitor;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.FieldVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

public class Modified implements Opcodes {

public static byte[] dump() throws Exception {

	ApplicationWriter aw = new ApplicationWriter();
	aw.visit();

	dumpBuildConfig(aw);
	dumpR$attr(aw);
	dumpR$drawable(aw);
	dumpR$id(aw);
	dumpR$layout(aw);
	dumpR$string(aw);
	dumpR(aw);
	dumpAndroidMouseActivity(aw);
	dumpBuildConfig2(aw);
	dumpR$attr2(aw);
	dumpR$drawable2(aw);
	dumpR$id2(aw);
	dumpR$layout2(aw);
	dumpR$string2(aw);
	dumpR2(aw);
	dumpTriceratopsApplication(aw);

	aw.visitEnd();

	return aw.toByteArray();
}

public static void dumpBuildConfig(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/BuildConfig;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/BuildConfig;", null, "Ljava/lang/Object;", null);
	cv.visitSource("BuildConfig.java", null);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "DEBUG", "Z", null, true);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$attr(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$attr;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$attr;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mous/R$attr;", "Lmy/android/mous/R;", "attr", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$drawable(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$drawable;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$drawable;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mous/R$drawable;", "Lmy/android/mous/R;", "drawable", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "ic_launcher", "I", null, 2130837504);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$id(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$id;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$id;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mous/R$id;", "Lmy/android/mous/R;", "id", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "leftClick", "I", null, 2131034112);
		fv.visitEnd();
	}
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "rightClick", "I", null, 2131034113);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$layout(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$layout;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$layout;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mous/R$layout;", "Lmy/android/mous/R;", "layout", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "main", "I", null, 2130903040);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$string(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$string;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R$string;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mous/R$string;", "Lmy/android/mous/R;", "string", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "app_name", "I", null, 2130968577);
		fv.visitEnd();
	}
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "hello", "I", null, 2130968576);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mous/R;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mous/R$attr;", "Lmy/android/mous/R;", "attr", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mous/R$drawable;", "Lmy/android/mous/R;", "drawable", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mous/R$id;", "Lmy/android/mous/R;", "id", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mous/R$layout;", "Lmy/android/mous/R;", "layout", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mous/R$string;", "Lmy/android/mous/R;", "string", ACC_PUBLIC + ACC_FINAL);
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpAndroidMouseActivity(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC, "Lmy/android/mouse/AndroidMouseActivity;", null, "Landroid/app/Activity;", new String[] { "Landroid/hardware/SensorEventListener;" });
	cv.visit(0, ACC_PUBLIC, "Lmy/android/mouse/AndroidMouseActivity;", null, "Landroid/app/Activity;", new String[] { "Landroid/hardware/SensorEventListener;" });
	cv.visitSource("AndroidMouseActivity.java", null);
	{
	av0 = cv.visitAnnotation("Landroid/annotation/TargetApi;", false);
		av0.visit("value", 3);
		av0.visitEnd();
	}
	{
		fv = cv.visitField(ACC_PRIVATE, "sensorManager", "Landroid/hardware/SensorManager;", null, null);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Activity;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PRIVATE, "getAccelerometer", "VLandroid/hardware/SensorEvent;", null, null);
		mv.visitCode();
		mv.visitMaxs(9, 0);
		mv.visitFieldInsn(INSN_IGET_OBJECT, "Landroid/hardware/SensorEvent;", "values", "[F", 0, 8);
		mv.visitVarInsn(INSN_CONST_4, 4, 0);
		mv.visitArrayOperationInsn(INSN_AGET, 1, 0, 4);
		mv.visitVarInsn(INSN_CONST_4, 4, 1);
		mv.visitArrayOperationInsn(INSN_AGET, 2, 0, 4);
		mv.visitVarInsn(INSN_CONST_4, 4, 2);
		mv.visitArrayOperationInsn(INSN_AGET, 3, 0, 4);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getApplication", "Landroid/app/Application;", new int[] { 7 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 4);
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 4, 0, "Lmy/android/mouse/TriceratopsApplication;");
		mv.visitFieldInsn(INSN_IGET_BOOLEAN, "Lmy/android/mouse/TriceratopsApplication;", "clickValidate", "Z", 4, 4);
		Label l0 = new Label();
		mv.visitJumpInsn(INSN_IF_EQZ, l0, 4, 0);
		mv.visitStringInsn(INSN_CONST_STRING, 4, "accelo");
		mv.visitTypeInsn(INSN_NEW_INSTANCE, 5, 0, 0, "Ljava/lang/StringBuilder;");
		mv.visitMethodInsn(INSN_INVOKE_STATIC, "Ljava/lang/String;", "valueOf", "Ljava/lang/String;F", new int[] { 1 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 6);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/StringBuilder;", "<init>", "VLjava/lang/String;", new int[] { 5, 6 });
		mv.visitStringInsn(INSN_CONST_STRING, 6, " ");
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Ljava/lang/StringBuilder;", "append", "Ljava/lang/StringBuilder;Ljava/lang/String;", new int[] { 5, 6 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 5);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Ljava/lang/StringBuilder;", "append", "Ljava/lang/StringBuilder;F", new int[] { 5, 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 5);
		mv.visitStringInsn(INSN_CONST_STRING, 6, " ");
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Ljava/lang/StringBuilder;", "append", "Ljava/lang/StringBuilder;Ljava/lang/String;", new int[] { 5, 6 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 5);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Ljava/lang/StringBuilder;", "append", "Ljava/lang/StringBuilder;F", new int[] { 5, 3 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 5);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Ljava/lang/StringBuilder;", "toString", "Ljava/lang/String;", new int[] { 5 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 5);
		mv.visitMethodInsn(INSN_INVOKE_STATIC, "Landroid/util/Log;", "i", "ILjava/lang/String;Ljava/lang/String;", new int[] { 4, 5 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC, "leftClick", "VLandroid/view/View;", null, null);
		mv.visitCode();
		mv.visitMaxs(4, 0);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getApplication", "Landroid/app/Application;", new int[] { 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "Lmy/android/mouse/TriceratopsApplication;");
		mv.visitVarInsn(INSN_CONST_4, 1, 1);
		mv.visitFieldInsn(INSN_IPUT_BOOLEAN, "Lmy/android/mouse/TriceratopsApplication;", "clickValidate", "Z", 1, 0);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getApplication", "Landroid/app/Application;", new int[] { 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "Lmy/android/mouse/TriceratopsApplication;");
		mv.visitFieldInsn(INSN_IGET_BOOLEAN, "Lmy/android/mouse/TriceratopsApplication;", "clickValidate", "Z", 0, 0);
		Label l0 = new Label();
		mv.visitJumpInsn(INSN_IF_EQZ, l0, 0, 0);
		mv.visitStringInsn(INSN_CONST_STRING, 0, "leftclick");
		mv.visitStringInsn(INSN_CONST_STRING, 1, "leftclick");
		mv.visitMethodInsn(INSN_INVOKE_STATIC, "Landroid/util/Log;", "i", "ILjava/lang/String;Ljava/lang/String;", new int[] { 0, 1 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC, "onAccuracyChanged", "VLandroid/hardware/Sensor;I", null, null);
		mv.visitCode();
		mv.visitMaxs(3, 0);
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC, "onCreate", "VLandroid/os/Bundle;", null, null);
		mv.visitCode();
		mv.visitMaxs(5, 0);
		mv.visitVarInsn(INSN_CONST_16, 1, 1024);
		mv.visitVarInsn(INSN_CONST_4, 2, 1);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "requestWindowFeature", "ZI", new int[] { 3, 2 });
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getWindow", "Landroid/view/Window;", new int[] { 3 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Landroid/view/Window;", "setFlags", "VII", new int[] { 0, 1, 1 });
		mv.visitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onCreate", "VLandroid/os/Bundle;", new int[] { 3, 4 });
		mv.visitVarInsn(INSN_CONST_HIGH16, 0, 2130903040);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "setContentView", "VI", new int[] { 3, 0 });
		mv.visitStringInsn(INSN_CONST_STRING, 0, "sensor");
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getSystemService", "Ljava/lang/Object;Ljava/lang/String;", new int[] { 3, 0 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "Landroid/hardware/SensorManager;");
		mv.visitFieldInsn(INSN_IPUT_OBJECT, "Lmy/android/mouse/AndroidMouseActivity;", "sensorManager", "Landroid/hardware/SensorManager;", 0, 3);
		mv.visitFieldInsn(INSN_IGET_OBJECT, "Lmy/android/mouse/AndroidMouseActivity;", "sensorManager", "Landroid/hardware/SensorManager;", 0, 3);
		mv.visitFieldInsn(INSN_IGET_OBJECT, "Lmy/android/mouse/AndroidMouseActivity;", "sensorManager", "Landroid/hardware/SensorManager;", 1, 3);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Landroid/hardware/SensorManager;", "getDefaultSensor", "Landroid/hardware/Sensor;I", new int[] { 1, 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 1);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Landroid/hardware/SensorManager;", "registerListener", "ZLandroid/hardware/SensorEventListener;Landroid/hardware/Sensor;I", new int[] { 0, 3, 1, 2 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PROTECTED, "onPause", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(2, 0);
		mv.visitFieldInsn(INSN_IGET_OBJECT, "Lmy/android/mouse/AndroidMouseActivity;", "sensorManager", "Landroid/hardware/SensorManager;", 0, 1);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Landroid/hardware/SensorManager;", "unregisterListener", "VLandroid/hardware/SensorEventListener;", new int[] { 0, 1 });
		mv.visitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onStop", "V", new int[] { 1 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PROTECTED, "onResume", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(4, 0);
		mv.visitVarInsn(INSN_CONST_4, 2, 1);
		mv.visitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onResume", "V", new int[] { 3 });
		mv.visitFieldInsn(INSN_IGET_OBJECT, "Lmy/android/mouse/AndroidMouseActivity;", "sensorManager", "Landroid/hardware/SensorManager;", 0, 3);
		mv.visitFieldInsn(INSN_IGET_OBJECT, "Lmy/android/mouse/AndroidMouseActivity;", "sensorManager", "Landroid/hardware/SensorManager;", 1, 3);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Landroid/hardware/SensorManager;", "getDefaultSensor", "Landroid/hardware/Sensor;I", new int[] { 1, 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 1);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Landroid/hardware/SensorManager;", "registerListener", "ZLandroid/hardware/SensorEventListener;Landroid/hardware/Sensor;I", new int[] { 0, 3, 1, 2 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC, "onSensorChanged", "VLandroid/hardware/SensorEvent;", null, null);
		mv.visitCode();
		mv.visitMaxs(4, 0);
		mv.visitFieldInsn(INSN_IGET_OBJECT, "Landroid/hardware/SensorEvent;", "sensor", "Landroid/hardware/Sensor;", 0, 3);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Landroid/hardware/Sensor;", "getType", "I", new int[] { 0 });
		mv.visitIntInsn(INSN_MOVE_RESULT, 0);
		mv.visitVarInsn(INSN_CONST_4, 1, 1);
		Label l0 = new Label();
		mv.visitJumpInsn(INSN_IF_NE, l0, 0, 1);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Lmy/android/mouse/AndroidMouseActivity;", "getAccelerometer", "VLandroid/hardware/SensorEvent;", new int[] { 2, 3 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getApplication", "Landroid/app/Application;", new int[] { 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "Lmy/android/mouse/TriceratopsApplication;");
		mv.visitVarInsn(INSN_CONST_4, 1, 7);
		mv.visitFieldInsn(INSN_IPUT, "Lmy/android/mouse/TriceratopsApplication;", "dangCapsLock", "I", 1, 0);
		Label l1 = new Label();
		mv.visitJumpInsn(INSN_GOTO, l1, 0, 0);
		mv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC, "rightClick", "VLandroid/view/View;", null, null);
		mv.visitCode();
		mv.visitMaxs(4, 0);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getApplication", "Landroid/app/Application;", new int[] { 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "Lmy/android/mouse/TriceratopsApplication;");
		mv.visitFieldInsn(INSN_IGET_BOOLEAN, "Lmy/android/mouse/TriceratopsApplication;", "clickValidate", "Z", 0, 0);
		Label l0 = new Label();
		mv.visitJumpInsn(INSN_IF_NEZ, l0, 0, 0);
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lmy/android/mouse/AndroidMouseActivity;", "getApplication", "Landroid/app/Application;", new int[] { 2 });
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "Lmy/android/mouse/TriceratopsApplication;");
		mv.visitFieldInsn(INSN_IGET_BOOLEAN, "Lmy/android/mouse/TriceratopsApplication;", "clickValidate", "Z", 0, 0);
		Label l1 = new Label();
		mv.visitJumpInsn(INSN_IF_EQZ, l1, 0, 0);
		mv.visitStringInsn(INSN_CONST_STRING, 0, "rightclick");
		mv.visitStringInsn(INSN_CONST_STRING, 1, "rightclick");
		mv.visitMethodInsn(INSN_INVOKE_STATIC, "Landroid/util/Log;", "i", "ILjava/lang/String;Ljava/lang/String;", new int[] { 0, 1 });
		mv.visitJumpInsn(INSN_GOTO, l1, 0, 0);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpBuildConfig2(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/BuildConfig;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/BuildConfig;", null, "Ljava/lang/Object;", null);
	cv.visitSource("BuildConfig.java", null);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "DEBUG", "Z", null, true);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$attr2(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$attr;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$attr;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mouse/R$attr;", "Lmy/android/mouse/R;", "attr", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$drawable2(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$drawable;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$drawable;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mouse/R$drawable;", "Lmy/android/mouse/R;", "drawable", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "ic_launcher", "I", null, 2130837504);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$id2(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$id;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$id;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mouse/R$id;", "Lmy/android/mouse/R;", "id", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "leftClick", "I", null, 2131034112);
		fv.visitEnd();
	}
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "rightClick", "I", null, 2131034113);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$layout2(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$layout;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$layout;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mouse/R$layout;", "Lmy/android/mouse/R;", "layout", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "main", "I", null, 2130903040);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR$string2(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$string;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R$string;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mouse/R$string;", "Lmy/android/mouse/R;", "string", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "app_name", "I", null, 2130968577);
		fv.visitEnd();
	}
	{
		fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "hello", "I", null, 2130968576);
		fv.visitEnd();
	}
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpR2(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R;", null, "Ljava/lang/Object;", null);
	cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lmy/android/mouse/R;", null, "Ljava/lang/Object;", null);
	cv.visitSource("R.java", null);
	cv.visitInnerClass("Lmy/android/mouse/R$attr;", "Lmy/android/mouse/R;", "attr", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mouse/R$drawable;", "Lmy/android/mouse/R;", "drawable", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mouse/R$id;", "Lmy/android/mouse/R;", "id", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mouse/R$layout;", "Lmy/android/mouse/R;", "layout", ACC_PUBLIC + ACC_FINAL);
	cv.visitInnerClass("Lmy/android/mouse/R$string;", "Lmy/android/mouse/R;", "string", ACC_PUBLIC + ACC_FINAL);
	{
		mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
		mv.visitCode();
		mv.visitMaxs(1, 0);
		mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
		mv.visitInsn(INSN_RETURN_VOID);
		mv.visitEnd();
	}
	cv.visitEnd();
}

public static void dumpTriceratopsApplication(ApplicationWriter aw) {
	ClassVisitor cv;
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	cv = aw.visitClass(ACC_PUBLIC, "Lmy/android/mouse/TriceratopsApplication;", null, "Landroid/app/Application;", null);
	cv.visit(0, ACC_PUBLIC, "Lmy/android/mouse/TriceratopsApplication;", null, "Landroid/app/Application;", null);
	cv.visitSource("TriceratopsApplication.java", null);
	{
		fv = cv.visitField(ACC_PUBLIC, "clickValidate", "Z", null, null);
		fv.visitEnd();
	}
	{
		fv = cv.visitField(ACC_PUBLIC, "dangCapsLock", "I", null, null);
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
}

}

