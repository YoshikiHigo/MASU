package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �t�B�[���h�̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
public final class FieldUsageInfo extends VariableUsageInfo<FieldInfo> {

    /**
     * �g�p����Ă���t�B�[���h��^���ăI�u�W�F�N�g��������
     * 
     * @param usedField �g�p����Ă���t�B�[���h
     * @param reference �Q�Ƃł���ꍇ�� true, ����ł���ꍇ�� false
     */
    public FieldUsageInfo(final FieldInfo usedField, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);
    }
}
