package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * �z��R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayConstructorCallInfo extends ConstructorCallInfo<ArrayTypeInfo> {

    /**
     * �^��^���Ĕz��R���X�g���N�^�Ăяo����������
     * 
     * @param arrayType �Ăяo���̌^
     * @param indexExpressions �C���f�b�N�X�̎�
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     */
    public ArrayConstructorCallInfo(final ArrayTypeInfo arrayType,
            final List<ExpressionInfo> indexExpressions, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(arrayType, null, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == indexExpressions) {
            throw new IllegalArgumentException();
        }
        this.indexExpressions = Collections.unmodifiableList(indexExpressions);

        for (final ExpressionInfo element : this.indexExpressions) {
            element.setOwnerExecutableElement(this);
        }
    }

    /**
     * �C���f�b�N�X�̎����擾
     * @param dimention �C���f�b�N�X�̎����擾����z��̎���
     * @return �w�肵�������̃C���f�b�N�X�̎�
     */
    public ExpressionInfo getIndexExpression(final int dimention) {
        return this.indexExpressions.get(dimention - 1);
    }

    /**
     * �C���f�b�N�X�̎��̃��X�g���擾
     * 
     * @return �C���f�b�N�X�̎��̃��X�g 
     */
    public List<ExpressionInfo> getIndexExpressions() {
        return this.indexExpressions;
    }

    /**
     * �z��̏��������̃e�L�X�g�\����Ԃ�
     * 
     * @return �z��̏��������̃e�L�X�g�\��
     * 
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        text.append("new ");

        final ArrayTypeInfo arrayType = this.getType();
        final TypeInfo elementType = arrayType.getElementType();
        text.append(elementType.getTypeName());

        final int dimension = arrayType.getDimension();
        for (int i = 1; i <= dimension; i++) {
            final ExpressionInfo indexExpression = this.getIndexExpression(i);
            text.append("[");
            text.append(indexExpression.getText());
            text.append("]");
        }

        return text.toString();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        final Set<ClassTypeInfo> thrownExceptions = new HashSet<ClassTypeInfo>();
        for (final ExpressionInfo indexExpression : this.getIndexExpressions()) {
            thrownExceptions.addAll(indexExpression.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExceptions);
    }

    private final List<ExpressionInfo> indexExpressions;
}
