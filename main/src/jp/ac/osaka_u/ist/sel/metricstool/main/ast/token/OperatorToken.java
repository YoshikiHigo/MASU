package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * ���Z�q��\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class OperatorToken extends AstTokenAdapter {

    /**
     * �L���X�g�͉��Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken CAST = new OperatorToken("CAST", 2, false, false, null);

    /**
     * �C���N�������g���Z�q�ƃf�N�������g���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken INCL_AND_DECL = new OperatorToken("INCLEMENT", 1, true, true,
            null);

    /**
     * ������Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken ASSIGN = new OperatorToken("ASSIGN", 2, true, false, null);

    /**
     * �񍀉��Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken TWO_TERM = new OperatorToken("TWO_TERM", 2, false, true, null);

    /**
     * �P�����Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken SINGLE_TERM = new OperatorToken("SINGLE_TERM", 1, false,
            true, null);

    /**
     * �Q�l���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken THREE_TERM = new OperatorToken("THREE_TERM", 3, true, false,
            null);

    /**
     * ��r���Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken COMPARE = new OperatorToken("COMPARE", 2, false, true,
            PrimitiveTypeInfo.BOOLEAN);

    /**
     * �ے艉�Z�q��\���萔�C���X�^���X
     */
    public static final OperatorToken NOT = new OperatorToken("NOT", 1, false, true,
            PrimitiveTypeInfo.BOOLEAN);

    /**
     * �z��L�q�q��\���萔�C���X�^���X
     */
    public static final OperatorToken ARRAY = new OperatorToken("ARRAY", 2, false, true, null);

    /**
     * ���Z�q�̕�����C�������̐��C���Ӓl�ւ̎Q�ƂƑ�����s�����ǂ����C���Z���ʂ̌^���w�肷��R���X�g���N�^.
     * 
     * @param text ���Z�q�̕�����
     * @param termCount �������̐�
     * @param leftIsAssignmentee ���Ӓl�ւ̑��������ꍇ��true
     * @param leftIsReferencee ���Ӓl�ւ̂�����ꍇ��true
     * @param specifiedResultType ���Z���ʂ̌^�����܂��Ă���ꍇ�͂��̌^���C���܂��Ă��Ȃ��ꍇ��null���w�肷��
     * @throws IllegalArgumentException termCount��0�ȉ��̏ꍇ
     */
    public OperatorToken(final String text, final int termCount, final boolean leftIsAssignmentee,
            final boolean leftIsReferencee, final UnresolvedTypeInfo specifiedResultType) {
        super(text);

        if (termCount <= 0) {
            throw new IllegalArgumentException("Operator must treat one or more terms.");
        }

        this.leftIsAssignmentee = leftIsAssignmentee;
        this.leftIsReferencee = leftIsReferencee;
        this.termCount = termCount;
        this.specifiedResultType = specifiedResultType;
    }

    /**
     * ���̉��Z�q����舵�����̐���Ԃ�.
     * @return ���̉��Z�q����舵�����̐�
     */
    public int getTermCount() {
        return this.termCount;
    }

    /**
     * ���Ӓl�ւ̑�������邩�ǂ�����Ԃ�.
     * @return�@���Ӓl�ւ̑��������ꍇ��true
     */
    @Override
    public boolean isAssignmentOperator() {
        return this.leftIsAssignmentee;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isOperator()
     */
    @Override
    public boolean isOperator() {
        return true;
    }

    /**
     * ���Ӓl���Q�ƂƂ��ė��p����邩�ǂ�����Ԃ�.
     * @return�@���Ӓl���Q�ƂƂ��ė��p�����ꍇ��true
     */
    public boolean isLeftTermIsReferencee() {
        return this.leftIsReferencee;
    }

    /**
     * ���Z���ʂ̌^�����܂��Ă���ꍇ�͂��̌^��Ԃ�.
     * ���܂��Ă��Ȃ��ꍇ��null��Ԃ�.
     * @return ���Z���ʂ̌^�����܂��Ă���ꍇ�͂��̌^�C���܂��Ă��Ȃ��ꍇ��null
     */
    public UnresolvedTypeInfo getSpecifiedResultType() {
        return this.specifiedResultType;
    }

    /**
     * ���Ӓl�ւ̑�������邩�ǂ�����\��
     */
    private final boolean leftIsAssignmentee;

    /**
     * ���Ӓl���Q�ƂƂ��ė��p����邩�ǂ�����\��
     */
    private final boolean leftIsReferencee;

    /**
     * ���̉��Z�q����舵�����̐�
     */
    private final int termCount;

    /**
     * ���Z���ʂ̌^��\��.
     * ���܂��Ă��Ȃ��ꍇ��null.
     */
    private final UnresolvedTypeInfo specifiedResultType;

}
