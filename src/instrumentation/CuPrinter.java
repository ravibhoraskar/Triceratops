package instrumentation;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.io.FileInputStream;

public class CuPrinter {

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
//        FileInputStream in = new FileInputStream("/Users/ravi/Work/sparta/everything-eclipse/sparta-subjects/AndroidMouse/src/my/android/mouse/AndroidMouseActivity.java");
    	FileInputStream in = new FileInputStream("/Users/ravi/Work/Triceratops/Triceratops/src/instrumentation/DummyClass.java");
        CompilationUnit cu=null;
        try {
            // parse the file
            cu = JavaParser.parse(in);
            //getStatement("if(x==0)");
            
        } finally {
            in.close();
        }
           

     // visit and print the methods names
        new MethodVisitor().visit(cu,null);
    
        // prints the resulting compilation unit to default system output
//        System.out.println(iu.toString());
        
    }
    
    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    
    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
//            System.out.println(n.getName()+" "+n.getReceiverAnnotations()+" ");
            BlockStmt body = n.getBody();
            
            DFS dfs = new DFS();
            List<Statement> s =  dfs.DepthFirstTraversal(body,  new DFS.BlockStmtify());
            System.out.println(s.toString());
                   
        }
        
    }
    

    
    
}