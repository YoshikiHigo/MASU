package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParenthesesExpressionInfo;


/**
 * ���ʎ������\�z����N���X 
 * <br>
 * �����\������v�f�ł͂��邪�C���̍\���؏��ɉe����^���Ă͂����Ȃ����߁C 
 * {@link ExpressionBuilder}�̎q�N���X�ɂ͂��Ă��Ȃ��D
 * 
 * @author g-yamada
 * 
 */
public class ParenthesesExpressionBuilder extends
        StateDrivenDataBuilder<UnresolvedParenthesesExpressionInfo> {

    /**
     * �I�u�W�F�N�g������������
     * 
     * @param expressionManager ExpressionElementManager
     * @param buildDataManager 
     */
    public ParenthesesExpressionBuilder(final ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;

        this.addStateManager(this.expressionStateManger);
    }

    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);
    }

    /**
     * exit �����m�[�h�����ʎ��Ȃ�CUnresolvedParenthesesExpressionInfo�����閽�߂�����
     */
    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);
        final AstToken token = e.getToken();
        if (this.isActive() && this.expressionStateManger.inExpression()
                && token.isParenthesesExpression()) {
            this.buildParenthesesExpressionBuilder(e);
        }
    }

    /**
     * ���߂���Ď��ۂ�UnresolvedParenthesesExpressionInfo������
     */
    protected void buildParenthesesExpressionBuilder(final AstVisitEvent e) {
        // ExpressionManager��expressionAnalyzeStack�̓��̗v�f���C���ʎ��̒����ɂ���ExpressionElement
        // �|�b�v����K�v�����邩�킩��Ȃ��̂ŃX�^�b�N�͂�����Ȃ�
        final ExpressionElement parentheticElement = expressionManager.getPeekExpressionElement();
        final UnresolvedExpressionInfo<? extends ExpressionInfo> parentheticExpression = parentheticElement
                .getUsage();

        if (null != parentheticExpression) {
            // expressionAnalyzeStack�̓��̗v�f���|�b�v���āC�����Ɋ��ʎ����v�b�V������
            expressionManager.popExpressionElement();
            final UnresolvedParenthesesExpressionInfo paren = new UnresolvedParenthesesExpressionInfo(
                    parentheticExpression);
            paren.setFromLine(e.getStartLine());
            paren.setFromColumn(e.getStartColumn());
            paren.setToLine(e.getEndLine());
            paren.setToColumn(e.getEndColumn());
            expressionManager.pushExpressionElement(new UsageElement(paren));
        } else {
            // TODO (a)�̂悤�ȏꍇ�̊��ʂ��Ƃ��悤�ɂ���
            /*
             * �����ɂ���̂�(a)�݂����Ɋ��ʎ��̒����ɕϐ������Ă���Ƃ��D
             * expressionAnalyzeStack��������Ȃ����߁C���ʂƂ��Ċ��ʎ����Ȃ��������̂悤��
             * �U�镑��������D
             * 
             * �J�b�R�������ɕϐ������Ă���ꍇ�CAST�̊��ʎ�����exit���鎞�_�ł�
             * �܂������̕ϐ���Usage�����݂��Ȃ��D
             * �ϐ�Element��resolveAsVariable���Ăяo�����Ƃ�Usage�𐶐�����̂����C
             * ���̏ꏊ�ł͕ϐ��������Ȃ̂�����Ȃ̂��킩��Ȃ����ߌĂяo���Ȃ��D
             */
        }
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        // nothing to do
    }

    protected final ExpressionElementManager expressionManager;

    protected final BuildDataManager buildDataManager;

    private final ExpressionStateManager expressionStateManger = new ExpressionStateManager();
}