package sdl.ist.osaka_u.newmasu.cfg.node;


import java.util.Collection;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * CFG�m�[�h�̃t�@�N�g����\���C���^�[�t�F�[�X
 * @author t-miyake
 *
 */
public interface ICFGNodeFactory {

    /**
     * CFG�̃R���g���[���m�[�h�𐶐�
     * @param element ��������CFG�m�[�h�ɑΉ����镶
     * @return CFG�m�[�h
     */
    CFGControlNode makeControlNode(ConditionInfo condition);

    /**
     * CFG�̃m�[�}���m�[�h�𐶐�
     * @param element ��������CFG�m�[�h�ɑΉ����镶
     * @return CFG�m�[�h
     */
    CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(ExecutableElementInfo element);

    /**
     * ���̃t�@�N�g���Ő������ꂽ�m�[�h�̂����C��Ŏw�肳�ꂽ���ɑΉ�����m�[�h��Ԃ�
     * @param element ��
     * @return ���̃t�@�N�g���Ő������ꂽ�m�[�h�D�Ή�����m�[�h�������ς݂łȂ��ꍇ��null�D
     */
    CFGNode<? extends ExecutableElementInfo> getNode(ExecutableElementInfo element);

    boolean removeNode(ExecutableElementInfo element);

    Collection<CFGNode<? extends ExecutableElementInfo>> getAllNodes();

    Set<CFGNode<? extends ExecutableElementInfo>> getDissolvedNodes(ExecutableElementInfo element);

    void addDissolvedNode(final ExecutableElementInfo element,
            final CFGNode<? extends ExecutableElementInfo> node);

    void addDissolvedNodes(final ExecutableElementInfo element,
            final Set<CFGNode<? extends ExecutableElementInfo>> nodes);

    boolean isDissolvedNode(final CFGNode<? extends ExecutableElementInfo> node);
}
