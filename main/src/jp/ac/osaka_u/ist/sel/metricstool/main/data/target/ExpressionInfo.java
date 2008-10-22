package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * ����\���C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface ExpressionInfo extends ConditionInfo {

    /**
     * �����ł̕ϐ��̎g�p��Ԃ�
     * 
     * @return �����ł̕ϐ��̎g�p
     */
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();
    
    public TypeInfo getType();
}
