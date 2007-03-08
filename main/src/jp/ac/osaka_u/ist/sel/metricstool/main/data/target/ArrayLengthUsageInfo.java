package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �z��^�� length �t�B�[���h�g�p��\���N���X
 * 
 * @author y-higo
 * 
 */
public final class ArrayLengthUsageInfo extends EntityUsageInfo {

    /**
     * �e�ƂȂ�G���e�B�e�B�g�p��^���ăI�u�W�F�N�g��������
     * 
     * @param ownerEntityUsage �e�G���e�B�e�B
     */
    public ArrayLengthUsageInfo(final EntityUsageInfo ownerEntityUsage) {

        if (null == ownerEntityUsage) {
            throw new NullPointerException();
        }

        if (!(ownerEntityUsage instanceof ArrayTypeInfo)) {
            throw new IllegalArgumentException();
        }

        this.ownerEntityUsage = ownerEntityUsage;
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

    /**
     * �e�G���e�B�e�B��Ԃ�
     * 
     * @return �e�G���e�B�e�B
     */
    public EntityUsageInfo getOwnerEntity() {
        return this.ownerEntityUsage;
    }

    private final EntityUsageInfo ownerEntityUsage;
}
