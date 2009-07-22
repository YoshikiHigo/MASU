package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
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
 * @param <T> �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class PDGNode<T extends ExecutableElementInfo> implements
        Comparable<PDGNode<? extends ExecutableElementInfo>> {

    /**
     * CFG�m�[�h����PDG�m�[�h�𐶐����郁�\�b�h
     * 
     * @param cfgNode
     * @return
     */
    public static PDGNode<?> generate(final CFGNode<?> cfgNode) {

        final ExecutableElementInfo element = cfgNode.getCore();
        if (cfgNode instanceof CFGControlNode) {
            return new PDGControlNode((ConditionInfo) element);

        } else if (cfgNode instanceof CFGNormalNode<?>) {

            if (element instanceof SingleStatementInfo) {
                return new PDGStatementNode((SingleStatementInfo) element);
            } else if (element instanceof ConditionInfo) {
                return new PDGExpressionNode((ConditionInfo) element);
            } else {
                throw new IllegalStateException();
            }

        } else {
            throw new IllegalStateException();
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
    protected T core;

    protected String text;

    /**
     * �m�[�h�̊j�ƂȂ����^���ď�����
     * @param core �m�[�h�̊j�ƂȂ���
     */
    protected PDGNode() {
        this.forwardEdges = new TreeSet<PDGEdge>();
        this.backwardEdges = new TreeSet<PDGEdge>();
    }

    /**
     * ���̃m�[�h�ɂāC�ύX�܂��͒�`�����ϐ���Set
     * 
     * @return
     */
    public abstract SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * ���̃m�[�h�ɂāC�Q�Ƃ���Ă���ϐ���Set
     * 
     * @return
     */
    public abstract SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables();

    /**
     * �����ŗ^����ꂽ�ϐ������̃m�[�h�Œ�`����Ă��邩�ǂ�����Ԃ�
     * 
     * @param variable
     * @return
     */
    public final boolean isDefine(final VariableInfo<? extends UnitInfo> variable) {
        return this.getDefinedVariables().contains(variable);
    }

    /**
     * �����ŗ^����ꂽ�ϐ������̃m�[�h�ŎQ�Ƃ���Ă��邩��Ԃ�
     * 
     * @param variable
     * @return
     */
    public final boolean isReferenace(final VariableInfo<? extends UnitInfo> variable) {
        return this.getReferencedVariables().contains(variable);
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�G�b�W��ǉ�
     * @param forwardEdge ���̃m�[�h�̃t�H���[�h�G�b�W
     */
    protected final boolean addForwardEdge(final PDGEdge forwardEdge) {
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
     * @param backwardEdge
     */
    protected final boolean addBackwardEdge(final PDGEdge backwardEdge) {
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
     * @param dependingNode
     */
    public boolean addDataDependingNode(final PDGNode<?> dependingNode, final VariableInfo<?> data) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGDataDependenceEdge dataEdge = new PDGDataDependenceEdge(this, dependingNode, data);
        boolean added = this.addForwardEdge(dataEdge);
        added &= dependingNode.addBackwardEdge(dataEdge);
        return added;
    }

    public boolean addExecutionDependingNode(final PDGNode<?> dependingNode) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGExecutionDependenceEdge executionEdge = new PDGExecutionDependenceEdge(this,
                dependingNode);
        boolean added = this.addForwardEdge(executionEdge);
        added &= dependingNode.addBackwardEdge(executionEdge);
        return added;
    }

    public boolean addCallDependingNode(final PDGNode<?> dependingNode, final CallInfo call) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGCallDependenceEdge callEdge = new PDGCallDependenceEdge(this, dependingNode, call);
        boolean added = this.addForwardEdge(callEdge);
        added &= dependingNode.addBackwardEdge(callEdge);
        return added;
    }

    public boolean addReturnDependingNode(final PDGNode<?> dependingNode) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGReturnDependenceEdge returnEdge = new PDGReturnDependenceEdge(this, dependingNode);
        boolean added = this.addForwardEdge(returnEdge);
        added &= dependingNode.addBackwardEdge(returnEdge);
        return added;
    }

    /**
     * ���̃m�[�h�̃o�b�N���[�h�G�b�W���擾
     * @return ���̃m�[�h�̃o�b�N���[�h�G�b�W
     */
    public final SortedSet<PDGEdge> getBackwardEdges() {
        return Collections.unmodifiableSortedSet(this.backwardEdges);
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�G�b�W���擾
     * @return ���̃m�[�h�̃t�H���[�h�G�b�W
     */
    public final SortedSet<PDGEdge> getForwardEdges() {
        return Collections.unmodifiableSortedSet(this.forwardEdges);
    }

    @Override
    public int compareTo(final PDGNode<? extends ExecutableElementInfo> node) {

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
     * ���̃m�[�h�̊j�ƂȂ���擾
     * @return ���̃m�[�h�̊j�ƂȂ���
     */
    public final T getCore() {
        return this.core;
    }

    public final String getText() {
        return this.text;
    }
}
