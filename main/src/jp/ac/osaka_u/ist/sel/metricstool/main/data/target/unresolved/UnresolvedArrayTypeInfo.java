package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������z��^��\�����߂̃N���X�D�ȉ��̏������D
 * <ul>
 * <li>�������^ (UnresolvedTypeInfo)</li>
 * <li>���� (int)</li>
 * </ul>
 * 
 * @author y-higo
 * @see UnresolvedTypeInfo
 */
public final class UnresolvedArrayTypeInfo implements UnresolvedTypeInfo {

    /**
     * �^����Ԃ�
     */
    public String getTypeName() {
        final UnresolvedTypeInfo elementType = this.getElementType();
        final int dimension = this.getDimension();

        final StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getTypeName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final UnresolvedTypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof UnresolvedArrayTypeInfo)) {
            return false;
        }

        final UnresolvedTypeInfo elementTypeInfo = this.getElementType();
        final UnresolvedTypeInfo correspondElementTypeInfo = ((UnresolvedArrayTypeInfo) typeInfo)
                .getElementType();
        if (!elementTypeInfo.equals(correspondElementTypeInfo)) {
            return false;
        } else {

            final int dimension = this.getDimension();
            final int correspondDimension = ((UnresolvedArrayTypeInfo) typeInfo).getDimension();
            return dimension == correspondDimension;
        }
    }

    /**
     * �z��̗v�f�̖������^��Ԃ�
     * 
     * @return �z��̗v�f�̖������^
     */
    public UnresolvedTypeInfo getElementType() {
        return this.type;
    }

    /**
     * �z��̎�����Ԃ�
     * 
     * @return �z��̎���
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * ���̃C���X�^���X���\���z��̎�����1�傫�������z���\���C���X�^���X��Ԃ��D
     * @return ���̃C���X�^���X���\���z��̎�����1�傫�������z��
     */
    public UnresolvedArrayTypeInfo getDimensionInclementedArrayType() {
        return getType(getElementType(), getDimension() + 1);
    }

    /**
     * UnresolvedArrayTypeInfo �̃C���X�^���X��Ԃ����߂̃t�@�N�g�����\�b�h�D
     * 
     * @param type �������^��\���ϐ�
     * @param dimension ������\���ϐ�
     * @return �������� UnresolvedArrayTypeInfo �I�u�W�F�N�g
     */
    public static UnresolvedArrayTypeInfo getType(final UnresolvedTypeInfo type, final int dimension) {

        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        final Key key = new Key(type, dimension);
        UnresolvedArrayTypeInfo arrayType = ARRAY_TYPE_MAP.get(key);
        if (arrayType == null) {
            arrayType = new UnresolvedArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayType);
        }

        return arrayType;
    }

    /**
     * �������z��^�I�u�W�F�N�g�̏��������s���D�z��̗v�f�̖������^�Ɣz��̎������^�����Ȃ���΂Ȃ�Ȃ�
     * 
     * @param type �z��̗v�f�̖������^
     * @param dimension �z��̎���
     */
    private UnresolvedArrayTypeInfo(final UnresolvedTypeInfo type, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.type = type;
        this.dimension = dimension;
    }

    /**
     * �z��̗v�f�̌^��ۑ�����ϐ�
     */
    private final UnresolvedTypeInfo type;

    /**
     * �z��̎�����ۑ�����ϐ�
     */
    private final int dimension;

    /**
     * UnresolvedArrayTypeInfo �I�u�W�F�N�g���ꌳ�Ǘ����邽�߂� Map�D�I�u�W�F�N�g�̓t�@�N�g�����\�b�h�Ő��������D
     */
    private static final Map<Key, UnresolvedArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, UnresolvedArrayTypeInfo>();

    /**
     * �ϐ��̌^�Ǝ�����p���ăL�[�ƂȂ�N���X�D
     * 
     * @author y-higo
     */
    static class Key {

        /**
         * ���L�[
         */
        private final UnresolvedTypeInfo type;

        /**
         * ���L�[
         */
        private final int dimension;

        /**
         * ���C���L�[����C�L�[�I�u�W�F�N�g�𐶐�����
         * 
         * @param type ���L�[
         * @param dimension ���L�[
         */
        Key(final UnresolvedTypeInfo type, final int dimension) {

            if (null == type) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.type = type;
            this.dimension = dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�̃n�b�V���R�[�h��Ԃ��D
         */
        public int hashCode() {
            final StringBuffer buffer = new StringBuffer();
            buffer.append(this.type.getTypeName());
            buffer.append(this.dimension);
            final String hashString = buffer.toString();
            return hashString.hashCode();
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public UnresolvedTypeInfo getFirstKey() {
            return this.type;
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public int getSecondKey() {
            return this.dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�ƈ����Ŏw�肳�ꂽ�I�u�W�F�N�g������������Ԃ��D
         */
        public boolean equals(Object o) {

            if (null == o) {
                throw new NullPointerException();
            }

            final UnresolvedTypeInfo firstKey = this.getFirstKey();
            final UnresolvedTypeInfo correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            } else {
                final int secondKey = this.getSecondKey();
                final int correspondSecondKey = ((Key) o).getSecondKey();
                return secondKey == correspondSecondKey;
            }
        }
    }
}
