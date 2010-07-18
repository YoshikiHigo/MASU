package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


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
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     */
    public ArrayConstructorCallInfo(final ArrayTypeInfo arrayType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(arrayType, null, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.indexExpressions = new ArrayList<ExpressionInfo>();

        for (final ExpressionInfo element : this.indexExpressions) {
            element.setOwnerExecutableElement(this);
        }
    }

    public void addIndexExpression(final ExpressionInfo indexExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == indexExpression) {
            throw new IllegalArgumentException();
        }

        this.indexExpressions.add(indexExpression);
    }

    public void addIndexExpressions(final List<ExpressionInfo> indexExpressions) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == indexExpressions) {
            throw new IllegalArgumentException();
        }

        this.indexExpressions.addAll(indexExpressions);
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

        final List<ExpressionInfo> arguments = this.getArguments();
        if (0 < arguments.size()) {
            text.append("{");
            for (final ExpressionInfo argument : arguments) {
                text.append(argument.getText());
                text.append(",");
            }
            text.deleteCharAt(text.length() - 1);
            text.append("}");
        }
        return text.toString();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        for (final ExpressionInfo indexExpression : this.getIndexExpressions()) {
            thrownExceptions.addAll(indexExpression.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExceptions);
    }

    private final List<ExpressionInfo> indexExpressions;
}
