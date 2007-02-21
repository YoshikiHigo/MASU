package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedExtendsTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * �^�p�����[�^���C���������N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param name �^�p�����[�^��
     * @param extendsType ���������N���X�^
     */
    public UnresolvedExtendsTypeParameterInfo(final String name,
            final UnresolvedTypeInfo extendsType) {

        super(name);

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
    public UnresolvedTypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * ���N���X�^��ۑ�����
     */
    private final UnresolvedTypeInfo extendsType;

}
