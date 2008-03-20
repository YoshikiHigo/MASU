package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


/**
 * @author higo
 *
 */
public interface StatementInfo extends Position, Comparable<StatementInfo> {

    /**
     * �����ł̕ϐ��̎g�p��Ԃ�
     * 
     * @return �����ł̕ϐ��̎g�p
     */
    public Set<VariableUsageInfo<?>> getVariableUsages();
}
