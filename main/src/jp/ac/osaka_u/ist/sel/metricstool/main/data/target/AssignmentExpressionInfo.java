package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * This class represents assignment-statement
 * 
 * @author higo
 *
 */
public class AssignmentExpressionInfo {

    public AssignmentExpressionInfo(final VariableUsageInfo leftVariable,
            final ExpressionInfo rightExpression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        this.leftVariable = leftVariable;
        this.rightExpression = rightExpression;

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }
    
    public VariableUsageInfo getLeftVariable(){
        return this.leftVariable;
    }
    
    public ExpressionInfo getRightVariable(){
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

    private final VariableUsageInfo leftVariable;

    private final ExpressionInfo rightExpression;
}
