package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeArgumentStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSimpleTypeParameterUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterUsage;


/**
 * 式中以外での型参照の情報を構築するビルダー．
 * 
 * @author kou-tngt
 *
 */
public class TypeBuilder extends CompoundDataBuilder<UnresolvedTypeInfo> {

    /**
     * 引数のBuildDataManagerを用いて初期化を行う．
     * 
     * @param buildDataManager　このビルダーで利用するデータ管理者
     */
    public TypeBuilder(final BuildDataManager buildDataManager) {
        if (null == buildDataManager) {
            throw new NullPointerException("nameSpaceManager is null.");
        }

        this.buildDataManager = buildDataManager;

        this.addStateManager(this.typeStateManager);
        this.addStateManager(new TypeArgumentStateManager());
        this.addStateManager(new TypeParameterStateManager());

        this.addInnerBuilder(this.identifierBuilder);
    }

    /**
     * ビジターがノードに入った時のイベント通知を受け取る．
     * @param ビジットイベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        if (this.isActive() && this.typeStateManager.isEntered()) {
            //型定義部内
            final AstToken token = event.getToken();

            if (null == this.primitiveType && null == this.voidType
                    && token instanceof BuiltinTypeToken) {
                //トークンから組み込み型情報をさくせいする
                final BuiltinTypeToken typeToken = (BuiltinTypeToken) token;
                if (typeToken.isPrimitiveType()) {
                    this.primitiveType = typeToken.getType();
                } else if (typeToken.isVoidType()) {
                    this.voidType = typeToken.getType();
                }
            }
        }
    }

    /**
     * ビジターがノードから出た時のイベント通知を受け取る．
     * @param ビジットイベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        if (this.isActive() && this.typeStateManager.isEntered()) {
            //型定義部内
            final AstToken token = event.getToken();
            if (token.isArrayDeclarator()) {
                //配列記述があればそれをカウントして次元数にする
                this.arrayCount++;
            } else if (token.isNameSeparator()) {
                //名前区切りに到達した時に可能なら過去に構築した型を連結する
                this.combineBuiltTypes();
            }
        }
    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        if (this.isActive()) {
            final StateChangeEventType type = event.getType();
            if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
                //型宣言の中に入ったので識別子ビルダーを起動
                this.identifierBuilder.activate();
            } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) {
                //型宣言の中から出たので，
                //その結果を構築して登録
                this.buildType();
                if (!this.typeStateManager.isEntered()) {
                    //全ての型構築部の外に出たなら識別子ビルダーを非アクティブに
                    this.identifierBuilder.deactivate();
                }
            } else if (type
                    .equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.ENTER_TYPE_ARGUMENTS)) {
                //型引数群の定義部に入ったので，新たな型引数群の情報を入れるリストをスタックにつむ
                this.typeArgumentsLists.push(new ArrayList<UnresolvedTypeInfo>());
            } else if (type
                    .equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENTS)) {
                //型引数群の定義部が終わったので，スタックの一番上のリストを，今現在利用できる型パラメータ群として取り出す．
                if (!this.typeArgumentsLists.isEmpty()) {
                    this.availableTypeArugments = this.typeArgumentsLists.pop();
                    this.buildType();
                } else {
                    assert (false) : "Illegal state: requested type arguments were not available.";
                }
            } else if (type.equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENT)) {
                //型引数記述部から出たので，直近の型情報を取り出して型引数情報として登録する
                if (!this.typeArgumentsLists.isEmpty()) {
                    this.typeArgumentsLists.peek().add(this.popLastBuiltData());
                } else {
                    assert (false) : "Illegal state: type argument could not be registered.";
                }
            } else if (type
                    .equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.ENTER_TYPE_WILDCARD)) {
                //ワイルドカード記述部に入ったのでインクリメント
                this.inWildCardCount++;
            } else if (type.equals(TypeArgumentStateManager.TYPE_ARGUMENT_STATE.EXIT_TYPE_WILDCARD)) {
                //ワイルドカード記述部から出るので，型上限情報を取得して型情報を登録
                final UnresolvedTypeInfo upperBounds = this.getCurrentUpperBounds();
                this.currentUpperBounds = null;
                if (null != upperBounds) {
                    this.registBuiltData(upperBounds);
                } else {
                    assert (false) : "Illegal state: type upper bounds was not specified.";
                }
                this.inWildCardCount--;
            } else if (this.inWildCardCount > 0
                    && type.equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS)) {
                //ワイルドカード内で型上限情報があったので，それを登録
                this.currentUpperBounds = this.popLastBuiltData();
            }
        }
    }

    /**
     * 型上限情報を取得する
     * @return　型上限の情報
     */
    protected UnresolvedTypeInfo getCurrentUpperBounds() {
        return this.currentUpperBounds;
    }

    /**
     * 型情報を構築するメソッド．
     * 型定義ノードから出た時，型情報以外を表すトークンに移ってしまった時，型パラメータ群の構築が終了したときに呼び出される．
     */
    protected void buildType() {
        UnresolvedTypeInfo resultType = null;

        if (null != this.primitiveType) {
            //組み込み型のデータが作られていたのでそれを使う
            resultType = this.primitiveType;
            this.primitiveType = null;
        } else if (null != this.voidType) {
            resultType = this.voidType;
            this.voidType = null;
        } else if (this.identifierBuilder.hasBuiltData()) {
            //識別子情報が構築されていたので，それを使って参照型を作る．
            final String[] identifier = this.identifierBuilder.popLastBuiltData();

            assert (0 != identifier.length) : "Illegal state: identifier was not built.";

            //名前置換を解決しておく
            final String[] trueName = this.buildDataManager.resolveAliase(identifier);

            //参照型を作成
            final UnresolvedReferenceTypeInfo referenceType = new UnresolvedReferenceTypeInfo(
                    this.buildDataManager.getAvailableNameSpaceSet(), trueName);

            //使える型引数があれば登録してしまう．
            if (null != this.availableTypeArugments) {
                for (final UnresolvedTypeInfo type : this.availableTypeArugments) {
                    referenceType
                            .addTypeParameterUsage(new UnresolvedSimpleTypeParameterUsage(type));
                }

                this.availableTypeArugments = null;
            }
            resultType = referenceType;

        } else if (this.hasBuiltData()) {
            //識別子が新しく作られて足りはしないけど過去に構築したデータがあったので，それを使っておく
            resultType = this.popLastBuiltData();

        } else {
            assert (false) : "Illegal state: type can not built.";
        }

        if (0 < this.arrayCount) {
            //配列記述があったのでそれらの型を配列にしておく
            resultType = UnresolvedArrayTypeInfo.getType(resultType, this.arrayCount);
        }

        this.arrayCount = 0;
        this.registBuiltData(resultType);
    }

    /**
     * ビジターが名前区切りノードから出る時に呼び出される．
     * 可能であれば，過去に構築した2つの型情報を用いて，1つの長い参照型情報を構築する．
     * 構築できないのであれば特に何もしない．
     */
    protected void combineBuiltTypes() {
        if (this.hasBuiltData() && this.getBuiltDataCount() == 2) {
            final UnresolvedTypeInfo second = this.popLastBuiltData();
            final UnresolvedTypeInfo first = this.popLastBuiltData();

            //どちらも参照型であるはず
            assert (first instanceof UnresolvedReferenceTypeInfo) : "Illegal state: firstType was not unresolvedReference.";
            assert (second instanceof UnresolvedReferenceTypeInfo) : "Illegal state: firstType was not unresolvedReference.";

            if (first instanceof UnresolvedReferenceTypeInfo
                    && second instanceof UnresolvedReferenceTypeInfo) {
                final UnresolvedReferenceTypeInfo secondReference = (UnresolvedReferenceTypeInfo) second;
                final String[] names = secondReference.getFullReferenceName();
                //ownerを持つ新しい参照型を作る
                final UnresolvedReferenceTypeInfo result = new UnresolvedReferenceTypeInfo(
                        this.buildDataManager.getAvailableNameSpaceSet(), names,
                        (UnresolvedReferenceTypeInfo) first);

                //型引数情報をセットする
                for (final UnresolvedTypeParameterUsage usage : secondReference.getTypeParameterUsages()) {
                    result.addTypeParameterUsage(usage);
                }

                //結果を構築済みの型として登録
                this.registBuiltData(result);
            }
        }
    }

    /**
     * 配列記述子の数をカウントする
     */
    private int arrayCount;

    /**
     * 型参照でワイルドカードが使われた時の上限情報を記憶しておく
     */
    private UnresolvedTypeInfo currentUpperBounds;

    /**
     * 型引数群を記録しておくスタック
     */
    private final Stack<List<UnresolvedTypeInfo>> typeArgumentsLists = new Stack<List<UnresolvedTypeInfo>>();

    /**
     * 構築が終わって利用できる型引数群
     */
    private List<UnresolvedTypeInfo> availableTypeArugments;

    /**
     * ワイルドカード情報の構築に入った深さをカウント
     */
    private int inWildCardCount;

    /**
     * 構築した基本型を記録する
     */
    private UnresolvedTypeInfo primitiveType;

    /**
     * 構築したvoid型を記録する
     */
    private UnresolvedTypeInfo voidType;

    /**
     * 識別子情報を構築するビルダー
     */
    private final IdentifierBuilder identifierBuilder = new IdentifierBuilder();

    /**
     * 構築情報管理者
     */
    private final BuildDataManager buildDataManager;

    /**
     * 型引数定義部に関する状態管理をするステートマネージャ
     */
    private final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();
}
