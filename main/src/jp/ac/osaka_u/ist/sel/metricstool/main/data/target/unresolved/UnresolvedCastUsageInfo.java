package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決キャスト使用を表すクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedCastUsageInfo extends UnresolvedEntityUsageInfo<CastUsageInfo> {

    /**
     * キャストした方を与えて初期化
     * 
     * @param castedType キャストした型
     */
    public UnresolvedCastUsageInfo(final UnresolvedTypeInfo castedType) {
        
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.castedType = castedType;
    }

    /**
     * キャストした型を返す
     * @return キャストした型
     */
    public UnresolvedTypeInfo getCastType() {
        return this.castedType;
    }

    @Override
    public CastUsageInfo resolve(final TargetClassInfo usingClass,
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

        // キャスト型使用を解決
        final UnresolvedTypeInfo unresolvedCastType = this.getCastType();
        final TypeInfo castType = unresolvedCastType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // キャスト使用を解決
        this.resolvedInfo = new CastUsageInfo(castType, fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * キャストした型を保存する変数
     */
    private final UnresolvedTypeInfo castedType;

}
