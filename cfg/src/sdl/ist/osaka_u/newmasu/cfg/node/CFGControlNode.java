package sdl.ist.osaka_u.newmasu.cfg.node;

import java.util.LinkedList;

import sdl.ist.osaka_u.newmasu.cfg.CFG;
import sdl.ist.osaka_u.newmasu.cfg.CFGUtility;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;

/**
 * CFG�̐���m�[�h��\���N���X
 * 
 * @author t-miyake, higo
 * 
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

	CFGControlNode(final ConditionInfo condition) {
		super(condition);
	}

	@Override
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		if (null == nodeFactory) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo condition = this.getDissolvingTarget();
		final ConditionInfo target = (ConditionInfo) condition.copy();
		final ExpressionInfo TargetExpression = (ExpressionInfo) target;

		// condition�������̕K�v���Ȃ��ꍇ�͉��������ɔ�����
		if (!CFGUtility.isDissolved(TargetExpression)) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final ConditionalBlockInfo ownerConditionalBlock = target
				.getOwnerConditionalBlock();
		final LocalSpaceInfo outerUnit = ownerConditionalBlock.getOwnerSpace();
		final int fromLine = TargetExpression.getFromLine();
		final int fromColumn = TargetExpression.getFromColumn();
		final int toLine = TargetExpression.getToLine();
		final int toColumn = TargetExpression.getToColumn();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(condition);
		this.remove();

		final VariableDeclarationStatementInfo newStatement = this
				.makeVariableDeclarationStatement(outerUnit, TargetExpression);
		final ExpressionInfo newCondition = LocalVariableUsageInfo.getInstance(
				newStatement.getDeclaredLocalVariable(), true, false,
				ownerConditionalBlock.getOuterCallableUnit(), toLine,
				toColumn, toLine, toColumn); // �킴��toLine, toColumn�ɂ��Ă���
		newCondition.setOwnerConditionalBlock(ownerConditionalBlock);
		newCondition.setOwnerExecutableElement(ownerConditionalBlock);
		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		dissolvedNodeList.add(nodeFactory.makeNormalNode(newStatement));
		dissolvedNodeList.add(nodeFactory.makeControlNode(newCondition));

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		// target�ł̓_���ȓ_�ɒ���!
		nodeFactory.addDissolvedNodes(condition, newCFG.getAllNodes());

		return newCFG;
	}

	@Override
	ExpressionInfo getDissolvingTarget() {
		final ConditionInfo condition = this.getCore();
		if (condition instanceof VariableDeclarationStatementInfo) {
			final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;
			if (statement.isInitialized()) {
				return statement.getInitializationExpression();
			}
		} else if (condition instanceof ExpressionInfo) {
			return (ExpressionInfo) condition;
		} else {
			throw new IllegalStateException();
		}
		return null;
	}

	/**
	 * �^����ꂽ��̏���p���āC�m�[�h�̊j�ƂȂ�v���O�����v�f�𐶐�����
	 */
	@Override
	ConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo condition = this.getCore();

		if (condition instanceof VariableDeclarationStatementInfo) {

			final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;
			final LocalVariableUsageInfo variableDeclaration = statement
					.getDeclaration();
			final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
					ownerSpace, variableDeclaration, requiredExpressions[0],
					fromLine, fromColumn, toLine, toColumn);
			return newStatement;
		}

		else if (condition instanceof ExpressionInfo) {
			return requiredExpressions[0];
		}

		else {
			throw new IllegalStateException();
		}
	}

	/**
	 * �^����ꂽ��̏���p���āC�m�[�h�̊j�ƂȂ�v���O�����v�f�𐶐�����
	 */
	@Override
	ConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo condition = this.getCore();

		if (condition instanceof VariableDeclarationStatementInfo) {

			final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;

			final int fromLine = statement.getFromLine();
			final int fromColumn = statement.getFromLine();
			final int toLine = statement.getToLine();
			final int toColumn = statement.getToColumn();

			return this.makeNewElement(ownerSpace, fromLine, fromColumn,
					toLine, toColumn, requiredExpressions);
		}

		else if (condition instanceof ExpressionInfo) {
			return requiredExpressions[0];
		}

		else {
			throw new IllegalStateException();
		}
	}
}
