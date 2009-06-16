package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * ����ˑ��O���t�̃m�[�h��\���N���X
 * @author t-miyake
 *
 * @param <T> �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class CFGNode<T extends ExecutableElementInfo> {

    /**
     * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
     */
    protected final Set<CFGNode<?>> forwardNodes;

    /**
     * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
     */
    protected final Set<CFGNode<?>> backwardNodes;

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
        this.forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        this.backwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        this.text = core.getText() + " <" + core.getFromLine() + ">";
    }

    void addForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        if (this.forwardNodes.add(forwardNode)) {
            forwardNode.addBackwardNode(this);
        }
    }

    void addBackwardNode(final CFGNode<? extends ExecutableElementInfo> backwardNode) {

        if (null == backwardNode) {
            throw new IllegalArgumentException();
        }

        if (this.backwardNodes.add(backwardNode)) {
            backwardNode.addForwardNode(this);
        }
    }

    void addForwardNodes(final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes) {

        if (null == forwardNodes) {
            throw new IllegalArgumentException();
        }

        if (this.forwardNodes.addAll(forwardNodes)) {
            for (final CFGNode<? extends ExecutableElementInfo> forwardNode : forwardNodes) {
                forwardNode.addBackwardNode(this);
            }
        }
    }

    void addBackwardNodes(final Set<CFGNode<? extends ExecutableElementInfo>> backwardNodes) {

        if (null == backwardNodes) {
            throw new IllegalArgumentException();
        }

        if (this.backwardNodes.addAll(backwardNodes)) {
            for (final CFGNode<? extends ExecutableElementInfo> backwardNode : backwardNodes) {
                backwardNode.addForwardNode(this);
            }
        }
    }

    void removeForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        if (this.forwardNodes.remove(forwardNode)) {
            this.addForwardNodes(forwardNode.getForwardNodes());
        }
    }

    final void removeBackwardNode(final CFGNode<? extends ExecutableElementInfo> backwardNode) {

        if (null == backwardNode) {
            throw new IllegalArgumentException();
        }

        if (this.backwardNodes.remove(backwardNode)) {
            this.addBackwardNodes(backwardNode.getBackwardNodes());
        }
    }

    /**
     * ���̃m�[�h�ɑΉ����镶�̏����擾
     * @return ���̃m�[�h�ɑΉ����镶
     */
    public T getCore() {
        return this.core;
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g���擾
     * @return ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
        return Collections.unmodifiableSet(this.forwardNodes);
    }

    /**
     * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g���擾
     * @return ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getBackwardNodes() {
        return Collections.unmodifiableSet(this.backwardNodes);
    }

    /**
     * �K�v�̂Ȃ��m�[�h�̏ꍇ�́C���̃��\�b�h���I�[�o�[���C�h���邱�Ƃɂ���āC�폜�����
     */
    protected void optimize() {
    }

    /**
     * ���̃m�[�h�������ŗ^����ꂽ���[�J����Ԃ̏o���̃m�[�h�ł��邩�ۂ��Ԃ��D
     * @param localSpace ���[�J�����
     * @return �����̃��[�J����Ԃ̏o���̏ꍇ�Ctrue
     */
    public boolean isExitNode(final LocalSpaceInfo localSpace) {
        if (this.core instanceof ReturnStatementInfo) {
            return true;
        } else if (this.core instanceof BreakStatementInfo) {
            final BreakStatementInfo breakStatement = (BreakStatementInfo) this.core;
            if (localSpace instanceof BlockInfo && ((BlockInfo) localSpace).isLoopStatement()) {
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
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this.getCore()
                .getVariableUsages()));
    }

    /**
     * ���̃m�[�h�ŗ��p�i�Q�Ɓj����Ă���ϐ���Set��Ԃ�
     * 
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getUsedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getVariableUsages()));
    }
}
