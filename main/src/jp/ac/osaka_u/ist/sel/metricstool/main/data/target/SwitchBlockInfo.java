package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class SwitchBlockInfo extends ConditionalBlockInfo {

    /**
     * switch �u���b�N����������
     *
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param outerSpace �O���̃u���b�N
     * @param conditionalClause ������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public SwitchBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, conditionalClause, outerSpace, fromLine, fromColumn, toLine,
                toColumn);
    }
}
