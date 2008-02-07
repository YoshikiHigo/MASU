package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * for �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class ForBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� for �u���b�N��������
     * 
     * @param ownerClass �����N���X
     * @param ownerMethod �������\�b�h
     * @param conditionalClause ������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ForBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, conditionalClause, fromLine, fromColumn, toLine, toColumn);
    }
}
