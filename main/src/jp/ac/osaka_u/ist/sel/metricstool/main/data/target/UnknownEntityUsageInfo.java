package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �������G���e�B�e�B���p��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class UnknownEntityUsageInfo extends ExpressionInfo {

    @Override
    public TypeInfo getType() {
        return UnknownTypeInfo.getInstance();
    }

    /**
     * �ʒu����^���āC�I�u�W�F�N�g��������
     * 
     * @param referencedName �Q�Ƃ���Ă��閼�O
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnknownEntityUsageInfo(final String[] referencedName,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn, final int parenthesesCount) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn, parenthesesCount);

        if (null == referencedName) {
            throw new IllegalArgumentException();
        }

        this.referencedName = referencedName;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
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
     * ���̖������G���e�B�e�B�g�p�̃e�L�X�g�\����Ԃ�
     * 
     * @return ���̖������G���e�B�e�B�g�p�̃e�L�X�g�\��
     */
    @Override
    public String getText() {
        final StringBuilder text = new StringBuilder();
        for (final String name : this.getReferencedName()) {
            text.append(name);
            text.append(".");
        }
        text.deleteCharAt(text.length() - 1);
        return this.getParenthesizedText(text.toString());
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    /**
     * �Q�Ƃ���Ă��镶�����Ԃ�
     * 
     * @return �Q�Ƃ���Ă��镶����
     */
    public String[] getReferencedName() {
        return this.referencedName;
    }

    @Override
    public ExecutableElementInfo copy() {
        final String[] referencedName = this.getReferencedName();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();
        final int parenthesesCount = this.getParenthesesCount();
        
        final UnknownEntityUsageInfo newEntityUsage = new UnknownEntityUsageInfo(referencedName,
                ownerMethod, fromLine, fromColumn, toLine, toColumn, parenthesesCount);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newEntityUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newEntityUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newEntityUsage;
    }

    private final String[] referencedName;
}
