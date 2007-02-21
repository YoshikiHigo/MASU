package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * <? super A>�̂悤�Ȍ^�p�����[�^�̎g�p��\���N���X
 * 
 * @author y-higo
 * 
 */
public class UnresolvedSuperTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * �������h���N���X�Q�Ƃ�^���ăI�u�W�F�N�g��������
     * 
     * @param superType �������h���N���X�Q��
     */
    public UnresolvedSuperTypeParameterUsage(final UnresolvedReferenceTypeInfo superType) {

        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * �������h���N���X�Q�Ƃ�Ԃ�
     * 
     * @return �������h���N���X�Q��
     */
    public UnresolvedReferenceTypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * �������h���N���X�Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private UnresolvedReferenceTypeInfo superType;
}
