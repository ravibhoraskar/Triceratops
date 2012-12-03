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
    	List<Statement> function(Statement s);
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
	


	public List<Statement> DepthFirstTraversal(Statement statement, StatementFunction stmtfunc, ExpressionFunction expfunc)
    {
    	if (statement == null) return null;
    	else if(statement.getClass()==BlockStmt.class)
    	{
    		BlockStmt bstmt = (BlockStmt) statement;
    		if(bstmt.getStmts()==null) 
    		{
    			List<Statement> toReturn = new ArrayList<Statement>();
    			toReturn.add(new BlockStmt());
    			return toReturn;
    		}
    		List<Statement> newStmtList = new ArrayList<Statement>();
    		for(Statement stmt : bstmt.getStmts())
    		{
    			newStmtList.addAll(DepthFirstTraversal(stmt, stmtfunc,expfunc));
    		}

			return listify(new BlockStmt(newStmtList));
    		
    	}
    	else if(statement.getClass()==DoStmt.class)
    	{
    		DoStmt dostmt = (DoStmt)statement;
    		Expression newcond = expfunc.function(dostmt.getCondition());
    		List<Statement> newbody = DepthFirstTraversal(dostmt.getBody(), stmtfunc,expfunc);
    		
			return listify(new DoStmt(new BlockStmt(newbody), newcond));

    	}
    	else if(statement.getClass()==ForeachStmt.class)
    	{
    		ForeachStmt foreachstmt = (ForeachStmt) statement;
    		List<Statement> newbody = DepthFirstTraversal(foreachstmt.getBody(), stmtfunc,expfunc);
    		Expression newiterable = expfunc.function(foreachstmt.getIterable());
    		VariableDeclarationExpr newvde = (VariableDeclarationExpr)expfunc.function(foreachstmt.getVariable());
    		
    		return listify(new ForeachStmt(newvde, newiterable, new BlockStmt(newbody)));
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
    		List<Statement> newbody = DepthFirstTraversal(forstmt.getBody(), stmtfunc,expfunc);
    		
			return listify((new ForStmt(newinit, newcompare, newupdate, new BlockStmt(newbody))));
    	}
    	else if(statement.getClass()==IfStmt.class)
    	{
    		IfStmt ifstmt = (IfStmt) statement;
    		Expression newcond = expfunc.function(ifstmt.getCondition());
    		List<Statement> newThenStmt = DepthFirstTraversal(ifstmt.getThenStmt(), stmtfunc,expfunc);
    		List<Statement> newElseStmt = DepthFirstTraversal(ifstmt.getElseStmt(), stmtfunc,expfunc);
    		
    		
    		return listify(new IfStmt(newcond, new BlockStmt(newThenStmt), new BlockStmt(newElseStmt)));
    	}
    	else if(statement.getClass()==LabeledStmt.class)
    	{
    		LabeledStmt lstmt = (LabeledStmt) statement;
    		List<Statement> dfs = DepthFirstTraversal(lstmt.getStmt(), stmtfunc,expfunc);
    		if(dfs.size()==1)
    			return listify(dfs.get(0));
    		else
    			return listify(new LabeledStmt(lstmt.getLabel(), new BlockStmt(dfs) ));
    	}
    	else if(statement.getClass()==TryStmt.class)
    	{
    		TryStmt trystmt = (TryStmt) statement;
    		List<Statement> newtryBlock =  DepthFirstTraversal(trystmt.getTryBlock(),stmtfunc,expfunc);
    		List<Statement> newfinallyblock = DepthFirstTraversal(trystmt.getFinallyBlock(),stmtfunc,expfunc);
    		
    		return listify(new TryStmt(new BlockStmt(newtryBlock), trystmt.getCatchs(), new BlockStmt(newfinallyblock)));
    	}
    	//TODO: Add other type of statements here
    	else
    	{
    		return stmtfunc.function(statement);
    	}
    }
	
	public static List<Statement> listify(Statement stmt) {
		List<Statement> toreturn = new ArrayList<Statement>();
		toreturn.add(stmt);
		return toreturn;
	}

	public  List<Statement> DepthFirstTraversal(Statement statement,StatementFunction stmtfunc)
	{
		return DepthFirstTraversal(statement, stmtfunc, new IdentityExpressionFunction());
	}
	
	public static class PrintAll implements DFS.StatementFunction {

		@Override
		public List<Statement> function(Statement s) {

			if(s.getClass()==ExpressionStmt.class)
			{
				ExpressionStmt estmt = (ExpressionStmt)s;
				System.out.print(estmt.getExpression().getClass()+" x ");
			}
			System.out.println(s.getClass()+" x "+s.toString());

			return listify(s);
		}
	}
	
	public static class BlockStmtify implements DFS.StatementFunction{

		@Override
		public List<Statement> function(Statement s) {
			List<Statement> l = new ArrayList<Statement>();
			l.add(s);
			return listify(new BlockStmt(l));
			
		}
		
	}
	
	public static class PrintAssignments implements StatementFunction{

		@Override
		public List<Statement> function(Statement s) {
			if(s.getClass()==ExpressionStmt.class)
			{
				ExpressionStmt exprstmt = (ExpressionStmt)s;
				Expression e = exprstmt.getExpression();
				if(e.getClass()==AssignExpr.class)
				{
					System.out.println(e);
				}
			}
			return listify(s);
		}
		
	}
	
	public static class PrintDeclarations implements StatementFunction{

		@Override
		public List<Statement> function(Statement s) {
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
			return listify(s);
		}
		
	}
}


