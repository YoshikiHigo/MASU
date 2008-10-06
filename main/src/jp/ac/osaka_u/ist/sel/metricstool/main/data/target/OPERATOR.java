package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import static jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE.*;


/**
 * ���Z�q��\���񋓌^
 * 
 * @author t-miyake
 *
 */
public enum OPERATOR {

    /**
     * �Z�p���Z�q"+"
     */
    PLUS(ARITHMETIC, "+"),

    /**
     * �Z�p���Z�q"-"
     */
    MINUS(ARITHMETIC, "-"),

    /**
     * �Z�p���Z�q"*"
     */
    STAR(ARITHMETIC, "*"),

    /**
     * �Z�p���Z�q"/"
     */
    DIV(ARITHMETIC, "/"),

    /**
     * �Z�p���Z�q"%"
     */
    MOD(ARITHMETIC, "%"),

    /**
     * ��r���Z�q"=="
     */
    EQUAL(COMPARATIVE, "=="),

    /**
     * ��r���Z�q"!="
     */
    NOT_EQUAL(COMPARATIVE, "!="),

    /**
     * ��r���Z�q"<"
     */
    LT(COMPARATIVE, "<"),

    /**
     * ��r���Z�q">"
     */
    GT(COMPARATIVE, ">"),

    /**
     * ��r���Z�q"<="
     */
    LE(COMPARATIVE, "<="),

    /**
     * ��r���Z�q">="
     */
    GE(COMPARATIVE, ">="),
    
    /**
     * instanceof���Z�q
     */
    INSTANCEOF(COMPARATIVE, "instanceof"),

    /**
     * �_�����Z�q"&&"
     */
    LAND(LOGICAL, "&&"),

    /**
     * �_�����Z�q"||"
     */
    LOR(LOGICAL, "||"),

    /**
     * �_�����Z�q"!"
     */
    LNOT(LOGICAL, "!"),

    /**
     * �r�b�g���Z�q"&"
     */
    BAND(BITS, "&"),

    /**
     * �r�b�g���Z�q"|"
     */
    BOR(BITS, "|"),

    /**
     * �r�b�g���Z�q"^"
     */
    BXOR(BITS, "^"),

    /**
     * �r�b�g���Z�q"~"
     */
    BNOT(BITS, "~"),

    /**
     * �V�t�g���Z�q"<<"
     */
    SL(SHIFT, "<<"),

    /**
     * �V�t�g���Z�q">>"
     */
    SR(SHIFT, ">>"),

    /**
     * �V�t�g���Z�q">>>"
     */
    BSR(SHIFT, ">>>"),

    /**
     * ������Z�q"="
     */
    ASSIGN(ASSIGNMENT, "="),

    /**
     * ������Z�q"+="
     */
    PLUS_ASSIGN(ASSIGNMENT, "+="),

    /**
     * ������Z�q"-="
     */
    MINUS_ASSIGN(ASSIGNMENT, "-="),

    /**
     * ������Z�q"*="
     */
    STAR_ASSIGN(ASSIGNMENT, "*="),

    /**
     * ������Z�q"/="
     */
    DIV_ASSIGN(ASSIGNMENT, "/="),

    /**
     * ������Z�q"%="
     */
    MOD_ASSIGN(ASSIGNMENT, "%="),

    /**
     * ������Z�q"&="
     */
    BAND_ASSIGN(ASSIGNMENT, "&="),

    /**
     * ������Z�q"|="
     */
    BOR_ASSIGN(ASSIGNMENT, "|="),

    /**
     * ������Z�q"^="
     */
    BXOR_ASSIGN(ASSIGNMENT, "^="),

    /**
     * ������Z�q"<<="
     */
    SL_ASSIGN(ASSIGNMENT, "<<="),

    /**
     * ������Z�q">>="
     */
    SR_ASSIGN(ASSIGNMENT, ">>="),

    /**
     * ������Z�q">>>="
     */
    BSR_ASSIGN(ASSIGNMENT, ">>>="),
    
    ;

    //    INC             :   "++"    ;

    //    DEC             :   "--"    ;

    /**
     * ���Z�q�̃^�C�v�ƃg�[�N����^���ď�����
     * 
     * @param operatorType ���Z�q�̃^�C�v
     * @param token ���Z�q�̃g�[�N��
     */
    private OPERATOR(final OPERATOR_TYPE operatorType, final String token) {
        this.operatorType = operatorType;
        this.token = token;
    }

    /**
     * ���Z�q�̃^�C�v��Ԃ�
     * 
     * @return ���Z�q�̃^�C�v
     */
    public OPERATOR_TYPE getOperatorType() {
        return this.operatorType;
    }

    /**
     * ���Z�q�̃g�[�N����Ԃ�
     * 
     * @return ���Z�q�̃g�[�N��
     */
    public String getToken() {
        return this.token;
    }
    
    public static OPERATOR getOperator(final String token) {
        for(final OPERATOR operator : OPERATOR.values()) {
            if(operator.getToken().equals(token)) {
                return operator;
            }
        }
        return null;
    }

    /**
     * ���Z�q�̃^�C�v��\���ϐ�
     */
    final private OPERATOR_TYPE operatorType;

    /**
     * ���Z�q�̃g�[�N����\���ϐ�
     */
    final private String token;
}
