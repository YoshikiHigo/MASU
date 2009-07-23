package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;


/**
 * CFG�̐���m�[�h��\���N���X
 * @author t-miyake, higo
 *
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

    public CFGControlNode(final ConditionInfo condition) {
        super(condition);
    }

    @Override
    protected void optimize() {

        //���̃m�[�h�̃o�b�N���[�h�m�[�h�Q���擾
        final Set<CFGNode<?>> backwardNodes = new HashSet<CFGNode<?>>();
        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            backwardNodes.add(backwardEdge.getFromNode());
        }

        // ���̃m�[�h�̃t�H���[�h�m�[�h�Q���擾
        final Set<CFGNode<?>> forwardNodes = new HashSet<CFGNode<?>>();
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            forwardNodes.add(forwardEdge.getFromNode());
        }

        // �o�b�N���[�h�m�[�h����C���̃m�[�h���t�H���[�h�m�[�h�Ƃ���G�b�W���폜
        for (final CFGNode<?> backwardNode : this.getBackwardNodes()) {
            backwardNode.removeForwardEdges(this.getBackwardEdges());
        }

        // �t�H���[�h�m�[�h����C���̃m�[�h���o�b�N���[�h�m�[�h�Ƃ���G�b�W���폜
        for (final CFGNode<?> forwardNode : this.getForwardNodes()) {
            forwardNode.removeBackwardEdges(this.getForwardEdges());
        }

        // �o�b�N���[�h�m�[�h�Q�ƃt�H���[�h�m�[�h�Q���Ȃ�
        for (final CFGNode<?> backwardNode : backwardNodes) {
            for (final CFGNode<?> forwardNode : forwardNodes) {
                final CFGNormalEdge edge = new CFGNormalEdge(backwardNode, forwardNode);
                backwardNode.addForwardEdge(edge);
            }
        }
    }
}
