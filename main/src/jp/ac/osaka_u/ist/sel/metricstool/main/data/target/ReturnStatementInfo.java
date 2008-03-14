package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


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

        if (null == returnedExpression) {
            throw new IllegalArgumentException("returnedExpression is null");
        }

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

    /**
     * return���̖߂�l��\������ۑ����邽�߂̕ϐ�
     */
    private final ExpressionInfo returnedExpression;
}
