package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


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
     * @param ownerUsage �t�B�[���h�g�p�����s�����e�G���e�B�e�B
     * @param usedField �g�p����Ă���t�B�[���h
     * @param reference �Q�Ƃł���ꍇ�� true, ����ł���ꍇ�� false
     */
    public FieldUsageInfo(final EntityUsageInfo ownerUsage, final TypeInfo ownerType,
            final FieldInfo usedField, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);

        this.ownerUsage = ownerUsage;
        this.ownerType = ownerType;
    }

    /**
     * ���̃t�B�[���h�g�p�̌^��Ԃ�
     * 
     * @return ���̃t�B�[���h�g�p�̌^
     */
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

    /**
     * ���̃t�B�[���h�g�p�̐e�C�܂肱�̃t�B�[���h�g�p���������Ă���v�f��Ԃ�
     * 
     * @return ���̃t�B�[���h�g�p�̐e
     */
    public final TypeInfo getOwnerType() {
        return this.ownerType;
    }

    /**
     * �t�B�[���h�g�p�����s�����e�G���e�B�e�B��Ԃ�
     * @return �t�B�[���h�g�p�����s�����e�G���e�B�e�B
     */
    public final EntityUsageInfo getOwnerUsage() {
        return this.ownerUsage;
    }

    /**
     * ���̎��i�t�B�[���h�g�p�j�ɂ�����ϐ����p�̈ꗗ��Ԃ�
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public SortedSet<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>(
                super.getVariableUsages());
        variableUsages.addAll(getOwnerUsage().getVariableUsages());
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final TypeInfo ownerType;

    /**
     * �t�B�[���h�Q�Ƃ����s�����e�G���e�B�e�B��ۑ�����ϐ�
     */
    private final EntityUsageInfo ownerUsage;

}
