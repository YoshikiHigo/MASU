package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����������g�p��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedParameterUsageInfo extends UnresolvedVariableUsageInfo {

    /**
     * �g�p����Ă�������C�Q�Ƃ��ǂ�����^���ď�����
     * 
     * @param usedVariable�@�g�p����Ă������
     * @param reference�@�Q�Ƃ̏ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public UnresolvedParameterUsageInfo(final UnresolvedParameterInfo usedVariable,
            boolean reference) {

        super(usedVariable.getName(), reference);

        this.usedVariable = usedVariable;
    }

    /**
     * ���̖����������g�p����������
     */
    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        final ParameterInfo usedVariable = this.getUsedVariable().resolveUnit(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final boolean reference = this.isReference();

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ParameterUsageInfo(usedVariable, reference, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * �g�p����Ă��������Ԃ�
     * 
     * @return�@�g�p����Ă������
     */
    public UnresolvedParameterInfo getUsedVariable() {
        return this.usedVariable;
    }

    private UnresolvedParameterInfo usedVariable;
}
