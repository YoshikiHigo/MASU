package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


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
    public ArrayElementUsageInfo(final EntityUsageInfo ownerEntityUsage, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

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

        // �e���z��^�ł���C�Ɖ����ł��Ă���ꍇ
        if (ownerType instanceof ArrayTypeInfo) {
            // �z��̎����ɉ����Č^�𐶐�
            final int ownerArrayDimension = ((ArrayTypeInfo) ownerType).getDimension();
            final TypeInfo ownerArrayElement = ((ArrayTypeInfo) ownerType).getElementType();

            // �z�񂪓񎟌��ȏ�̏ꍇ�́C����������Ƃ����z���Ԃ��C�ꎟ���̏ꍇ�́C�v�f�̌^��Ԃ��D
            return 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                    ownerArrayDimension - 1) : ownerArrayElement;
        }

        // �z��^�łȂ��C���s���^�łȂ��ꍇ�͂�������
        assert ownerType instanceof UnknownTypeInfo : "ArrayElementUsage attaches unappropriate type!";

        return ownerType;
    }

    /**
     * ���̗v�f�̐e�C�܂�z��^�̃G���e�B�e�B�g�p��Ԃ�
     * 
     * @return ���̗v�f�̐e��Ԃ�
     */
    public EntityUsageInfo getOwnerEntityUsage() {
        return this.ownerEntityUsage;
    }
    
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getOwnerEntityUsage().getVariableUsages();
    }

    private final EntityUsageInfo ownerEntityUsage;
}
