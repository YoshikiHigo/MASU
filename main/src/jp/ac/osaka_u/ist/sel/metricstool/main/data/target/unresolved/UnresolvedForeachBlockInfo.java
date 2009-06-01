package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedForeachBlockInfo extends UnresolvedBlockInfo<ForeachBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCforeach �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedForeachBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    /**
     * ���̖����� for �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public ForeachBlockInfo resolve(final TargetClassInfo usingClass,
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

        // ���� foreach���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �J��Ԃ��p�̕ϐ����擾
        final UnresolvedVariableDeclarationStatementInfo unresolvedVariableDeclaration = this
                .getIteratorVariableDeclaration();
        final VariableDeclarationStatementInfo variableDeclaration = unresolvedVariableDeclaration
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        // �J��Ԃ��p�̎����擾
        final UnresolvedExpressionInfo<?> unresolvedIteratorExpression = this
                .getIteratorExpression();
        final ExpressionInfo iteratorExpression = unresolvedIteratorExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // �O���̋�Ԃ��擾
        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ForeachBlockInfo(usingClass, outerSpace, fromLine, fromColumn,
                toLine, toColumn, variableDeclaration, iteratorExpression);
        return this.resolvedInfo;
    }

    /**
     * �ϐ���`��ݒ肷��
     * 
     * @param iteraotorVariableDeclaration �ϐ���`
     */
    public void setIteratorVariableDeclaration(
            final UnresolvedVariableDeclarationStatementInfo iteraotorVariableDeclaration) {
        this.iteratorVariableDeclaration = iteraotorVariableDeclaration;
    }

    /**
     * �J��Ԃ��p�̎���ݒ肷��
     * 
     * @param iteratorExpression �J��Ԃ��p�̎�
     */
    public void setIteratorExpression(final UnresolvedExpressionInfo<?> iteratorExpression) {
        this.iteratorExpression = iteratorExpression;
    }

    /**
     * �ϐ���`��Ԃ�
     * 
     * @return �ϐ���`
     */
    public UnresolvedVariableDeclarationStatementInfo getIteratorVariableDeclaration() {
        return this.iteratorVariableDeclaration;
    }

    /**
     * �J��Ԃ��p�̎���Ԃ�
     * 
     * @return �J��Ԃ��p�̎�
     */
    public UnresolvedExpressionInfo<?> getIteratorExpression() {
        return this.iteratorExpression;
    }

    private UnresolvedVariableDeclarationStatementInfo iteratorVariableDeclaration;

    private UnresolvedExpressionInfo<?> iteratorExpression;
}
