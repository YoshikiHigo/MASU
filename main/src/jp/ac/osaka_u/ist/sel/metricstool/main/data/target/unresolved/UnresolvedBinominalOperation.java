package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;


/**
 * �������񍀉��Z���i�[���邽�߂̃N���X
 * 
 * @author y-higo
 * 
 */
public class UnresolvedBinominalOperation implements UnresolvedTypeInfo{

    /**
     * �����Ȃ��R���X�g���N�^
     */
    public UnresolvedBinominalOperation() {
    }

    /**
     * ���Z�q��2�̃I�y�����h��^���ď���������
     * 
     * @param operator ���Z�q
     * @param firstOperand ���i�������j�I�y�����h
     * @param secondOperand ���i�������j�I�y�����h
     */
    public UnresolvedBinominalOperation(final OPERATOR operator,
            final UnresolvedTypeInfo firstOperand, final UnresolvedTypeInfo secondOperand) {

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }
    
    /**
     * ���̃N���X�̌^����Ԃ�
     * 
     * @return ���̃N���X�̌^��
     */
    public String getTypeName() {
        return "UnresolvedBinominalOperation";
    }

    /**
     * ���Z�q���擾����
     * 
     * @return ���Z�q
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * ���i�������j�I�y�����h���擾����
     * 
     * @return ���i�������j�I�y�����h
     */
    public UnresolvedTypeInfo getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * ���i�������j�I�y�����h���擾����
     * 
     * @return ���i�������j�I�y�����h
     */
    public UnresolvedTypeInfo getSecondOperand() {
        return this.secondOperand;
    }

    /**
     * ���Z�q���Z�b�g����
     * 
     * @param operator ���Z�q
     */
    public void setOperator(final OPERATOR operator) {

        if (null == operator) {
            throw new NullPointerException();
        }

        this.operator = operator;
    }

    /**
     * ���i�������j�I�y�����h���Z�b�g����
     * 
     * @param firstOperand ���i�������j�I�y�����h
     */
    public void setFirstOperand(final UnresolvedTypeInfo firstOperand) {

        if (null == firstOperand) {
            throw new NullPointerException();
        }

        this.firstOperand = firstOperand;
    }

    /**
     * ���i�������j�I�y�����h���Z�b�g����
     * 
     * @param secondOperand ���i�������j�I�y�����h
     */
    public void setSecondOperand(final UnresolvedTypeInfo secondOperand) {

        if (null == secondOperand) {
            throw new NullPointerException();
        }

        this.secondOperand = secondOperand;
    }

    private OPERATOR operator;

    private UnresolvedTypeInfo firstOperand;

    private UnresolvedTypeInfo secondOperand;
}
