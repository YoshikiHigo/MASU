package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * for �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class ForBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� for �u���b�N��������
     * 
     * @param ownerClass �����N���X
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ForBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        this.initilizerExpressions = new TreeSet<ConditionInfo>();
        this.iteratorExpressions = new TreeSet<ExpressionInfo>();

    }

    /**
     * ����for���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����for���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("for (");

        final SortedSet<ConditionInfo> initializerExpressions = this.getInitializerExpressions();
        for (final ConditionInfo initializerExpression : initializerExpressions) {
            sb.append(initializerExpression.getText());
            sb.append(",");
        }

        sb.append(" ; ");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

        sb.append(" ; ");

        final SortedSet<ExpressionInfo> iteratorExpressions = this.getIteratorExpressions();
        for (final ExpressionInfo iteratorExpression : iteratorExpressions) {
            sb.append(iteratorExpression.getText());
            sb.append(",");
        }

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

    /**
     * for���̏���������ǉ�
     * @param initializerExpression ��������
     */
    public final void addInitializerExpressions(final ConditionInfo initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == initializerExpression) {
            throw new IllegalArgumentException("initializerExpression is null");
        }

        this.initilizerExpressions.add(initializerExpression);
    }

    /**
     * for���̍X�V����ǉ�
     * @param iteratorExpression �J��Ԃ���
     */
    public final void addIteratorExpressions(final ExpressionInfo iteratorExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null");
        }

        this.iteratorExpressions.add(iteratorExpression);
    }

    /**
     * ���������̃Z�b�g��Ԃ�
     * @return ���������̃Z�b�g
     */
    public final SortedSet<ConditionInfo> getInitializerExpressions() {
        return Collections.unmodifiableSortedSet(this.initilizerExpressions);
    }

    /**
     * �X�V���̃Z�b�g��Ԃ�
     * @return �X�V��
     */
    public final SortedSet<ExpressionInfo> getIteratorExpressions() {
        return Collections.unmodifiableSortedSet(this.iteratorExpressions);
    }

    /**
     * ����������ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ConditionInfo> initilizerExpressions;

    /**
     * �X�V����ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ExpressionInfo> iteratorExpressions;
}
