package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

/**
 * 型引数を表す式要素
 * 
 * @author kou-tngt
 *
 */
public class TypeArgumentElement implements ExpressionElement{

    /**
     * 引数typeを型引数として表すインスタンスを作成する．
     * 
     * @param type
     */
    public TypeArgumentElement(UnresolvedTypeInfo type){
        this.type = type;
    }
    
    /**
     * 型引数を返す．
     * @return このインスタンスが表す型引数
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement#getType()
     */
    public UnresolvedTypeInfo getType() {
        return type;
    }
    
    /**
     * このインスタンスが表す型引数
     */
    private final UnresolvedTypeInfo type;
}
