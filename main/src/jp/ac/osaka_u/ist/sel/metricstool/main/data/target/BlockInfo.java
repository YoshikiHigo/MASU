package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if �u���b�N�� for �u���b�N�Ȃ� ���\�b�h���̍\���I�Ȃ܂Ƃ܂�̒P�ʂ�\�����ۃN���X
 * 
 * @author higo
 */
public abstract class BlockInfo implements UnitInfo, Position, Comparable<BlockInfo> {

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

        this.assignmentees = new TreeSet<FieldInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.callees = new TreeSet<MethodInfo>();
        this.innerBlocks = new TreeSet<BlockInfo>();
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
     * ���̃u���b�N�Œ�`����Ă��郍�[�J���ϐ���ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param localVariable �ǉ��������
     */
    public final void addLocalVariable(final LocalVariableInfo localVariable) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * ���̃u���b�N���Q�Ƃ��Ă���ϐ���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param referencee �ǉ�����Q�Ƃ���Ă���ϐ�
     */
    public final void addReferencee(final FieldInfo referencee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencee) {
            throw new NullPointerException();
        }

        this.referencees.add(referencee);
    }

    /**
     * ���̃u���b�N��������s���Ă���ϐ���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param assignmentee �ǉ�����������Ă���ϐ�
     */
    public final void addAssignmentee(final FieldInfo assignmentee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmentee) {
            throw new NullPointerException();
        }

        this.assignmentees.add(assignmentee);
    }

    /**
     * ���̃u���b�N���Ăяo���Ă��郁�\�b�h��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param callee �ǉ�����Ăяo����郁�\�b�h
     */
    public final void addCallee(final MethodInfo callee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == callee) {
            throw new NullPointerException();
        }

        this.callees.add(callee);
    }

    /**
     * ���̃u���b�N�̒����̃u���b�N��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param innerBlock �ǉ����钼���u���b�N
     */
    public void addInnerBlock(final BlockInfo innerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        this.innerBlocks.add(innerBlock);
    }

    /**
     * ���̃u���b�N���Q�Ƃ��Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃u���b�N���Q�Ƃ��Ă���t�B�[���h�� SortedSet
     */
    public final SortedSet<FieldInfo> getReferencees() {
        return Collections.unmodifiableSortedSet(this.referencees);
    }

    /**
     * ���̃u���b�N��������Ă���t�B�[���h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃u���b�N��������Ă���t�B�[���h�� SortedSet
     */
    public final SortedSet<FieldInfo> getAssignmentees() {
        return Collections.unmodifiableSortedSet(this.assignmentees);
    }

    /**
     * ���̃u���b�N�Œ�`����Ă��郍�[�J���ϐ��� SortedSet ��Ԃ��D
     * 
     * @return ���̃u���b�N�Œ�`����Ă��郍�[�J���ϐ��� SortedSet
     */
    public final SortedSet<LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSortedSet(this.localVariables);
    }

    /**
     * ���̃u���b�N���ŌĂяo����Ă��郁�\�b�h�� SortedSet ��Ԃ��D
     * 
     * @return ���̃u���b�N���ŌĂяo����Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<MethodInfo> getCallees() {
        return Collections.unmodifiableSortedSet(this.callees);
    }

    /**
     * ���̃u���b�N�̒����̃u���b�N�� SortedSet ��Ԃ��D
     * 
     * @return ���̃u���b�N�̒����̃u���b�N�� SortedSet
     */
    public final SortedSet<BlockInfo> getInnerBlocks() {
        return Collections.unmodifiableSortedSet(this.innerBlocks);
    }

    /**
     * ���̃u���b�N�̓����Œ�`����Ă��郍�[�J���ϐ�
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * �Q�Ƃ��Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<FieldInfo> referencees;

    /**
     * ������Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<FieldInfo> assignmentees;

    /**
     * �Ăяo���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<MethodInfo> callees;

    /**
     * ���̃u���b�N�̒����̃u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<BlockInfo> innerBlocks;

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
