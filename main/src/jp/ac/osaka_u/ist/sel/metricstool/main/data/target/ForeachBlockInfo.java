package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;


/**
 * Foreach�u���b�N��\���N���X
 * 
 * @author higo
 *
 */
public final class ForeachBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^����Foreach�u���b�N��������
     * 
     * @param ownerClass �����N���X
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ForeachBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ����Foreach�u���b�N�̃e�L�X�g�\����Ԃ�
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();

        text.append("for (");

        text.append(this.getConditionalClause().getText());

        text.append(") {");
        text.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            text.append(statement.getText());
            text.append(System.getProperty("line.separator"));
        }

        text.append("}");

        return text.toString();
    }

    @Override
    public boolean isLoopStatement() {
        return true;
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        final Set<ClassTypeInfo> thrownExpressions = new HashSet<ClassTypeInfo>();
        thrownExpressions.addAll(super.getThrownExceptions());
        thrownExpressions.addAll(this.getConditionalClause().getCondition().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExpressions);
    }
}
