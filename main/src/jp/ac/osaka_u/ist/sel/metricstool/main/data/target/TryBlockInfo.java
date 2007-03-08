package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * try �u���b�N��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class TryBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� try �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TryBlockInfo(final TargetClassInfo ownerClass, final TargetMethodInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }
}
