package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �ϐ��g�p��\�����ۃN���X
 * 
 * @author higo
 *
 */
public abstract class VariableUsageInfo<T extends VariableInfo> extends EntityUsageInfo {

    VariableUsageInfo(final T usedVariable, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.usedVariable = usedVariable;
        this.reference = reference;
    }

    public final T getUsedVariable() {
        return this.usedVariable;
    }

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

    private final T usedVariable;

    private final boolean reference;
}
