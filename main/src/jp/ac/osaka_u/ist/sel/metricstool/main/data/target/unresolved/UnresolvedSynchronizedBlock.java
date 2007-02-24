package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ synchronized ����\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedSynchronizedBlock extends UnresolvedBlock {

    /**
     * synchronized �u���b�N����������
     */
    public UnresolvedSynchronizedBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
