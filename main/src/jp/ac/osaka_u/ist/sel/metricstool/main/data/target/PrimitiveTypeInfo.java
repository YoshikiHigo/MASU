package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �v���~�e�B�u�^��\�����߂̃N���X�D�v���~�e�B�u�^�̓v���O���~���O����ɂ���Ē񏥂���Ă���^�ł��邽�߁C ���[�U���V���炵���^����邱�Ƃ��ł��Ȃ��悤�C�R���X�g���N�^�� private
 * �ɂ��Ă���D
 * 
 * @author y-higo
 * 
 */
public class PrimitiveTypeInfo implements TypeInfo {

    /**
     * boolean �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BOOLEAN = new PrimitiveTypeInfo("boolean");

    /**
     * byte �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BYTE = new PrimitiveTypeInfo("byte");

    /**
     * short �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo SHORT = new PrimitiveTypeInfo("short");

    /**
     * long �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo INT = new PrimitiveTypeInfo("int");

    /**
     * long �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo LONG = new PrimitiveTypeInfo("long");

    /**
     * float �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo FLOAT = new PrimitiveTypeInfo("float");

    /**
     * double �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo DOUBLE = new PrimitiveTypeInfo("double");

    /**
     * ���̌^����Ԃ��D
     */
    public String getName() {
        return this.name;
    }

    /**
     * �I�u�W�F�N�g�Ɍ^����^���ď���������D �^���͌Œ�ł��邽�߁C�O������̓I�u�W�F�N�g�𐶐��ł��Ȃ��悤�ɂ��Ă���D
     * 
     * @param name �^��
     */
    private PrimitiveTypeInfo(String name) {
        this.name = name;
    }

    /**
     * �^����\���ϐ��D
     */
    private final String name;
}
