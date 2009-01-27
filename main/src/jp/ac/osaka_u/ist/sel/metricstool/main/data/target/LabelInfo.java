package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * ���x����`��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class LabelInfo extends UnitInfo implements StatementInfo {

    /**
     * �ʒu����^���ă��x���I�u�W�F�N�g��������
     * 
     * @param name ���x����
     * @param labeledStatement ���̃��x�����t������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public LabelInfo(final String name, final StatementInfo labeledStatement, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.name = name;
        this.labeledStatement = labeledStatement;
    }

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
     * ���̕��i���x����`�j�ŗp�����Ă���ϐ����p�̈ꗗ��Ԃ��D
     * �ǂ̕ϐ����p�����Ă��Ȃ��̂ŁC���set���Ԃ����
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * �ϐ���`��Set��Ԃ�
     * 
     * @return �ϐ���`��Set��Ԃ�
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * ���̃��x���t�����̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̃��x���t�����̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(" : ");
        return sb.toString();
    }

    /**
     * ���̃��x���̖��O��Ԃ�
     * 
     * @return ���̃��x���̖��O
     */
    public String getName() {
        return this.name;
    }

    /**
     * ���̃��x�����t��������Ԃ�
     * 
     * @return ���̃��x�����t������
     */
    public StatementInfo getLabeledStatement() {
        return this.labeledStatement;
    }

    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.labeledStatement.getOwnerSpace();
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.labeledStatement.getOwnerMethod();
    }

    /**
     * ���x���̖��O��\���ϐ�
     */
    private final String name;

    /**
     * ���̃��x�����t��������\���ϐ���Ԃ�
     */
    private final StatementInfo labeledStatement;
}
