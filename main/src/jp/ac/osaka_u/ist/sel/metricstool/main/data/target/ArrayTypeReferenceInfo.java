package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �z��^�Q�Ƃ�\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayTypeReferenceInfo extends ExpressionInfo {

    /**
     * �I�u�W�F�N�g��������
     * 
     * @param arrayType �Q�Ƃ���Ă���z��̌^
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ArrayTypeReferenceInfo(final ArrayTypeInfo arrayType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

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

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo���̃Z�b�g
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * ���̔z��^�Q�Ƃ̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̔z��^�̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {
        final TypeInfo type = this.getType();
        return type.getTypeName();
    }

    private final ArrayTypeInfo arrayType;
}
