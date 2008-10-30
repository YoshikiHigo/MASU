package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * �ꍀ���Z��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake
 *
 */
public final class MonominalOperationInfo extends EntityUsageInfo {

    /**
     * �I�y�����h�A�ꍀ���Z�̌��ʂ̌^�A�ʒu����^���ď�����
     * @param operand �I�y�����h
     * @param type �ꍀ���Z�̌��ʂ̌^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public MonominalOperationInfo(final EntityUsageInfo operand, final OPERATOR operator,
            final boolean isPreposed, final PrimitiveTypeInfo type, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if(null == operand || null == operator || null == type) {
            throw new IllegalArgumentException();
        }
        
        this.operand = operand;
        this.operator = operator;
        this.isPreposed = isPreposed;
        this.type = type;
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * �I�y�����h��Ԃ�
     * @return �I�y�����h
     */
    public final EntityUsageInfo getOperand() {
        return this.operand;
    }

    /**
     * �ꍀ���Z�̉��Z�q��Ԃ��D
     * @return ���Z�q
     */
    public final OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * ���Z�q���O�u����Ă��邩�ǂ����Ԃ�
     * @return ���Z�q���O�u����Ă���Ȃ�true
     */
    public final boolean isPreposed() {
        return this.isPreposed;
    }
    
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getOperand().getVariableUsages());
        return variableUsages;
    }

    /**
     * �I�y�����h��ۑ����邽�߂̕ϐ�
     */
    private final EntityUsageInfo operand;

    /**
     * �ꍀ���Z�̉��Z�q��ۑ����邽�߂̕ϐ�
     */
    private final OPERATOR operator;

    /**
     * �ꍀ���Z�̌��ʂ̌^��ۑ����邽�߂̕ϐ�
     */
    private final PrimitiveTypeInfo type;
    
    /**
     * ���Z�q���O�u���Ă��邩�ǂ����������ϐ�
     */
    private final boolean isPreposed;
}
