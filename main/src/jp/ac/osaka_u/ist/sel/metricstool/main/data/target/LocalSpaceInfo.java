package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���[�J���̈�(���\�b�h�⃁�\�b�h���u���b�N)��\���N���X
 * 
 * @author higo
 *
 */
public abstract class LocalSpaceInfo extends UnitInfo {

    LocalSpaceInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.fieldUsages = new HashSet<FieldUsageInfo>();
        this.innerBlocks = new TreeSet<BlockInfo>();
        this.callees = new HashSet<MemberCallInfo>();
    }

    /**
     * ���\�b�h����уR���X�g���N�^�Ăяo����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param callee �ǉ�����Ăяo����郁�\�b�h
     */
    public final void addCallee(final MemberCallInfo memberCall) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.callees.add(memberCall);
    }

    /**
     * ���̃��\�b�h�Œ�`����Ă��郍�[�J���ϐ���ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param localVariable �ǉ��������
     */
    public void addLocalVariable(final LocalVariableInfo localVariable) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * ���̃��\�b�h���Q�Ƃ��Ă���ϐ���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param fieldUsage �ǉ�����t�B�[���h���p
     */
    public void addFieldUsage(final FieldUsageInfo fieldUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldUsages.add(fieldUsage);
    }

    /**
     * ���̃��\�b�h�̒����u���b�N��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
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
     * ���\�b�h����уR���X�g���N�^�Ăяo���ꗗ��Ԃ�
     */
    public Set<MemberCallInfo> getMemberCalls() {
        return Collections.unmodifiableSet(this.callees);

    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h����уR���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        final SortedSet<MethodInfo> callees = new TreeSet<MethodInfo>();
        for (final MemberCallInfo memberCall : this.getMemberCalls()) {
            callees.add(memberCall.getCallee());
        }
        return Collections.unmodifiableSortedSet(callees);
    }

    /**
     * ���̃��\�b�h�Œ�`����Ă��郍�[�J���ϐ��� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h�Œ�`����Ă��郍�[�J���ϐ��� SortedSet
     */
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSortedSet(this.localVariables);
    }

    /**
     * ���̃��\�b�h�̃t�B�[���h���p��Set��Ԃ�
     */
    public Set<FieldUsageInfo> getFieldUsages() {
        return Collections.unmodifiableSet(this.fieldUsages);
    }

    /**
     * ���̃��\�b�h�̒����u���b�N�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h�̒����u���b�N�� SortedSet ��Ԃ��D
     */
    public SortedSet<BlockInfo> getInnerBlocks() {
        return Collections.unmodifiableSortedSet(this.innerBlocks);
    }

    /**
     * ���\�b�h�Ăяo���ꗗ��ۑ����邽�߂̕ϐ�
     */
    protected final Set<MemberCallInfo> callees;

    /**
     * ���̃��\�b�h�̓����Œ�`����Ă��郍�[�J���ϐ�
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * ���p���Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<FieldUsageInfo> fieldUsages;

    /**
     * ���̃��\�b�h�����̃u���b�N�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<BlockInfo> innerBlocks;
}
