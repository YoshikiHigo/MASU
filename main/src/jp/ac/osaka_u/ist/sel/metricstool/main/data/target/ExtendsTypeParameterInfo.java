package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


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
    ExtendsTypeParameterInfo(final String name, final ClassInfo extendsType) {
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
    public ClassInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * ���N���X�^��ۑ�����
     */
    private final ClassInfo extendsType;

}
