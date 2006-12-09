package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������t�B�[���h�g�p��ۑ����邽�߂̃N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldUsage implements Comparable<UnresolvedFieldUsage> {

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̌^���ƕϐ�����^���ăI�u�W�F�N�g��������
     * 
     * @param ownerClassName �t�B�[���h�g�p�����s�����ϐ��̌^��
     * @param fieldName �ϐ���
     */
    public UnresolvedFieldUsage(final String[] ownerClassName, final String fieldName) {
        
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassName) || (null == fieldName)){
            throw new NullPointerException();
        }
        
        this.ownerClassName = ownerClassName;
        this.fieldName = fieldName;
    }

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̌^����Ԃ�
     * 
     * @return �t�B�[���h�g�p�����s�����ϐ��̌^��
     */
    public String[] getOwnerClassName() {
        return this.ownerClassName;
    }

    /**
     * �t�B�[���h����Ԃ�
     * 
     * @return �t�B�[���h��
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * �t�B�[���h�g�p�̏������`
     * 
     * @return �t�B�[���h�g�p�̏���
     */
    public int compareTo(final UnresolvedFieldUsage unresolvedFieldUsage) {

        if (null == unresolvedFieldUsage) {
            throw new NullPointerException();
        }

        // �t�B�[���h���Ŕ�r
        int fieldNameOrder = this.getFieldName().compareTo(unresolvedFieldUsage.getFieldName());
        if (0 != fieldNameOrder) {
            return fieldNameOrder;
        }

        // ���\�b�h�Ăяo�����s���Ă���ϐ��̌^�Ŕ�r
        final String[] ownerClassName = this.getOwnerClassName();
        final String[] correspondOwnerClassName = unresolvedFieldUsage.getOwnerClassName();
        if (ownerClassName.length > correspondOwnerClassName.length) {
            return 1;
        } else if (ownerClassName.length < correspondOwnerClassName.length) {
            return -1;
        } else {
            for (int i = 0; i < ownerClassName.length; i++) {
                final int stringOrder = ownerClassName[i].compareTo(correspondOwnerClassName[i]);
                if (0 != stringOrder) {
                    return stringOrder;
                }
            }

            return 0;
        }
    }

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̌^����ۑ����邽�߂̕ϐ�
     */
    private final String[] ownerClassName;

    /**
     * �t�B�[���h����ۑ����邽�߂̕ϐ�
     */
    private final String fieldName;
}
