package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������return������\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedReturnStatementInfo extends
        UnresolvedSingleStatementInfo<ReturnStatementInfo> {

    /**
     * return���̖߂�l��\�����̖���������^���ď�����
     * 
     * @param returnedExpression
     */
    public UnresolvedReturnStatementInfo(
            UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression) {
        super();
        
        if(null == returnedExpression) {
            throw new IllegalArgumentException("returnedExpression is null");
        }
        
        this.returnedExpression = returnedExpression;
    }

    @Override
    public ReturnStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ExpressionInfo returnedExpression = this.returnedExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ReturnStatementInfo(returnedExpression, fromLine, fromColumn,
                toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * return���̖߂�l��\�����̖���������ۑ�����ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression;

}
