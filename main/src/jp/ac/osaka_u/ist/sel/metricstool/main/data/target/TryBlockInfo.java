package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * try �u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public class TryBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� try �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TryBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }
}
