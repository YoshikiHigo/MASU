package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch ���� default �G���g����\���N���X
 * 
 * @author higo
 */
public final class DefaultEntryInfo extends CaseEntryInfo {

    /**
     * �Ή����� switch �u���b�N����^���� default �G���g����������
     * 
     * @param ownerSwitchBlock �Ή����� switch�u���b�N
     */
    public DefaultEntryInfo(final SwitchBlockInfo ownerSwitchBlock, int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSwitchBlock, "default", fromLine, fromColumn, toLine, toColumn);
    }
}
