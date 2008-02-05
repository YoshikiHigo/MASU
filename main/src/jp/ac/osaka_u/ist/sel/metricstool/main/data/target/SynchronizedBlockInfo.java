package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * synchronized �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class SynchronizedBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� synchronized �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public SynchronizedBlockInfo(final TargetClassInfo ownerClass,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }
}
