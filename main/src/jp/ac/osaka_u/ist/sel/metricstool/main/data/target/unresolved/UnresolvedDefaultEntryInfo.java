package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch ���� default �G���g����\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedDefaultEntryInfo extends UnresolvedCaseEntryInfo {

    /**
     * �Ή����� switch �u���b�N����^���� default �G���g����������
     * 
     * @param correspondingSwitchBlock
     */
    public UnresolvedDefaultEntryInfo(final UnresolvedSwitchBlockInfo correspondingSwitchBlock) {
        super(correspondingSwitchBlock);
    }

    /**
     * ���̖����� default �G���g������������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManger �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public final DefaultEntryInfo resolveUnit(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return (DefaultEntryInfo) this.getResolvedUnit();
        }

        // ���� default �G���g���������� switch �����擾
        final UnresolvedSwitchBlockInfo unresolvedOwnerSwitchBlock = this.getOwnerSwitchBlock();
        final SwitchBlockInfo ownerSwitchBlock = unresolvedOwnerSwitchBlock.resolveUnit(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // break ���̗L�����擾
        final boolean breakStatement = this.hasBreakStatement();

        // ���� case �G���g���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new DefaultEntryInfo(usingClass, usingMethod, fromLine, fromColumn,
                toLine, toColumn, ownerSwitchBlock, breakStatement);
        return (DefaultEntryInfo) this.resolvedInfo;
    }
}
