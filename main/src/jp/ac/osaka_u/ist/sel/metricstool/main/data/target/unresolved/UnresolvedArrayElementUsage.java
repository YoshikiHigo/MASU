package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * �������z��ɑ΂���v�f�̎Q�Ƃ�\�����߂̃N���X�D�ȉ��̏������D
 * 
 * @author kou-tngt
 * @see UnresolvedTypeInfo
 */
public class UnresolvedArrayElementUsage implements UnresolvedTypeInfo {

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^��^����.
     * 
     * @param ownerArrayType �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    public UnresolvedArrayElementUsage(final UnresolvedTypeInfo ownerArrayType) {
        if (null == ownerArrayType) {
            throw new NullPointerException("ownerArrayType is null.");
        }

        this.ownerArrayType = ownerArrayType;
    }

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^��Ԃ�
     * 
     * @return �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    public UnresolvedTypeInfo getOwnerArrayType() {
        return this.ownerArrayType;
    }

    /**
     * ���̔z��v�f�̎Q�Ƃ̌^�Ƃ��Ă̖��O.
     */
    public String getTypeName() {
        return this.ownerArrayType.getTypeName() + "[]";
    }

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    private final UnresolvedTypeInfo ownerArrayType;

}
