package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Resolved;


/**
 * ���O��������Ă��Ȃ����ł��邱�Ƃ�\���C���^�[�t�F�[�X
 * 
 * @author y-higo
 */
public interface Unresolved {

    /**
     * ���O�������ꂽ�����Z�b�g����
     * 
     * @param resolvedInfo ���O�������ꂽ���
     */
    void setResolvedInfo(Resolved resolvedInfo);

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     */
    Resolved getResolvedInfo();
}
