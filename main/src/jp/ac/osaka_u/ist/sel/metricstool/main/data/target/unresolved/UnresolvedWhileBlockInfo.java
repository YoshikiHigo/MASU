package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ while �u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedWhileBlockInfo extends UnresolvedBlockInfo{

    /**
     * while �u���b�N����������
     */
    public UnresolvedWhileBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
