package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.LocalVariableBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForeachBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForeachBlockStateManager.FOREACH_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


/* **
 * For-Eachブロックのビルダー
 * @author a-saitoh
 *
 */
public class ForeachBlockBuilder extends
        InnerBlockBuilder<ForeachBlockInfo, UnresolvedForeachBlockInfo> {

    public ForeachBlockBuilder(final BuildDataManager targetDataManager,
            final LocalVariableBuilder iteratorVariableBuilder,
            final ExpressionElementManager expressionManager) {
        super(targetDataManager, new ForeachBlockStateManager());

        if (null == iteratorVariableBuilder) {
            throw new IllegalArgumentException("iteratorVariableBuilder is null.");
        }

        this.expressionManager = expressionManager;
        this.iteratorVariableBuilder = iteratorVariableBuilder;
        this.addInnerBuilder(this.iteratorVariableBuilder);

    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChangend(event);
        final StateChangeEventType type = event.getType();

        if (type.equals(FOREACH_BLOCK_STATE_CHANGE.ENTER_FOREACH_VARIABLE)) {
            this.iteratorVariableBuilder.clearBuiltData();
            this.iteratorVariableBuilder.activate();
        } else if (type.equals(FOREACH_BLOCK_STATE_CHANGE.EXIT_FOREACH_VARIABLE)) {
            this.getBuildingBlock().setIteratorVariable(
                    this.iteratorVariableBuilder.getLastBuildData());
            this.iteratorVariableBuilder.deactivate();
        } else if (type.equals(FOREACH_BLOCK_STATE_CHANGE.ENTER_FOREACH_EXPRESSION)) {
            // なにもしないでOK
        } else if (type.equals(FOREACH_BLOCK_STATE_CHANGE.EXIT_FOREACH_EXPRESSION)) {
            final UnresolvedExpressionInfo<? extends ExpressionInfo> iteratorExpression = null == this.expressionManager
                    .getPeekExpressionElement() ? null : this.expressionManager
                    .getPeekExpressionElement().getUsage();
            assert null != iteratorExpression;
            this.getBuildingBlock().setIteratorExpression(iteratorExpression);
            this.getBuildingBlock().initBody();
        }
    }

    @Override
    protected UnresolvedForeachBlockInfo createUnresolvedBlockInfo(
            UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedForeachBlockInfo(outerSpace);
    }

    private final LocalVariableBuilder iteratorVariableBuilder;

    private final ExpressionElementManager expressionManager;
}
