package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �������̏����߂̏���\���N���X
 * 
 * @author t-miyake
 *
 */
public class ConditionalClauseInfo extends UnitInfo {

    /**
     * �����߂�ێ�����u���b�N���ƈʒu���
     * @param ownerConditionalBlock �������̏����߂�ێ�����u���b�N
     * @param condition �����߂ɋL�q����Ă������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n�ʒu
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ConditionalClauseInfo(final ConditionalBlockInfo ownerConditionalBlock,
            final ConditionInfo condition, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == ownerConditionalBlock) {
            throw new IllegalArgumentException();
        }

        this.ownerCondtionalBlock = ownerConditionalBlock;
        if (null != condition) {
            this.condition = condition;
            if (this.condition instanceof ExpressionInfo) {
                ((ExpressionInfo) this.condition)
                        .setOwnerExecutableElement(this.ownerCondtionalBlock);
            }
        } else {
            this.condition = new EmptyExpressionInfo(null, toLine, toColumn - 1, toLine,
                    toColumn - 1);
            ((ExpressionInfo) this.condition).setOwnerExecutableElement(ownerConditionalBlock);
        }
    }

    /**
     * �����߂�ێ�����u���b�N��Ԃ�
     * @return �����߂�ێ�����u���b�N
     */
    public final ConditionalBlockInfo getOwnerConditionalBlock() {
        return this.ownerCondtionalBlock;
    }

    /**
     * �����߂ɋL�q����Ă��������Ԃ�
     * @return �����߂ɋL�q����Ă������
     */
    public final ConditionInfo getCondition() {
        return this.condition;
    }

    public final String getText() {
        return "";
    }

    /**
     * �����߂�ێ�����u���b�N��\���ϐ�
     */
    private final ConditionalBlockInfo ownerCondtionalBlock;

    /**
     * �����߂ɋL�q����Ă��������\���ϐ�
     */
    private final ConditionInfo condition;
}
