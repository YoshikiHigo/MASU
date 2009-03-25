package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


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
    public CFGControlNode makeControlNode(ConditionInfo condition);

    /**
     * CFG�̃m�[�}���m�[�h�𐶐�
     * @param element ��������CFG�m�[�h�ɑΉ����镶
     * @return CFG�m�[�h
     */
    public CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(
            ExecutableElementInfo element);

    /**
     * ���̃t�@�N�g���Ő������ꂽ�m�[�h�̂����C�����Ŏw�肳�ꂽ���ɑΉ�����m�[�h��Ԃ�
     * @param element ��
     * @return ���̃t�@�N�g���Ő������ꂽ�m�[�h�D�Ή�����m�[�h�������ς݂łȂ��ꍇ��null�D
     */
    public CFGNode<? extends ExecutableElementInfo> getNode(ExecutableElementInfo element);
}
