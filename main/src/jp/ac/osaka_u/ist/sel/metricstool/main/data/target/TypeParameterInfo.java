package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �^�p�����[�^��\�����ۃN���X
 * 
 * @author y-higo
 * 
 */
public abstract class TypeParameterInfo implements TypeInfo {

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param name �^�p�����[�^��
     */
    public TypeParameterInfo(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * ���̌^�p�����[�^�������ŗ^����ꂽ�^�Ɠ��������ǂ����𔻒肷��
     * 
     * @param typeInfo ��r�Ώی^���
     * @return �������ꍇ�� true�C�������Ȃ��ꍇ�� false;
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            return false;
        }

        if (!(typeInfo instanceof TypeParameterInfo)) {
            return false;
        }

        return this.getName().equals(((TypeParameterInfo) typeInfo).getName());
    }

    /**
     * �^�p�����[�^����Ԃ�
     * 
     * @return �^�p�����[�^��
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �^���i���ۂɂ͌^�p�����[�^���j��Ԃ��D
     * 
     * @return �^��
     */
    public final String getTypeName() {
        return this.name;
    }

    /**
     * �^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final String name;
}
