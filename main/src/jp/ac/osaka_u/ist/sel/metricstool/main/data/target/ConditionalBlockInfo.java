package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if�@�� while �ȂǁC�����߂��������u���b�N����\���N���X
 * 
 * @author higo
 *
 */
public abstract class ConditionalBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���ď�����
     * 
     * @param ownerClass ���̃u���b�N�����L����N���X
     * @param ownerMethod ���̃u���b�N�����L���郁�\�b�h
     * @param conditionalClause ���̃u���b�N�̏�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ConditionalBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == conditionalClause) {
            throw new NullPointerException();
        }

        this.conditionalClause = conditionalClause;
    }

    /**
     * ���̏����t�u���b�N�̏����߂�Ԃ�
     * 
     * @return�@���̏����t�u���b�N�̏�����
     */
    public final ConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    private final ConditionalClauseInfo conditionalClause;
}
