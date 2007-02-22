package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class SuperTypeParameterInfo extends TypeParameterInfo {

    /**
     * �^�p�����[�^���C�h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param name �^�p�����[�^��
     * @param superType �h���N���X�^
     */
    public SuperTypeParameterInfo(final String name, final TypeInfo superType) {

        super(name);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * �h���N���X�^��Ԃ�
     * 
     * @return �h���N���X�^
     */
    public TypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * �������h���N���X�^��ۑ�����
     */
    private final TypeInfo superType;
}
