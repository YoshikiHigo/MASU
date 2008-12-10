package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * assert����\���N���X
 * 
 * @author t-miyake�Chigo
 *
 */
public class AssertStatementInfo extends SingleStatementInfo {

    public AssertStatementInfo(final LocalSpaceInfo ownrSpace,
            final ExpressionInfo assertedExpression, final ExpressionInfo messageExpresssion,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownrSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == assertedExpression) {
            throw new IllegalArgumentException("assertedExpressoin is null.");
        }

        this.assertedExpression = assertedExpression;
        this.messageExpression = messageExpresssion;
    }

    public final ExpressionInfo getAssertedExpression() {
        return this.assertedExpression;
    }

    public final ExpressionInfo getMessageExpression() {
        return this.messageExpression;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        SortedSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> result = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        result.addAll(this.assertedExpression.getVariableUsages());
        result.addAll(this.messageExpression.getVariableUsages());
        return null;
    }

    /**
     * ���̃A�T�[�g���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̃A�T�[�g���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        StringBuilder sb = new StringBuilder();
        sb.append("assert ");

        final ExpressionInfo expression = this.getAssertedExpression();
        sb.append(expression.getText());

        sb.append(";");

        return sb.toString();
    }

    private final ExpressionInfo assertedExpression;

    private final ExpressionInfo messageExpression;

}
