package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.io.Serializable;
import java.util.Set;


public interface ExecutableElementInfo extends Position, Comparable<ExecutableElementInfo>, Serializable{

    /**
     * �����ł̕ϐ��̎g�p��Ԃ�
     * 
     * @return �����ł̕ϐ��̎g�p
     */
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();
}
