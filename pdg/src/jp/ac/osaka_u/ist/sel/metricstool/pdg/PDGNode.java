package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * PDG���\������m�[�h��\���N���X
 * 
 * @author t-miyake, higo
 *
 * @param <T> �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class PDGNode<T extends ExecutableElementInfo> {

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
    private final Set<PDGEdge> forwardEdges;

    /**
     * �o�b�N���[�h�G�b�W�i���̃m�[�h�ւ̈ˑ��Ӂj
     */
    private final Set<PDGEdge> backwardEdges;

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
        this.forwardEdges = new HashSet<PDGEdge>();
        this.backwardEdges = new HashSet<PDGEdge>();
    }

    /**
     * ���̃m�[�h�ɂāC�ύX�܂��͒�`�����ϐ���Set
     * 
     * @return
     */
    public abstract Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * ���̃m�[�h�ɂāC�Q�Ƃ���Ă���ϐ���Set
     * 
     * @return
     */
    public abstract Set<VariableInfo<? extends UnitInfo>> getReferencedVariables();

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
    protected final boolean addFowardEdge(final PDGEdge forwardEdge) {
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

    final void removeBackwardEdge(final PDGEdge backwardEdge) {
        this.backwardEdges.remove(backwardEdge);
    }

    final void removeForwardEdge(final PDGEdge forwardEdge) {
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

        boolean added = false;
        final PDGDataDependenceEdge dataFlow = new PDGDataDependenceEdge(this, dependingNode, data);
        added = this.addFowardEdge(dataFlow);
        added &= dependingNode.addBackwardEdge(dataFlow);
        return added;
    }

    /**
     * ���̃m�[�h�̃o�b�N���[�h�G�b�W���擾
     * @return ���̃m�[�h�̃o�b�N���[�h�G�b�W
     */
    public final Set<PDGEdge> getBackwardEdges() {
        return Collections.unmodifiableSet(this.backwardEdges);
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�G�b�W���擾
     * @return ���̃m�[�h�̃t�H���[�h�G�b�W
     */
    public final Set<PDGEdge> getForwardEdges() {
        return Collections.unmodifiableSet(this.forwardEdges);
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
