package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


/**
 * finally �u���b�N����\���N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class FinallyBlockInfo extends BlockInfo {

    /**
     * �Ή����� try �u���b�N����^���� finally �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @param ownerTryBlock ���� finally �߂������� try �u���b�N
     */
    public FinallyBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final TryBlockInfo ownerTryBlock) {

        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * ����finally�߂̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����finally�߂̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("finally {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    public TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    private final TryBlockInfo ownerTryBlock;
}
