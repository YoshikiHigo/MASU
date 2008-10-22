package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ switch �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedSwitchBlockInfo extends
        UnresolvedConditionalBlockInfo<SwitchBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCswitch �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedSwitchBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    /**
     * ���̖����� switch �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public SwitchBlockInfo resolve(final TargetClassInfo usingClass,
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

        // ����switch�I�u�W�F�N�g�̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo outerSpace = this.getOuterSpace().getResolved();

        this.resolvedInfo = new SwitchBlockInfo(usingClass, usingMethod, outerSpace, fromLine,
                fromColumn, toLine, toColumn);

        // �������u���b�N�������������C�����ς݃I�u�W�F�N�g�ɒǉ�
        this.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // ���[�J���ϐ������������C�����ς�case�G���g���I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        this.resolveVariableUsages(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        return this.resolvedInfo;
    }

    /**
     * ����switch �u���b�N�� case �G���g����ǉ�����
     * 
     * @param innerBlock �ǉ����� case �G���g��
     */
    //    @Override
    //    public void addInnerBlock(final UnresolvedBlockInfo<?> innerBlock) {
    //
    //        MetricsToolSecurityManager.getInstance().checkAccess();
    //        if (null == innerBlock) {
    //            throw new NullPointerException();
    //        }
    //
    //        if (!(innerBlock instanceof UnresolvedCaseEntryInfo)) {
    //            throw new IllegalArgumentException(
    //                    "Inner block of switch statement must be case or default entry!");
    //        }
    //
    //        super.addInnerBlock(innerBlock);
    //    }
}
