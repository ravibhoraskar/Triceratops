package byteinstrumentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import byteinstrumentation.Triceratops.Precondition;

public class RunTriceratops {
    public static void main(String args[]) throws IOException {
        // TODO: Validate these
        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        
        List<Triceratops.Function> validateFunctions = new ArrayList<>();
        validateFunctions.add(new Triceratops.Function("Lmy/android/mouse/AndroidMouseActivity;", true, "leftClick", "VLandroid/view/View;"));
        validateFunctions.add(new Triceratops.Function("Lmy/android/mouse/AndroidMouseActivity;", true, "rightClick", "VLandroid/view/View;"));
        
        List<Triceratops.Function> restrictedFunctions = new ArrayList<>();
        restrictedFunctions.add(new Triceratops.Function("Landroid/util/Log;", false, "i", "ILjava/lang/String;Ljava/lang/String;"));
        
        List<Precondition> conditions = new ArrayList<>();
        conditions.add(new Precondition(restrictedFunctions, validateFunctions, "clickValidate"));
        
        Triceratops.instrumentDex(inFile, outFile, conditions);
    }
}
