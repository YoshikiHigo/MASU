package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ do �u���b�N��\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedDoBlock extends UnresolvedBlock {

    /**
     * do �u���b�N����������
     */
    public UnresolvedDoBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
