package sdl.ist.osaka_u.newmasu.AST;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.IBinding;

import sdl.ist.osaka_u.newmasu.dataManager.BindingManager;

public class ASTRequestor extends FileASTRequestor {

	@Override
	public void acceptAST(String path, CompilationUnit ast) {

		System.out.println(" ** parsing -  " + path);
		BindingManager.getRel().put(Paths.get(path), ast);
		ast.accept(new ASTVisitorImpl2());

//		List<IBinding> l = new ArrayList<IBinding>();
//
//		ASTVisitorImpl impl =  new ASTVisitorImpl();
//		ast.accept(impl);
//		for(IBinding binding : impl.getList()){
//			if(binding.getKind() == IBinding.VARIABLE ){
//				l.add(binding);
//				System.out.println(binding.toString());
//				ASTNode node = ast.findDeclaringNode(binding);
//				System.out.println(node);
//				for(IBinding b : l){
//					System.out.print(binding.equals(b) + " "+binding.isEqualTo(b) +" : " );
//				}
//				System.out.println();
//			}
//
//			System.out.println("++++++++++++++++++");
//		}





//		ASTVisitorImpl2 visitor = new ASTVisitorImpl2(Paths.get(path));
//		IProblem[] problem = ast.getProblems();
//		if (problem.length != 0) {
//			System.out.println("------ " + problem.length
//					+ " Errors and Warnings ------");
//			for (IProblem p : problem) {
//				if (p.isError())
//					System.out.println(p.toString());
//			}
//			System.out.println("---------------------------");
//		}


//		 System.out.println("----------------------------------");
//		 HashSet<Pair<IMethodBinding, IMethodBinding>> set = MethodManager.getRelations();
//		 for( Pair<IMethodBinding, IMethodBinding> p : set )
//		 {
//		 System.out.println(p);
//		 }
//		 System.out.println("----------------------------------");

//		System.out.println("----------------------------------");
//		HashMap<ITypeBinding, ASTNode> set = ClassManager.getClasses();
//		for (Map.Entry<ITypeBinding, ASTNode> p : set.entrySet()) {
//			System.out.println("-----" + p.getKey());
//		}
//
//		System.out.println("----------------------------------");
//		HashSet<Pair<ITypeBinding, ITypeBinding>> set2 = ClassManager.getInjeritances();
//		for (Pair<ITypeBinding, ITypeBinding> p : set2) {
//			System.out.println("1: " + p.getFirst());
//			System.out.println("2: " + p.getSecond());
//		}
//
//		System.out.println("----------------------------------");
//		Set<Entry<IBinding, Collection<ASTNode>>> set2 = MethodManager.rel.getCalleeMap().entrySet();
//		for (Entry<IBinding, Collection<ASTNode>>  p : set2) {
//			System.out.println("1: " + p.getKey());
//			System.out.println("2: " + p.getValue());
//			for(ASTNode n : p.getValue())
//				if(n!=null && n.getNodeType() == ASTNode.METHOD_DECLARATION )
//					System.out.println(((MethodDeclaration)n).getName());
//		}

	}
}