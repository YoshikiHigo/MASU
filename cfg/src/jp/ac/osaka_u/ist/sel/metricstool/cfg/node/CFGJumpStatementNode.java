package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGJumpEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;


abstract public class CFGJumpStatementNode extends CFGStatementNode<JumpStatementInfo> {

    CFGJumpStatementNode(final JumpStatementInfo jumpStatement) {
        super(jumpStatement);
    }

    @Override
    public final boolean optimize() {

        final Set<CFGNode<?>> backwardNodes = this.getBackwardNodes();
        final Set<CFGNode<?>> forwardNodes = this.getForwardNodes();

        this.remove();

        // �o�b�N���[�h�m�[�h�Q�ƃt�H���[�h�m�[�h�Q���Ȃ�
        for (final CFGNode<?> backwardNode : backwardNodes) {
            for (final CFGNode<?> forwardNode : forwardNodes) {
                final CFGJumpEdge edge = new CFGJumpEdge(backwardNode, forwardNode);
                backwardNode.addForwardEdge(edge);
                forwardNode.addBackwardEdge(edge);
            }
        }

        return true;
    }
}