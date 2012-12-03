package instrumentation;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

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
				 Triceratops.instrumentFile(in, out, new DFS.BlockStmtify());
			} catch (IOException e) {
				System.out.println("Destination Invalid. Aborting");
				System.exit(1);
			}
		}
		
		
		
	}

}
