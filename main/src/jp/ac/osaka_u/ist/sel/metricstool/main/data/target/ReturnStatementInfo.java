package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


/**
 * return���̏���ۑ����邽�߂̃N���X
 * 
 * @author t-miyake
 *
 */
public class ReturnStatementInfo extends SingleStatementInfo {

    /**
     * return���̖߂�l��\�����ƈʒu����^���ď�����
     * @param returnedExpression
     * @param fromLine
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ReturnStatementInfo(final ExpressionInfo returnedExpression, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.returnedExpression = returnedExpression;
    }

    /**
     * return���̖߂�l��\������Ԃ�
     * 
     * @return return���̖߂�l��\����
     */
    public final ExpressionInfo getReturnedExpression() {
        return this.returnedExpression;
    }
    
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.returnedExpression.getVariableUsages();
    }

    /**
     * return���̖߂�l��\������ۑ����邽�߂̕ϐ�
     */
    private final ExpressionInfo returnedExpression;
}
