package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * finally �u���b�N����\���N���X
 * 
 * @author y-higo
 */
public final class FinallyBlockInfo extends BlockInfo {

    /**
     * �Ή����� try �u���b�N����^���� finally �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @param ownerTryBlock ���� finally �߂������� try �u���b�N
     */
    public FinallyBlockInfo(final TargetClassInfo ownerClass, final TargetMethodInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final TryBlockInfo ownerTryBlock) {

        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    public TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    private final TryBlockInfo ownerTryBlock;
}
