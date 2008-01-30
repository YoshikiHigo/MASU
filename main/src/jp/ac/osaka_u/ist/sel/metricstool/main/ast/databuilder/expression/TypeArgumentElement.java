package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

/**
 * �^������\�����v�f
 * 
 * @author kou-tngt
 *
 */
public class TypeArgumentElement implements ExpressionElement{

    /**
     * ����type���^�����Ƃ��ĕ\���C���X�^���X���쐬����D
     * 
     * @param type
     */
    public TypeArgumentElement(UnresolvedTypeInfo type){
        this.type = type;
    }
    
    /**
     * �^������Ԃ��D
     * @return ���̃C���X�^���X���\���^����
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement#getType()
     */
    public UnresolvedTypeInfo getType() {
        return type;
    }
    
    public UnresolvedEntityUsageInfo getUsage() {
        return null;
    }
    
    /**
     * ���̃C���X�^���X���\���^����
     */
    private final UnresolvedTypeInfo type;
}
