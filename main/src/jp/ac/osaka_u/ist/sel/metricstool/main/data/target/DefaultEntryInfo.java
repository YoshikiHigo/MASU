package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch ���� default �G���g����\���N���X
 * 
 * @author y-higo
 */
public final class DefaultEntryInfo extends CaseEntryInfo {

    /**
     * �Ή����� switch �u���b�N����^���� default �G���g����������
     * 
     * @param ownerSwitchBlock
     */
    public DefaultEntryInfo(final TargetClassInfo ownerClass, final TargetMethodInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final SwitchBlockInfo ownerSwitchBlock, final boolean breakStatement) {
        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn, ownerSwitchBlock,
                breakStatement);
    }
}
