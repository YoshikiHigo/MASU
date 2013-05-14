package sdl.ist.osaka_u.newmasu.cfg.node;

import java.util.LinkedList;

import sdl.ist.osaka_u.newmasu.cfg.CFG;
import sdl.ist.osaka_u.newmasu.cfg.CFGUtility;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;

/**
 * assert����\��CFG�m�[�h
 * 
 * @author higo
 * 
 */
public class CFGAssertStatementNode extends
		CFGStatementNode<AssertStatementInfo> {

	CFGAssertStatementNode(final AssertStatementInfo statement) {
		super(statement);
	}

	/**
	 * assert���𕪉�����
	 */
	@Override
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		if (null == nodeFactory) {
			throw new IllegalArgumentException();
		}

		final AssertStatementInfo statement = this.getCore();
		final ExpressionInfo target = (ExpressionInfo) this
				.getDissolvingTarget().copy();

		// assert�̔��蕔���������̕K�v���Ȃ��ꍇ�͉��������ɔ�����
		if (!CFGUtility.isDissolved(target)) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(statement);
		this.remove();

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		this.makeDissolvedNode(target, nodeFactory, dissolvedNodeList,
				dissolvedVariableUsageList);
		final AssertStatementInfo newStatement = this.makeNewElement(
				ownerSpace, dissolvedVariableUsageList.getFirst());
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(statement, newCFG.getAllNodes());

		return newCFG;
	}

	@Override
	ExpressionInfo getDissolvingTarget() {
		final AssertStatementInfo statement = this.getCore();
		return statement.getAssertedExpression();
	}

	@Override
	AssertStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final AssertStatementInfo statement = this.getCore();
		final ExpressionInfo messageExpression = statement
				.getMessageExpression();

		final AssertStatementInfo newStatement = new AssertStatementInfo(
				ownerSpace, requiredExpressions[0], messageExpression,
				fromLine, fromColumn, toLine, toColumn);
		return newStatement;

	}

	@Override
	AssertStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final AssertStatementInfo statement = this.getCore();
		final int fromLine = statement.getFromLine();
		final int fromColumn = statement.getFromColumn();
		final int toLine = statement.getToLine();
		final int toColumn = statement.getToColumn();
		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpressions);
	}
}
