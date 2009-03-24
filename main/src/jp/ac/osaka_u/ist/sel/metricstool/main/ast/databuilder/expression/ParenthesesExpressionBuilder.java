package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParenthesesExpressionInfo;


/**
 * ���ʎ������\�z����N���X
 * 
 * @author g-yamada
 * 
 */
public class ParenthesesExpressionBuilder extends ExpressionBuilder {

    /**
     * �I�u�W�F�N�g������������
     * 
     * @param expressionManager ExpressionElementManager
     * @param buildDataManager 
     */
    public ParenthesesExpressionBuilder(final ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    /**
     * exit �����m�[�h�����ʎ��Ȃ�CUnresolvedParenthesesExpressionInfo�����閽�߂�����
     */
    @Override
    protected void afterExited(AstVisitEvent event) throws ASTParseException {
        final AstToken token = event.getToken();
        if (isTriggerToken(token)) {
            this.buildParenthesesExpressionBuilder();
        }
    }

    /**
     * ���߂���Ď��ۂ�UnresolvedParenthesesExpressionInfo������
     */
    protected void buildParenthesesExpressionBuilder() {
        final ExpressionElement[] elements = this.getAvailableElements();
        if (elements.length > 0) {
            final ExpressionElement from = elements[0];
            final ExpressionElement to = elements[elements.length - 1];
            // expressionManager ����Ō�� pop ���ꂽ ExpressionElement ��
            // ���ʓ��̎�(UnresolvedExpressionInfo)������
            final UnresolvedExpressionInfo<? extends ExpressionInfo> parentheticExpression = expressionManager
                    .getLastPoppedExpressionElement().getUsage();

            if (null != parentheticExpression) {
                final UnresolvedParenthesesExpressionInfo paren = new UnresolvedParenthesesExpressionInfo(
                        parentheticExpression);
                paren.setFromLine(from.fromLine);
                paren.setFromColumn(from.fromColumn);
                paren.setToLine(to.toLine);
                paren.setToColumn(to.toColumn);
                expressionManager.pushExpressionElement(new UsageElement(paren));
            }
        }
    }

    /**
     * token �����ʎ���\���̂��ǂ�����Ԃ�
     */
    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isParenthesesExpression();
    }

}
