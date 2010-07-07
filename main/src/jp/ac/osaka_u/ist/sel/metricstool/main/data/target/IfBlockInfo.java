package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class IfBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� if �u���b�N��������
     * 
     * @param ownerClass ���L�N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public IfBlockInfo(final TargetClassInfo ownerClass, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * else ����ǉ�����
     * 
     * @param sequentElseBlock �ǉ����� else ��
     */
    public void setSequentElseBlock(final ElseBlockInfo sequentElseBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentElseBlock) {
            throw new NullPointerException();
        }

        this.sequentElseBlock = sequentElseBlock;
    }

    /**
     * ����If���ɑΉ�����Else����Ԃ�
     * 
     * @return ����If���ɑΉ�����Else��
     */
    public ElseBlockInfo getSequentElseBlock() {
        return this.sequentElseBlock;
    }

    /**
     * �Ή�����else�u���b�N�����݂��邩�ǂ����\��
     * @return �Ή�����else�u���b�N�����݂���Ȃ�true
     */
    public boolean hasElseBlock() {
        return null != this.sequentElseBlock;
    }

    /**
     * ����if���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����if���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("if (");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

        sb.append(") {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    private ElseBlockInfo sequentElseBlock;
}
