package instrumentation;

import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.TryStmt;

import java.util.ArrayList;
import java.util.List;

public class DFS {
    
	public interface StatementFunction {
    	Statement function(Statement s);
    }
	
	public interface ExpressionFunction {
		Expression function(Expression e);
	}
    
	public class IdentityExpressionFunction implements ExpressionFunction {
		@Override
		public Expression function(Expression e) {
			return e;
		}
		
	}
	


	public Statement DepthFirstTraversal(Statement statement, StatementFunction stmtfunc, ExpressionFunction expfunc)
    {
    	if (statement == null) return null;
    	else if(statement.getClass()==BlockStmt.class)
    	{
    		BlockStmt bstmt = (BlockStmt) statement;
    		if(bstmt.getStmts()==null) 
    			return new BlockStmt();
    		List<Statement> newStmtList = new ArrayList<Statement>();
    		for(Statement stmt : bstmt.getStmts())
    		{
    			newStmtList.add(DepthFirstTraversal(stmt, stmtfunc,expfunc));
    		}
    		return new BlockStmt(newStmtList);
    	}
    	else if(statement.getClass()==DoStmt.class)
    	{
    		DoStmt dostmt = (DoStmt)statement;
    		Expression newcond = expfunc.function(dostmt.getCondition());
    		Statement newbody = DepthFirstTraversal(dostmt.getBody(), stmtfunc,expfunc);
    		return new DoStmt(newbody, newcond);
    	}
    	else if(statement.getClass()==ForeachStmt.class)
    	{
    		ForeachStmt foreachstmt = (ForeachStmt) statement;
    		Statement newbody = DepthFirstTraversal(foreachstmt.getBody(), stmtfunc,expfunc);
    		Expression newiterable = expfunc.function(foreachstmt.getIterable());
    		VariableDeclarationExpr newvde = (VariableDeclarationExpr)expfunc.function(foreachstmt.getVariable());
    		return new ForeachStmt(newvde, newiterable, newbody);
    	}
    	else if(statement.getClass()==ForStmt.class)
    	{
    		ForStmt forstmt = (ForStmt)statement;
    		List<Expression> newinit = new ArrayList<Expression>();
    		for(Expression e: forstmt.getInit())
    		{
    			newinit.add(expfunc.function(e));
    		}
    		Expression newcompare = expfunc.function(forstmt.getCompare());
    		List<Expression> newupdate = new ArrayList<Expression>();
    		for(Expression e: forstmt.getUpdate())
    		{
    			newupdate.add(expfunc.function(e));
    		}
    		Statement newbody = DepthFirstTraversal(forstmt.getBody(), stmtfunc,expfunc);
    		return new ForStmt(newinit, newcompare, newupdate, newbody);
    	}
    	else if(statement.getClass()==IfStmt.class)
    	{
    		IfStmt ifstmt = (IfStmt) statement;
    		Expression newcond = expfunc.function(ifstmt.getCondition());
    		Statement newThenStmt = DepthFirstTraversal(ifstmt.getThenStmt(), stmtfunc,expfunc);
    		Statement newElseStmt = DepthFirstTraversal(ifstmt.getElseStmt(), stmtfunc,expfunc);
    		return new IfStmt(newcond, newThenStmt, newElseStmt);
    	}
    	else if(statement.getClass()==LabeledStmt.class)
    	{
    		LabeledStmt lstmt = (LabeledStmt) statement;
    		return new LabeledStmt(lstmt.getLabel(), DepthFirstTraversal(lstmt.getStmt(), stmtfunc,expfunc));
    	}
    	else if(statement.getClass()==TryStmt.class)
    	{
    		TryStmt trystmt = (TryStmt) statement;
    		BlockStmt newtryBlock = (BlockStmt) DepthFirstTraversal(trystmt.getTryBlock(),stmtfunc,expfunc);
    		BlockStmt newfinallyblock = (BlockStmt) DepthFirstTraversal(trystmt.getFinallyBlock(),stmtfunc,expfunc);
    		
    		return new TryStmt(newtryBlock, trystmt.getCatchs(), newfinallyblock);
    	}
    	//TODO: Add other type of statements here
    	else
    	{
    		return stmtfunc.function(statement);
    	}
    }
	
	public  Statement DepthFirstTraversal(Statement statement,StatementFunction stmtfunc)
	{
		return DepthFirstTraversal(statement, stmtfunc, new IdentityExpressionFunction());
	}
	
	public static class PrintAll implements DFS.StatementFunction {

		@Override
		public Statement function(Statement s) {

			if(s.getClass()==ExpressionStmt.class)
			{
				ExpressionStmt estmt = (ExpressionStmt)s;
				System.out.print(estmt.getExpression().getClass()+" x ");
			}
			System.out.println(s.getClass()+" x "+s.toString());

			return s;
		}
	}
	
	public static class BlockStmtify implements DFS.StatementFunction{

		@Override
		public Statement function(Statement s) {
			List<Statement> l = new ArrayList<Statement>();
			l.add(s);
			return new BlockStmt(l);
			
		}
		
	}
	
	public static class PrintAssignments implements StatementFunction{

		@Override
		public Statement function(Statement s) {
			if(s.getClass()==ExpressionStmt.class)
			{
				ExpressionStmt exprstmt = (ExpressionStmt)s;
				Expression e = exprstmt.getExpression();
				if(e.getClass()==AssignExpr.class)
				{
					System.out.println(e);
				}
			}
			return s;
		}
		
	}
	
	public static class PrintDeclarations implements StatementFunction{

		@Override
		public Statement function(Statement s) {
			if(s.getClass()==ExpressionStmt.class)
			{
				ExpressionStmt exprstmt = (ExpressionStmt)s;
				Expression e = exprstmt.getExpression();
				if(e.getClass()==VariableDeclarationExpr.class)
				{
					VariableDeclarationExpr vde = (VariableDeclarationExpr)e;
					List<AnnotationExpr> annotations = vde.getAnnotations();
					if(annotations!=null)
					{
						for(AnnotationExpr ae : annotations)
						{
							NameExpr name = ae.getName();
							System.out.print("annotation:("+name.getName()+","+ae.getData()+"),");
						}
					}
					System.out.print(";type=" +vde.getType()+";");
					System.out.println(e);
				}
			}
			return s;
		}
		
	}
}


