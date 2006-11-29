package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �v���~�e�B�u�^��\�����߂̃N���X�D�v���~�e�B�u�^�̓v���O���~���O����ɂ���Ē񏥂���Ă���^�ł��邽�߁C ���[�U���V���炵���^����邱�Ƃ��ł��Ȃ��悤�C�R���X�g���N�^�� private
 * �ɂ��Ă���D
 * 
 * @author y-higo
 * 
 */
public final class PrimitiveTypeInfo implements TypeInfo {

    /**
     * boolean ��\���萔
     */
    public static final String BOOLEAN_STRING = new String("boolean");

    /**
     * byte ��\���萔
     */
    public static final String BYTE_STRING = new String("byte");

    /**
     * char ��\���萔
     */
    public static final String CHAR_STRING = new String("char");

    /**
     * short ��\���萔
     */
    public static final String SHORT_STRING = new String("short");

    /**
     * int ��\���萔
     */
    public static final String INT_STRING = new String("int");

    /**
     * long ��\���萔
     */
    public static final String LONG_STRING = new String("long");

    /**
     * float ��\���萔
     */
    public static final String FLOAT_STRING = new String("float");

    /**
     * double ��\���萔
     */
    public static final String DOUBLE_STRING = new String("double");

    /**
     * �s���Ȍ^��\���萔
     */
    public static final String UNKNOWN_STRING = new String("unknown");

    /**
     * boolean �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BOOLEAN = new PrimitiveTypeInfo(BOOLEAN_STRING);

    /**
     * byte �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BYTE = new PrimitiveTypeInfo(BYTE_STRING);

    /**
     * char �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo CHAR = new PrimitiveTypeInfo(CHAR_STRING);

    /**
     * short �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo SHORT = new PrimitiveTypeInfo(SHORT_STRING);

    /**
     * int �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo INT = new PrimitiveTypeInfo(INT_STRING);

    /**
     * long �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo LONG = new PrimitiveTypeInfo(LONG_STRING);

    /**
     * float �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo FLOAT = new PrimitiveTypeInfo(FLOAT_STRING);

    /**
     * double �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo DOUBLE = new PrimitiveTypeInfo(DOUBLE_STRING);

    /**
     * �s���Ȍ^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo UNKNOWN = new PrimitiveTypeInfo(UNKNOWN_STRING);

    /**
     * {@link PrimitiveTypeInfo}�̃t�@�N�g�����\�b�h�D
     * 
     * @param typeName �쐬����^�̖��O
     * @return �w�肳�ꂽ����\�� {@link PrimitiveTypeInfo} �̃C���X�^���X�D
     */
    public static PrimitiveTypeInfo getType(final String typeName) {

        if (null == typeName) {
            throw new NullPointerException();
        }
        
        if (typeName.equals(BOOLEAN_STRING)) {
            return BOOLEAN;
        } else if (typeName.equals(BYTE_STRING)) {
            return BYTE;
        } else if (typeName.equals(CHAR_STRING)) {
            return CHAR;
        } else if (typeName.equals(SHORT_STRING)) {
            return SHORT;
        } else if (typeName.equals(INT_STRING)) {
            return INT;
        } else if (typeName.equals(LONG_STRING)) {
            return LONG;
        } else if (typeName.equals(FLOAT_STRING)) {
            return FLOAT;
        } else if (typeName.equals(DOUBLE_STRING)) {
            return DOUBLE;
        } else {
            assert (false) : "Illegal state: primitive type " + typeName + " is unknown.";
        }

        return UNKNOWN;
    }

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
    private PrimitiveTypeInfo(final String name) {
        
        if (null == name) {
            throw new NullPointerException();
        }
        
        this.name = name;
    }

    /**
     * �^����\���ϐ��D
     */
    private final String name;
}
