package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ try �u���b�N��\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedTryBlockInfo extends UnresolvedBlockInfo {

    /**
     * try �u���b�N����������
     */
    public UnresolvedTryBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
