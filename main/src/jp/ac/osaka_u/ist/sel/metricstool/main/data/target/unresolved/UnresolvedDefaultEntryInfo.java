package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * switch ���� default �G���g����\���N���X
 * 
 * @author y-higo
 */
public final class UnresolvedDefaultEntryInfo extends UnresolvedCaseEntryInfo {

    /**
     * �Ή����� switch �u���b�N����^���� default �G���g����������
     * 
     * @param correspondingSwitchBlock
     */
    public UnresolvedDefaultEntryInfo(final UnresolvedSwitchBlockInfo correspondingSwitchBlock) {
        super(correspondingSwitchBlock);
    }
}
