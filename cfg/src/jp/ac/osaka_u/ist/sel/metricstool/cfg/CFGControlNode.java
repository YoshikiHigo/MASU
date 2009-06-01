package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * CFG�̐���m�[�h��\���N���X
 * @author t-miyake, higo
 *
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

    /**
     * ���̃m�[�h��True�t�H���[�h�m�[�h�̃Z�b�g
     */
    private final Set<CFGNode<? extends ExecutableElementInfo>> trueForwardNodes;

    /**
     * ���̃m�[�h��False�t�H���[�h�m�[�h�̃Z�b�g
     */
    private final Set<CFGNode<? extends ExecutableElementInfo>> falseForwardNodes;

    /**
     * ��������m�[�h�ɑΉ����鐧�䕶��^���ď�����
     * @param controlStatement ��������m�[�h�ɑΉ����鐧�䕶
     */
    CFGControlNode(final ConditionInfo condition) {
        super(condition);
        this.trueForwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        this.falseForwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
    }

    /**
     * ��������True�̏ꍇ�̃t�H���[�h�m�[�h��ǉ�
     * 
     * @param forwardNode ��������True�̏ꍇ�̃t�H���[�h�m�[�h
     */
    void addTrueForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        this.trueForwardNodes.add(forwardNode);
        forwardNode.backwardNodes.add(this);
    }

    /**
     * ��������False�̏ꍇ�̃t�H���[�h�m�[�h��ǉ�
     * 
     * @param forwardNode ��������False�̏ꍇ�̃t�H���[�h�m�[�h
     */
    void addFalseForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        this.falseForwardNodes.add(forwardNode);
        forwardNode.backwardNodes.add(this);
    }

    /**
     * �ʏ�̃t�H���[�h�m�[�h�Ƃ��ēo�^����ꍇ��False�t�H���[�h
     * 
     */
    @Override
    void addForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {
        this.addFalseForwardNode(forwardNode);
    }

    /**
     * �K�v�̂Ȃ��m�[�h�̏ꍇ�͍폜
     */
    @Override
    protected void removeIfUnnecessarily() {
        final Object core = this.getCore();
        if (core instanceof EmptyExpressionInfo) {
            for (final CFGNode<?> backwardNode : this.getBackwardNodes()) {
                backwardNode.forwardNodes.remove(this);
                backwardNode.forwardNodes.addAll(this.getTrueForwardNodes());
            }
            for (final CFGNode<?> forwardNode : this.getForwardNodes()) {
                forwardNode.backwardNodes.remove(this);
                forwardNode.backwardNodes.addAll(this.getBackwardNodes());
            }
        }
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g���擾
     * @return ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
     */
    @Override
    public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
        final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        forwardNodes.addAll(this.trueForwardNodes);
        forwardNodes.addAll(this.falseForwardNodes);
        return Collections.unmodifiableSet(forwardNodes);
    }

    /**
     * ���̃m�[�h��True�t�H���[�h�m�[�h�̃Z�b�g���擾
     * @return ���̃m�[�h��True�t�H���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getTrueForwardNodes() {
        return Collections.unmodifiableSet(this.trueForwardNodes);
    }

    /**
     * ���̃m�[�h��False�t�H���[�h�m�[�h�̃Z�b�g���擾
     * @return ���̃m�[�h��False�t�H���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getFalseForwardNodes() {
        return Collections.unmodifiableSet(this.falseForwardNodes);
    }
}
