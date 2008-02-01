package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if����while���Ȃǂ̃��\�b�h���̍\���i�u���b�N�j��\�����߂̃N���X
 * 
 * @author higo
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedUnitInfo<T> {

    /**
     * �u���b�N�\����\���I�u�W�F�N�g������������
     * 
     */
    public UnresolvedBlockInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.methodCalls = new HashSet<UnresolvedMethodCallInfo>();
        this.fieldUsages = new HashSet<UnresolvedFieldUsageInfo>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.innerBlocks = new HashSet<UnresolvedBlockInfo<?>>();
    }

    /**
     * ���̃u���b�N�����ɉ�������Ă��邩�ǂ�����������
     * 
     * @return ���ɉ�������Ă���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public final T getResolvedUnit() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * ���\�b�h�Ăяo����ǉ�����
     * 
     * @param methodCall ���\�b�h�Ăяo��
     */
    public final void addMethodCall(final UnresolvedMethodCallInfo methodCall) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodCall) {
            throw new NullPointerException();
        }

        this.methodCalls.add(methodCall);
    }

    /**
     * �t�B�[���h�g�p��ǉ�����
     * 
     * @param fieldUsage �t�B�[���h�g�p
     */
    public final void addFieldUsage(final UnresolvedFieldUsageInfo fieldUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldUsages.add(fieldUsage);
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
    public void addInnerBlock(final UnresolvedBlockInfo<?> innerBlock) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        this.innerBlocks.add(innerBlock);
    }

    /**
     * ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo���� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo���� Set
     */
    public final Set<UnresolvedMethodCallInfo> getMethodCalls() {
        return Collections.unmodifiableSet(this.methodCalls);
    }

    /**
     * ���̃u���b�N���ōs���Ă��関�����t�B�[���h�g�p�� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�����t�B�[���h�g�p�� Set
     */
    public final Set<UnresolvedFieldUsageInfo> getFieldUsages() {
        return Collections.unmodifiableSet(this.fieldUsages);
    }

    /**
     * ���̃u���b�N���Œ�`����Ă��関�������[�J���ϐ��� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���Œ�`����Ă��関�������[�J���ϐ��� Set
     */
    public final Set<UnresolvedLocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSet(this.localVariables);
    }

    /**
     * ���̃u���b�N���̖����������u���b�N�� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���̖����������u���b�N�� Set
     */
    public final Set<UnresolvedBlockInfo<?>> getInnerBlocks() {
        return Collections.unmodifiableSet(this.innerBlocks);
    }

    /**
     * ���\�b�h�Ăяo����ۑ�����ϐ�
     */
    private final Set<UnresolvedMethodCallInfo> methodCalls;

    /**
     * �t�B�[���h�g�p��ۑ�����ϐ�
     */
    private final Set<UnresolvedFieldUsageInfo> fieldUsages;

    /**
     * ���̃��\�b�h���Œ�`����Ă��郍�[�J���ϐ���ۑ�����ϐ�
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * ���̃u���b�N�̓����Œ�`���ꂽ�u���b�N��ۑ�����ϐ�
     */
    private final Set<UnresolvedBlockInfo<?>> innerBlocks;

    /**
     * �����ς݃u���b�N����ۑ����邽�߂̕ϐ�
     */
    protected T resolvedInfo;
}
