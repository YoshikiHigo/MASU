package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �R���X�g���N�^�Ăяo����ۑ�����ϐ�
 * 
 * @author higo
 *
 */
public final class ConstructorCallInfo extends CallInfo {

    /**
     * �^��^���ăR���X�g���N�^�Ăяo����������
     * 
     * @param referenceType �Ăяo���̌^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
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
