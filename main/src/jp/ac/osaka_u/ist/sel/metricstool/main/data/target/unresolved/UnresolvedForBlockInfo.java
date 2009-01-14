package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ for �u���b�N��\���N���X
 * 
 * @author higo
 */
public final class UnresolvedForBlockInfo extends UnresolvedConditionalBlockInfo<ForBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCfor �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedForBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        this.initializerExpressions = new HashSet<UnresolvedConditionInfo<? extends ConditionInfo>>();
        this.iteratorExpressions = new HashSet<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
    }

    /**
     * ���̖����� for �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public ForBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

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

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ForBlockInfo(usingClass, outerSpace, fromLine, fromColumn, toLine,
                toColumn);

        // �������u���b�N�������������C�����ς݃I�u�W�F�N�g�ɒǉ�
        this.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // �������������������������C�����ς݃I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedConditionInfo<? extends ConditionInfo> initializerExpression : this.initializerExpressions) {
            this.resolvedInfo.addInitializerExpressions(initializerExpression.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager));
        }

        // �������X�V������ǉ����C�����ς݃I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> updateExpression : this.iteratorExpressions) {
            this.resolvedInfo.addIteratorExpressions(updateExpression.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager));
        }

        return this.resolvedInfo;
    }

    /**
     * ����������ǉ����郁�\�b�h
     * 
     * @param initializerExpression �ǉ����鏉������
     */
    public final void addInitializerExpression(
            final UnresolvedConditionInfo<? extends ConditionInfo> initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == initializerExpression) {
            throw new IllegalArgumentException("initailizerExpression is null.");
        }

        this.initializerExpressions.add(initializerExpression);
    }

    /**
     * �X�V����ǉ����郁�\�b�h
     * 
     * @param iteratorExpression �ǉ�����X�V��
     */
    public final void addIteratorExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> iteratorExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null.");
        }

        this.iteratorExpressions.add(iteratorExpression);
    }

    private final Set<UnresolvedConditionInfo<? extends ConditionInfo>> initializerExpressions;

    private final Set<UnresolvedExpressionInfo<? extends ExpressionInfo>> iteratorExpressions;

}
