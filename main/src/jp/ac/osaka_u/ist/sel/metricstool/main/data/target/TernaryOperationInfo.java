package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �O�����Z�g�p��\���N���X
 * 
 * @author t-miyake
 *
 */
public class TernaryOperationInfo extends EntityUsageInfo {

    /**
     * �O�����Z�̏�����(��ꍀ)�C��������true�̎��ɕԂ���鎮�C��������false�̎��ɕԂ���鎮(��O��)�C�J�n�ʒu�C�I���ʒu��^���ď�����
     * @param condtionalExpression ������(��ꍀ)
     * @param trueExpression ��������true�̂Ƃ��ɕԂ���鎮(���)
     * @param falseExpression ��������false�̂Ƃ��ɕԂ���鎮(��O��)
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TernaryOperationInfo(final ExpressionInfo condtionalExpression,
            ExpressionInfo trueExpression, ExpressionInfo falseExpression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == condtionalExpression || null == trueExpression || null == falseExpression) {
            throw new IllegalArgumentException();
        }

        this.conditionalExpression = condtionalExpression;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public TypeInfo getType() {
        return this.trueExpression.getType();
    }

    /**
     * �O�����Z�̏�����(��ꍀ)��Ԃ�
     * @return �O�����Z�̏�����(��ꍀ)
     */
    public ExpressionInfo getConditionalExpression() {
        return conditionalExpression;
    }

    /**
     * �O�����Z�̏�������true�̂Ƃ��ɕԂ���鎮(���)��Ԃ�
     * @return �O�����Z�̏�������true�̂Ƃ��ɕԂ���鎮(���)
     */
    public ExpressionInfo getTrueExpression() {
        return trueExpression;
    }

    /**
     * �O�����Z�̏�������false�Ƃ��ɕԂ���鎮(��O��)��Ԃ�
     * @return �O�����Z�̏�������false�̂Ƃ��ɕԂ���鎮(��O��)
     */
    public ExpressionInfo getFalseExpression() {
        return falseExpression;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * ���̎O�����Z�̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̎O�����Z�̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo condition = this.getConditionalExpression();
        sb.append(condition.getText());

        sb.append(" ? ");

        final ExpressionInfo trueExpression = this.getTrueExpression();
        sb.append(trueExpression.getText());

        sb.append(" : ");

        final ExpressionInfo falseExpression = this.getFalseExpression();
        sb.append(falseExpression.getText());

        return sb.toString();

    }

    /**
     * �O�����Z�̏�����(��ꍀ)��ۑ�����ϐ�
     */
    private final ExpressionInfo conditionalExpression;

    /**
     * �O�����Z�̏�������true�̂Ƃ��ɕԂ���鎮(���)��ۑ�����ϐ�
     */
    private final ExpressionInfo trueExpression;

    /**
     * �O�����Z�̏�������false�̂Ƃ��ɕԂ���鎮(��O��)��ۑ�����ϐ�
     */
    private final ExpressionInfo falseExpression;
}
