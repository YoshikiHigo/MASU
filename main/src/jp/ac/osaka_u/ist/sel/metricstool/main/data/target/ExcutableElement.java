package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


public interface ExcutableElement extends Position, Comparable<ExcutableElement> {

    /**
     * �����ł̕ϐ��̎g�p��Ԃ�
     * 
     * @return �����ł̕ϐ��̎g�p
     */
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();
}
