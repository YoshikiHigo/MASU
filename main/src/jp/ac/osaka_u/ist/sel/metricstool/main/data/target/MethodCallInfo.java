package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���\�b�h�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
public final class MethodCallInfo extends MemberCallInfo {

    /**
     * �Ăяo����郁�\�b�h��^���ăI�u�W�F�N�g��������
     * 
     * @param callee �Ăяo����郁�\�b�h
     */
    public MethodCallInfo(final MethodInfo callee) {
        super(callee);
    }

    /**
     * ���̃��\�b�h�Ăяo���̌^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        final MethodInfo callee = (MethodInfo) super.getCallee();
        return callee.getReturnType();
    }
}
