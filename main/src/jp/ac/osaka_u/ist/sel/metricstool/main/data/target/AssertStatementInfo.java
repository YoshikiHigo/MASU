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

    /**
     * �A�T�[�g���𐶐�
     * 
     * @param ownerSpace �O���̃u���b�N
     * @param assertedExpression ���؎�
     * @param messageExpression ���b�Z�[�W
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public AssertStatementInfo(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo assertedExpression, final ExpressionInfo messageExpression,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == assertedExpression) {
            throw new IllegalArgumentException("assertedExpressoin is null.");
        }

        this.assertedExpression = assertedExpression;
        this.messageExpression = messageExpression;
        
        this.assertedExpression.setOwnerExecutableElement(this);
        if(null != this.messageExpression) {
            this.messageExpression.setOwnerExecutableElement(this);
        }
        
    }

    /**
     * ���؎���Ԃ�
     * 
     * @return�@���؎�
     */
    public final ExpressionInfo getAssertedExpression() {
        return this.assertedExpression;
    }

    /**
     * ���b�Z�[�W��Ԃ�
     * 
     * @return�@���b�Z�[�W
     */
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
