package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * else �u���b�N��\���N���X
 * 
 * @author y-higo
 */
public final class ElseBlockInfo extends BlockInfo {

    /**
     * �Ή����� if �u���b�N��^���āCelse �u���b�N����������
     * 
     * @param correspondingIfBlock
     */
    ElseBlockInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final IfBlockInfo correspondingIfBlock) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == correspondingIfBlock) {
            throw new NullPointerException();
        }

        this.correspondingIfBlock = correspondingIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��Ԃ�
     * 
     * @return ���� else �u���b�N�ƑΉ����� if �u���b�N
     */
    public IfBlockInfo getCorrespondingIfBlock() {
        return this.correspondingIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final IfBlockInfo correspondingIfBlock;
}
