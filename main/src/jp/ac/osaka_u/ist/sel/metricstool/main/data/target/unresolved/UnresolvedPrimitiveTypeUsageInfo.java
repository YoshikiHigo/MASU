package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class UnresolvedPrimitiveTypeUsageInfo extends UnresolvedEntityUsageInfo {

    public UnresolvedPrimitiveTypeUsageInfo(final PrimitiveTypeInfo type, final String literal) {
        this.type = type;
        this.literal = literal;
    }

    public String getLiteral() {
        return this.literal;
    }

    /**
     * �������G���e�B�e�B�g�p����������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ��������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ς݃G���e�B�e�B�g�p��Ԃ�
     * 
     * @return �����ς݃G���e�B�e�B�g�p
     * @throws ��������Ă��Ȃ��ꍇ�ɃX���[�����
     */
    @Override
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new PrimitiveTypeUsageInfo(this.type, this.literal, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    private PrimitiveTypeUsageInfo resolvedInfo;

    private PrimitiveTypeInfo type;

    private String literal;
}
