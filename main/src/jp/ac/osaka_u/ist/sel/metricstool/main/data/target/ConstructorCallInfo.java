package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �R���X�g���N�^�Ăяo����ۑ�����ϐ�
 * 
 * @author higo
 *
 */
public class ConstructorCallInfo extends CallInfo<ConstructorInfo> {

    /**
     * �^��^���ăR���X�g���N�^�Ăяo����������
     * 
     * @param referenceType �Ăяo���̌^
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     */
    public ConstructorCallInfo(final ReferenceTypeInfo referenceType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

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

    /**
     * ���̃R���X�g���N�^�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̃R���X�g���N�^�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("new ");

        final TypeInfo type = this.getType();
        sb.append(type.getTypeName());

        sb.append("(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }

        sb.append(")");

        return sb.toString();
    }

    private final ReferenceTypeInfo referenceType;

}
