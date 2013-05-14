package sdl.ist.osaka_u.newmasu.cfg.node;

import java.util.LinkedList;

import sdl.ist.osaka_u.newmasu.cfg.CFG;
import sdl.ist.osaka_u.newmasu.cfg.CFGUtility;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;

public class CFGForeachControlNode extends CFGControlNode {

	CFGForeachControlNode(final ForeachConditionInfo condition) {
		super(condition);
	}

	@Override
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		if (null == nodeFactory) {
			throw new IllegalArgumentException();
		}

		final ExpressionInfo iteratorExpression = this.getDissolvingTarget();
		final ExpressionInfo target = (ExpressionInfo) iteratorExpression
				.copy();

		// condition�������̕K�v���Ȃ��ꍇ�͉��������ɔ�����
		if (!CFGUtility.isDissolved(target)) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final ForeachConditionInfo condition = (ForeachConditionInfo) this
				.getCore();
		final ConditionalBlockInfo ownerForeachBlock = (ConditionalBlockInfo) condition
				.getOwnerExecutableElement();
		final LocalSpaceInfo outerUnit = ownerForeachBlock.getOwnerSpace();
		final int fromLine = target.getFromLine();
		final int fromColumn = target.getFromColumn();
		final int toLine = target.getToLine();
		final int toColumn = target.getToColumn();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(condition);
		this.remove();

		final VariableDeclarationStatementInfo newStatement = this
				.makeVariableDeclarationStatement(outerUnit, target);
		final ExpressionInfo newIteratorExpression = LocalVariableUsageInfo
				.getInstance(newStatement.getDeclaredLocalVariable(), true,
						false, ownerForeachBlock.getOuterCallableUnit(),
						fromLine, fromColumn, toLine, toColumn);
		final ForeachConditionInfo newCondition = this.makeNewElement(
				ownerForeachBlock, newIteratorExpression);
		newCondition.setOwnerConditionalBlock(ownerForeachBlock);
		newCondition.setOwnerExecutableElement(ownerForeachBlock);
		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		dissolvedNodeList.add(nodeFactory.makeNormalNode(newStatement));
		dissolvedNodeList.add(nodeFactory.makeControlNode(newCondition));

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(condition, newCFG.getAllNodes());

		return newCFG;
	}

	@Override
	ExpressionInfo getDissolvingTarget() {
		final ForeachConditionInfo condition = (ForeachConditionInfo) this
				.getCore();
		return condition.getIteratorExpression();
	}

	/**
	 * �^����ꂽ��̏���p���āC�m�[�h�̊j�ƂȂ�v���O�����v�f�𐶐�����
	 */
	@Override
	ForeachConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ForeachConditionInfo originalCondition = (ForeachConditionInfo) this
				.getCore();
		final VariableDeclarationStatementInfo iteratorVariable = (VariableDeclarationStatementInfo) originalCondition
				.getIteratorVariable().copy();
		final CallableUnitInfo ownerMethod = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		return new ForeachConditionInfo(ownerMethod, fromLine, fromColumn,
				toLine, toColumn, iteratorVariable, requiredExpressions[0]);
	}

	/**
	 * �^����ꂽ��̏���p���āC�m�[�h�̊j�ƂȂ�v���O�����v�f�𐶐�����
	 */
	@Override
	ForeachConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ForeachConditionInfo originalCondition = (ForeachConditionInfo) this
				.getCore();
		final int fromLine = originalCondition.getFromLine();
		final int fromColumn = originalCondition.getFromLine();
		final int toLine = originalCondition.getToLine();
		final int toColumn = originalCondition.getToColumn();

		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpressions);
	}
}
