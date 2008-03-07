package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �z��^�� length �t�B�[���h�g�p��\���N���X
 * 
 * @author higo
 * 
 */
public final class ArrayLengthUsageInfo extends FieldUsageInfo {

    /**
     * �e�ƂȂ�G���e�B�e�B�g�p��^���ăI�u�W�F�N�g��������
     * 
     * @param ownerEntityUsage �e�G���e�B�e�B
     */
    public ArrayLengthUsageInfo(final EntityUsageInfo ownerUsage, final ArrayTypeInfo ownerType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerUsage, ownerType, new ArrayLengthInfo(ownerType), true, fromLine, fromColumn, toLine,
                toColumn);
    }

    /**
     * length �t�B�[���h�g�p�̌^��Ԃ��D
     * 
     * @return length �t�B�[���h�g�p�̌^
     */
    @Override
    public TypeInfo getType() {
        return PrimitiveTypeInfo.INT;
    }
}
