package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ do �u���b�N��\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedDoBlockInfo extends UnresolvedBlockInfo {

    /**
     * do �u���b�N����������
     */
    public UnresolvedDoBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
