package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class UnresolvedLiteralUsageInfo extends UnresolvedEntityUsageInfo {

    public UnresolvedLiteralUsageInfo(final String literal, final PrimitiveTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == literal) || (null == type)) {
            throw new IllegalArgumentException();
        }
        
        this.literal = literal;
        this.type = type;
        this.resolvedInfo = null;
    }

    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public final EntityUsageInfo getResolvedEntityUsage() {
        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final String literal = this.getLiteral();
        final PrimitiveTypeInfo type = (PrimitiveTypeInfo) this.getType();

        this.resolvedInfo = new LiteralUsageInfo(literal, type, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * ���̃��e�����g�p�̕������Ԃ�
     * 
     * @return ���̃��e�����g�p�̕�����
     */
    public final String getLiteral() {
        return this.literal;
    }

    /**
     * ���̃��e�����g�p�̕������Ԃ�
     * 
     * @return�@���̃��e�����g�p�̕������Ԃ�
     */
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    private final String literal;

    private final PrimitiveTypeInfo type;

    private EntityUsageInfo resolvedInfo;
}
