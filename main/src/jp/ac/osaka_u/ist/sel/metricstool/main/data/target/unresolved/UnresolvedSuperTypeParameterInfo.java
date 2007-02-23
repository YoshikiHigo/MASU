package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSuperTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * �^�p�����[�^���C�������h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param name �^�p�����[�^��
     * @param extendsType ���������N���X�^
     * @param superType �������h���N���X�^
     */
    public UnresolvedSuperTypeParameterInfo(final String name,
            final UnresolvedTypeInfo extendsType, final UnresolvedTypeInfo superType) {

        super(name, extendsType);

        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * �������h���N���X�^��Ԃ�
     * 
     * @return �������h���N���X�^
     */
    public UnresolvedTypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * �������h���N���X�^��ۑ�����
     */
    private final UnresolvedTypeInfo superType;
}
