package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * for �u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class ForBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� for �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ForBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }
}
