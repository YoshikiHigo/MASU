package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
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

    /**
     * �ʒu����^���ď�����
     */
    public UnresolvedLocalSpaceInfo() {
        
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.calls = new HashSet<UnresolvedCallInfo<?>>();
        this.variableUsages = new HashSet<UnresolvedVariableUsageInfo<VariableUsageInfo<?>>>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.statements = new HashSet<UnresolvedStatementInfo<?>>();
    }

    /**
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ǉ�����
     * 
     * @param call ���\�b�h�܂��̓R���X�g���N�^�Ăяo��
     */
    public final void addCall(final UnresolvedCallInfo<?> call) {

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
     * @param variableUsage �ϐ��g�p
     */
    public final void addVariableUsage(final UnresolvedVariableUsageInfo<VariableUsageInfo<?>> variableUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == variableUsage) {
            throw new NullPointerException();
        }

        this.variableUsages.add(variableUsage);
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
     * ����������ǉ�����
     * 
     * @param statement ��������
     */
    public void addStatement(final UnresolvedStatementInfo<?> statement) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new NullPointerException();
        }

        this.statements.add(statement);
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
    public final Set<UnresolvedCallInfo<?>> getCalls() {
        return Collections.unmodifiableSet(this.calls);
    }

    /**
     * ���̃u���b�N���ōs���Ă��関�����ϐ��g�p�� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�����ϐ��g�p�� Set
     */
    public final Set<UnresolvedVariableUsageInfo<VariableUsageInfo<?>>> getVariableUsages() {
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
    public final Set<UnresolvedStatementInfo<?>> getStatements() {
        return Collections.unmodifiableSet(this.statements);
    }
    
    protected final void resolveVariableUsages(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        
        if(!alreadyResolved()) {
            throw new NotResolvedException();
        }
        
        for (final UnresolvedVariableUsageInfo<VariableUsageInfo<?>> unresolvedVariableUsage : this.getVariableUsages()) {

            final VariableUsageInfo<?> variableUsage = unresolvedVariableUsage.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addVariableUsage(variableUsage);
        }
    }

    /**
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ۑ�����ϐ�
     */
    private final Set<UnresolvedCallInfo<?>> calls;

    /**
     * �t�B�[���h�g�p��ۑ�����ϐ�
     */
    private final Set<UnresolvedVariableUsageInfo<VariableUsageInfo<?>>> variableUsages;

    /**
     * ���̃��\�b�h���Œ�`����Ă��郍�[�J���ϐ���ۑ�����ϐ�
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * ���̃u���b�N�̓����Œ�`���ꂽ����������ۑ�����ϐ�
     */
    private final Set<UnresolvedStatementInfo<?>> statements;

}
