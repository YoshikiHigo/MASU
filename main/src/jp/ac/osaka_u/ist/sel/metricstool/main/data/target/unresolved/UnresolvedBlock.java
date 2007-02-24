package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if����while���Ȃǂ̃��\�b�h���̍\���i�u���b�N�j��\�����߂̃N���X
 * 
 * @author y-higo
 * 
 */
public abstract class UnresolvedBlock {

    /**
     * �u���b�N�\����\���I�u�W�F�N�g������������
     * 
     */
    public UnresolvedBlock() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.methodCalls = new HashSet<UnresolvedMethodCall>();
        this.fieldReferences = new HashSet<UnresolvedFieldUsage>();
        this.fieldAssignments = new HashSet<UnresolvedFieldUsage>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.innerBlocks = new HashSet<UnresolvedBlock>();

        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;
    }

    /**
     * ���\�b�h�Ăяo����ǉ�����
     * 
     * @param methodCall ���\�b�h�Ăяo��
     */
    public final void addMethodCall(final UnresolvedMethodCall methodCall) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodCall) {
            throw new NullPointerException();
        }

        this.methodCalls.add(methodCall);
    }

    /**
     * �t�B�[���h�Q�Ƃ�ǉ�����
     * 
     * @param fieldUsage �t�B�[���h�Q��
     */
    public final void addFieldReference(final UnresolvedFieldUsage fieldUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldReferences.add(fieldUsage);
    }

    /**
     * �t�B�[���h�����ǉ�����
     * 
     * @param fieldUsage �t�B�[���h���
     */
    public final void addFieldAssignment(final UnresolvedFieldUsage fieldUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldAssignments.add(fieldUsage);
    }

    /**
     * ���[�J���ϐ���ǉ�����
     * 
     * @param localVariable ���[�J���ϐ�
     */
    public final void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * �C���i�[�u���b�N��ǉ�����
     * 
     * @param innerBlock ���[�J���ϐ�
     */
    public void addInnerBlock(final UnresolvedBlock innerBlock) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        this.innerBlocks.add(innerBlock);
    }

    public final Set<UnresolvedMethodCall> getMethodCalls() {
        return Collections.unmodifiableSet(this.methodCalls);
    }

    public final Set<UnresolvedFieldUsage> getFieldReferences() {
        return Collections.unmodifiableSet(this.fieldReferences);
    }

    public final Set<UnresolvedFieldUsage> getFieldAssignments() {
        return Collections.unmodifiableSet(this.fieldAssignments);
    }

    public final Set<UnresolvedLocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSet(this.localVariables);
    }

    /**
     * �J�n�s���Z�b�g����
     * 
     * @param fromLine �J�n�s
     */
    public final void setFromLine(final int fromLine) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * �J�n����Z�b�g����
     * 
     * @param fromColumn �J�n��
     */
    public final void setFromColumn(final int fromColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * �I���s���Z�b�g����
     * 
     * @param toLine �I���s
     */
    public final void setToLine(final int toLine) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * �I������Z�b�g����
     * 
     * @param toColumn �I����
     */
    public final void setToColumn(final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
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
     * ���\�b�h�Ăяo����ۑ�����ϐ�
     */
    private final Set<UnresolvedMethodCall> methodCalls;

    /**
     * �t�B�[���h�Q�Ƃ�ۑ�����ϐ�
     */
    private final Set<UnresolvedFieldUsage> fieldReferences;

    /**
     * �t�B�[���h�����ۑ�����ϐ�
     */
    private final Set<UnresolvedFieldUsage> fieldAssignments;

    /**
     * ���̃��\�b�h���Œ�`����Ă��郍�[�J���ϐ���ۑ�����ϐ�
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * ���̃u���b�N�̓����Œ�`���ꂽ�u���b�N��ۑ�����ϐ�
     */
    private final Set<UnresolvedBlock> innerBlocks;

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int toColumn;
}
