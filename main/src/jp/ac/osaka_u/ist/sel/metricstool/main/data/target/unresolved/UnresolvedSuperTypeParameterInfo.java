package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSuperTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * �^�p�����[�^���C�������h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param name �^�p�����[�^��
     * @param extendsType �������h���N���X�^
     */
    public UnresolvedSuperTypeParameterInfo(final String name,
            final UnresolvedTypeInfo superType) {

        super(name);

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
