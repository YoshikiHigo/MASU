package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �v���~�e�B�u�^�̎g�p��\���N���X
 * 
 * @author higo
 *
 */
public final class PrimitiveTypeUsageInfo extends EntityUsageInfo {

    /**
     * �g�p����Ă���^�C������C�ʒu����^���ď�����
     * @param type �g�p����Ă���^
     * @param literal�@������
     * @param fromLine�@�J�n�s
     * @param fromColumn�@�J�n��
     * @param toLine�@�I���s
     * @param toColumn�@�I����
     */
    public PrimitiveTypeUsageInfo(final PrimitiveTypeInfo type, final String literal,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.type = type;
        this.literal = literal;
    }

    public String getLiteral() {
        return this.literal;
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    private final PrimitiveTypeInfo type;

    private final String literal;
}
