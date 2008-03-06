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
     * @param term 項
     * @param type 一項演算の結果の型
     */
    public UnresolvedMonominalOperationInfo(final UnresolvedEntityUsageInfo<?> term,
            final PrimitiveTypeInfo type) {

        if (null == term || null == type) {
            throw new IllegalArgumentException("term or type is null");
        }

        this.term = term;
        this.type = type;
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolved;
    }

    @Override
    public MonominalOperationInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolved;
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

        final UnresolvedEntityUsageInfo<?> unresolvedTerm = this.getTerm();
        final EntityUsageInfo term = unresolvedTerm.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final PrimitiveTypeInfo type = this.getResultType();

        this.resolved = new MonominalOperationInfo(term, type, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolved;
    }

    /**
     * 一項演算の項を返す
     * 
     * @return 一項演算の項
     */
    public UnresolvedEntityUsageInfo<?> getTerm() {
        return this.term;
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
    private final UnresolvedEntityUsageInfo<?> term;

    /**
     * 一項演算の結果の型
     */
    private final PrimitiveTypeInfo type;

    private MonominalOperationInfo resolved;
}
