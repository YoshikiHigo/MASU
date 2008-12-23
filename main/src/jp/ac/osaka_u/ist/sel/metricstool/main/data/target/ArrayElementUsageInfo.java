package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �z��v�f�̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
public class ArrayElementUsageInfo extends EntityUsageInfo {

    /**
     * �v�f�̐e�C�܂�z��^�̎��ƃC���f�b�N�X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerExecutableElement �I�[�i�[�v�f
     * @param qualifierExpression �z��^�̎�
     * @param indexExpression �C���f�b�N�X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ArrayElementUsageInfo(final ExecutableElementInfo ownerExecutableElement,
            final ExpressionInfo qualifierExpression, final ExpressionInfo indexExpression,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerExecutableElement, fromLine, fromColumn, toLine, toColumn);

        if (null == qualifierExpression) {
            throw new NullPointerException();
        }

        this.qualifierExpression = qualifierExpression;
        this.indexExpression = indexExpression;
    }

    /**
     * ���̔z��v�f�̎g�p�̌^��Ԃ�
     * 
     * @return ���̔z��v�f�̎g�p�̌^
     */
    @Override
    public TypeInfo getType() {

        final TypeInfo ownerType = this.getQualifierExpression().getType();

        // �e���z��^�ł���C�Ɖ����ł��Ă���ꍇ
        if (ownerType instanceof ArrayTypeInfo) {
            // �z��̎����ɉ����Č^�𐶐�
            final int ownerArrayDimension = ((ArrayTypeInfo) ownerType).getDimension();
            final TypeInfo ownerArrayElement = ((ArrayTypeInfo) ownerType).getElementType();

            // �z�񂪓񎟌��ȏ�̏ꍇ�́C����������Ƃ����z���Ԃ��C�ꎟ���̏ꍇ�́C�v�f�̌^��Ԃ��D
            return 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                    ownerArrayDimension - 1) : ownerArrayElement;
        }

        // �z��^�łȂ��C���s���^�łȂ��ꍇ�͂�������
        assert ownerType instanceof UnknownTypeInfo : "ArrayElementUsage attaches unappropriate type!";

        return ownerType;
    }

    /**
     * ���̗v�f�̐e�C�܂�z��^�̎���Ԃ�
     * 
     * @return ���̗v�f�̐e��Ԃ�
     */
    public ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    /**
     * ���̗v�f�̃C���f�b�N�X��Ԃ�
     * 
     * @return�@���̗v�f�̃C���f�b�N�X
     */
    public ExpressionInfo getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * ���̎��i�z��v�f�̎g�p�j�ɂ�����ϐ����p�̈ꗗ��Ԃ�
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final Set<VariableUsageInfo<?>> variableUsages = new HashSet<VariableUsageInfo<?>>(
                this.indexExpression.getVariableUsages());
        variableUsages.addAll(this.getQualifierExpression().getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
        //return this.getOwnerEntityUsage().getVariableUsages();
    }

    /**
     * ���̔z��v�f�g�p�̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̔z��v�f�g�p�̃e�L�X�g�\��
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo expression = this.getQualifierExpression();
        sb.append(expression.getText());

        sb.append("[");

        final ExpressionInfo indexExpression = this.getIndexExpression();
        sb.append(indexExpression.getText());

        sb.append("]");

        return sb.toString();
    }

    private final ExpressionInfo qualifierExpression;

    private final ExpressionInfo indexExpression;
}
