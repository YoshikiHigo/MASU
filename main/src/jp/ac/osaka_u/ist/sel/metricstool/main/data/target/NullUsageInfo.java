package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * null�g�p��\���N���X�D
 * 
 * @author higo, t-miyake
 * 
 */
public final class NullUsageInfo extends EntityUsageInfo {

    public NullUsageInfo() {
        super();
    }

    /**
     * null�g�p�̌^�͕s��
     * 
     * @return �s���^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        return NULLTYPE;
    }

    /**
     * null�g�p�̌^��ۑ����邽�߂̒萔
     */
    private static final TypeInfo NULLTYPE = UnknownTypeInfo.getInstance();
}
