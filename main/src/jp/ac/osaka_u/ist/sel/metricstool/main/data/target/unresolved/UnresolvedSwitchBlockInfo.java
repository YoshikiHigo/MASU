package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch �u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedSwitchBlockInfo extends UnresolvedBlockInfo {

    /**
     * switch �u���b�N����������
     * 
     */
    public UnresolvedSwitchBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }

    /**
     * ����switch �u���b�N�� case �G���g����ǉ�����
     * 
     * @param innerBlock �ǉ����� case �G���g��
     */
    @Override
    public void addInnerBlock(final UnresolvedBlockInfo innerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        if (!(innerBlock instanceof UnresolvedCaseEntryInfo)) {
            throw new IllegalArgumentException(
                    "Inner block of switch statement must be case or default entry!");
        }

        super.addInnerBlock(innerBlock);
    }
}
