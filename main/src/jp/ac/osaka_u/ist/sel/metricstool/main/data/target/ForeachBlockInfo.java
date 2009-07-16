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
public final class ForeachBlockInfo extends BlockInfo {

    /**
     * �ʒu����^����Foreach�u���b�N��������
     * 
     * @param ownerClass �����N���X
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @param iteratorExpression �J��Ԃ��p�̎�
     */
    public ForeachBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final ExpressionInfo iteratorExpression) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        this.iteratorExpression = iteratorExpression;
    }

    /**
     * �J��Ԃ��p�̕ϐ���Ԃ�
     * 
     * @return�@�J��Ԃ��p�̕ϐ�
     */
    public LocalVariableInfo getIteratorVariable() {
        return this.iteratorVariable;
    }

    public void setIteratorVariable(final LocalVariableInfo iteratorVariable) {
        this.iteratorVariable = iteratorVariable;
    }

    /**
     * �J��Ԃ��p�̎���Ԃ�
     * 
     * @return �J��Ԃ��p�̎�
     */
    public ExpressionInfo getIteratorExpression() {
        return this.iteratorExpression;
    }

    /**
     * ����Foreach�u���b�N�̃e�L�X�g�\����Ԃ�
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();

        text.append("for (");

        final LocalVariableInfo iteratorVariable = this.getIteratorVariable();
        text.append(iteratorVariable.getType().getTypeName());
        text.append(" ");
        text.append(iteratorVariable.getName());

        text.append(":");

        text.append(this.getIteratorExpression().getText());

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

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        final Set<ClassTypeInfo> thrownExpressions = new HashSet<ClassTypeInfo>();
        thrownExpressions.addAll(super.getThrownExceptions());
        thrownExpressions.addAll(this.getIteratorExpression().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExpressions);
    }

    private LocalVariableInfo iteratorVariable;

    private final ExpressionInfo iteratorExpression;
}
