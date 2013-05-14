package sdl.ist.osaka_u.newmasu.util;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public class NodeFinder {

	private NodeFinder() {
	}

	public static final ASTNode getDeclaringNode(ASTNode node){

		if(node == null){
			return null;
		}

		ASTNode ast = node;
		while (true) {
			switch (ast.getNodeType()) {
			case ASTNode.ASSERT_STATEMENT:
			case ASTNode.BREAK_STATEMENT:
			case ASTNode.CONSTRUCTOR_INVOCATION:
			case ASTNode.CONTINUE_STATEMENT:
			case ASTNode.DO_STATEMENT:
			case ASTNode.EMPTY_STATEMENT:
			case ASTNode.ENHANCED_FOR_STATEMENT:
			case ASTNode.EXPRESSION_STATEMENT:
			case ASTNode.FOR_STATEMENT:
			case ASTNode.IF_STATEMENT:
			case ASTNode.LABELED_STATEMENT:
			case ASTNode.RETURN_STATEMENT:
			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
			case ASTNode.SWITCH_CASE:
			case ASTNode.SWITCH_STATEMENT:
			case ASTNode.SYNCHRONIZED_STATEMENT:
			case ASTNode.THROW_STATEMENT:
			case ASTNode.TRY_STATEMENT:
			case ASTNode.TYPE_DECLARATION_STATEMENT:
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
			case ASTNode.WHILE_STATEMENT:

			case ASTNode.METHOD_DECLARATION:
			case ASTNode.FIELD_DECLARATION:
			case ASTNode.INITIALIZER:
			case ASTNode.ENUM_CONSTANT_DECLARATION:

			case ASTNode.ENUM_DECLARATION:
			case ASTNode.TYPE_DECLARATION:
				return ast;

			case ASTNode.PACKAGE_DECLARATION:
			case ASTNode.IMPORT_DECLARATION:
				return null;
			}

			if (ast.getParent() != null) {
				ast = ast.getParent();
			} else {
				System.err.println("Error Error Error  can't find declared node");
				System.err.println(node);
				return null;
			}
		}

	}

	public static final AbstractTypeDeclaration getTypeNode(ASTNode node) {

		ASTNode ast = node;
		while (true) {
			switch (ast.getNodeType()) {
			case ASTNode.TYPE_DECLARATION:
			case ASTNode.ENUM_DECLARATION:
				return (TypeDeclaration) ast;
			}

			if (ast.getParent() != null) {
				ast = ast.getParent();
			} else {
				System.err.println("can't resolve node");
				return null;
			}
		}
	}

	public static final BodyDeclaration getMethodNode(ASTNode node) {

		ASTNode ast = node;
		while (true) {
			switch (ast.getNodeType()) {
			case ASTNode.METHOD_DECLARATION:
			case ASTNode.FIELD_DECLARATION:
			case ASTNode.INITIALIZER:
			case ASTNode.ENUM_CONSTANT_DECLARATION:
				return (BodyDeclaration) ast;
			}

			if (ast.getParent() != null) {
				ast = ast.getParent();
			} else {
				System.err.println("can't resolve node");
				return null;
			}
		}
	}

//	public static ASTNode getVariableNode(ASTNode node) {
//
//		ASTNode ast = node;
//		while (true) {
//			switch (ast.getNodeType()) {
//			case ASTNode.ASSERT_STATEMENT:
//			case ASTNode.BREAK_STATEMENT:
//			case ASTNode.CONSTRUCTOR_INVOCATION:
//			case ASTNode.CONTINUE_STATEMENT:
//			case ASTNode.DO_STATEMENT:
//			case ASTNode.EMPTY_STATEMENT:
//			case ASTNode.ENHANCED_FOR_STATEMENT:
//			case ASTNode.EXPRESSION_STATEMENT:
//			case ASTNode.FOR_STATEMENT:
//			case ASTNode.IF_STATEMENT:
//			case ASTNode.LABELED_STATEMENT:
//			case ASTNode.RETURN_STATEMENT:
//			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
//			case ASTNode.SWITCH_CASE:
//			case ASTNode.SWITCH_STATEMENT:
//			case ASTNode.SYNCHRONIZED_STATEMENT:
//			case ASTNode.THROW_STATEMENT:
//			case ASTNode.TRY_STATEMENT:
//			case ASTNode.TYPE_DECLARATION_STATEMENT:
//			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
//			case ASTNode.WHILE_STATEMENT:
//			case ASTNode.METHOD_DECLARATION:
//			case ASTNode.FIELD_DECLARATION:
//			case ASTNode.INITIALIZER:
//			case ASTNode.ENUM_DECLARATION:
//				return ast;
//			}
//
//			if (ast.getParent() != null) {
//				ast = ast.getParent();
//			} else {
//				System.err.println("can't resolve node");
//				return null;
//			}
//		}
//	}

	public static final CompilationUnit getCompilationUnitNode(ASTNode node) {

		ASTNode ast = node;
		while (true) {
			switch (ast.getNodeType()) {
			case ASTNode.COMPILATION_UNIT:
				return (CompilationUnit) ast;
			}

			if (ast.getParent() != null) {
				ast = ast.getParent();
			} else {
				System.err.println("can't resolve node");
				return null;
			}
		}
	}

	public static final VariableDeclaration getDeclaration(ASTNode node){

		if(node == null){
			return null;
		}

		ASTNode ast = node;
		while (true) {
			switch (ast.getNodeType()) {
			case ASTNode.SINGLE_VARIABLE_DECLARATION:
			case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
				return (VariableDeclaration) ast;
			}

			if (ast.getParent() != null) {
				ast = ast.getParent();
			} else {
				System.err.println("can't resolve node");
				System.err.println(node);
				return null;
			}
		}
	}

}