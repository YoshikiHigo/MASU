package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �u�^�v��\���C���^�[�t�F�[�X�D
 * 
 * @author y-higo
 * 
 */
public interface TypeInfo {

    /**
     * �^����Ԃ�
     */
    String getTypeName();
    
    /**
     * �������̃`�F�b�N
     */
    boolean equals(TypeInfo typeInfo);
    
}
