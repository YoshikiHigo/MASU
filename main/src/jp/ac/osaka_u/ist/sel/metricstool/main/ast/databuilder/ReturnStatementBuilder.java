package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReturnStatementInfo;


/**
 * ���^�[�����̏����\�z����N���X
 * 
 * @author t-miyake
 *
 */
public class ReturnStatementBuilder extends SingleStatementBuilder<UnresolvedReturnStatementInfo> {

    /**
     * �\�z�ς݂̎����}�l�[�W���[�C�\�z�ς݃f�[�^�}�l�[�W���[��^���ď������D
     * 
     * @param expressionManager �\�z�ςݎ����}�l�[�W���[
     * @param buildDataManager �\�z�ς݃f�[�^�}�l�[�W���[
     */
    public ReturnStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedReturnStatementInfo buildStatement(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedStatement = this
                .getLastBuiltExpression();
        
        final UnresolvedReturnStatementInfo returnStatement = new UnresolvedReturnStatementInfo(
                returnedStatement);
        returnStatement.setFromLine(fromLine);
        returnStatement.setFromColumn(fromColumn);
        returnStatement.setToLine(toLine);
        returnStatement.setToColumn(toColumn);

        return returnStatement;
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isReturn();
    }

}