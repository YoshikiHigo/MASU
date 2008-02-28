package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * else �u���b�N��\���N���X
 * 
 * @author higo
 */
public final class ElseBlockInfo extends BlockInfo {

    /**
     * �Ή����� if �u���b�N��^���āCelse �u���b�N����������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @param ownerIfBlock �Ή�����if�u���b�N
     */
    public ElseBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn, final IfBlockInfo ownerIfBlock) {

        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerIfBlock) {
            throw new NullPointerException();
        }

        this.ownerIfBlock = ownerIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��Ԃ�
     * 
     * @return ���� else �u���b�N�ƑΉ����� if �u���b�N
     */
    public IfBlockInfo getOwnerIfBlock() {
        return this.ownerIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final IfBlockInfo ownerIfBlock;
}
