package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * �g�ݍ��݌^��\���g�[�N���N���X.
 * 
 * @author kou-tngt
 *
 */
public class BuiltinTypeToken extends AstTokenAdapter {

    /**
     * bool�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken BOOLEAN = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.BOOLEAN));

    /**
     * byte�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken BYTE = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.BYTE));

    /**
     * char�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken CHAR = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.CHAR));

    /**
     * short�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken SHORT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.SHORT));

    /**
     * int�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken INT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.INT));

    /**
     * long�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken LONG = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.LONG));

    /**
     * float�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken FLOAT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.FLOAT));

    /**
     * double�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken DOUBLE = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.DOUBLE));

    /**
     * void�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken VOID = new BuiltinTypeToken(VoidTypeInfo.getInstance());

    public UnresolvedTypeInfo getType() {
        return this.type;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isPrimitiveType()
     */
    @Override
    public boolean isPrimitiveType() {
        return !this.isVoidType();
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isVoidType()
     */
    @Override
    public boolean isVoidType() {
        return this.equals(VOID);
    }

    /**
     * �w�肳�ꂽ������ŕ\������{�^��\���g�[�N�����쐬����R���X�g���N�^.
     * 
     * @param text�@���̑g�ݍ��݌^��\��������
     */
    protected BuiltinTypeToken(final UnresolvedTypeInfo type) {
        super(type.getTypeName());

        this.type = type;
    }

    /**
     * ���̃g�[�N�����\����{�^
     */
    private final UnresolvedTypeInfo type;
}
