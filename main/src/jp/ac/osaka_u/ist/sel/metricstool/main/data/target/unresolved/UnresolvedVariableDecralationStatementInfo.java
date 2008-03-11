package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedVariableDecralationStatementInfo implements PositionSetting,
        UnresolvedStatementInfo<VariableDeclarationStatementInfo> {

    public UnresolvedVariableDecralationStatementInfo(
            final UnresolvedLocalVariableInfo declaredVariable,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        
        if(null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }
        
        this.declaredLocalVarialbe = declaredVariable;
        this.initializationExpression = initializationExpression;
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public VariableDeclarationStatementInfo getResolved() {
        return this.resolvedInfo;
    }

    @Override
    public VariableDeclarationStatementInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalVariableInfo localVariable = this.declaredLocalVarialbe.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        if (null != this.initializationExpression) {
            final ExpressionInfo initializationExpression = this.initializationExpression.resolve(
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new VariableDeclarationStatementInfo(localVariable,
                    initializationExpression, fromLine, fromColumn, toLine, toColumn);
        } else {
            this.resolvedInfo = new VariableDeclarationStatementInfo(localVariable, fromLine,
                    fromColumn, toLine, toColumn);
        }
        
        return this.resolvedInfo;
    }

    @Override
    public void setFromColumn(int column) {
        this.fromColumn = column;
    }

    @Override
    public void setFromLine(int line) {
        this.fromLine = line;
    }

    @Override
    public void setToColumn(int column) {
        this.toColumn = column;
    }

    @Override
    public void setToLine(int line) {
        this.toLine = line;
    }

    @Override
    public final int getFromColumn() {
        return this.fromColumn;
    }

    @Override
    public final int getFromLine() {
        return this.fromLine;
    }

    @Override
    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public final int getToLine() {
        return this.toLine;
    }

    public final UnresolvedLocalVariableInfo getDeclaredLocalVariable() {
        return this.declaredLocalVarialbe;
    }

    /**
     * �錾����Ă���ϐ��̏���������Ԃ�
     * 
     * @return �錾����Ă���ϐ��̏��������D����������Ă��ꍇ��null
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * �錾����Ă���ϐ�������������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �錾����Ă���ϐ�������������Ă����true
     */
    public boolean isInitialized() {
        return null != this.initializationExpression;
    }

    /**
     * �錾����Ă���ϐ���\���t�B�[���h
     */
    private final UnresolvedLocalVariableInfo declaredLocalVarialbe;

    /**
     * �錾����Ă���ϐ��̏���������\���t�B�[���h
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression;

    /**
     * �J�n�s��\���ϐ�
     */
    private int fromLine;

    /**
     * �J�n���\���ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��\���ϐ�
     */
    private int toLine;

    /**
     * �I�����\���ϐ�
     */
    private int toColumn;

    /**
     * �����ςݏ���ۑ�����ϐ�
     */
    private VariableDeclarationStatementInfo resolvedInfo;

}
