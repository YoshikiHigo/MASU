package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
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

		final ForeachConditionInfo condition = (ForeachConditionInfo) this
				.getCore();
		final ExpressionInfo iteratorExpression = condition
				.getIteratorExpression();

		// condition�������̕K�v���Ȃ��ꍇ�͉��������ɔ�����
		if (!CFGUtility.isDissolved(iteratorExpression)) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final int conditionFromLine = condition.getFromLine();
		final int conditionFromColumn = condition.getFromColumn();
		final int conditionToLine = condition.getToLine();
		final int conditionToColumn = condition.getToColumn();
		final ConditionalBlockInfo ownerBlock = (ConditionalBlockInfo) condition
				.getOwnerExecutableElement();
		final LocalSpaceInfo outerUnit = ownerBlock.getOwnerSpace();
		final CallableUnitInfo outerMethod = outerUnit instanceof CallableUnitInfo ? (CallableUnitInfo) outerUnit
				: outerUnit.getOuterCallableUnit();
		final int expressionFromLine = iteratorExpression.getFromLine();
		final int expressionFromColumn = iteratorExpression.getFromColumn();
		final int expressionToLine = iteratorExpression.getToLine();
		final int expressionToColumn = iteratorExpression.getToColumn();
		final VariableDeclarationStatementInfo iteratorVariable = condition
				.getIteratorVariable();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(condition);
		this.remove();

		final VariableDeclarationStatementInfo newStatement = this
				.makeVariableDeclarationStatement(outerUnit, iteratorExpression);
		final ExpressionInfo newIteratorExpression = LocalVariableUsageInfo
				.getInstance(newStatement.getDeclaredLocalVariable(), true,
						false, ownerBlock.getOuterCallableUnit(),
						expressionFromLine, expressionFromColumn,
						expressionToLine, expressionToColumn);
		final ForeachConditionInfo newCondition = new ForeachConditionInfo(
				outerMethod, conditionFromLine, conditionFromColumn,
				conditionToLine, conditionToColumn, iteratorVariable,
				newIteratorExpression);
		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		dissolvedNodeList.add(nodeFactory.makeNormalNode(newStatement));
		dissolvedNodeList.add(nodeFactory.makeControlNode(newCondition));

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ownerSpace�Ƃ̒���
		outerUnit.addStatement(newStatement);
		final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(
				ownerBlock, newCondition, conditionFromLine,
				conditionFromColumn, conditionToLine, conditionToColumn);
		ownerBlock.setConditionalClause(newConditionalClause);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		return newCFG;
	}
}
