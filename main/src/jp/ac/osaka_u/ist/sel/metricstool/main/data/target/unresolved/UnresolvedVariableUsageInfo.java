package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * �������ϐ��g�p��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedVariableUsageInfo<T extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>
        extends UnresolvedEntityUsageInfo<T> {

    public UnresolvedVariableUsageInfo(final String usedVariableName, final boolean reference,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        if (null == usedVariableName) {
            throw new IllegalArgumentException("usedVarialbeName is null");
        }

        this.usedVariableName = usedVariableName;
        this.reference = reference;

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * ���̕ϐ��g�p���Q�Ƃł��邩�ǂ�����Ԃ�
     * 
     * @return �Q�Ƃł���ꍇ�� true�C����ł���ꍇ�� false
     */
    public final boolean isReference() {
        return this.reference;
    }

    /**
     * ���̕ϐ��g�p������ł��邩�ǂ�����Ԃ�
     * 
     * @return ����ł���ꍇ�� true�C�Q�Ƃł���ꍇ�� false
     */
    public final boolean isAssignment() {
        return !this.reference;
    }

    /**
     * �g�p����Ă���ϐ��̖��O��Ԃ�
     * @return �g�p����Ă���ϐ��̖��O
     */
    public String getUsedVariableName() {
        return this.usedVariableName;
    }

    /**
     * �g�p����Ă���ϐ��̖��O��ۑ�����ϐ�
     */
    protected final String usedVariableName;

    protected boolean reference;

}
