package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 一項演算の内容を表すクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedMonominalOperationInfo extends
        UnresolvedEntityUsageInfo<MonominalOperationInfo> {

    /**
     * 項と一項演算の結果の型を与えて初期化
     * 
     * @param operand 項
     * @param type 一項演算の結果の型
     */
    public UnresolvedMonominalOperationInfo(final UnresolvedEntityUsageInfo<? extends EntityUsageInfo> operand,
            final PrimitiveTypeInfo type) {

        if (null == operand || null == type) {
            throw new IllegalArgumentException("term or type is null");
        }

        this.operand = operand;
        this.type = type;
    }

    @Override
    public MonominalOperationInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedEntityUsageInfo<?> unresolvedTerm = this.getOperand();
        final EntityUsageInfo term = unresolvedTerm.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final PrimitiveTypeInfo type = this.getResultType();

        this.resolvedInfo = new MonominalOperationInfo(term, type, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * 一項演算の項を返す
     * 
     * @return 一項演算の項
     */
    public UnresolvedEntityUsageInfo<? extends EntityUsageInfo> getOperand() {
        return this.operand;
    }

    /**
     * 一項演算の結果の型を返す
     * 
     * @return 一項演算の結果の型
     */
    public PrimitiveTypeInfo getResultType() {
        return this.type;
    }

    /**
     * 一項演算の項
     */
    private final UnresolvedEntityUsageInfo<? extends EntityUsageInfo> operand;

    /**
     * 一項演算の結果の型
     */
    private final PrimitiveTypeInfo type;

}
