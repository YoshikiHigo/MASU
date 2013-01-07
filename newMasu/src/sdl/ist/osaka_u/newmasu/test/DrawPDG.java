package sdl.ist.osaka_u.newmasu.test;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.multimap.MultiHashMap;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import sdl.ist.osaka_u.newmasu.dataManager.ClassManager;
import sdl.ist.osaka_u.newmasu.dataManager.MethodManager;
import sdl.ist.osaka_u.newmasu.dataManager.VariableManager;

public class DrawPDG {

	public DrawPDG() {

		final ASTParser parser = ASTParser.newParser(AST.JLS4);


//		System.out.println("*************************");
//	
//		
		MultiHashMap<ASTNode, IMethodBinding> rel = MethodManager.getRel().getCallerMap();
		for(Entry<ASTNode, Collection<IMethodBinding>> e : rel.entrySet()){
			System.out.println("1: " + e.getKey());
			System.out.println("2: " + e.getValue());
			
			System.out.println();
			ASTNode node = e.getKey();
			while(node.getNodeType()!=ASTNode.METHOD_DECLARATION){
				node = node.getParent();
				if(node == null) break;
			}
			
			if(node!= null){
				IMethodBinding bn = ((MethodDeclaration)node).resolveBinding();
				System.out.println("(((  " + bn);
				IMethodBinding b = ClassManager.getOverrideMethod(bn);
				System.out.println("------- " + b);
			}
			
		}
//		
//
//		System.out.println("*************************");
//	
//		
//		MultiHashMap<IMethodBinding, ASTNode> rel2 = MethodManager.getCallertocallee();
//		for(Entry<IMethodBinding, Collection<ASTNode>> e : rel2.entrySet()){
//			System.out.println("1: " + e.getKey());
//			System.out.println("2: " + e.getValue());
//			
//			System.out.println();
//			IMethodBinding b = ClassManager.getOverrideMethod(e.getKey());
//			System.out.println("------- " + b);
//		}
		
		
//
//		System.out.println("*************************");
//	
//		
//		MultiHashMap<IBinding, ASTNode> rel2 = VariableManager.getCallertocallee();
//		for(Entry<IBinding, Collection<ASTNode>> e : rel2.entrySet()){
//			System.out.println("1: " + e.getKey());
//			System.out.println("2: " + e.getValue());
//		}
//		
//		
//
//		System.out.println("*************************");
//	
//		
//		MultiHashMap<ASTNode, IBinding> rel = VariableManager.getCalleetocaller();
//		for(Entry<ASTNode, Collection<IBinding>> e : rel.entrySet()){
//			System.out.println("1: " + e.getKey());
//			System.out.println("2: " + e.getValue());
//		}
//		
		
//		System.out.println("*************************");
//	
//		
//		MultiHashMap<ITypeBinding, ITypeBinding> rel = ClassManager.getToFrom();
//		for(Entry<ITypeBinding, Collection<ITypeBinding>> e : rel.entrySet()){
//			System.out.println("1: " + e.getKey());
//			System.out.println("2: " + e.getValue());
//		}
//		
	}
}
