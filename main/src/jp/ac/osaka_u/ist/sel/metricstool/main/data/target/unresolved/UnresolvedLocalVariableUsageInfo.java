package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決ローカル変数使用を保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public class UnresolvedLocalVariableUsageInfo extends
        UnresolvedVariableUsageInfo<LocalVariableUsageInfo> {

    public UnresolvedLocalVariableUsageInfo(final UnresolvedLocalVariableInfo usedVariable,
            boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(usedVariable.getName(), reference, fromLine, fromColumn, toLine, toColumn);

        this.usedVariable = usedVariable;
    }

    @Override
    public LocalVariableUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final LocalVariableInfo usedVariable = this.getUsedVariable().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final boolean reference = this.isReference();

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolved = new LocalVariableUsageInfo(usedVariable, reference, fromLine,
                fromColumn, toLine, toColumn);

        return this.resolved;
    }

    public UnresolvedLocalVariableInfo getUsedVariable() {
        return this.usedVariable;
    }

    private UnresolvedLocalVariableInfo usedVariable;
}
