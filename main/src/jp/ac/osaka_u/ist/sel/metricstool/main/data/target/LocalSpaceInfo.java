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

    LocalSpaceInfo(final ClassInfo ownerClass, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.ownerClass = ownerClass;
        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.fieldUsages = new HashSet<FieldUsageInfo>();
        this.localVariableUsages = new HashSet<LocalVariableUsageInfo>();
        this.parameterUsages = new HashSet<ParameterUsageInfo>();
        this.statements = new TreeSet<StatementInfo>();
        this.calls = new HashSet<CallInfo>();
    }

    /**
     * ���\�b�h����уR���X�g���N�^�Ăяo����ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param memberCall �ǉ����郁�\�b�h����уR���X�g���N�^�Ăяo��
     */
    public final void addCall(final CallInfo memberCall) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.calls.add(memberCall);
    }

    /**
     * ���̃��\�b�h�Œ�`����Ă��郍�[�J���ϐ���ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
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
     * ���̃��\�b�h���Q�Ƃ��Ă���ϐ���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param fieldUsage �ǉ�����t�B�[���h���p
     */
    public final void addVariableUsage(final VariableUsageInfo<?> variableUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == variableUsage) {
            throw new NullPointerException();
        }

        if (variableUsage instanceof FieldUsageInfo) {
            this.fieldUsages.add((FieldUsageInfo) variableUsage);
        } else if (variableUsage instanceof LocalVariableUsageInfo) {
            this.localVariableUsages.add((LocalVariableUsageInfo) variableUsage);
        } else if (variableUsage instanceof ParameterUsageInfo) {
            this.parameterUsages.add((ParameterUsageInfo) variableUsage);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * ���̃��[�J���R�[�v�̒����̎���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param innerBlock �ǉ����钼���u���b�N
     */
    public void addStatement(final StatementInfo statement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new NullPointerException();
        }

        this.statements.add(statement);
    }

    /**
     * ���\�b�h����уR���X�g���N�^�Ăяo���ꗗ��Ԃ�
     */
    public Set<CallInfo> getCalls() {
        return Collections.unmodifiableSet(this.calls);

    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�̈ꗗ��Ԃ�
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        final SortedSet<MethodInfo> callees = new TreeSet<MethodInfo>();
        for (final CallInfo call : this.getCalls()) {
            if (call instanceof MethodCallInfo) {
                callees.add(((MethodCallInfo) call).getCallee());
            }
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
     * ���̃��\�b�h�̃��[�J���ϐ����p��Set��Ԃ�
     */
    public Set<LocalVariableUsageInfo> getLocalVariableUsages() {
        return Collections.unmodifiableSet(this.localVariableUsages);
    }
    
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getLocalVariableUsages());
        variableUsages.addAll(this.getFieldUsages());
        variableUsages.addAll(this.getParameterUsages());
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * ���̃��\�b�h�̃t�B�[���h���p��Set��Ԃ�
     */
    public Set<ParameterUsageInfo> getParameterUsages() {
        return Collections.unmodifiableSet(this.parameterUsages);
    }

    /**
     * ���̃��[�J���X�y�[�X�̒����̕����� SortedSet ��Ԃ��D
     * 
     * @return ���̃��[�J���X�y�[�X�̒����̕����� SortedSet
     */
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(this.statements);
    }

    /**
     * �������Ă���N���X��Ԃ�
     * 
     * @return �������Ă���N���X
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���\�b�h�Ăяo���ꗗ��ۑ����邽�߂̕ϐ�
     */
    protected final Set<CallInfo> calls;

    /**
     * ���̃��\�b�h�̓����Œ�`����Ă��郍�[�J���ϐ�
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * ���p���Ă���t�B�[���h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<FieldUsageInfo> fieldUsages;

    /**
     * ���p���Ă��郍�[�J���ϐ��̈ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<LocalVariableUsageInfo> localVariableUsages;

    /**
     * ���p���Ă�������̈ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final Set<ParameterUsageInfo> parameterUsages;

    /**
     * ���̃��[�J���X�R�[�v�̒����̕����ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<StatementInfo> statements;

    /**
     * �������Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private final ClassInfo ownerClass;
}
