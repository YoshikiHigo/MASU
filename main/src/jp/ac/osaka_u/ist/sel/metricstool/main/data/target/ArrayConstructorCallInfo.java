package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �z��R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
public final class ArrayConstructorCallInfo extends ConstructorCallInfo {

    /**
     * �^��^���Ĕz��R���X�g���N�^�Ăяo����������
     * 
     * @param arrayType �Ăяo���̌^
     * @param indexExpression �C���f�b�N�X�̎�
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     */
    public ArrayConstructorCallInfo(final ArrayTypeInfo arrayType,
            final ExpressionInfo indexExpression, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(arrayType, null, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == indexExpression) {
            throw new IllegalArgumentException();
        }
        this.indexExpression = indexExpression;
    }

    /**
     * �C���f�b�N�X�̎����擾
     * 
     * @return �C���f�b�N�X�̎� 
     */
    public ExpressionInfo getIndexExpression() {
        return this.indexExpression;
    }

    private final ExpressionInfo indexExpression;
}
