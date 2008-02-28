package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class IfBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� if �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param conditionalClause ������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public IfBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, conditionalClause, outerSpace, fromLine, fromColumn, toLine,
                toColumn);
    }

    public void setSequentElseBlock(final ElseBlockInfo sequentElseBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentElseBlock) {
            throw new NullPointerException();
        }

        this.sequentElseBlock = sequentElseBlock;
    }

    public ElseBlockInfo getSequentElseBlock() {
        return this.sequentElseBlock;
    }

    /**
     * �Ή�����else�u���b�N�����݂��邩�ǂ����\��
     * @return �Ή�����else�u���b�N�����݂���Ȃ�true
     */
    public boolean hasElseBlock() {
        return null != this.sequentElseBlock;
    }

    private ElseBlockInfo sequentElseBlock;
}
