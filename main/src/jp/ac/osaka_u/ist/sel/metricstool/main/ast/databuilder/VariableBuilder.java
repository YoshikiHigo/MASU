package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;


/**
 * 変数データを構築する抽象クラス.
 * 
 * このクラスのサブクラスは，コンストラクタに変数宣言部の状態管理を扱う {@link VariableDefinitionStateManager} を与え，
 * {@link #buildVariable(String[], UnresolvedTypeInfo, ModifierInfo[])}メソッドを実装することで，
 * 任意の変数データに対応させることができる．
 * 
 * @author kou-tngt
 *
 * @param <T> 構築する変数の型
 */
public abstract class VariableBuilder<T extends UnresolvedVariableInfo> extends
        CompoundDataBuilder<T> {

    /**
     * 引数で与えられた構築データ管理者，変数宣言に関する状態管理者と，デフォルトの修飾子情報ビルダー，型情報ビルダー，名前情報ビルダーを用いて初期化する．
     * @param buildDataManager 構築データ管理者
     * @param stateManager 変数宣言に関する状態管理者
     */
    public VariableBuilder(final BuildDataManager buildDataManager,
            final VariableDefinitionStateManager stateManager) {
        this(stateManager, new ModifiersBuilder(), new TypeBuilder(buildDataManager),
                new NameBuilder());
    }

    /**
     * 引数で与えられた変数宣言に関する状態管理者，修飾子情報ビルダー，型情報ビルダー，名前情報ビルダーを用いて初期化する．
     * @param variableStateManager 変数宣言に関する状態管理者
     * @param modifiersBuilder　修飾子情報ビルダー
     * @param typeBuilder　型情報ビルダー
     * @param nameBuilder　名前情報ビルダー
     */
    public VariableBuilder(final VariableDefinitionStateManager variableStateManager,
            final ModifiersBuilder modifiersBuilder, final TypeBuilder typeBuilder, final NameBuilder nameBuilder) {

        if (null == variableStateManager) {
            throw new NullPointerException("stateManager is null.");
        }

        if (null == typeBuilder) {
            throw new NullPointerException("typeBuilder is null.");
        }

        if (null == nameBuilder) {
            throw new NullPointerException("nameBuilder is null.");
        }

        //nullチェック終了

        //状態通知を受け取りたいものを登録
        this.variableStateManager = variableStateManager;
        this.addStateManager(variableStateManager);
        this.addStateManager(this.typeStateManager);
        this.addStateManager(new ModifiersDefinitionStateManager());
        this.addStateManager(new NameStateManager());

        //内部ビルダーを登録
        this.addInnerBuilder(modifiersBuilder);
        this.addInnerBuilder(typeBuilder);
        this.addInnerBuilder(nameBuilder);

        this.modifiersBuilder = modifiersBuilder;
        this.typeBuilder = typeBuilder;
        this.nameBuilder = nameBuilder;
    }

    /**
     * 状態変化イベントの通知を受け取るメソッド．
     * @param event 状態変化イベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChangend(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType eventType = event.getType();
        if (eventType.equals(VariableDefinitionStateManager.VARIABLE_STATE.EXIT_VARIABLE_DEF)) {
            //変数宣言部から出たので，変数情報を登録する
            //スコープの関係上，宣言が終わらないと登録してはいけない
            final AstVisitEvent trigger = event.getTrigger();
            this.endVariableBuild(trigger.getStartLine(), trigger.getStartColumn(),
                    trigger.getEndLine(), trigger.getEndColumn());
        } else if (this.variableStateManager.isInDefinition()) {
            if (eventType
                    .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
                //修飾子情報の構築開始
                if (null != this.modifiersBuilder) {
                    this.modifiersBuilder.activate();
                }
            } else if (eventType
                    .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)) {
                //修飾子情報の構築終了
                //すぐに構築したデータをスタックへ（変数宣言は初期化部以降で入れ子になることがあるため，構築したデータはさっさと取得しておく）
                if (null != this.modifiersBuilder) {
                    this.modifiersBuilder.deactivate();
                    this.builtModifiersStack.push(this.modifiersBuilder.popLastBuiltData());
                }
            } else if (eventType.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
                //型情報構築開始
                this.typeBuilder.activate();
            } else if (eventType.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) {
                if (!this.typeStateManager.isEntered()) {
                    //型情報はそれ単体ですら入れ子になっているので，
                    //とりあえず一番外側の型定義が終わっていたら型情報構築終了
                    //構築した型情報をすぐに取得してスタックへ
                    this.typeBuilder.deactivate();
                    this.builtTypeStack.push(this.typeBuilder.popLastBuiltData());
                }
            } else if (eventType.equals(NameStateManager.NAME_STATE.ENTER_NAME)) {
                //名前構築開始
                this.nameBuilder.activate();
            } else if (eventType.equals(NameStateManager.NAME_STATE.EXIT_NAME)) {
                //名前構築終了
                //同じくさっさと取得してすぐにスタックへ
                this.nameBuilder.deactivate();
                this.builtNameStack.push(this.nameBuilder.popLastBuiltData());
            }
        }
    }

    /**
     * 変数データを構築する抽象メソッド．
     * 構築した変数データを構築データ管理者に渡したい場合もこのメソッドで行う．
     * 
     * @param name　変数の名前
     * @param type　変数の型
     * @param modifiers　変数の修飾子
     * @return　構築した変数情報
     */
    protected abstract T buildVariable(String[] name, UnresolvedTypeInfo type,
            ModifierInfo[] modifiers);

    /**
     * 変数定義が終了したときに呼び出され， {@link #buildVariable(String[], UnresolvedTypeInfo, ModifierInfo[])}
     * メソッドを呼び出して変数データを構築する．
     * 
     * このメソッドをオーバーライドすることで変数定義の終了時の動作を変更することができる．
     * 
     * @param startLine 変数定義部の開始行
     * @param startColumn　変数定義部の開始列
     * @param endLine　変数定義部の終了行
     * @param endColumn　変数定義部の終了列
     */
    protected void endVariableBuild(final int startLine, final int startColumn, final int endLine, final int endColumn) {
        final T variable = this.buildVariable(this.getName(), this.getType(), this.getModifiers());
        variable.setFromLine(startLine);
        variable.setFromColumn(startColumn);
        variable.setToLine(endLine);
        variable.setToColumn(endColumn);
        this.registBuiltData(variable);
    }

    /**
     * 最も最後に構築した変数名を返す．
     * @return　最も最後に構築した変数名
     */
    private String[] getName() {
        return this.builtNameStack.pop();
    }

    /**
     * 最も最後に構築した変数に関する修飾子の配列を返す．
     * @return　最も最後に構築した変数に関する修飾子の配列
     */
    private ModifierInfo[] getModifiers() {
        if (null != this.builtModifiersStack) {
            return this.builtModifiersStack.pop();
        } else {
            return EMPTY_MODIFIERS;
        }
    }

    /**
     * 最も最後に構築した変数の型を返す．
     * @return　最も最後に構築した変数の型
     */
    private UnresolvedTypeInfo getType() {
        return this.builtTypeStack.pop();
    }

    /**
     * 変数定義部に関する状態を管理するステートマネージャ
     */
    private final VariableDefinitionStateManager variableStateManager;

    /**
     * 型パラメータ記述部に関する状態を管理するステートマネージャ
     */
    private final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();

    /**
     * 修飾子情報を構築するビルダー
     */
    private final ModifiersBuilder modifiersBuilder;

    /**
     * 型情報を構築するビルダー
     */
    private final TypeBuilder typeBuilder;

    /**
     * 名前情報を構築するビルダー
     */
    private final NameBuilder nameBuilder;

    /**
     * 構築した型情報を構築後すぐに格納しておくスタック
     */
    private final Stack<UnresolvedTypeInfo> builtTypeStack = new Stack<UnresolvedTypeInfo>();

    /**
     * 構築した修飾子情報を構築後すぐに格納しておくスタック
     */
    private final Stack<ModifierInfo[]> builtModifiersStack = new Stack<ModifierInfo[]>();

    /**
     * 構築した名前情報を構築後すぐに格納しておくスタック
     */
    private final Stack<String[]> builtNameStack = new Stack<String[]>();

    /**
     * 空の修飾子配列を表す定数
     */
    private static final ModifierInfo[] EMPTY_MODIFIERS = new ModifierInfo[0];
}
