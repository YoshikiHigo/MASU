package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����������߂�\���N���X
 * 
 * @author higo
 *
 */
public final class UnresolvedConditionalClauseInfo extends
        UnresolvedLocalSpaceInfo<ConditionalClauseInfo> {

    UnresolvedConditionalClauseInfo(
            final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerBlock) {
            throw new IllegalArgumentException("ownerBlock is null");
        }

        this.ownerBlock = ownerBlock;
        this.resolvedInfo = null;
    }

    @Override
    public ConditionalClauseInfo resolve(final TargetClassInfo usingClass,
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

        // ���� for �G���g���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ConditionalClauseInfo(usingClass, fromLine, fromColumn, toLine,
                toColumn);
        
        // �����߂ɑΉ������u���b�N���̏�������
        final ConditionalBlockInfo ownerBlock = this.ownerBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOwnerBlock(ownerBlock);


        // ���[�J���ϐ������������C�����ςݏ����߃I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        this.resolveVariableUsages(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        return this.resolvedInfo;
    }

    private final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerBlock;
}
