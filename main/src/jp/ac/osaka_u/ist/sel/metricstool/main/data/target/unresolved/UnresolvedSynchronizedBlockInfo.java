package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ synchronized ����\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedSynchronizedBlockInfo extends UnresolvedBlockInfo {

    /**
     * synchronized �u���b�N����������
     */
    public UnresolvedSynchronizedBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
