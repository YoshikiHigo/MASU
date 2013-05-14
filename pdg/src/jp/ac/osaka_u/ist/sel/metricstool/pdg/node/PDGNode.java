package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import sdl.ist.osaka_u.newmasu.cfg.CFGUtility;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGControlNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGNormalNode;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGCallDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGReturnDependenceEdge;

/**
 * PDG���\������m�[�h��\���N���X
 * 
 * @author t-miyake, higo
 * 
 * @param <T>
 *            �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class PDGNode<T extends CFGNode<? extends ExecutableElementInfo>>
		implements Comparable<PDGNode<?>> {

	/**
	 * CFG�m�[�h����PDG�m�[�h�𐶐����郁�\�b�h
	 * 
	 * @param cfgNode
	 * @return
	 */
	public static PDGNode<?> generate(final CFGNode<?> cfgNode) {

		final IPDGNodeFactory pdgNodeFactory = new DefaultPDGNodeFactory();
		if (cfgNode instanceof CFGControlNode) {
			return pdgNodeFactory.makeControlNode((CFGControlNode) cfgNode);
		} else if (cfgNode instanceof CFGNormalNode<?>) {
			return pdgNodeFactory.makeNormalNode((CFGNormalNode<?>) cfgNode);
		} else {
			assert false : "cfgNode is incorrect type.";
			return null;
		}
	}

	/**
	 * �t�H���[�h�G�b�W�i���̃m�[�h����̈ˑ��Ӂj
	 */
	private final SortedSet<PDGEdge> forwardEdges;

	/**
	 * �o�b�N���[�h�G�b�W�i���̃m�[�h�ւ̈ˑ��Ӂj
	 */
	private final SortedSet<PDGEdge> backwardEdges;

	/**
	 * �m�[�h�̊j�ƂȂ���
	 */
	protected final T cfgNode;

	protected final String text;

	public final long id;

	private static final AtomicLong MAKE_INDEX = new AtomicLong(0);

	/**
	 * �m�[�h�̊j�ƂȂ����^���ď���
	 * 
	 * @param core
	 *            �m�[�h�̊j�ƂȂ���
	 */
	protected PDGNode(final T node) {

		if (null == node) {
			throw new IllegalArgumentException();
		}

		this.cfgNode = node;
		this.text = node.getCore().getText() + " <"
				+ node.getCore().getFromLine() + ">";
		this.forwardEdges = Collections
				.synchronizedSortedSet(new TreeSet<PDGEdge>());
		this.backwardEdges = Collections
				.synchronizedSortedSet(new TreeSet<PDGEdge>());
		this.id = MAKE_INDEX.getAndIncrement();
	}

	/**
	 * ���̃m�[�h�ɂāC�ύX�܂��͒�`�����ϐ���Set
	 * 
	 * @return
	 */
	public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
		final SortedSet<VariableInfo<?>> definedVariables = new TreeSet<VariableInfo<?>>();
		definedVariables.addAll(VariableUsageInfo
				.getUsedVariables(VariableUsageInfo.getAssignments(this
						.getCore().getVariableUsages())));
		return definedVariables;
	}

	/**
	 * ���̃m�[�h�ɂāC�Q�Ƃ���Ă���ϐ���Set
	 * 
	 * @return
	 */
	public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
		final SortedSet<VariableInfo<?>> referencedVariables = new TreeSet<VariableInfo<?>>();
		referencedVariables.addAll(VariableUsageInfo
				.getUsedVariables(VariableUsageInfo.getReferencees(this
						.getCore().getVariableUsages())));
		return referencedVariables;
	}

	/**
	 * ��ŗ^����ꂽ�ϐ������̃m�[�h�Œ�`����Ă��邩�ǂ�����Ԃ�
	 * 
	 * @param variable
	 * @return
	 */
	public final boolean isDefine(
			final VariableInfo<? extends UnitInfo> variable) {
		return this.getDefinedVariables().contains(variable);
	}

	/**
	 * ��ŗ^����ꂽ�ϐ������̃m�[�h�ŎQ�Ƃ���Ă��邩��Ԃ�
	 * 
	 * @param variable
	 * @return
	 */
	public final boolean isReferenace(
			final VariableInfo<? extends UnitInfo> variable) {
		return this.getReferencedVariables().contains(variable);
	}

	/**
	 * ���̃m�[�h�̃t�H���[�h�G�b�W��ǉ�
	 * 
	 * @param forwardEdge
	 *            ���̃m�[�h�̃t�H���[�h�G�b�W
	 */
	public final boolean addForwardEdge(final PDGEdge forwardEdge) {
		if (null == forwardEdge) {
			throw new IllegalArgumentException("forwardNode is null.");
		}

		if (!forwardEdge.getFromNode().equals(this)) {
			throw new IllegalArgumentException();
		}

		return this.forwardEdges.add(forwardEdge);
	}

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�G�b�W��ǉ�
	 * 
	 * @param backwardEdge
	 */
	public final boolean addBackwardEdge(final PDGEdge backwardEdge) {
		if (null == backwardEdge) {
			throw new IllegalArgumentException("backwardEdge is null.");
		}

		if (!(backwardEdge.getToNode().equals(this))) {
			throw new IllegalArgumentException();
		}

		return this.backwardEdges.add(backwardEdge);
	}

	final public void removeBackwardEdge(final PDGEdge backwardEdge) {
		this.backwardEdges.remove(backwardEdge);
	}

	final public void removeForwardEdge(final PDGEdge forwardEdge) {
		this.forwardEdges.remove(forwardEdge);
	}

	/**
	 * ���̃m�[�h����̃f�[�^�ˑ��ӂ�ǉ�
	 * 
	 * @param dependingNode
	 */
	public boolean addDataDependingNode(final PDGNode<?> dependingNode,
			final VariableInfo<?> data) {

		if (null == dependingNode) {
			throw new IllegalArgumentException();
		}

		final PDGDataDependenceEdge dataEdge = new PDGDataDependenceEdge(this,
				dependingNode, data);
		boolean added = this.addForwardEdge(dataEdge);
		added &= dependingNode.addBackwardEdge(dataEdge);
		return added;
	}

	public boolean addExecutionDependingNode(final PDGNode<?> dependingNode) {

		if (null == dependingNode) {
			throw new IllegalArgumentException();
		}

		final PDGExecutionDependenceEdge executionEdge = new PDGExecutionDependenceEdge(
				this, dependingNode);
		boolean added = this.addForwardEdge(executionEdge);
		added &= dependingNode.addBackwardEdge(executionEdge);
		return added;
	}

	public boolean addCallDependingNode(final PDGNode<?> dependingNode,
			final CallInfo<?> call) {

		if (null == dependingNode) {
			throw new IllegalArgumentException();
		}

		final PDGCallDependenceEdge callEdge = new PDGCallDependenceEdge(this,
				dependingNode, call);
		boolean added = this.addForwardEdge(callEdge);
		added &= dependingNode.addBackwardEdge(callEdge);
		return added;
	}

	public boolean addReturnDependingNode(final PDGNode<?> dependingNode) {

		if (null == dependingNode) {
			throw new IllegalArgumentException();
		}

		final PDGReturnDependenceEdge returnEdge = new PDGReturnDependenceEdge(
				this, dependingNode);
		boolean added = this.addForwardEdge(returnEdge);
		added &= dependingNode.addBackwardEdge(returnEdge);
		return added;
	}

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�G�b�W���擾
	 * 
	 * @return ���̃m�[�h�̃o�b�N���[�h�G�b�W
	 */
	public final SortedSet<PDGEdge> getBackwardEdges() {
		return Collections.unmodifiableSortedSet(this.backwardEdges);
	}

	/**
	 * ���̃m�[�h�̃t�H���[�h�G�b�W���擾
	 * 
	 * @return ���̃m�[�h�̃t�H���[�h�G�b�W
	 */
	public final SortedSet<PDGEdge> getForwardEdges() {
		return Collections.unmodifiableSortedSet(this.forwardEdges);
	}

	/**
	 * ���̃m�[�h���ɂ����ČĂяo����Ă��郁�\�b�h���I�u�W�F�N�g�̓��e��ς��Ă��邩�ǂ�����Ԃ�
	 * 
	 * @return
	 */
	public final boolean isStateChanged() {

		for (final CallInfo<?> call : this.getCore().getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				if (CFGUtility.stateChange(methodCall.getCallee())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public int compareTo(final PDGNode<?> node) {

		if (null == node) {
			throw new IllegalArgumentException();
		}

		if (this.id < node.id) {
			return 1;
		} else if (this.id > node.id) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * ���̃m�[�h�̌��ɂȂ����v���O�����v�f
	 * 
	 * @return ���̃m�[�h�̌��ɂȂ����v���O�����v�f
	 */
	public final ExecutableElementInfo getCore() {
		return this.getCFGNode().getCore();
	}

	/**
	 * ����PDG�m�[�h�̌��ƂȂ���CFG�m�[�h
	 * 
	 * @return
	 */
	public final T getCFGNode() {
		return this.cfgNode;
	}

	public final String getText() {
		return this.text;
	}
}
