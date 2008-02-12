package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �����̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
public final class ParameterUsageInfo extends VariableUsageInfo<ParameterInfo> {

    /**
     * �g�p����Ă��������^���ăI�u�W�F�N�g��������
     * 
     * @param usedParameter �g�p����Ă������
     * @param reference �Q�Ƃł���ꍇ�� true, ����ł���ꍇ�� false
     */
    public ParameterUsageInfo(final ParameterInfo usedParameter, final boolean reference,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(usedParameter, reference, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public TypeInfo getType() {
        final ParameterInfo parameter = this.getUsedVariable();
        final TypeInfo usedVariableType = parameter.getType();
        return usedVariableType;
    }
}
