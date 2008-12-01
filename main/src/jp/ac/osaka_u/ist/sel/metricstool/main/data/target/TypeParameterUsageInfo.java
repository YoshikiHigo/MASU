package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �^�p�����[�^�̎g�p��\���N���X
 * 
 * @author higo
 *
 */
public final class TypeParameterUsageInfo extends EntityUsageInfo {

    /**
     * �K�v�ȏ���^���āC�I�u�W�F�N�g��������
     * 
     * @param expression ���p����Ă��鎮
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TypeParameterUsageInfo(final ExpressionInfo expression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == expression) {
            throw new NullPointerException();
        }

        this.expression = expression;
    }

    @Override
    public TypeInfo getType() {
        return this.expression.getType();
    }

    /**
     * ���̌^�p�����[�^�g�p���̎���Ԃ�
     * 
     * @return ���̌^�p�����[�^�g�p���̎�
     */
    public ExpressionInfo getExpression() {
        return this.expression;
    }

    /**
     * �^�p�����[�^�̎g�p�ɕϐ��g�p���܂܂�邱�Ƃ͂Ȃ��̂ŋ�̃Z�b�g��Ԃ�
     * 
     * @return ��̃Z�b�g
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    private final ExpressionInfo expression;
}
