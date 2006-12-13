package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �z��^��\�����߂̃N���X�D
 * 
 * @author y-higo
 * 
 */
public final class ArrayTypeInfo implements TypeInfo {

    /**
     * �^����Ԃ�
     */
    public String getTypeName() {
        TypeInfo elementType = this.getElementType();
        int dimension = this.getDimension();

        StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getTypeName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof ArrayTypeInfo)) {
            return false;
        }

        TypeInfo elementTypeInfo = this.getElementType();
        TypeInfo correspondElementTypeInfo = ((ArrayTypeInfo) typeInfo).getElementType();
        if (!elementTypeInfo.equals(correspondElementTypeInfo)) {
            return false;
        } else {

            int dimension = this.getDimension();
            int correspondDimension = ((ArrayTypeInfo) typeInfo).getDimension();
            return dimension == correspondDimension;
        }
    }

    /**
     * �z��̗v�f�̌^��Ԃ�
     * 
     * @return �z��̗v�f�̌^
     */
    public TypeInfo getElementType() {
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
     * ArrayTypeInfo �̃C���X�^���X��Ԃ����߂̃t�@�N�g�����\�b�h�D
     * 
     * @param type �^��\���ϐ�
     * @param dimension ������\���ϐ�
     * @return �������� ArrayTypeInfo �I�u�W�F�N�g
     */
    public static ArrayTypeInfo getType(final TypeInfo type, final int dimension) {

        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        Key key = new Key(type, dimension);
        ArrayTypeInfo arrayType = ARRAY_TYPE_MAP.get(key);
        if (arrayType == null) {
            arrayType = new ArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayType);
        }

        return arrayType;
    }

    /**
     * �I�u�W�F�N�g�̏��������s���D�z��̗v�f�̌^�Ɣz��̎������^�����Ȃ���΂Ȃ�Ȃ�
     * 
     * @param type �z��̗v�f�̌^
     * @param dimension �z��̎���
     */
    private ArrayTypeInfo(final TypeInfo type, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 < dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.type = type;
        this.dimension = dimension;
    }

    /**
     * �z��̗v�f�̌^��ۑ�����ϐ�
     */
    private final TypeInfo type;

    /**
     * �z��̎�����ۑ�����ϐ�
     */
    private final int dimension;

    /**
     * ArrayTypeInfo �I�u�W�F�N�g���ꌳ�Ǘ����邽�߂� Map�D�I�u�W�F�N�g�̓t�@�N�g�����\�b�h�Ő��������D
     */
    private static final Map<Key, ArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, ArrayTypeInfo>();

    /**
     * �ϐ��̌^�Ǝ�����p���ăL�[�ƂȂ�N���X�D
     * 
     * @author y-higo
     */
    static class Key {

        /**
         * ���L�[
         */
        private final TypeInfo type;

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
        Key(final TypeInfo type, final int dimension) {

            if (null == type) {
                throw new NullPointerException();
            }
            if (1 < dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.type = type;
            this.dimension = dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�̃n�b�V���R�[�h��Ԃ��D
         */
        public int hashCode() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(this.type.getTypeName());
            buffer.append(this.dimension);
            String hashString = buffer.toString();
            return hashString.hashCode();
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public String getFirstKey() {
            return this.type.getTypeName();
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

            String firstKey = this.getFirstKey();
            String correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            } else {
                int secondKey = this.getSecondKey();
                int correspondSecondKey = ((Key) o).getSecondKey();
                return secondKey == correspondSecondKey;
            }
        }
    }
}
