package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������if�u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedIfBlock extends UnresolvedBlock {

    /**
     * if �u���b�N����������
     */
    public UnresolvedIfBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
