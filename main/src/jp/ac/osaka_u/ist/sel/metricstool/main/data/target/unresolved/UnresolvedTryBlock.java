package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ try �u���b�N��\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedTryBlock extends UnresolvedBlock {

    /**
     * try �u���b�N����������
     */
    public UnresolvedTryBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
