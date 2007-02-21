package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������^�p�����[�^��\�����ۃN���X
 * 
 * @author y-higo
 * 
 */
public abstract class UnresolvedTypeParameterInfo implements UnresolvedTypeInfo {

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param name �^�p�����[�^��
     */
    public UnresolvedTypeParameterInfo(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }
        
        this.name = name;
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
