package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * try �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class TryBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� try �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param ownerMethod ���L���\�b�h
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TryBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);

        this.sequentFinallyBlock = null;
        this.sequentCatchBlocks = new HashSet<CatchBlockInfo>();
    }

    /**
     * �Ή����� finally �����Z�b�g����
     * 
     * @param sequentFinallyBlock �Ή����� finally ��
     */
    public void setSequentFinallyBlock(final FinallyBlockInfo sequentFinallyBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentFinallyBlock) {
            throw new NullPointerException();
        }

        this.sequentFinallyBlock = sequentFinallyBlock;
    }

    /**
     * �Ή����� finally ����Ԃ�
     * 
     * @return �Ή����� finally ��
     */
    public FinallyBlockInfo getSequentFinallyBlock() {
        return this.sequentFinallyBlock;
    }

    /**
     * �Ή�����catch�u���b�N��ǉ�����
     * @param catchBlock �Ή�����catch�u���b�N
     */
    public void addSequentCatchBlock(final CatchBlockInfo catchBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == catchBlock) {
            throw new IllegalArgumentException("catchBlock is null");
        }

        this.sequentCatchBlocks.add(catchBlock);
    }

    /**
     * �Ή�����catch�u���b�N��Set��Ԃ�
     * @return �Ή�����catch�u���b�N��Set
     */
    public Set<CatchBlockInfo> getSequentCatchBlocks() {
        return this.sequentCatchBlocks;
    }

    /**
     * �Ή�����finally�u���b�N�����݂��邩�ǂ����Ԃ�
     * @return �Ή�����finally�u���b�N�����݂���Ȃ�true
     */
    public boolean hasFinallyBlock() {
        return null != this.sequentFinallyBlock;
    }

    /**
     * ����try���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ����try���̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("try {");
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
     * �Ή�����catch�u���b�N��ۑ�����ϐ�
     */
    private final Set<CatchBlockInfo> sequentCatchBlocks;

    private FinallyBlockInfo sequentFinallyBlock;
}
