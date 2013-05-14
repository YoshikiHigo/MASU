package sdl.ist.osaka_u.newmasu.cfg.node;

import java.util.List;
import java.util.Set;

import sdl.ist.osaka_u.newmasu.cfg.edge.CFGControlEdge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;

/**
 * case�G���g����\��CFG�m�[�h
 * 
 * @author higo
 * 
 */
public class CFGCaseEntryNode extends CFGNormalNode<CaseEntryInfo> {

	CFGCaseEntryNode(final CaseEntryInfo caseEntry) {
		super(caseEntry);
	}

	@Override
	public boolean optimize() {

		final Set<CFGNode<?>> backwardNodes = this.getBackwardNodes();
		final Set<CFGNode<?>> forwardNodes = this.getForwardNodes();

		this.remove();

		// �o�b�N���[�h�m�[�h�Q�ƃt�H���[�h�m�[�h�Q���Ȃ�
		for (final CFGNode<?> backwardNode : backwardNodes) {
			for (final CFGNode<?> forwardNode : forwardNodes) {
				final CFGControlEdge edge = new CFGControlEdge(backwardNode,
						forwardNode, true);
				backwardNode.addForwardEdge(edge);
				forwardNode.addBackwardEdge(edge);
			}
		}

		return true;
	}

	@Override
	final ExpressionInfo getDissolvingTarget() {
		return null;
	}

	@Override
	CaseEntryInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {
		return null;
	}

	@Override
	CaseEntryInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {
		return null;
	}
}
