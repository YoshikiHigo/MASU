package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���\�b�h�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
public final class MethodCallInfo extends CallInfo {

    /**
     * �Ăяo����郁�\�b�h��^���ăI�u�W�F�N�g��������
     * 
     * @param callee �Ăяo����郁�\�b�h
     */
    public MethodCallInfo(final MethodInfo callee, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == callee) {
            throw new NullPointerException();
        }

        this.callee = callee;
    }

    /**
     * ���̃��\�b�h�Ăяo���̌^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        final MethodInfo callee = this.getCallee();
        return callee.getReturnType();
    }

    /**
     * ���̃��\�b�h�Ăяo���ŌĂяo����Ă��郁�\�b�h��Ԃ�
     * @return
     */
    public MethodInfo getCallee() {
        return this.callee;
    }

    private final MethodInfo callee;
}
