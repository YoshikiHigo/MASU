package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * assert���̖���������\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedAssertStatementInfo extends
        UnresolvedSingleStatementInfo<AssertStatementInfo> {

    public UnresolvedAssertStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace) {
        super(ownerSpace);
    }

    @Override
    public AssertStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * ���؂̌��ʂ�false�ł������Ƃ��ɏo�͂���郁�b�Z�[�W��\�����̖���������ݒ肷��
     * @param messageExpression
     */
    public final void setMessageExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> messageExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.messsageExpression = messageExpression;
    }

    /**
     * ���؎��̖���������ݒ肷��
     * @param assertedExpression ���؎��̖��������
     */
    public final void setAsserttedExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.assertedExpression = assertedExpression;
    }

    /**
     * ���؎��̖���������ۑ�����ϐ�
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression;

    /**
     * ���؎���false��Ԃ��Ƃ��ɏo�͂���郁�b�Z�[�W��\�����̖���������ۑ����邽�߂̕ϐ�
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> messsageExpression;

}
