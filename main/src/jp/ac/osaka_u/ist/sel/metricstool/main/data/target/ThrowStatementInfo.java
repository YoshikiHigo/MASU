package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * throw���̏���ۗL����N���X
 * 
 * @author t-miyake
 *
 */
public class ThrowStatementInfo extends SingleStatementInfo {

    /**
     * throw���ɂ���ē��������O��\�����ƈʒu����^���ď�����
     * 
     * @param ownerSpace ���𒼐ڏ��L������
     * @param thrownEpression throw���ɂ���ē��������O��\����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ThrowStatementInfo(final LocalSpaceInfo ownerSpace, ExpressionInfo thrownEpression,
            int fromLine, int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == thrownEpression) {
            throw new IllegalArgumentException("thrownExpression is null");
        }
        this.thrownEpression = thrownEpression;

        this.thrownEpression.setOwnerExecutableElement(this);
    }

    /**
     * throw���ɂ���ē��������O��\������Ԃ�
     * 
     * @return throw���ɂ���ē��������O��\����
     */
    public final ExpressionInfo getThrownExpression() {
        return this.thrownEpression;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.thrownEpression.getVariableUsages();
    }

    /**
     * ����throw���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ����throw���̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("throw ");

        final ExpressionInfo expression = this.getThrownExpression();
        sb.append(expression.getText());

        sb.append(";");

        return sb.toString();
    }

    /**
     * throw���ɂ���ē��������O��\����
     */
    private final ExpressionInfo thrownEpression;

}
