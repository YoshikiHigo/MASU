package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;


/**
 * �\�t�g�E�F�A�̒P�ʂ��C���X�^���X�Ȃ̂��X�^�e�B�b�N�Ȃ̂����`����C���^�[�t�F�[�X
 * 
 * @author higo
 */
public interface MemberSetting extends Member {

    /**
     * �C���X�^���X�����o�[���ǂ������Z�b�g����
     * 
     * @param instance �C���X�^���X�����o�[�̏ꍇ�� true�C �X�^�e�B�b�N�����o�[�̏ꍇ�� false
     */
    void setInstanceMember(boolean instance);
}
