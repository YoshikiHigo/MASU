package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 finally ブロック情報を表すクラス
 * 
 * @author higo
 */
public final class UnresolvedFinallyBlockInfo extends UnresolvedBlockInfo<FinallyBlockInfo> {

    /**
     * 対応する try ブロック情報を与えて finally ブロックを初期化
     * 
     * @param ownerTryBlock
     */
    public UnresolvedFinallyBlockInfo(final UnresolvedTryBlockInfo ownerTryBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerTryBlock) {
            throw new IllegalArgumentException("ownerTryBlock is null");
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * この未解決 finally 節を解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManger 用いるメソッドマネージャ
     */
    @Override
    public FinallyBlockInfo resolve(final TargetClassInfo usingClass,
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

        // この finally 節が属する try ブロックを取得
        final UnresolvedTryBlockInfo unresolvedOwnerTryBlock = this.getOwnerTryBlock();
        final TryBlockInfo ownerTryBlock = unresolvedOwnerTryBlock.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // この finally 節の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo outerSpace = this.getOuterSpace().getResolved();

        this.resolvedInfo = new FinallyBlockInfo(usingClass, usingMethod, outerSpace, fromLine,
                fromColumn, toLine, toColumn, ownerTryBlock);

        //　内部ブロック情報を解決し，解決済みcaseエントリオブジェクトに追加
        for (final UnresolvedStatementInfo<?> unresolvedStatement : this.getStatements()) {
            final StatementInfo statement = unresolvedStatement.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addStatement(statement);
        }        

        // ローカル変数情報を解決し，解決済みcaseエントリオブジェクトに追加
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        return this.resolvedInfo;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    public UnresolvedTryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    private final UnresolvedTryBlockInfo ownerTryBlock;
}
