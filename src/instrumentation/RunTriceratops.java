package instrumentation;

import instrumentation.Triceratops.globalVariables.Variable;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunTriceratops {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length<2)
		{
			System.err.println("Expected argument: Src-Directory Dest-Directory");
			System.exit(1);
		}
		
		File src=new File(args[0]), dest=new File(args[1]);
		
		if(!dest.exists())
		{
			dest.mkdirs();
		}
		
		FilenameFilter javafilter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("java");
			}
		};
		
		//TODO: Accept a more general directory structure
		for(File in: src.listFiles(javafilter))
		{
			File out;
			try {
				 out = new File(dest.getCanonicalPath()+File.separator+in.getName());
				 
				 List<String> restrictedFunctions = new ArrayList<String>();
				 restrictedFunctions.add("i");
				 List<String> validateFunctions = new ArrayList<String>();
				 validateFunctions.add("leftClick");
				 validateFunctions.add("rightClick");
				 Triceratops.instrumentFile(in, out, new Triceratops.preCondition(restrictedFunctions, validateFunctions,"clickValidate"), new Triceratops.preCondition(restrictedFunctions, validateFunctions,"clickValidate"));
				 
				 
			} catch (IOException e) {
				System.out.println("Destination Invalid. Aborting");
				System.exit(1);
			}
		}
		List<Variable> variableList = new ArrayList<Variable>();
		 variableList.add(new Variable("boolean", "clickValidate"));
		 CompilationUnit application = Triceratops.globalVariables.getApplicationCU(variableList,"my.android.mouse");
		 try
		 {
		 FileWriter out = new FileWriter(dest.getCanonicalFile()+File.separator+"TriceratopsApplication.java");
		 out.write(application.toString());
		 out.close();
		 } catch(IOException e)
		 {
			 System.err.println("Unable to write Application File");
		 }
		 
		
		
		
	}

}
