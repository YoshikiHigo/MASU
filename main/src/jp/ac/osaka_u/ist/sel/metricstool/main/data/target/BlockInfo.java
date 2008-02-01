package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if �u���b�N�� for �u���b�N�Ȃ� ���\�b�h���̍\���I�Ȃ܂Ƃ܂�̒P�ʂ�\�����ۃN���X
 * 
 * @author higo
 */
public abstract class BlockInfo extends LocalSpaceInfo implements Position, Comparable<BlockInfo> {

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
    BlockInfo(final TargetClassInfo ownerClass, final TargetMethodInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClass) || (null == ownerMethod)) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.ownerMethod = ownerMethod;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    public final int compareTo(BlockInfo o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return 1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return -1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return 1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return -1;
        } else if (this.getToLine() < o.getToLine()) {
            return 1;
        } else if (this.getToLine() > o.getToLine()) {
            return -1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return 1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return -1;
        }

        return 0;
    }

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

    @Override
    public final int hashCode() {
        return this.getFromLine() + this.getFromColumn() + this.getToLine() + this.getToColumn();
    }

    /**
     * ���̃u���b�N�����L����N���X��Ԃ�
     * 
     * @return ���̃u���b�N�����L����N���X
     */
    public final TargetClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃u���b�N�����L�����Ԃ�
     * 
     * @return ���̃u���b�N�����L���郁�\�b�h
     */
    public final TargetMethodInfo getOwnerMethod() {
        return this.ownerMethod;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    /**
     * ���̃u���b�N�����L����N���X��ۑ����邽�߂̕ϐ�
     */
    private final TargetClassInfo ownerClass;

    /**
     * ���̃u���b�N�����L���郁�\�b�h��ۑ����邽�߂̕ϐ�
     */
    private final TargetMethodInfo ownerMethod;

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private final int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private final int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private final int toColumn;
}
