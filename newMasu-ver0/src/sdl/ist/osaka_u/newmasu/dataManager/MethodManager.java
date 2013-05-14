package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;

import sdl.ist.osaka_u.newmasu.util.DualMultiMap;

public class MethodManager {

	final private static DualMultiMap<ASTNode, IMethodBinding> rel = new DualMultiMap<>();

	// // 呼び出し元→呼び出し先
	// final private static MultiHashMap<ASTNode, IMethodBinding> calleeToCaller
	// = new MultiHashMap<>();
	// public static final MultiHashMap<ASTNode, IMethodBinding>
	// getCalleetocaller() {
	// return calleeToCaller;
	// }
	//
	// // 呼び出し先→呼び出し元
	// final private static MultiHashMap<IMethodBinding, ASTNode> callerToCaller
	// = new MultiHashMap<>();
	// public static final MultiHashMap<IMethodBinding, ASTNode>
	// getCallertocallee() {
	// return callerToCaller;
	// }
	//
	// public static void addRelation(final MethodInvocation node, final
	// IMethodBinding bind){
	// calleeToCaller.put(node, bind);
	// callerToCaller.put(bind, node);
	// }

	public static final DualMultiMap<ASTNode, IMethodBinding> getRel() {
		return rel;
	}

	public static final List<ASTNode> getCalleeNode(IBinding binding) {

		List<ASTNode> list = new ArrayList<ASTNode>();
		Collection<ASTNode> col = getRel().getCalleeMap()
				.getCollection(binding);

		if (col != null) {
			for (ASTNode node : col) {
				ASTNode ast = node.getParent();
				NODE_RESOLVE: while (true) {
					switch (ast.getNodeType()) {
					case ASTNode.METHOD_DECLARATION:
					case ASTNode.FIELD_DECLARATION:
					case ASTNode.INITIALIZER:
					case ASTNode.ENUM_DECLARATION:
						list.add(ast);
						break NODE_RESOLVE;
					}
					if (ast.getParent() != null) {
						ast = ast.getParent();
					} else {
						System.err.println("can't resolve node");
						break;
					}
				}
			}
		}

		return list;
	}

	// インスタンスの生成を防ぐ
	private MethodManager() {
	}

}