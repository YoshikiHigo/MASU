package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���[�J���̈�(���\�b�h�ƃ��\�b�h���̃u���b�N)��\���N���X
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class UnresolvedLocalSpaceInfo<T extends LocalSpaceInfo> extends
        UnresolvedUnitInfo<T> {

    public UnresolvedLocalSpaceInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.calls = new HashSet<UnresolvedCallInfo>();
        this.variableUsages = new HashSet<UnresolvedVariableUsageInfo>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.innerBlocks = new HashSet<UnresolvedBlockInfo<?>>();
    }

    /**
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ǉ�����
     * 
     * @param call ���\�b�h�܂��̓R���X�g���N�^�Ăяo��
     */
    public final void addCall(final UnresolvedCallInfo call) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == call) {
            throw new NullPointerException();
        }

        this.calls.add(call);
    }

    /**
     * �ϐ��g�p��ǉ�����
     * 
     * @param fieldUsage �t�B�[���h�g�p
     */
    public final void addVariableUsage(final UnresolvedFieldUsageInfo fieldUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.variableUsages.add(fieldUsage);
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

    public void addChildSpaceInfo(final UnresolvedLocalSpaceInfo<?> childLocalInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == childLocalInfo) {
            throw new NullPointerException();
        }

        this.variableUsages.addAll(childLocalInfo.variableUsages);
        this.localVariables.addAll(childLocalInfo.localVariables);
        this.calls.addAll(childLocalInfo.calls);
    }

    /**
     * ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo������уR���X�g���N�^�Ăяo���� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo������уR���X�g���N�^�Ăяo���� Set
     */
    public final Set<UnresolvedCallInfo> getCalls() {
        return Collections.unmodifiableSet(this.calls);
    }

    /**
     * ���̃u���b�N���ōs���Ă��関�����ϐ��g�p�� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�����ϐ��g�p�� Set
     */
    public final Set<UnresolvedVariableUsageInfo> getVariableUsages() {
        return Collections.unmodifiableSet(this.variableUsages);
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
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ۑ�����ϐ�
     */
    private final Set<UnresolvedCallInfo> calls;

    /**
     * �t�B�[���h�g�p��ۑ�����ϐ�
     */
    private final Set<UnresolvedVariableUsageInfo> variableUsages;

    /**
     * ���̃��\�b�h���Œ�`����Ă��郍�[�J���ϐ���ۑ�����ϐ�
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * ���̃u���b�N�̓����Œ�`���ꂽ�u���b�N��ۑ�����ϐ�
     */
    private final Set<UnresolvedBlockInfo<?>> innerBlocks;

}
