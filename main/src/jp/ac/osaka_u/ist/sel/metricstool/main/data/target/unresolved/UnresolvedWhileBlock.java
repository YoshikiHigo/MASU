package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ while �u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedWhileBlock extends UnresolvedBlock{

    /**
     * while �u���b�N����������
     */
    public UnresolvedWhileBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
