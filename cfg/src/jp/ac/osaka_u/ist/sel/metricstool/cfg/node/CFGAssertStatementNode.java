package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.Collections;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

public class CFGAssertStatementNode extends
		CFGStatementNode<AssertStatementInfo> {

	CFGAssertStatementNode(final AssertStatementInfo statement) {
		super(statement);
	}

	@Override
	public CFGNode<? extends ExecutableElementInfo> dissolve(
			final ICFGNodeFactory nodeFactory) {

		final AssertStatementInfo statement = this.getCore();
		final ExpressionInfo expression = statement.getAssertedExpression();

		// assert�̔��蕔�����ϐ��g�p�̎��łȂ��ꍇ�͕������s��
		if (CFGUtility.isDissolved(expression)) {

			// ����O�̕�����K�v�ȏ����擾
			final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
			final ExpressionInfo messageExpression = statement
					.getMessageExpression();
			final int fromLine = statement.getFromLine();
			final int fromColumn = statement.getFromColumn();
			final int toLine = statement.getToLine();
			final int toColumn = statement.getToColumn();
			final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
					: ownerSpace.getOuterCallableUnit();

			// �_�~�[�ϐ��̐錾�𐶐�
			final LocalVariableInfo dummyVariable = new LocalVariableInfo(
					Collections.<ModifierInfo> emptySet(),
					getDummyVariableName(), expression.getType(), ownerSpace,
					fromLine, fromColumn - 1, toLine, toColumn - 1);
			final LocalVariableUsageInfo dummyVariableInitialization = LocalVariableUsageInfo
					.getInstance(dummyVariable, false, true, outerCallableUnit,
							fromLine, fromColumn - 1, toLine, toColumn - 1);
			final VariableDeclarationStatementInfo dummyVariableDeclaration = new VariableDeclarationStatementInfo(
					dummyVariableInitialization, expression, fromLine,
					fromColumn - 1, toLine, toColumn - 1);

			// �_�~�[�ϐ��𗘗p����assert���𐶐�
			final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo
					.getInstance(dummyVariable, true, false, outerCallableUnit,
							fromLine, fromColumn, toLine, toColumn);
			final AssertStatementInfo newAssertStatement = new AssertStatementInfo(
					ownerSpace, dummyVariableUsage, messageExpression,
					fromLine, fromColumn, toLine, toColumn);

			// �Â��m�[�h���폜
			nodeFactory.removeNode(statement);
			this.remove();

			// �V��������CFG�m�[�h���쐬���C�t���[�𐶐�
			final CFGNode<?> definitionNode = nodeFactory
					.makeNormalNode(dummyVariableDeclaration);
			final CFGNode<?> referenceNode = nodeFactory
					.makeNormalNode(newAssertStatement);
			final CFGEdge newEdge = new CFGNormalEdge(definitionNode,
					referenceNode);
			definitionNode.addForwardEdge(newEdge);
			referenceNode.addBackwardEdge(newEdge);
			for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
				final CFGNode<?> backwardNode = backwardEdge.getFromNode();
				final CFGEdge newBackwardEdge = backwardEdge
						.replaceToNode(definitionNode);
				backwardNode.addForwardEdge(newBackwardEdge);
			}
			for (final CFGEdge forwardEdge : this.getForwardEdges()) {
				final CFGNode<?> forwardNode = forwardEdge.getToNode();
				final CFGEdge newForwardEdge = forwardEdge
						.replaceFromNode(referenceNode);
				forwardNode.addBackwardEdge(newForwardEdge);
			}

			// ���o����Expression�ɑ΂��Ă͍ċA�I��dissolve�����s
			definitionNode.dissolve(nodeFactory);

			// ���o���s��ꂽassert���̃m�[�h��Ԃ�
			return referenceNode;
		}

		// �������Ȃ��ꍇ�� null��Ԃ�
		else {
			return null;
		}
	}
}
