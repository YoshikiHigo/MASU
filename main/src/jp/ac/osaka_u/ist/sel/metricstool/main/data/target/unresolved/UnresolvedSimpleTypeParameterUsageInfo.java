package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSimpleTypeParameterUsageInfo extends UnresolvedTypeParameterUsageInfo {

    /**
     * �������^��^���ăI�u�W�F�N�g��������
     * 
     * @param type �������^
     */
    public UnresolvedSimpleTypeParameterUsageInfo(final UnresolvedEntityUsageInfo type) {

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
    public UnresolvedEntityUsageInfo getType() {
        return this.type;
    }

    /**
     * �������^��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedEntityUsageInfo type;
}
