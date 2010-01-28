package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

/**
 * ����ˑ��O���t�̃m�[�h��\���N���X
 * 
 * @author t-miyake
 * 
 * @param <T>
 *            �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class CFGNode<T extends ExecutableElementInfo> implements
		Comparable<CFGNode<? extends ExecutableElementInfo>> {

	/**
	 * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
	 */
	protected final Set<CFGEdge> forwardEdges;

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
	 */
	protected final Set<CFGEdge> backwardEdges;

	private final String text;

	/**
	 * ���̃m�[�h�ɑΉ����镶
	 */
	private final T core;

	protected CFGNode(final T core) {

		if (null == core) {
			throw new IllegalArgumentException("core is null");
		}
		this.core = core;
		this.forwardEdges = new HashSet<CFGEdge>();
		this.backwardEdges = new HashSet<CFGEdge>();
		this.text = core.getText() + " <" + core.getFromLine() + ">";
	}

	void addForwardEdge(final CFGEdge forwardEdge) {

		if (null == forwardEdge) {
			throw new IllegalArgumentException();
		}

		if (!this.equals(forwardEdge.getFromNode())) {
			throw new IllegalArgumentException();
		}

		if (this.forwardEdges.add(forwardEdge)) {
			forwardEdge.getToNode().backwardEdges.add(forwardEdge);
		}
	}

	void addBackwardEdge(final CFGEdge backwardEdge) {

		if (null == backwardEdge) {
			throw new IllegalArgumentException();
		}

		if (!this.equals(backwardEdge.getToNode())) {
			throw new IllegalArgumentException();
		}

		if (this.backwardEdges.add(backwardEdge)) {
			backwardEdge.getFromNode().forwardEdges.add(backwardEdge);
		}
	}

	void removeForwardEdges(final Collection<CFGEdge> forwardEdges) {

		if (null == forwardEdges) {
			throw new IllegalArgumentException();
		}

		this.forwardEdges.removeAll(forwardEdges);
	}

	void removeBackwardEdges(final Collection<CFGEdge> backwardEdges) {

		if (null == backwardEdges) {
			throw new IllegalArgumentException();
		}

		this.backwardEdges.removeAll(backwardEdges);
	}

	/**
	 * ���̃m�[�h�ɑΉ����镶�̏����擾
	 * 
	 * @return ���̃m�[�h�ɑΉ����镶
	 */
	public T getCore() {
		return this.core;
	}

	/**
	 * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
	 */
	public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
		final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		for (final CFGEdge forwardEdge : this.getForwardEdges()) {
			forwardNodes.add(forwardEdge.getToNode());
		}
		return Collections.unmodifiableSet(forwardNodes);
	}

	/**
	 * ���̃m�[�h�̃t�H���[�h�G�b�W�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃t�H���[�h�G�b�W�̃Z�b�g
	 */
	public Set<CFGEdge> getForwardEdges() {
		return Collections.unmodifiableSet(this.forwardEdges);
	}

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
	 */
	public Set<CFGNode<? extends ExecutableElementInfo>> getBackwardNodes() {
		final Set<CFGNode<? extends ExecutableElementInfo>> backwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
			backwardNodes.add(backwardEdge.getFromNode());
		}
		return Collections.unmodifiableSet(backwardNodes);
	}

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�G�b�W�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃o�b�N���[�h�G�b�W�̃Z�b�g
	 */
	public Set<CFGEdge> getBackwardEdges() {
		return Collections.unmodifiableSet(this.backwardEdges);
	}

	@Override
	public int compareTo(final CFGNode<? extends ExecutableElementInfo> node) {

		if (null == node) {
			throw new IllegalArgumentException();
		}

		final int methodOrder = this.getCore().getOwnerMethod().compareTo(
				node.getCore().getOwnerMethod());
		if (0 != methodOrder) {
			return methodOrder;
		}

		return this.getCore().compareTo(node.getCore());
	}

	/**
	 * �K�v�̂Ȃ��m�[�h�̏ꍇ�́C���̃��\�b�h���I�[�o�[���C�h���邱�Ƃɂ���āC�폜�����
	 */
	protected void optimize() {
	}

	/**
	 * ���̃m�[�h�������ŗ^����ꂽ���[�J����Ԃ̏o���̃m�[�h�ł��邩�ۂ��Ԃ��D
	 * 
	 * @param localSpace
	 *            ���[�J�����
	 * @return �����̃��[�J����Ԃ̏o���̏ꍇ�Ctrue
	 */
	public boolean isExitNode(final LocalSpaceInfo localSpace) {
		if (this.core instanceof ReturnStatementInfo) {
			return true;
		} else if (this.core instanceof BreakStatementInfo) {
			final BreakStatementInfo breakStatement = (BreakStatementInfo) this.core;
			if (localSpace instanceof BlockInfo
					&& ((BlockInfo) localSpace).isLoopStatement()) {
				if (null == breakStatement.getDestinationLabel()) {
					return true;
				} else {

				}
			}
		}
		return false;
	}

	public final String getText() {
		return this.text;
	}

	/**
	 * ���̃m�[�h�Œ�`�E�ύX����Ă���ϐ���Set��Ԃ�
	 * 
	 * @param countObjectStateChange �Ăяo���ꂽ���\�b�h�Ȃ��ł̃I�u�W�F�N�g�̏�ԕύX
	 * �i�t�B�[���h�ւ̑���Ȃǁj���Q�Ƃ���Ă���ϐ��̕ύX�Ƃ���ꍇ��true�D
	 * 
	 * @return
	 */
	public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables(
			final boolean countObjectStateChange) {
		final Set<VariableInfo<? extends UnitInfo>> assignments = new HashSet<VariableInfo<? extends UnitInfo>>();
		assignments.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
				.getAssignments(this.getCore().getVariableUsages())));

		// �I�u�W�F�N�g�̏�ԕύX���C�ϐ��̕ύX�Ƃ����ꍇ
		if (countObjectStateChange) {
			for (final CallInfo<?> call : this.getCore().getCalls()) {
				if (call instanceof MethodCallInfo) {
					final MethodCallInfo methodCall = (MethodCallInfo) call;
					if (methodCall.getCallee().stateChange()) {
						final ExpressionInfo qualifier = methodCall
								.getQualifierExpression();
						if (qualifier instanceof VariableUsageInfo<?>) {
							assignments.add(((VariableUsageInfo<?>) qualifier)
									.getUsedVariable());
						}
					}
				}
			}
		}

		return assignments;

	}

	/**
	 * ���̃m�[�h�ŗ��p�i�Q�Ɓj����Ă���ϐ���Set��Ԃ�
	 * 
	 * @return
	 */
	public final Set<VariableInfo<? extends UnitInfo>> getUsedVariables() {
		return VariableUsageInfo.getUsedVariables(VariableUsageInfo
				.getReferencees(this.getCore().getVariableUsages()));
	}
}
