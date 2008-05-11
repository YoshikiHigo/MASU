package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �z��^�Q�Ƃ�\���N���X
 * 
 * @author higo
 *
 */
public final class ArrayTypeReferenceInfo extends EntityUsageInfo {

    /**
     * �I�u�W�F�N�g�������� 
     */
    public ArrayTypeReferenceInfo(final ArrayTypeInfo arrayType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arrayType) {
            throw new IllegalArgumentException();
        }

        this.arrayType = arrayType;
    }

    /**
     * �^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        return this.arrayType;
    }

    /**
     * �z��̌^�Q�Ƃɂ����ĕϐ����g�p����邱�Ƃ͂Ȃ��̂ŋ�̃Z�b�g��Ԃ�
     * 
     * @return ��̃Z�b�g
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    private final ArrayTypeInfo arrayType;
}
