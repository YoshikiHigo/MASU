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
     * @param correspondingTryBlock
     */
    public FinallyBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn, final TryBlockInfo correspondingTryBlock) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == correspondingTryBlock) {
            throw new NullPointerException();
        }

        this.correspondingTryBlock = correspondingTryBlock;
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    public TryBlockInfo getCorrespondingTryBlock() {
        return this.correspondingTryBlock;
    }

    private final TryBlockInfo correspondingTryBlock;
}
