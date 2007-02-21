package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

/**
 * <? extends A>�̂悤�Ȍ^�p�����[�^�̎g�p��\���N���X
 * 
 * @author y-higo
 *
 */
public class UnresolvedExtendsTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * ���������N���X�Q�Ƃ�^���ăI�u�W�F�N�g��������
     * 
     * @param extendsType ���������N���X�Q��
     */
    public UnresolvedExtendsTypeParameterUsage(final UnresolvedReferenceTypeInfo extendsType) {

        if (null == extendsType) {
            throw new NullPointerException();
        }

        this.extendsType = extendsType;
    }

    /**
     * ���������N���X�Q�Ƃ�Ԃ�
     * 
     * @return ���������N���X�Q��
     */
    public UnresolvedReferenceTypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * ���������N���X�Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private UnresolvedReferenceTypeInfo extendsType;
}
