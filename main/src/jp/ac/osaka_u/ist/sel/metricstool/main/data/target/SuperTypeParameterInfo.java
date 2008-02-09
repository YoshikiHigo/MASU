package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class SuperTypeParameterInfo extends TypeParameterInfo {

    /**
     * �^�p�����[�^���C�h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param ownerUnit ���̌^�p�����[�^�̏��L���j�b�g(�N���X or ���\�b�h)
     * @param name �^�p�����[�^��
     * @param extendsType ���N���X�^
     * @param superType �h���N���X�^
     */
    public SuperTypeParameterInfo(final UnitInfo ownerUnit, final String name,
            final TypeInfo extendsType, final TypeInfo superType) {

        super(ownerUnit, name, extendsType);

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
