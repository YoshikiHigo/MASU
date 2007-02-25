package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch ���� case �G���g����\���N���X
 * 
 * @author y-higo
 */
public class UnresolvedCaseEntryInfo extends UnresolvedBlockInfo {

    /**
     * �Ή����� switch �u���b�N����^���� case �G���g����������
     * 
     * @param correspondingSwitchBlock
     */
    public UnresolvedCaseEntryInfo(final UnresolvedSwitchBlockInfo correspondingSwitchBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == correspondingSwitchBlock) {
            throw new NullPointerException();
        }

        this.correspondingSwitchBlock = correspondingSwitchBlock;
        this.breakStatement = false;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��Ԃ�
     * 
     * @return ���� case �G���g���������� switch �u���b�N
     */
    public final UnresolvedSwitchBlockInfo getCorrespondingSwitchBlock() {
        return this.correspondingSwitchBlock;
    }

    /**
     * ���� case �G���g���� break ���������ǂ�����ݒ肷��
     * 
     * @param breakStatement break �������ꍇ�� true, �����Ȃ��ꍇ�� false
     */
    public final void setHasBreak(final boolean breakStatement) {
        this.breakStatement = breakStatement;
    }

    /**
     * ���� case �G���g���� break ���������ǂ�����Ԃ�
     * 
     * @return break �������ꍇ��true�C�����Ȃ��ꍇ�� false
     */
    public final boolean hasBreakStatement() {
        return this.breakStatement;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedSwitchBlockInfo correspondingSwitchBlock;

    /**
     * ���� case �G���g���� break ���������ǂ�����ۑ�����ϐ�
     */
    private boolean breakStatement;
}
