package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
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

    public UnresolvedReturnStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace) {
        super(ownerSpace);
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

        final LocalSpaceInfo ownerSpace = this.getOwnerSpace().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        final ExpressionInfo returnedExpression = null == this.returnedExpression ? new EmptyExpressionInfo(
                toLine, toColumn, toLine, toColumn)
                : this.returnedExpression.resolve(usingClass, usingMethod, classInfoManager,
                        fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ReturnStatementInfo(ownerSpace, returnedExpression, fromLine,
                fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    public void setReturnedExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.returnedExpression = returnedExpression;
    }

    /**
     * return���̖߂�l��\�����̖���������ۑ�����ϐ�
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression;

}
