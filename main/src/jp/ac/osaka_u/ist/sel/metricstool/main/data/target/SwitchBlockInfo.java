package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * switch �u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public class SwitchBlockInfo extends BlockInfo {

    /**
     * switch �u���b�N����������
     * 
     */
    public SwitchBlockInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ����switch �u���b�N�� case �G���g����ǉ�����
     * 
     * @param innerBlock �ǉ����� case �G���g��
     */
    @Override
    public final void addInnerBlock(final BlockInfo innerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        if (!(innerBlock instanceof CaseEntryInfo)) {
            throw new IllegalArgumentException(
                    "Inner block of switch statement must be case or default entry!");
        }

        super.addInnerBlock(innerBlock);
    }
}

