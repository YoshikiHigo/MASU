package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

/**
 * CFG�m�[�h�̃t�@�N�g����\���C���^�[�t�F�[�X
 * @author t-miyake
 *
 */
public interface ICFGNodeFactory {

    /**
     * CFG�m�[�h�𐶐�
     * @param statement ��������CFG�m�[�h�ɑΉ����镶
     * @return CFG�m�[�h
     */
    public CFGNode<? extends StatementInfo> makeNode(StatementInfo statement);
    
    /**
     * ���̃t�@�N�g���Ő������ꂽ�m�[�h�̂����C�����Ŏw�肳�ꂽ���ɑΉ�����m�[�h��Ԃ�
     * @param statement ��
     * @return ���̃t�@�N�g���Ő������ꂽ�m�[�h�D�Ή�����m�[�h�������ς݂łȂ��ꍇ��null�D
     */
    public CFGNode<? extends StatementInfo> getNode(StatementInfo statement);
}
