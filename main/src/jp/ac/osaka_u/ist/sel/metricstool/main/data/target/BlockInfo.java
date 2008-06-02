package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if �u���b�N�� for �u���b�N�Ȃ� ���\�b�h���̍\���I�Ȃ܂Ƃ܂�̒P�ʂ�\�����ۃN���X
 * 
 * @author higo
 */
public abstract class BlockInfo extends LocalSpaceInfo implements StatementInfo {

    /**
     * �ʒu����^���ď�����
     * 
     * @param ownerClass ���̃u���b�N�����L����N���X
     * @param ownerMethod ���̃u���b�N�����L���郁�\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    BlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClass) || (null == ownerMethod) || (null == outerSpace)) {
            throw new NullPointerException();
        }

        this.ownerMethod = ownerMethod;
        this.outerSpace = outerSpace;
    }

    /**
     * ���̃u���b�N�I�u�W�F�N�g�𑼂̃u���b�N�I�u�W�F�N�g�Ɣ�r����
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof BlockInfo)) {
            return false;
        }

        return 0 == this.compareTo((BlockInfo) o);
    }

    /**
     * ���̃u���b�N�I�u�W�F�N�g�𑼂̃u���b�N�I�u�W�F�N�g�Ɣ�r����
     */
    @Override
    public final int compareTo(StatementInfo o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
        }

        return 0;
    }

    /**
     * ���̃u���b�N�I�u�W�F�N�g�̃n�b�V���l��Ԃ�
     * 
     * @return ���̃u���b�N�I�u�W�F�N�g�̃n�b�V���l
     */
    @Override
    public final int hashCode() {
        return this.getFromLine() + this.getFromColumn() + this.getToLine() + this.getToColumn();
    }

    /**
     * ���̃u���b�N�����L�����Ԃ�
     * 
     * @return ���̃u���b�N�����L���郁�\�b�h
     */
    public final CallableUnitInfo getOwnerMethod() {
        return this.ownerMethod;
    }

    /**
     * ���̃u���b�N�𒼐ڏ��L���郍�[�J����Ԃ�Ԃ�
     * 
     * @return ���̃u���b�N�𒼐ڏ��L���郍�[�J�����
     */
    public final LocalSpaceInfo getOuterSpace() {
        return this.outerSpace;
    }

    /**
     * ���̃u���b�N�����L���郁�\�b�h��ۑ����邽�߂̕ϐ�
     */
    private final CallableUnitInfo ownerMethod;

    /**
     * ���̃u���b�N�𒼐ڏ��L���郍�[�J����Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final LocalSpaceInfo outerSpace;
}
