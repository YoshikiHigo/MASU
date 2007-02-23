package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������^�p�����[�^��\�����ۃN���X
 * 
 * @author y-higo
 * 
 */
public class UnresolvedTypeParameterInfo implements UnresolvedTypeInfo {

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param name �^�p�����[�^��
     * @param extends ���������N���X�^
     */
    public UnresolvedTypeParameterInfo(final String name, final UnresolvedTypeInfo extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
        this.extendsType = extendsType;
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
     * ���N���X�̖������^����Ԃ�
     * 
     * @return ���N���X�̖������^���
     */
    public final UnresolvedTypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * ���N���X�������ǂ�����Ԃ�
     * 
     * @return ���N���X�����ꍇ�� true, �����Ȃ��ꍇ�� false
     */
    public final boolean hasExtendsType() {
        return null != this.extendsType;
    }

    /**
     * �^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final String name;

    /**
     * ���N���X��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedTypeInfo extendsType;
}
