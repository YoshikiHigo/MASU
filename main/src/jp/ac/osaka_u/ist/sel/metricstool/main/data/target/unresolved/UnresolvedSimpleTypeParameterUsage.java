package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSimpleTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * �������^��^���ăI�u�W�F�N�g��������
     * 
     * @param type �������^
     */
    public UnresolvedSimpleTypeParameterUsage(final UnresolvedTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * �^�p�����[�^�̖������^��Ԃ�
     * 
     * @return �������^
     */
    public UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * �������^��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedTypeInfo type;
}
