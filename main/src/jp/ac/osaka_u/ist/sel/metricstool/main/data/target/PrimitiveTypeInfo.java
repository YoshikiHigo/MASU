package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * �v���~�e�B�u�^��\�����߂̃N���X�D�v���~�e�B�u�^�̓v���O���~���O����ɂ���Ē񏥂���Ă���^�ł��邽�߁C ���[�U���V���炵���^����邱�Ƃ��ł��Ȃ��悤�C�R���X�g���N�^�� private
 * �ɂ��Ă���D
 * 
 * @author y-higo
 * 
 */
public final class PrimitiveTypeInfo implements TypeInfo, UnresolvedTypeInfo {

    /**
     * �v���~�e�B�u�^�̊e�v�f��\�����߂̗񋓌^
     * 
     * @author y-higo
     * 
     */
    public enum TYPE {

        BOOLEAN {

            @Override
            public String getName() {
                return "boolean";
            }
        },

        BYTE {
            @Override
            public String getName() {
                return "byte";
            }
        },

        CHAR {
            @Override
            public String getName() {
                return "char";
            }
        },

        SHORT {
            @Override
            public String getName() {
                return "short";
            }
        },

        INT {
            @Override
            public String getName() {
                return "int";
            }
        },
        
        LONG {
            @Override
            public String getName() {
                return "long";
            }
        },
        
        FLOAT {
            @Override
            public String getName() {
                return "float";
            }
        },
        
        DOUBLE {
            @Override
            public String getName() {
                return "double";
            }
        },
        ;

        public abstract String getName();
    }

    /**
     * boolean ��\���萔
     */
    public static final String BOOLEAN_STRING = TYPE.BOOLEAN.getName();

    /**
     * byte ��\���萔
     */
    public static final String BYTE_STRING = TYPE.BYTE.getName();

    /**
     * char ��\���萔
     */
    public static final String CHAR_STRING = TYPE.CHAR.getName();

    /**
     * short ��\���萔
     */
    public static final String SHORT_STRING = TYPE.SHORT.getName();

    /**
     * int ��\���萔
     */
    public static final String INT_STRING = TYPE.INT.getName();

    /**
     * long ��\���萔
     */
    public static final String LONG_STRING = TYPE.LONG.getName();

    /**
     * float ��\���萔
     */
    public static final String FLOAT_STRING = TYPE.FLOAT.getName();

    /**
     * double ��\���萔
     */
    public static final String DOUBLE_STRING = TYPE.DOUBLE.getName();

    /**
     * boolean �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BOOLEAN = new PrimitiveTypeInfo(TYPE.BOOLEAN);

    /**
     * byte �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BYTE = new PrimitiveTypeInfo(TYPE.BYTE);

    /**
     * char �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo CHAR = new PrimitiveTypeInfo(TYPE.CHAR);

    /**
     * short �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo SHORT = new PrimitiveTypeInfo(TYPE.SHORT);

    /**
     * int �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo INT = new PrimitiveTypeInfo(TYPE.INT);

    /**
     * long �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo LONG = new PrimitiveTypeInfo(TYPE.LONG);

    /**
     * float �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo FLOAT = new PrimitiveTypeInfo(TYPE.FLOAT);

    /**
     * double �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo DOUBLE = new PrimitiveTypeInfo(TYPE.DOUBLE);

    /**
     * {@link PrimitiveTypeInfo}�̃t�@�N�g�����\�b�h�D
     * 
     * @param type �쐬����^�̗񋓌^
     * @return �w�肳�ꂽ����\�� {@link PrimitiveTypeInfo} �̃C���X�^���X�D
     */
    public static PrimitiveTypeInfo getType(final TYPE type) {

        if (null == type) {
            throw new NullPointerException();
        }

        switch (type) {
        case BOOLEAN:
            return BOOLEAN;
        case BYTE:
            return BYTE;
        case CHAR:
            return CHAR;
        case DOUBLE:
            return DOUBLE;
        case FLOAT:
            return FLOAT;
        case INT:
            return INT;
        case LONG:
            return LONG;
        case SHORT:
            return SHORT;
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * �^��Ԃ��D
     * 
     * @return �^
     */
    public TYPE getType() {
        return this.type;
    }

    /**
     * �^����Ԃ��D
     * 
     * @return �^��
     */
    public String getTypeName() {
        return this.type.getName();
    }

    /**
     * �I�u�W�F�N�g�̓������̃`�F�b�N���s��
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof PrimitiveTypeInfo)) {
            return false;
        }

        return this.getTypeName().equals(typeInfo.getTypeName());
    }

    /*
     * �I�u�W�F�N�g�̓������̃`�F�b�N���s��
     * 
     * public boolean equals(final UnresolvedTypeInfo typeInfo) {
     * 
     * if (null == typeInfo) { throw new NullPointerException(); }
     * 
     * if (!(typeInfo instanceof PrimitiveTypeInfo)) { return false; }
     * 
     * return this.getTypeName().equals(typeInfo.getTypeName()); }
     */

    /*
     * �����֌W���`����
     * 
     * public int compareTo(final UnresolvedTypeInfo typeInfo) {
     * 
     * if (null == typeInfo) { throw new NullPointerException(); }
     * 
     * return this.getTypeName().compareTo(typeInfo.getTypeName()); }
     */

    /**
     * �I�u�W�F�N�g�Ɍ^��^���ď���������D �^���͌Œ�ł��邽�߁C�O������̓I�u�W�F�N�g�𐶐��ł��Ȃ��悤�ɂ��Ă���D
     * 
     * @param type �^
     */
    private PrimitiveTypeInfo(final TYPE type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * �^��\���ϐ��D
     */
    private final TYPE type;
}
