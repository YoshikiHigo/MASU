package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


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
     * @param iteratorVariable �J��Ԃ��p�̕ϐ�
     * @param iteratorExpression �J��Ԃ��p�̎�
     */
    public ForeachBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final VariableDeclarationStatementInfo iteratorVariableDeclaration,
            final ExpressionInfo iteratorExpression) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        this.iteratorVariableDeclaration = iteratorVariableDeclaration;
        this.iteratorExpression = iteratorExpression;
    }

    /**
     * �J��Ԃ��p�̕ϐ���Ԃ�
     * 
     * @return�@�J��Ԃ��p�̕ϐ�
     */
    public VariableDeclarationStatementInfo getIteratorVariableDeclaration() {
        return this.iteratorVariableDeclaration;
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

        text.append(this.getIteratorVariableDeclaration().getText());
        text.deleteCharAt(text.length() - 1);

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

    private final VariableDeclarationStatementInfo iteratorVariableDeclaration;

    private final ExpressionInfo iteratorExpression;
}
