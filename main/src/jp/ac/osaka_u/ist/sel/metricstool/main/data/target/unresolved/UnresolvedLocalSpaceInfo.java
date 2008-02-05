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

        this.memberCalls = new HashSet<UnresolvedCallInfo>();
        this.fieldUsages = new HashSet<UnresolvedFieldUsageInfo>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.innerBlocks = new HashSet<UnresolvedBlockInfo<?>>();
    }

    /**
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ǉ�����
     * 
     * @param memberCall ���\�b�h�܂��̓R���X�g���N�^�Ăяo��
     */
    public final void addMemberCall(final UnresolvedCallInfo memberCall) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.memberCalls.add(memberCall);
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

    public void addChildSpaceInfo(final UnresolvedLocalSpaceInfo<?> childLocalInfo){
    	MetricsToolSecurityManager.getInstance().checkAccess();
    	if (null == childLocalInfo) {
    		throw new NullPointerException();
    	}
    	
    	this.fieldUsages.addAll(childLocalInfo.fieldUsages);
    	this.localVariables.addAll(childLocalInfo.localVariables);
    	this.memberCalls.addAll(childLocalInfo.memberCalls);
    }
    
    /**
     * ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo���� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo���� Set
     */
    public final Set<UnresolvedCallInfo> getMethodCalls() {
        return Collections.unmodifiableSet(this.memberCalls);
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
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ۑ�����ϐ�
     */
    private final Set<UnresolvedCallInfo> memberCalls;

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

}
