package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * �ϐ��g�p��\�����ۃN���X
 * 
 * @author higo
 * @param <T> �g�p����Ă���ϐ�
 */
public abstract class VariableUsageInfo<T extends VariableInfo<? extends UnitInfo>> extends
        ExpressionInfo {

    /**
     * �ϐ��g�p��Collection����g�p����Ă���ϐ���Set��Ԃ�
     * 
     * @param variableUsages �ϐ��g�p��Collection
     * @return �g�p����Ă���ϐ���Set
     */
    public static Set<VariableInfo<? extends UnitInfo>> getUsedVariables(
            Collection<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages) {

        final Set<VariableInfo<?>> usedVariables = new HashSet<VariableInfo<?>>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            final VariableInfo<?> variable = variableUsage.getUsedVariable();
            usedVariables.add(variable);
        }
        return usedVariables;
    }

    /**
     * �����ŗ^�����Ă��ϐ��g�p�Ɋ܂܂��ϐ��Q�Ƃ�Set��Ԃ�
     * 
     * @param variableUsages �ϐ��g�p��Set
     * @return �����ŗ^�����Ă��ϐ��g�p�Ɋ܂܂��ϐ��Q�Ƃ�Set
     */
    public static Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getReferencees(
            Collection<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages) {

        final Set<VariableUsageInfo<?>> references = new HashSet<VariableUsageInfo<?>>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (variableUsage.isReference()) {
                references.add(variableUsage);
            }
        }

        return Collections.unmodifiableSet(references);
    }

    /**
     * �����ŗ^�����Ă��ϐ��g�p�Ɋ܂܂��ϐ������Set��Ԃ�
     * 
     * @param variableUsages �ϐ��g�p��Set
     * @return �����ŗ^�����Ă��ϐ��g�p�Ɋ܂܂��ϐ������Set
     */
    public static Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getAssignments(
            Collection<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages) {

        final Set<VariableUsageInfo<?>> assignments = new HashSet<VariableUsageInfo<?>>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (variableUsage.isAssignment()) {
                assignments.add(variableUsage);
            }
        }

        return Collections.unmodifiableSet(assignments);
    }

    /**
     * 
     * @param usedVariable �g�p����Ă���ϐ�
     * @param reference �Q�Ƃ��ǂ���
     * @param assignment ������ǂ���
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    VariableUsageInfo(final T usedVariable, final boolean reference, final boolean assignment,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.usedVariable = usedVariable;
        this.reference = reference;
        this.assignment = assignment;
    }

    /**
     * �g�p����Ă���ϐ���Ԃ�
     * 
     * @return �g�p����Ă���ϐ�
     */
    public final T getUsedVariable() {
        return this.usedVariable;
    }

    /**
     * �Q�Ƃ��������Ԃ�
     * 
     * @return �Q�Ƃł���ꍇ�� true�C����ł���ꍇ�� false
     */
    public final boolean isReference() {
        return this.reference;
    }

    /**
     * ���̃t�B�[���h�g�p������ł��邩�ǂ�����Ԃ�
     * 
     * @return ����ł���ꍇ�� true�C�Q�Ƃł���ꍇ�� false
     */
    public final boolean isAssignment() {
        return this.assignment;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsage = new TreeSet<VariableUsageInfo<?>>();
        variableUsage.add(this);
        return Collections.unmodifiableSortedSet(variableUsage);
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * ���̕ϐ��g�p�̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̕ϐ��g�p�̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {
        final T variable = this.getUsedVariable();
        return variable.getName();
    }

    /**
     * �ϐ��g�p�̌^��Ԃ�
     * 
     * @return �ϐ��g�p�̌^
     */
    @Override
    public TypeInfo getType() {

        final T usedVariable = this.getUsedVariable();
        final TypeInfo definitionType = usedVariable.getType();

        // ��`�̕Ԃ�l���^�p�����[�^�łȂ���΂��̂܂ܕԂ���
        if (!(definitionType instanceof TypeParameterInfo)) {
            return definitionType;
        }

        // �^�p�����[�^����C���ۂɎg�p����Ă���^���擾���Ԃ�
        // ���\�b�h�̌^�p�����[�^���ǂ���
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        for (final TypeParameterInfo typeParameter : ownerMethod.getTypeParameters()) {
            if (typeParameter.equals(definitionType)) {
                return ((TypeParameterInfo) definitionType).getExtendsType();
            }
        }

        // �N���X�̌^�p�����[�^���ǂ���
        for (ClassInfo ownerClass = ownerMethod.getOwnerClass(); true; ownerClass = ((TargetInnerClassInfo) ownerClass)
                .getOuterClass()) {

            //�@�^�p�����[�^�����̂܂܂�
            for (final TypeParameterInfo typeParameter : ownerClass.getTypeParameters()) {
                if (typeParameter.equals(definitionType)) {
                    return ((TypeParameterInfo) definitionType).getExtendsType();
                }
            }

            //�@�e�N���X�Œ�`���ꂽ�^�p�����[�^��
            final Map<TypeParameterInfo, TypeInfo> typeParameterUsages = ownerClass
                    .getTypeParameterUsages();
            for (final Map.Entry<TypeParameterInfo, TypeInfo> entry : typeParameterUsages
                    .entrySet()) {
                final TypeParameterInfo typeParameter = entry.getKey();
                if (typeParameter.equals(definitionType)) {
                    return entry.getValue();
                }
            }

            if (!(ownerClass instanceof TargetInnerClassInfo)) {
                break;
            }
        }

        throw new IllegalStateException();
    }

    private final T usedVariable;

    private final boolean reference;

    private final boolean assignment;

    /**
     * ��̕ϐ����p��Set��\��
     */
    public static final SortedSet<VariableUsageInfo<?>> EmptySet = Collections
            .unmodifiableSortedSet(new TreeSet<VariableUsageInfo<?>>());
}
