package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���\�b�h�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
public final class MethodCallInfo extends CallInfo {

    /**
     * �Ăяo����郁�\�b�h��^���ăI�u�W�F�N�g��������
     * 
     * @param callee �Ăяo����郁�\�b�h
     */
    public MethodCallInfo(final TypeInfo ownerType, final MethodInfo callee, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if ((null == ownerType) || (null == callee)) {
            throw new NullPointerException();
        }

        this.ownerType = ownerType;
        this.callee = callee;
    }

    /**
     * ���̃��\�b�h�Ăяo���̌^��Ԃ�
     */
    @Override
    public TypeInfo getType() {

        final MethodInfo callee = this.getCallee();
        final TypeInfo definitionType = callee.getReturnType();

        // ��`�̕Ԃ�l���^�p�����[�^�łȂ���΂��̂܂ܕԂ���
        if (!(definitionType instanceof TypeParameterInfo)) {
            return definitionType;
        }

        //�@�^�p�����[�^�̏ꍇ�͌^������Ԃ�
        final int typeParameterIndex = ((TypeParameterInfo) definitionType).getIndex();
        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getOwnerType();
        final TypeInfo typeArgument = callOwnerType.getTypeArgument(typeParameterIndex);
        return typeArgument;
    }

    /**
     * ���̃��\�b�h�Ăяo�����������Ă���^��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo�����������Ă���^
     */
    public TypeInfo getOwnerType() {
        return this.ownerType;
    }

    /**
     * ���̃��\�b�h�Ăяo���ŌĂяo����Ă��郁�\�b�h��Ԃ�
     * @return
     */
    public MethodInfo getCallee() {
        return this.callee;
    }

    private final TypeInfo ownerType;

    private final MethodInfo callee;
}
