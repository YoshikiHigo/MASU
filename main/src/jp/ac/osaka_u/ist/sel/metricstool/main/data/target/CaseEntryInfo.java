package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch ���� case �G���g����\���N���X
 * 
 * @author higo
 */
public class CaseEntryInfo extends BlockInfo {

    /**
     * �Ή����� switch �u���b�N����^���� case �G���g����������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param ownerSwitchBlock ���� case �G���g���������� switch �u���b�N
     * @param breakStatement ���� case �G���g���� break ���������ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public CaseEntryInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final SwitchBlockInfo ownerSwitchBlock, final boolean breakStatement,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerSwitchBlock) {
            throw new NullPointerException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.breakStatement = breakStatement;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��Ԃ�
     * 
     * @return ���� case �G���g���������� switch �u���b�N
     */
    public final SwitchBlockInfo getOwnerSwitchBlock() {
        return this.ownerSwitchBlock;
    }

    /**
     * ���� case �G���g���� break ���������ǂ�����Ԃ�
     * 
     * @return break �������ꍇ��true�C�����Ȃ��ꍇ�� false
     */
    public final boolean hasBreakStatement() {
        return this.breakStatement;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final SwitchBlockInfo ownerSwitchBlock;

    /**
     * ���� case �G���g���� break ���������ǂ�����ۑ�����ϐ�
     */
    private boolean breakStatement;
}
