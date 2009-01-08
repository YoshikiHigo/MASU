package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列コンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
public class UnresolvedArrayConstructorCallInfo extends
        UnresolvedConstructorCallInfo<UnresolvedArrayTypeInfo, ArrayConstructorCallInfo> {

    /**
     * 配列コンストラクタ呼び出しが実行される参照型を与えてオブジェクトを初期化
     * 
     * @param unresolvedArrayType コンストラクタ呼び出しが実行される型
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType) {

        super(unresolvedArrayType);

        this.indexExpressions = new ArrayList<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
    }

    /**
     * 配列コンストラクタ呼び出しが実行される型と位置情報を与えて初期化
     * @param unresolvedArrayType コンストラクタ呼び出しが実行される型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        this(unresolvedArrayType);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * 名前解決を行う
     */
    @Override
    public ArrayConstructorCallInfo resolve(final TargetClassInfo usingClass,
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

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // コンストラクタのシグネチャを取得
        final List<ExpressionInfo> actualParameters = super.resolveArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final List<ReferenceTypeInfo> typeArguments = super.resolveTypeArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //　コンストラクタの型を解決
        final UnresolvedArrayTypeInfo unresolvedArrayType = this.getReferenceType();
        final ArrayTypeInfo arrayType = unresolvedArrayType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // インデックスの式を解決
        final List<UnresolvedExpressionInfo<? extends ExpressionInfo>> unresolvedIndexExpressions = this
                .getIndexExpressions();
        final List<ExpressionInfo> indexExpressions = new ArrayList<ExpressionInfo>();
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> unresolvedIndexExpression : unresolvedIndexExpressions) {
            indexExpressions.add(unresolvedIndexExpression.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager));
        }

        this.resolvedInfo = new ArrayConstructorCallInfo(arrayType, indexExpressions, usingMethod,
                fromLine, fromColumn, toLine, toColumn);
        this.resolvedInfo.addArguments(actualParameters);
        this.resolvedInfo.addTypeArguments(typeArguments);
        return this.resolvedInfo;
    }

    /**
     * インデックスの式をセット
     * 
     * @param indexExpression
     */
    public void addIndexExpression(final int dimention,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == indexExpression) {
            throw new IllegalArgumentException("indexExpression is null");
        }

        this.indexExpressions.add(dimention - 1, indexExpression);
    }

    /**
     * インデックスの式を取得
     * 
     * @return インデックスの式
     */
    public List<UnresolvedExpressionInfo<? extends ExpressionInfo>> getIndexExpressions() {
        return this.indexExpressions;
    }

    /**
     * インデックスの式を保存するための変数
     */
    private List<UnresolvedExpressionInfo<? extends ExpressionInfo>> indexExpressions;
}
