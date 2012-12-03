package instrumentation;

import instrumentation.DFS.PrintDeclarations;
import instrumentation.DFS.StatementFunction;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Triceratops {

	

	public static void instrumentFile(File in,File out,DFS.StatementFunction stmtfunc)
	{
		CompilationUnit cu=null;
		try {
			cu=JavaParser.parse(in);
			
		} catch (ParseException | IOException e) {
			System.err.println("Could not parse file "+in.getName());
		}
		
		new MethodVisitor(stmtfunc).visit(cu,null);
		
		FileWriter writer=null;
		try {
			writer = new FileWriter(out);
			writer.write(cu.toString());
			writer.close();
		} catch (IOException e) {
			System.out.println("Unable to write to file "+out.getName());
		} 
		
	}

    private static class MethodVisitor extends VoidVisitorAdapter {
    	private StatementFunction stmtfunc;
		public MethodVisitor(DFS.StatementFunction stmtfunc) {
			super();
    		this.stmtfunc=stmtfunc;
		}
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            BlockStmt body = n.getBody();            
            DFS dfs = new DFS();
            Statement newbody =  dfs.DepthFirstTraversal(body, stmtfunc);
            n.setBody((BlockStmt)newbody);
        }
        
    }
    
    public class preCondition implements DFS.StatementFunction
    {
    	List<String> restricedFunctions;
    	List<String> validateFunctions;
		@Override
		public Statement function(Statement s) {
			if(s.getClass()==ExpressionStmt.class)
			{
				Expression e = ((ExpressionStmt)s).getExpression();
				if(e.getClass() == MethodCallExpr.class)
				{
					MethodCallExpr mce = (MethodCallExpr)e;
					if(restricedFunctions.contains(mce.getName()))
					{
						//Encapsulate in an if statement
					}
					else if(validateFunctions.contains(mce.getName()))
					{
						//Set flag to true
					}
					
				}
			}
			
			//If none of the above is satisfied
			return s;
			
		}
    	
    }
}
