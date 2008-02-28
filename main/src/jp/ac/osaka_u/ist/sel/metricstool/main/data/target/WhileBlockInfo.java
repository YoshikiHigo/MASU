package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * while �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class WhileBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� while �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param conditionalClause ������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public WhileBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, conditionalClause, outerSpace, fromLine, fromColumn, toLine,
                toColumn);
    }
}
