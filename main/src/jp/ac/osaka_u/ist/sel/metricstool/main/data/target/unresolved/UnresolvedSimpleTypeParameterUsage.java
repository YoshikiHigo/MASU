package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSimpleTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * �������N���X�Q�Ƃ�^���ăI�u�W�F�N�g��������
     * 
     * @param type �������N���X�Q��
     */
    public UnresolvedSimpleTypeParameterUsage(final UnresolvedReferenceTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * �^�p�����[�^�̖������N���X�Q�Ƃ�Ԃ�
     * 
     * @return �������N���X�Q��
     */
    public UnresolvedReferenceTypeInfo getType() {
        return this.type;
    }

    /**
     * �������N���X�Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedReferenceTypeInfo type;
}
