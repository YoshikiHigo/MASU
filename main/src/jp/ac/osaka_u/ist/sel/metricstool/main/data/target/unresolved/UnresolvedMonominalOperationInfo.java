package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �ꍀ���Z�̓��e��\���N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedMonominalOperationInfo extends
        UnresolvedExpressionInfo<MonominalOperationInfo> {

    /**
     * ���ƈꍀ���Z�̌��ʂ̌^��^���ď�����
     * 
     * @param operand ��
     * @param operator �ꍀ���Z�̉��Z�q
     * @param type �ꍀ���Z�̌��ʂ̌^
     */
    public UnresolvedMonominalOperationInfo(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> operand,
            final OPERATOR operator, final PrimitiveTypeInfo type) {

        if (null == operand || null == operator || null == type) {
            throw new IllegalArgumentException("term or type is null");
        }

        this.operand = operand;
        this.operator = operator;
        this.type = type;
    }

    @Override
    public MonominalOperationInfo resolve(final TargetClassInfo usingClass,
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

        // �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        final UnresolvedExpressionInfo<?> unresolvedTerm = this.getOperand();
        final ExpressionInfo term = unresolvedTerm.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final PrimitiveTypeInfo type = this.getResultType();
        final boolean isPreposed = fromColumn < term.getFromColumn() ? true : false;

        this.resolvedInfo = new MonominalOperationInfo(term, this.operator, isPreposed, type,
                fromLine, fromColumn, toLine, toColumn);
        this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);
        return this.resolvedInfo;
    }

    /**
     * �ꍀ���Z�̍���Ԃ�
     * 
     * @return �ꍀ���Z�̍�
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getOperand() {
        return this.operand;
    }

    /**
     * �ꍀ���Z�̌��ʂ̌^��Ԃ�
     * 
     * @return �ꍀ���Z�̌��ʂ̌^
     */
    public PrimitiveTypeInfo getResultType() {
        return this.type;
    }

    /**
     * �ꍀ���Z�̍�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> operand;

    /**
     * �ꍀ���Z�̉��Z�q
     */
    private final OPERATOR operator;

    /**
     * �ꍀ���Z�̌��ʂ̌^
     */
    private final PrimitiveTypeInfo type;

}
