package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������if�u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedIfBlockInfo extends UnresolvedBlockInfo {

    /**
     * if �u���b�N����������
     */
    public UnresolvedIfBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
