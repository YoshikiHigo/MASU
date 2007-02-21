package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class SuperTypeParameterInfo extends TypeParameterInfo {

    /**
     * �^�p�����[�^���C�h���N���X�^��^���ăI�u�W�F�N�g��������
     * 
     * @param name �^�p�����[�^��
     * @param extendsType �h���N���X�^
     */
    public SuperTypeParameterInfo(final String name, final ClassInfo superType) {

        super(name);

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
    public ClassInfo getSuperType() {
        return this.superType;
    }

    /**
     * �������h���N���X�^��ۑ�����
     */
    private final ClassInfo superType;
}
