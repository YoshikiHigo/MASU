package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * This class represents assignment-statement
 * 
 * @author higo
 *
 */
public class AssignmentExpressionInfo implements ExpressionInfo {

    public AssignmentExpressionInfo(final VariableUsageInfo<?> leftVariable,
            final ExpressionInfo rightExpression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        this.leftVariable = leftVariable;
        this.rightExpression = rightExpression;

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    @Override
    public int compareTo(StatementInfo o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return 1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return -1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return 1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return -1;
        } else if (this.getToLine() < o.getToLine()) {
            return 1;
        } else if (this.getToLine() > o.getToLine()) {
            return -1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return 1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return -1;
        }

        return 0;
    }

    public VariableUsageInfo<?> getLeftVariable() {
        return this.leftVariable;
    }

    public ExpressionInfo getRightExpression() {
        return this.rightExpression;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getLeftVariable().getVariableUsages());
        variableUsages.addAll(this.getRightExpression().getVariableUsages());
        return variableUsages;
    }
    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private final int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private final int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int toColumn;

    private final VariableUsageInfo<?> leftVariable;

    private final ExpressionInfo rightExpression;
}
