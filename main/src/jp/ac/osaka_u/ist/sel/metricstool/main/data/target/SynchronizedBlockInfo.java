package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


/**
 * synchronized �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class SynchronizedBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� synchronized �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public SynchronizedBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final ExpressionInfo synchronizedExpression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == synchronizedExpression) {
            throw new IllegalArgumentException();
        }

        this.synchronizedExpression = synchronizedExpression;
        this.synchronizedExpression.setOwnerExecutableElement(this);
    }

    /**
     * ����synchronized���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����synchronized���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("synchronized (");

        final ExpressionInfo expression = this.getSynchronizedExpression();
        sb.append(expression.getText());

        sb.append(") {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    public final ExpressionInfo getSynchronizedExpression() {
        return synchronizedExpression;
    }

    private final ExpressionInfo synchronizedExpression;
}
