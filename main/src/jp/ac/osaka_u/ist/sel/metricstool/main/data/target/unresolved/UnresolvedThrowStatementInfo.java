package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * ������throw������\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedThrowStatementInfo extends UnresolvedSingleStatementInfo<ThrowStatementInfo> {

    /**
     * �X���[����Ă����O��^���āC�I�u�W�F�N�g��������
     * 
     * @param thrownExpression �X���[����Ă����O
     */
    public UnresolvedThrowStatementInfo(final UnresolvedExpressionInfo<? extends ExpressionInfo> thrownExpression) {
        super();
        
        if(null == thrownExpression) {
            throw new IllegalArgumentException("thrownExpression is null");
        }
        
        this.thrownExpression = thrownExpression;
    }

    @Override
    public ThrowStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
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

        final ExpressionInfo thrownExpression = this.thrownExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ThrowStatementInfo(thrownExpression, fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }
    
    /**
     * throw���ɂ���ē��������O�̖���������ۑ�����ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> thrownExpression;

}
