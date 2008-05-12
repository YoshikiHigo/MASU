package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch ���� case �G���g����\���N���X
 * 
 * @author higo
 */
public class CaseEntryInfo extends UnitInfo implements StatementInfo {

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
    public CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final String name,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerSwitchBlock) || (null == name)) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.name = name;
    }

    /**
     * ���̕��icase �G���g���j�ŗp�����Ă���ϐ����p�̈ꗗ��Ԃ��D
     * �ǂ̕ϐ����p�����Ă��Ȃ��̂ŁC���set���Ԃ����
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return new HashSet<VariableUsageInfo<?>>();
    }

    @Override
    public int compareTo(StatementInfo o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return 1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return -1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return 1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return -1;
        } else if (this.getToLine() < o.getToLine()) {
            return 1;
        } else if (this.getToLine() > o.getToLine()) {
            return -1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return 1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return -1;
        }

        return 0;
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
     * ���� case �G���g���̖��O��Ԃ�
     * 
     * @return ���� case �G���g���̖��O
     */
    public final String getName() {
        return this.name;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final SwitchBlockInfo ownerSwitchBlock;

    /**
     * ���� case �G���g���̖��O��ۑ�����ϐ�
     */
    private String name;
}
