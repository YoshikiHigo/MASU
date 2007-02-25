package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ catch �u���b�N����\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedCatchBlockInfo extends UnresolvedBlockInfo {

    /**
     * �Ή����� try �u���b�N����^���� catch �u���b�N��������
     * 
     * @param correspondingTryBlock
     */
    public UnresolvedCatchBlockInfo(final UnresolvedTryBlockInfo correspondingTryBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == correspondingTryBlock) {
            throw new NullPointerException();
        }

        this.correspondingTryBlock = correspondingTryBlock;
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    public UnresolvedTryBlockInfo getCorrespondingTryBlock() {
        return this.correspondingTryBlock;
    }

    private final UnresolvedTryBlockInfo correspondingTryBlock;
}
