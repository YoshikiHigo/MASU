package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �z��v�f�̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
public class ArrayElementUsageInfo extends EntityUsageInfo {

    /**
     * �v�f�̐e�C�܂�z��^�̃G���e�B�e�B�g�p��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerEntityUsage �z��^�̃G���e�B�e�B�g�p
     */
    public ArrayElementUsageInfo(final EntityUsageInfo ownerEntityUsage) {

        super();

        if (null == ownerEntityUsage) {
            throw new NullPointerException();
        }

        this.ownerEntityUsage = ownerEntityUsage;
    }

    /**
     * ���̔z��v�f�̎g�p�̌^��Ԃ�
     * 
     * @return ���̔z��v�f�̎g�p�̌^
     */
    @Override
    public TypeInfo getType() {

        final TypeInfo ownerType = this.getOwnerEntityUsage().getType();
        assert ownerType instanceof ArrayTypeInfo : "ArrayElementUsage attaches unappropriate type!";

        // �z��̎����ɉ����Č^�𐶐�
        final int ownerArrayDimension = ((ArrayTypeInfo) ownerType).getDimension();
        final EntityUsageInfo ownerArrayElement = ((ArrayTypeInfo) ownerType).getElement();

        // �z�񂪓񎟌��ȏ�̏ꍇ�́C����������Ƃ����z���Ԃ��C�ꎟ���̏ꍇ�́C�v�f�̌^��Ԃ��D
        return 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                ownerArrayDimension - 1).getType() : ownerArrayElement.getType();
    }

    /**
     * ���̗v�f�̐e�C�܂�z��^�̃G���e�B�e�B�g�p��Ԃ�
     * 
     * @return ���̗v�f�̐e��Ԃ�
     */
    public EntityUsageInfo getOwnerEntityUsage() {
        return this.ownerEntityUsage;
    }

    private final EntityUsageInfo ownerEntityUsage;
}
