package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * Unresolved�Ȍ^��\���C���^�[�t�F�[�X�D
 * 
 * @author y-higo
 * 
 */
public interface UnresolvedTypeInfo extends Comparable<UnresolvedTypeInfo> {

    /**
     * �^����Ԃ�
     */
    String getTypeName();

    /**
     * �I�u�W�F�N�g�̓��������`�F�b�N����
     * 
     * @param typeInfo ��r�ΏۃI�u�W�F�N�g
     * @return �������ꍇ�� true,�����łȂ��ꍇ�� false
     */
    boolean equals(UnresolvedTypeInfo typeInfo);
}
