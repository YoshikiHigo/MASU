package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LocalVariableStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableDeclarationStatementInfo;


public class LocalVariableBuilder
        extends
        InitializableVariableBuilder<UnresolvedLocalVariableInfo, UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>> {

    public LocalVariableBuilder(BuildDataManager buildDataManager,
            final ExpressionElementManager expressionManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder, ModifiersInterpriter interpriter) {
        super(buildDataManager, expressionManager, new LocalVariableStateManager(),
                modifiersBuilder, typeBuilder, nameBuilder);

        this.interpriter = interpriter;
        this.statemetnStack = new Stack<UnresolvedVariableDeclarationStatementInfo>();

    }

    @Override
    protected UnresolvedLocalVariableInfo buildVariable(final String[] name,
            final UnresolvedTypeInfo type, final ModifierInfo[] modifiers,
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> definitionSpace,
            final int startLine, final int startColumn, final int endLine, final int endColumn) {
        String varName = "";
        if (name.length > 0) {
            varName = name[0];
        }

        final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression = this.builtInitializerStack
                .pop();

        final UnresolvedLocalVariableInfo var = new UnresolvedLocalVariableInfo(varName, type,
                definitionSpace, initializationExpression, startLine, startColumn, endLine,
                endColumn);
        for (ModifierInfo modifier : modifiers) {
            var.addModifier(modifier);
        }

        if (null != interpriter) {
            // TODO            interpriter.interpirt(modifiers, var);
        }

        if (null != buildDataManager) {
            buildDataManager.addLocalVariable(var);
        }

        final UnresolvedVariableDeclarationStatementInfo declarationStatement = new UnresolvedVariableDeclarationStatementInfo(
                var, initializationExpression);
        declarationStatement.setFromLine(startLine);
        declarationStatement.setFromColumn(startColumn);
        declarationStatement.setToLine(endLine);
        declarationStatement.setToColumn(endColumn);

        final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this.buildDataManager
                .getCurrentLocalSpace();
        if (null != currentLocal) {
            currentLocal.addStatement(declarationStatement);
        }

        this.statemetnStack.add(declarationStatement);

        return var;
    }

    @Override
    public void reset() {
        super.reset();
        this.statemetnStack.clear();
    }

    @Override
    protected UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> validateDefinitionSpace(
            UnresolvedUnitInfo<? extends UnitInfo> definitionUnit) {
        return definitionUnit instanceof UnresolvedLocalSpaceInfo ? (UnresolvedLocalSpaceInfo<?>) definitionUnit
                : null;
    }

    public UnresolvedVariableDeclarationStatementInfo getLastStackedDeclationStatement() {
        return this.statemetnStack.isEmpty() ? null : this.statemetnStack.peek();
    }

    private final Stack<UnresolvedVariableDeclarationStatementInfo> statemetnStack;

    private final ModifiersInterpriter interpriter;
}
