package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �z��^�� length �t�B�[���h�g�p��\���N���X
 * 
 * @author higo
 * 
 */
public final class ArrayLengthUsageInfo extends FieldUsageInfo {

    /**
     * �e�ƂȂ�G���e�B�e�B�g�p��^���ăI�u�W�F�N�g��������
     *
     * @param ownerExecutableElement �I�[�i�[�G�������g
     * @param qualifierExpression �e�G���e�B�e�B
     * @param qualifierType �e�G���e�B�e�B�̌^�i�K�v�Ȃ������D�D�D�j
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ArrayLengthUsageInfo(final ExecutableElementInfo ownerExecutableElement,
            final ExpressionInfo qualifierExpression, final ArrayTypeInfo qualifierType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerExecutableElement, qualifierExpression, qualifierType, new ArrayLengthInfo(
                qualifierType), true, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * length �t�B�[���h�g�p�̌^��Ԃ��D
     * 
     * @return length �t�B�[���h�g�p�̌^
     */
    @Override
    public TypeInfo getType() {
        return PrimitiveTypeInfo.INT;
    }
}
