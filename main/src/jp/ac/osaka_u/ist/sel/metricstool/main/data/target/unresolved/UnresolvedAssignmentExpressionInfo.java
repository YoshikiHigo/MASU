package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssignmentExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������������\���N���X
 * 
 * @author higo
 */
public final class UnresolvedAssignmentExpressionInfo implements
        UnresolvedExpressionInfo<AssignmentExpressionInfo> {

    /**
     * �I�u�W�F�N�g��������
     * 
     */
    public UnresolvedAssignmentExpressionInfo() {
        this.leftVariable = null;
        this.rightExpression = null;
        this.resolved = null;
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolved;
    }

    @Override
    public AssignmentExpressionInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolved;
    }

    @Override
    public AssignmentExpressionInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� for���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ���ӂ�����
        final UnresolvedVariableUsageInfo<?> unresolvedLeftVariable = this.getLeftVariable();
        final VariableUsageInfo<?> leftVariable = unresolvedLeftVariable.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // �E�ӂ�����
        final UnresolvedExpressionInfo<?> unresolvedRightExpression = this.getRightExpression();
        final ExpressionInfo rightExpression = unresolvedRightExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolved = new AssignmentExpressionInfo(leftVariable, rightExpression, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolved;
    }

    /**
     * ���ӂ̕ϐ����Z�b�g����
     * 
     * @param leftVariable ���ӂ̕ϐ�
     */
    public void setLeftVariable(final UnresolvedVariableUsageInfo<?> leftVariable) {
        this.leftVariable = leftVariable;
    }

    /**
     * �E�ӂ̎����Z�b�g����
     * 
     * @param rightExpression �E�ӂ̎�
     */
    public void setRightVariable(final UnresolvedExpressionInfo<?> rightExpression) {
        this.rightExpression = rightExpression;
    }

    /**
     * ���ӂ̕ϐ���Ԃ�
     * 
     * @return ���ӂ̕ϐ�
     */
    public UnresolvedVariableUsageInfo<?> getLeftVariable() {
        return this.leftVariable;
    }

    /**
     * �E�ӂ̎���Ԃ�
     * 
     * @return �E�ӂ̎�
     */
    public UnresolvedExpressionInfo<?> getRightExpression() {
        return this.rightExpression;
    }

    /**
     * �J�n�s���Z�b�g����
     * 
     * @param fromLine �J�n�s
     */
    public final void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * �J�n����Z�b�g����
     * 
     * @param fromColumn �J�n��
     */
    public final void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * �I���s���Z�b�g����
     * 
     * @param toLine �I���s
     */
    public final void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * �I������Z�b�g����
     * 
     * @param toColumn �I����
     */
    public final void setToColumn(final int toColumn) {

        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    /**
     * ���̃��j�b�g�̍s����Ԃ�
     * 
     * @return ���j�b�g�̍s��
     */
    public final int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int toColumn;

    private UnresolvedVariableUsageInfo<?> leftVariable;

    private UnresolvedExpressionInfo<?> rightExpression;

    private AssignmentExpressionInfo resolved;
}
