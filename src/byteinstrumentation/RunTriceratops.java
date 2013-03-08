package byteinstrumentation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RunTriceratops {
    public static void main(String args[]) throws IOException {
        // TODO: Validate these
        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        
        TriceratopsPolicy tripolicy = TriceratopsPolicy.readJson(new FileReader("res/AndroidMouseActivity.policy"));
        
        // List<Triceratops.Function> validateFunctions = new ArrayList<>();
        // validateFunctions.add(new Triceratops.Function("Lmy/android/mouse/AndroidMouseActivity;", true, "leftClick", "VLandroid/view/View;"));
        // validateFunctions.add(new Triceratops.Function("Lmy/android/mouse/AndroidMouseActivity;", true, "rightClick", "VLandroid/view/View;"));
        
        // List<Triceratops.Function> restrictedFunctions = new ArrayList<>();
        // restrictedFunctions.add(new Triceratops.Function("Landroid/util/Log;", false, "i", "ILjava/lang/String;Ljava/lang/String;"));
        
        // List<Precondition> conditions = new ArrayList<>();
        // conditions.add(new Precondition(restrictedFunctions, validateFunctions, "clickValidate"));
        
        Triceratops.instrumentDex(inFile, outFile, tripolicy);
        System.out.println("Done!");
    }
}
