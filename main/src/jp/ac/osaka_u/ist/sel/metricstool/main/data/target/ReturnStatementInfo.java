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
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param ownerSpaceInfo ���𒼐ڏ��L������
     * @param returnedExpression
     * @param fromLine
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ReturnStatementInfo(final LocalSpaceInfo ownerSpaceInfo,
            final ExpressionInfo returnedExpression, int fromLine, int fromColumn, int toLine,
            int toColumn) {
        super(ownerSpaceInfo, fromLine, fromColumn, toLine, toColumn);

        if (null != returnedExpression) {
            this.returnedExpression = returnedExpression;
        } else {
            this.returnedExpression = new EmptyExpressionInfo(null, toLine, toColumn - 1, toLine,
                    toColumn - 1);
        }
        this.returnedExpression.setOwnerExecutableElement(this);
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
     * ����return���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ����return���̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("return ");

        final ExpressionInfo statement = this.getReturnedExpression();
        sb.append(statement.getText());

        sb.append(";");

        return sb.toString();
    }

    /**
     * return���̖߂�l��\������ۑ����邽�߂̕ϐ�
     */
    private final ExpressionInfo returnedExpression;
}
