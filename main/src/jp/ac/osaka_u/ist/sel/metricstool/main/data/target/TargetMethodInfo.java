package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 対象メソッドの情報を保有するクラス． 以下の情報を持つ．
 * <ul>
 * <li>メソッド名</li>
 * <li>修飾子</li>
 * <li>返り値の型</li>
 * <li>引数のリスト</li>
 * <li>行数</li>
 * <li>コントロールグラフ（しばらくは未実装）</li>
 * <li>ローカル変数</li>
 * <li>所属しているクラス</li>
 * <li>呼び出しているメソッド</li>
 * <li>呼び出されているメソッド</li>
 * <li>オーバーライドしているメソッド</li>
 * <li>オーバーライドされているメソッド</li>
 * <li>参照しているフィールド</li>
 * <li>代入しているフィールド</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public final class TargetMethodInfo extends MethodInfo implements Member {

    /**
     * メソッドオブジェクトを初期化する．
     * 
     * @param modifiers 修飾子
     * @param name メソッド名
     * @param ownerClass 所有しているクラス
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetMethodInfo(final Set<ModifierInfo> modifiers, final String name,
            final ClassInfo ownerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, name, ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.instance = instance;
    }

    /**
     * このメソッドが参照しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが参照しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getReferencees() {

        final SortedSet<FieldInfo> referencees = new TreeSet<FieldInfo>();
        for (final FieldUsageInfo fieldUsage : this.getFieldUsages()) {
            if (fieldUsage.isReference()) {
                referencees.add(fieldUsage.getUsedVariable());
            }
        }

        return Collections.unmodifiableSortedSet(referencees);
    }

    /**
     * このメソッドが代入しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが代入しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getAssignmentees() {
        final SortedSet<FieldInfo> assignmentees = new TreeSet<FieldInfo>();
        for (final FieldUsageInfo fieldUsage : this.getFieldUsages()) {
            if (fieldUsage.isAssignment()) {
                assignmentees.add(fieldUsage.getUsedVariable());
            }
        }

        return Collections.unmodifiableSortedSet(assignmentees);
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private final boolean instance;
}
