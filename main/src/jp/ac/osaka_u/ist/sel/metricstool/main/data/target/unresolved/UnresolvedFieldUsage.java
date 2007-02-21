package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������t�B�[���h�g�p��ۑ����邽�߂̃N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldUsage implements UnresolvedTypeInfo {

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̌^���ƕϐ����C���p�\�Ȗ��O��Ԃ�^���ăI�u�W�F�N�g��������
     * 
     * @param availableNamespaces ���p�\�Ȗ��O���
     * @param ownerClassType �t�B�[���h�g�p�����s�����ϐ��̌^��
     * @param fieldName �ϐ���
     */
    public UnresolvedFieldUsage(final AvailableNamespaceInfoSet availableNamespaces,
            final UnresolvedTypeInfo ownerClassType, final String fieldName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == ownerClassType) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.ownerClassType = ownerClassType;
        this.fieldName = fieldName;
    }

    /**
     * �g�p�\�Ȗ��O��Ԃ�Ԃ�
     * 
     * @return �g�p�\�Ȗ��O��Ԃ�Ԃ�
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaces;
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
     * ���̃t�B�[���h�g�p�̌^�i�Ԃ�l�݂����Ȃ��́j��Ԃ�
     * 
     * @return ���̃t�B�[���h�g�p�̌^�i�Ԃ�l�݂����Ȃ��́j
     */
    public String getTypeName() {
        return UnresolvedTypeInfo.UNRESOLVED;
    }

    /**
     * �g�p�\�Ȗ��O��Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final AvailableNamespaceInfoSet availableNamespaces;

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedTypeInfo ownerClassType;

    /**
     * �t�B�[���h����ۑ����邽�߂̕ϐ�
     */
    private final String fieldName;
}
