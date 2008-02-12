package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���[�J���ϐ��̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
public final class LocalVariableUsageInfo extends VariableUsageInfo<LocalVariableInfo> {

    /**
     * �g�p����Ă��郍�[�J���ϐ���^���ăI�u�W�F�N�g��������
     * 
     * @param usedLocalVariable �g�p����Ă��郍�[�J���ϐ�
     * @param reference �Q�Ƃł���ꍇ�� true, ����ł���ꍇ�� false
     */
    public LocalVariableUsageInfo(final LocalVariableInfo usedLocalVariable,
            final boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(usedLocalVariable, reference, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public TypeInfo getType() {
        final LocalVariableInfo usedVariable = this.getUsedVariable();
        final TypeInfo usedVariableType = usedVariable.getType();
        return usedVariableType;
    }
}
