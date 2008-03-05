package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �t�B�[���h�̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
public class FieldUsageInfo extends VariableUsageInfo<FieldInfo> {

    /**
     * �g�p����Ă���t�B�[���h��^���ăI�u�W�F�N�g��������
     * 
     * @param usedField �g�p����Ă���t�B�[���h
     * @param reference �Q�Ƃł���ꍇ�� true, ����ł���ꍇ�� false
     */
    public FieldUsageInfo(final TypeInfo ownerType, final FieldInfo usedField,
            final boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);

        this.ownerType = ownerType;
    }

    @Override
    public TypeInfo getType() {

        final VariableInfo<?, ?> usedVariable = this.getUsedVariable();
        final TypeInfo definitionType = usedVariable.getType();

        // ��`�̕Ԃ�l���^�p�����[�^�łȂ���΂��̂܂ܕԂ���
        if (!(definitionType instanceof TypeParameterInfo)) {
            return definitionType;
        }

        //�@����
        final int typeParameterIndex = ((TypeParameterInfo) definitionType).getIndex();
        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getOwnerType();
        final TypeInfo typeArgument = callOwnerType.getTypeArgument(typeParameterIndex);
        return typeArgument;
    }

    public final TypeInfo getOwnerType() {
        return this.ownerType;
    }

    private final TypeInfo ownerType;
}
