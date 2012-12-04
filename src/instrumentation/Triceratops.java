package instrumentation;

import instrumentation.DFS.PrintDeclarations;
import instrumentation.DFS.StatementFunction;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BinaryExpr.Operator;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
            List<Statement> newbody =  dfs.DepthFirstTraversal(body, stmtfunc);
            n.setBody(new BlockStmt(newbody));
        }
        
    }
    
    public class preCondition implements DFS.StatementFunction
    {
    	List<String> restricedFunctions;
    	List<String> validateFunctions;
    	String name;
		@Override
		public List<Statement> function(Statement s) {
			if(s.getClass()==ExpressionStmt.class)
			{
				Expression e = ((ExpressionStmt)s).getExpression();
				if(e.getClass() == MethodCallExpr.class)
				{
					MethodCallExpr mce = (MethodCallExpr)e;
					if(restricedFunctions.contains(mce.getName()))
					{
						BinaryExpr cond = new BinaryExpr(globalVariables.getGlobalVar("precondition"), new BooleanLiteralExpr(true), Operator.equals);
						IfStmt toReturn = new IfStmt(cond, s, null);
						return DFS.listify(toReturn);
					}
					else if(validateFunctions.contains(mce.getName()))
					{
						List<Statement> toreturn = new ArrayList<Statement>();
						toreturn.add(s);
						toreturn.add(globalVariables.setGlobalVar("precondition", new BooleanLiteralExpr(true)));
						return toreturn;
					}
					
				}
			}
			
			//If none of the above is satisfied
			return DFS.listify(s);
			
		}
    	
    }
    
    /**
     * Provides an interface to get and set global variables(accessible across activities) in Android  
     */
    public static class globalVariables
    
    {   
    	private static Statement getDummyStmt()
    	{
    		try {
            	FileInputStream in = new FileInputStream("res/DummyClass");
                CompilationUnit cu=null;
                
                // parse the file
                cu = JavaParser.parse(in);
                //getStatement("if(x==0)");
                 List<Statement> body = ((MethodDeclaration) cu.getTypes().get(0).getMembers().get(0)).getBody().getStmts();
                 return body.get(0);
                     
                } catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		return null;
    	}
    	/**
    	 * Returns a Statement that sets the variable called <i>name</i> to the Expression <i>value</i> 
    	 */
    	static Statement setGlobalVar(String name,Expression value){

			ExpressionStmt setStmt = (ExpressionStmt) getDummyStmt();
			
			AssignExpr setExpr = (AssignExpr) setStmt.getExpression();
			setExpr.setValue(value);
			
			FieldAccessExpr target = (FieldAccessExpr) setExpr.getTarget();
			target.setField(name);
			
			System.out.println(target.getClass());
			return setStmt;
    	}
    	
    	/**
    	 * Returns an expression giving the value of the variable <i>name</i>
    	 */
    	static Expression getGlobalVar(String name)
    	{
			ExpressionStmt setStmt = (ExpressionStmt) getDummyStmt();
			AssignExpr setExpr = (AssignExpr) setStmt.getExpression();

			FieldAccessExpr target = (FieldAccessExpr) setExpr.getTarget();

			target.setField(name);
			return target;
    	}
    	
    	/**
    	 * Returns a <i>CompilationUnit</i> corresponding to an Application class, that has getters and setters for
    	 * all the variables in <i>variableList</i>
    	 */
    	static CompilationUnit getApplicationCU(List<Variable> variableList)
    	{
    		
        	FileInputStream in = null;
            
        	CompilationUnit cu=null;
    		try {
                
    			in=new FileInputStream("res/TriceratopsApplication");
                // parse the file
                cu = JavaParser.parse(in);
       
                } catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) cu.getTypes().get(0);
    		
    		
    		List<BodyDeclaration> members = clazz.getMembers();
    		FieldDeclaration varDecl = (FieldDeclaration) members.get(0);
    		
    		List<BodyDeclaration> newmembers = new ArrayList<BodyDeclaration>();
    		for(Variable var:variableList)
    		{
    			newmembers.add(new FieldDeclaration(varDecl.getModifiers(), new ClassOrInterfaceType(var.type), new VariableDeclarator(new VariableDeclaratorId(var.name))));
    		}
    		clazz.setMembers(newmembers);
    		
    		return cu;
    	}
    	
    	static class Variable
    	{
    		public String type;
    		public String name;
    		
    		Variable(String type,String name)
    		{
    			this.type=type;
    			this.name=name;
    		}
    	}
    	
    }
    
	public static void main(String[] args) 
    {
    	globalVariables.setGlobalVar("hey", new BooleanLiteralExpr(true));
//    	System.out.println(globalVariables.getGlobalVar("var"));
		List<globalVariables.Variable> lv = new ArrayList<globalVariables.Variable>();
		lv.add(new globalVariables.Variable("foo", "bar"));
		System.out.println(globalVariables.getApplicationCU(lv));
    }
}
