package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * PDGParameterNodeで用いるためのみに作成したクラス
 * 
 * @author higo
 *
 */
public class ParameterDeclarationStatementInfo implements ExecutableElementInfo {

    /**
     * 宣言されているパラメータ，位置情報を与えて初期化
     * 
     * @param parameter 宣言されているパラメータ
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ParameterDeclarationStatementInfo(final ParameterInfo parameter, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        this.parameter = parameter;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        final TypeInfo type = this.getParameter().getType();
        text.append(type.getTypeName());

        text.append(" ");

        text.append(this.getParameter().getName());

        return text.toString();
    }

    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> variables = new HashSet<VariableInfo<? extends UnitInfo>>();
        variables.add(this.getParameter());
        return Collections.unmodifiableSet(variables);
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ClassTypeInfo>());
    }

    public ParameterInfo getParameter() {
        return this.parameter;
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.parameter.getDefinitionUnit();
    }

    @Override
    public LocalSpaceInfo getOwnerSpace() {
        return this.parameter.getDefinitionUnit();
    }

    @Override
    public int getFromColumn() {
        return this.fromColumn;
    }

    @Override
    public int getFromLine() {
        return this.fromLine;
    }

    @Override
    public int getToColumn() {
        return this.toColumn;
    }

    @Override
    public int getToLine() {
        return this.toLine;
    }

    @Override
    public int compareTo(Position o) {
        return this.parameter.compareTo(o);
    }

    private final ParameterInfo parameter;

    private final int fromLine;

    private final int fromColumn;

    private final int toLine;

    private final int toColumn;
}
