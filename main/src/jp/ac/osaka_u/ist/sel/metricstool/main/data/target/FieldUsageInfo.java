package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


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
    protected FieldUsageInfo(final EntityUsageInfo ownerUsage, final TypeInfo ownerType,
            final FieldInfo usedField, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);

        this.ownerExpression = ownerUsage;
        this.ownerType = ownerType;
    }

    /**
     * ���̃t�B�[���h�g�p�̌^��Ԃ�
     * 
     * @return ���̃t�B�[���h�g�p�̌^
     */
    @Override
    public TypeInfo getType() {

        final FieldInfo usedVariable = this.getUsedVariable();
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
    public final EntityUsageInfo getOwnerExpression() {
        return this.ownerExpression;
    }

    /**
     * ���̎��i�t�B�[���h�g�p�j�ɂ�����ϐ����p�̈ꗗ��Ԃ�
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public SortedSet<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo ownerExpression = this.getOwnerExpression();
        variableUsages.addAll(ownerExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final TypeInfo ownerType;

    /**
     * �t�B�[���h�Q�Ƃ����s�����e�G���e�B�e�B��ۑ�����ϐ�
     */
    private final EntityUsageInfo ownerExpression;

    /**
     * �K�v�ȏ���^���āC�C���X�^���X���擾
     * 
     * @param ownerUsage �e�G���e�B�e�B
     * @param ownerType �e�G���e�B�e�B�̌^
     * @param usedField �g�p����Ă���t�B�[���h
     * @param reference �Q�Ƃł���ꍇ��true�C����ł���ꍇ��false
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return �t�B�[���h�g�p�̃C���X�^���X
     */
    public static FieldUsageInfo getInstance(final EntityUsageInfo ownerUsage,
            final TypeInfo ownerType, final FieldInfo usedField, final boolean reference,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final FieldUsageInfo instance = new FieldUsageInfo(ownerUsage, ownerType, usedField,
                reference, fromLine, fromColumn, toLine, toColumn);
        addFieldUsage(instance);
        return instance;
    }

    /**
     * �t�B�[���h�g�p�̃C���X�^���X���t�B�[���h����t�B�[���h�g�p�ւ̃}�b�v�ɒǉ�
     * @param fieldUsage �t�B�[���h�g�p
     */
    private static void addFieldUsage(final FieldUsageInfo fieldUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == fieldUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final FieldInfo usedField = fieldUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedField)) {
            USAGE_MAP.get(usedField).add(fieldUsage);
        } else {
            final TreeSet<FieldUsageInfo> usages = new TreeSet<FieldUsageInfo>();
            usages.add(fieldUsage);
            USAGE_MAP.put(usedField, usages);
        }
    }

    /**
     * �^����ꂽ�t�B�[���h�̎g�p���̃Z�b�g���擾
     * @param field �g�p�����擾�������t�B�[���h
     * @return �t�B�[���h�g�p�̃Z�b�g�D�����ŗ^����ꂽ�t�B�[���h���g�p����Ă��Ȃ��ꍇ��null
     */
    public final static Set<FieldUsageInfo> getUsages(final FieldInfo field) {
        if (USAGE_MAP.containsKey(field)) {
            return USAGE_MAP.get(field);
        } else {
            return Collections.<FieldUsageInfo> emptySet();
        }
    }

    private static final Map<FieldInfo, TreeSet<FieldUsageInfo>> USAGE_MAP = new HashMap<FieldInfo, TreeSet<FieldUsageInfo>>();
}
