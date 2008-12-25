package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * ���e�����̎g�p��\���N���X
 * @author t-miyake
 *
 */
public final class LiteralUsageInfo extends ExpressionInfo {

    /**
     * ���e�����A���e�����̌^�A�o���ʒu��^���ď�����
     * 
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param literal ���e����
     * @param type ���e�����̌^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public LiteralUsageInfo(final String literal, final PrimitiveTypeInfo type,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.literal = literal;
        this.type = type;

    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo#getType()
     */
    @Override
    public final TypeInfo getType() {
        return this.type;
    }

    /**
     * ���e�����̕������Ԃ�
     * 
     * @return ���e�����̕�����
     */
    public final String getLiteral() {
        return this.literal;
    }

    /**
     * ���e�����͕ϐ��Q�Ƃł͂Ȃ��̂ŋ�̃Z�b�g��Ԃ�
     * 
     * @return ��̃Z�b�g
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * ���̃��e�����g�p�̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̃��e�����g�p�̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {
        return this.getLiteral();
    }

    /**
     * ���e������ۑ����邽�߂̕ϐ�
     */
    private final String literal;

    /**
     * ���e�����̌^��ۑ����邽�߂̕ϐ�
     */
    private final PrimitiveTypeInfo type;
}