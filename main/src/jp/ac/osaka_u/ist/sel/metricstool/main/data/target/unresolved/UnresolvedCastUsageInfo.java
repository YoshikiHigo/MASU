package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������L���X�g�g�p��\���N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedCastUsageInfo extends UnresolvedEntityUsageInfo<CastUsageInfo> {

    /**
     * �L���X�g��������^���ď�����
     * 
     * @param castedType �L���X�g�����^
     */
    public UnresolvedCastUsageInfo(final UnresolvedTypeInfo castedType) {
        
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.castedType = castedType;
    }

    /**
     * �L���X�g�����^��Ԃ�
     * @return �L���X�g�����^
     */
    public UnresolvedTypeInfo getCastType() {
        return this.castedType;
    }

    @Override
    public CastUsageInfo resolve(final TargetClassInfo usingClass,
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
            return this.getResolved();
        }

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �L���X�g�^�g�p������
        final UnresolvedTypeInfo unresolvedCastType = this.getCastType();
        final TypeInfo castType = unresolvedCastType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // �L���X�g�g�p������
        this.resolvedInfo = new CastUsageInfo(castType, fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * �L���X�g�����^��ۑ�����ϐ�
     */
    private final UnresolvedTypeInfo castedType;

}
