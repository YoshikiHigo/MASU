package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * <T extends A> �Ȃǂ� extends ��p�����^�p�����[�^��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class ExtendsTypeParameterInfo extends TypeParameterInfo {

    /**
     * �^�p�����[�^���C���N���X��^���ď�����
     * @param name
     * @param extendsType
     */
    public ExtendsTypeParameterInfo(final String name, final TypeInfo extendsType) {
        super(name);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == extendsType) {
            throw new NullPointerException();
        }

        this.extendsType = extendsType;

    }

    /**
     * ���������N���X�^��Ԃ�
     * 
     * @return ���������N���X�^
     */
    public TypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * ���N���X�^��ۑ�����
     */
    private final TypeInfo extendsType;

}
