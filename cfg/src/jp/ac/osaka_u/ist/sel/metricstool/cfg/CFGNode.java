package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


/**
 * ����ˑ��O���t�̃m�[�h��\���N���X
 * @author t-miyake
 *
 * @param <T> �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class CFGNode<T extends StatementInfo> {

    /**
     * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
     */
    private final Set<CFGNode<? extends StatementInfo>> forwardNodes;

    /**
     * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
     */
    private final Set<CFGNode<? extends StatementInfo>> backwardNodes;

    protected String text;

    /**
     * ���̃m�[�h�ɑΉ����镶
     */
    private final T statement;

    protected CFGNode(final T statement) {

        if (null == statement) {
            throw new IllegalArgumentException("statement is null");
        }
        this.statement = statement;
        this.forwardNodes = new HashSet<CFGNode<? extends StatementInfo>>();
        this.backwardNodes = new HashSet<CFGNode<? extends StatementInfo>>();
    }

    public void addForwardNode(final CFGNode<? extends StatementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        if (!(forwardNode instanceof CFGEmptyNode)) {
            this.forwardNodes.add(forwardNode);
        }

        forwardNode.backwardNodes.add(this);
    }

    /**
     * ���̃m�[�h�ɑΉ����镶�̏����擾
     * @return ���̃m�[�h�ɑΉ����镶
     */
    public T getStatement() {
        return this.statement;
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g���擾
     * @return ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends StatementInfo>> getForwardNodes() {
        return Collections.unmodifiableSet(this.forwardNodes);
    }

    /**
     * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g���擾
     * @return ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
     */
    public Set<CFGNode<? extends StatementInfo>> getBackwardNodes() {
        return this.backwardNodes;
    }

    /**
     * ���̃m�[�h�������ŗ^����ꂽ���[�J����Ԃ̏o���̃m�[�h�ł��邩�ۂ��Ԃ��D
     * @param localSpace ���[�J�����
     * @return �����̃��[�J����Ԃ̏o���̏ꍇ�Ctrue
     */
    public boolean isExitNode(final LocalSpaceInfo localSpace) {
        if (this.statement instanceof ReturnStatementInfo) {
            return true;
        } else if (this.statement instanceof BreakStatementInfo) {
            final BreakStatementInfo breakStatement = (BreakStatementInfo) this.statement;
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
}
