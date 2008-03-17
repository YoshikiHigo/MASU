package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


/**
 * null�g�p��\���N���X�D
 * 
 * @author higo, t-miyake
 * 
 */
public final class NullUsageInfo extends EntityUsageInfo {

    public NullUsageInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * null�g�p�̌^�͕s��
     * 
     * @return �s���^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        return NULLTYPE;
    }

    /**
     * null�̎g�p�ɕϐ��g�p���܂܂�邱�Ƃ͂Ȃ��̂ŋ�̃Z�b�g��Ԃ�
     * 
     * @return ��̃Z�b�g
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }
    
    /**
     * null�g�p�̌^��ۑ����邽�߂̒萔
     */
    private static final TypeInfo NULLTYPE = UnknownTypeInfo.getInstance();
}
