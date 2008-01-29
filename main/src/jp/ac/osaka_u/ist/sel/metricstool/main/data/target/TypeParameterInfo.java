package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �^�p�����[�^��\�����ۃN���X
 * 
 * @author higo
 * 
 */
public class TypeParameterInfo implements TypeInfo {

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param name �^�p�����[�^��
     */
    public TypeParameterInfo(final String name, final TypeInfo extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
        this.extendsType = extendsType;
    }

    /**
     * ���̌^�p�����[�^�������ŗ^����ꂽ�^�Ɠ��������ǂ����𔻒肷��
     * 
     * @param o ��r�Ώی^���
     * @return �������ꍇ�� true�C�������Ȃ��ꍇ�� false;
     */
    public boolean equals(final TypeInfo o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof TypeParameterInfo)) {
            return false;
        }

        return this.getName().equals(((TypeParameterInfo) o).getName());
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
     * ���N���X�^��Ԃ�
     * 
     * @return ���N���X�^
     */
    public final TypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * * ���N���X�������ǂ�����Ԃ�
     * 
     * @return ���N���X�����ꍇ�� true,�����Ȃ��ꍇ�� false
     */
    public final boolean hasExtendsType() {
        return null != this.extendsType;
    }

    /**
     * �^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final String name;

    /**
     * ���������N���X�^��ۑ����邽�߂̕ϐ�
     */
    private final TypeInfo extendsType;
}
