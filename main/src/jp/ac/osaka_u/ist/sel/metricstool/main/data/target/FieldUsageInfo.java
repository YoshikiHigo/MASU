package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �t�B�[���h�̎g�p��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class FieldUsageInfo extends EntityUsageInfo {

    /**
     * �g�p����Ă���t�B�[���h��^���ăI�u�W�F�N�g��������
     * 
     * @param usedField �g�p����Ă���t�B�[���h
     * @param reference �Q�Ƃł���ꍇ�� true, ����ł���ꍇ�� false
     */
    public FieldUsageInfo(final FieldInfo usedField, final boolean reference) {

        super();

        if (null == usedField) {
            throw new NullPointerException();
        }

        this.usedField = usedField;
        this.reference = reference;
    }

    @Override
    public TypeInfo getType() {
        return this.getUsedField().getType();
    }

    public FieldInfo getUsedField() {
        return this.usedField;
    }

    /**
     * ���̃t�B�[���h�g�p���Q�Ƃł��邩�ǂ�����Ԃ�
     * 
     * @return �Q�Ƃł���ꍇ�� true�C����̏ꍇ�� false
     */
    public boolean isReference() {
        return this.reference;
    }

    /**
     * ���̃t�B�[���h�g�p������ł��邩�ǂ�����Ԃ�
     * 
     * @return ����ł���ꍇ�� true�C�Q�Ƃł���ꍇ�� false
     */
    public boolean isAssignment() {
        return !this.reference;
    }

    private final FieldInfo usedField;

    private final boolean reference;
}
