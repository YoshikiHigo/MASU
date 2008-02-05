package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * do �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class DoBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� do �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public DoBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }
}
