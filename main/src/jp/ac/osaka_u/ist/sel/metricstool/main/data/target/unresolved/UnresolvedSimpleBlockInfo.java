package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ �P���u���b�N({})��\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedSimpleBlockInfo extends UnresolvedBlockInfo {

    /**
     * �P���u���b�N����������
     */
    public UnresolvedSimpleBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
