package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if����while���̏����߂�\���N���X
 * 
 * @author higo
 *
 */
public final class ConditionalClauseInfo extends LocalSpaceInfo {

    /**
     * �ʒu����^���ď�����
     * 
     * @param ownerClass ���̃u���b�N�����L����N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ConditionalClauseInfo(final TargetClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();

    }

    /**
     * ���̏����߂����u���b�N��Ԃ�
     * 
     * @return ���̏ꌚ�݂����u���b�N
     */
    public final ConditionalBlockInfo getOwnerBlock() {
        return this.ownerBlock;
    }
    
    /**
     * ���̏����߂����u���b�N���Z�b�g����
     * 
     * @param ownerBlock ���̏����߂����u���b�N 
     */
    public final void setOwnerBlock(final ConditionalBlockInfo ownerBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if(null == ownerBlock) {
            throw new IllegalArgumentException("ownerBlock is null");
        }
        
        this.ownerBlock = ownerBlock;
    }

    private ConditionalBlockInfo ownerBlock;
}
