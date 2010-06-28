package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * ���ʂŊ���ꂽ����\���N���X
 * 
 * @author higo
 *
 */
public final class ParenthesesExpressionInfo extends ExpressionInfo {

    /**
     * 
     */
    private static final long serialVersionUID = -742042745531180181L;

    /**
     * �I�u�W�F�N�g���������@
     * 
     * @param parentheticExpression ���ʓ��̎�
     * @param ownerMethod ���L���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ParenthesesExpressionInfo(final ExpressionInfo parentheticExpression,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == parentheticExpression) {
            throw new IllegalArgumentException();
        }
        this.parentheticExpression = parentheticExpression;
        this.parentheticExpression.setOwnerExecutableElement(this);
    }

    /**
     * ���ʂ̓����̎���Ԃ�
     * 
     * @return ���ʂ̓����̎�
     */
    public ExpressionInfo getParnentheticExpression() {
        return this.parentheticExpression;
    }

    /**
     * ���̌^��Ԃ�
     * 
     * @return ���̌^
     */
    @Override
    public TypeInfo getType() {
        return this.getParnentheticExpression().getType();
    }

    /**
     * ���̃e�L�X�g�\����Ԃ�
     * 
     * @return ���̃e�L�X�g�\��
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        text.append("(");

        final ExpressionInfo parentheticExpression = this.getParnentheticExpression();
        text.append(parentheticExpression.getText());

        text.append(")");

        return text.toString();
    }

    /**
     * �����̃��\�b�h�Ăяo���ꗗ��Ԃ�
     * 
     * @return �����̃��\�b�h�Ăяo���ꗗ
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getParnentheticExpression().getCalls();
    }

    /**
     * �����̕ϐ��g�p�ꗗ��Ԃ�
     * 
     * @return �����̕ϐ��g�p�ꗗ
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return this.getParnentheticExpression().getVariableUsages();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getParnentheticExpression().getThrownExceptions());
    }

    final ExpressionInfo parentheticExpression;
}
