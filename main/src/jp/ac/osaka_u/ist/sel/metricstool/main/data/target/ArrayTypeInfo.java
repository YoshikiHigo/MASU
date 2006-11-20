package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �z��^��\�����߂̃N���X�D
 * 
 * @author y-higo
 * 
 */
public class ArrayTypeInfo implements TypeInfo {

    /**
     * �^����Ԃ�
     */
    public String getName() {
        TypeInfo elementType = this.getElementType();
        int dimension = this.getDimension();

        StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
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
     * �I�u�W�F�N�g�̏��������s���D�z��̗v�f�̌^�Ɣz��̎������^�����Ȃ���΂Ȃ�Ȃ�
     * 
     * @param type �z��̗v�f�̌^
     * @param dimension �z��̎���
     */
    public ArrayTypeInfo(TypeInfo type, int dimension) {
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

}
