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
    public MonominalOperationInfo(final EntityUsageInfo operand, final PrimitiveTypeInfo type,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.operand = operand;
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
    public EntityUsageInfo getOperand() {
        return this.operand;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getOperand().getVariableUsages());
        return null;
    }
    
    /**
     * �I�y�����h��ۑ����邽�߂̕ϐ�
     */
    private final EntityUsageInfo operand;

    /**
     * �ꍀ���Z�̌��ʂ̌^��ۑ����邽�߂̕ϐ�
     */
    private final PrimitiveTypeInfo type;
}
