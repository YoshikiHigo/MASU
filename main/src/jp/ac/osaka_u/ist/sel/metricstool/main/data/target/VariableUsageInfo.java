package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * �ϐ��g�p��\�����ۃN���X
 * 
 * @author higo
 * @param <T> �g�p����Ă���ϐ�
 */
public abstract class VariableUsageInfo<T extends VariableInfo<? extends UnitInfo>> extends
        EntityUsageInfo {

    /**
     * 
     * @param ownerExecutableElement �I�[�i�[�G�������g
     * @param usedVariable �g�p����Ă���ϐ�
     * @param reference �Q�Ƃ��ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    VariableUsageInfo(final ExecutableElementInfo ownerExecutableElement, final T usedVariable,
            final boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(ownerExecutableElement, fromLine, fromColumn, toLine, toColumn);

        this.usedVariable = usedVariable;
        this.reference = reference;

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
        return !this.reference;
    }

    @Override
    public SortedSet<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsage = new TreeSet<VariableUsageInfo<?>>();
        variableUsage.add(this);
        return Collections.unmodifiableSortedSet(variableUsage);
    }

    /**
     * ���̕ϐ��g�p�̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̕ϐ��g�p�̃e�L�X�g�\���i�^�j
     */
    @Override
    public final String getText() {
        final T variable = this.getUsedVariable();
        return variable.getName();
    }

    private final T usedVariable;

    private final boolean reference;

    /**
     * ��̕ϐ����p��Set��\��
     */
    public static final SortedSet<VariableUsageInfo<?>> EmptySet = Collections
            .unmodifiableSortedSet(new TreeSet<VariableUsageInfo<?>>());
}
