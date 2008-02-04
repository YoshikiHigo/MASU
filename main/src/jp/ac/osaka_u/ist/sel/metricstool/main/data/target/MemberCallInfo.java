package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���\�b�h�Ăяo���C�R���X�g���N�^�Ăяo���̋��ʂ̐e�N���X
 * 
 * @author higo
 *
 */
public abstract class MemberCallInfo extends EntityUsageInfo {

    /**
     * �Ăяo����Ă��郁�\�b�h�܂��̓R���X�g���N�^��^���ď�����
     * 
     * @param callee
     */
    MemberCallInfo(final MethodInfo callee, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == callee) {
            throw new NullPointerException();
        }

        this.callee = callee;
    }

    public final MethodInfo getCallee() {
        return this.callee;
    }

    private final MethodInfo callee;
}
