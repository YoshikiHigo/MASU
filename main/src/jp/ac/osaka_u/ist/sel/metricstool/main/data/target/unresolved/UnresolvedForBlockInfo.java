package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ for �u���b�N��\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedForBlockInfo extends UnresolvedBlockInfo {

    /**
     * for �u���b�N����������
     */
    public UnresolvedForBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
