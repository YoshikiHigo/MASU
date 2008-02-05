package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �R���X�g���N�^�Ăяo����ۑ�����ϐ�
 * 
 * @author higo
 *
 */
public final class ConstructorCallInfo extends CallInfo {

    /**
     * �Ăяo�����R���X�g���N�^��^���ăI�u�W�F�N�g��������
     * 
     * @param callee �Ăяo�����R���X�g���N�^
     */
    public ConstructorCallInfo(final ReferenceTypeInfo referenceType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;

    }

    /**
     * �R���X�g���N�^�Ăяo���̌^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        return this.referenceType;
    }

    private final ReferenceTypeInfo referenceType;

}
