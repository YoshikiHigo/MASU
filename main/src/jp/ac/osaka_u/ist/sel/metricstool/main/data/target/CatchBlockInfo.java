package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * catch �u���b�N����\���N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class CatchBlockInfo extends BlockInfo {

    /**
     * �Ή����� try �u���b�N����^���� catch �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @param ownerTryBlock ����catch�߂�������try�u���b�N
     * @param caughtException ����catch�߂��L���b�`�����O
     */
    public CatchBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final TryBlockInfo ownerTryBlock) {

        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    public final TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }
    
    /**
     * catch�����O��\���ϐ��̏���Ԃ�
     * @return catch�����O��\���ϐ��̏��
     */
    public final LocalVariableInfo getCaughtException() {
        return caughtException;
    }
    
    public void setCaughtException(LocalVariableInfo caughtException) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caughtException) {
            throw new NullPointerException();
        }
        
        if(null != this.caughtException) {
            throw new IllegalStateException();
        }

        this.caughtException = caughtException;
    }

    /**
     * ����catch�@���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����catch�@���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("catch (UNDER IMPLEMENTATION) {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    private final TryBlockInfo ownerTryBlock;

    private LocalVariableInfo caughtException;
}
