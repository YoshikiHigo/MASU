package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ catch �u���b�N����\���N���X
 * 
 * @author higo
 */
public final class UnresolvedCatchBlockInfo extends UnresolvedBlockInfo<CatchBlockInfo> {

    /**
     * �Ή�����try������^���� catch �u���b�N��������
     * 
     * @param ownerTryBlock �Ή�����try��
     */
    public UnresolvedCatchBlockInfo(final UnresolvedTryBlockInfo ownerTryBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerTryBlock) {
            throw new IllegalArgumentException("ownerTryBlock is null");
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * ���̖����� catch �߂���������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManger �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public CatchBlockInfo resolve(final TargetClassInfo usingClass,
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

        // ���� catch �߂������� try �����擾
        final UnresolvedTryBlockInfo unresolvedOwnerTryBlock = this.getOwnerTryBlock();
        final TryBlockInfo ownerTryBlock = unresolvedOwnerTryBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // ���� catch�u���b�N�̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo outerSpace = this.getOuterSpace().getResolved();

        //�@�����ς� catch�u���b�N�I�u�W�F�N�g���쐬
        this.resolvedInfo = new CatchBlockInfo(usingClass, usingMethod, outerSpace, fromLine,
                fromColumn, toLine, toColumn, ownerTryBlock);

        //�@�����u���b�N�����������C�����ς�catch�u���b�N�I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedStatementInfo<?> unresolvedStatement : this.getStatements()) {
            final StatementInfo statement = unresolvedStatement.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addStatement(statement);
        }

        // ���[�J���ϐ������������C�����ς�catch�u���b�N�I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        return this.resolvedInfo;
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    public UnresolvedTryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    private final UnresolvedTryBlockInfo ownerTryBlock;
}
