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
    public UnresolvedFieldUsage(final UnresolvedTypeInfo ownerClassType, final String fieldName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassType) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.ownerClassType = ownerClassType;
        this.fieldName = fieldName;
    }

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����Ԃ�
     * 
     * @return �t�B�[���h�g�p�����s�����ϐ��̖������^��
     */
    public UnresolvedTypeInfo getOwnerClassType() {
        return this.ownerClassType;
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
        final UnresolvedTypeInfo ownerClassType = this.getOwnerClassType();
        final UnresolvedTypeInfo correspondOwnerClassType = unresolvedFieldUsage
                .getOwnerClassType();
        return ownerClassType.compareTo(correspondOwnerClassType);
    }

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedTypeInfo ownerClassType;

    /**
     * �t�B�[���h����ۑ����邽�߂̕ϐ�
     */
    private final String fieldName;
}
