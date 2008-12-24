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
     * @param ownerSwitchBlock ���� case �G���g���������� switch �u���b�N
     * @param label ���� case �G���g���̃��x��
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final ExpressionInfo label,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerSwitchBlock) || (null == label)) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = label;
        
        this.label.setOwnerExecutableElement(this);
    }

    /**
     * �Ή����� switch �u���b�N����^���� case �G���g����������
     * 
     * @param ownerSwitchBlock ���� case �G���g���������� switch �u���b�N
     * @param breakStatement ���� case �G���g���� break ���������ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    protected CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = null;
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

    /**
     * ����case�G���g���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����case�G���g���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("case ");

        final ExpressionInfo expression = this.getLabel();
        sb.append(expression.getText());

        sb.append(":");

        return sb.toString();
    }

    /**
     * ��r����
     */
    @Override
    public int compareTo(ExecutableElementInfo o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
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

    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.getOwnerSwitchBlock();
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.getOwnerSwitchBlock().getOwnerMethod();
    }

    /**
     * ���� case �G���g���̂̃��x����Ԃ�
     * 
     * @return ���� case �G���g���̃��x��
     */
    public final ExpressionInfo getLabel() {
        return this.label;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final SwitchBlockInfo ownerSwitchBlock;

    /**
     * ���� case �G���g���̃��x����ۑ�����ϐ�
     */
    private ExpressionInfo label;
}
