package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * 
 * @author higo
 *
 */
public class CFGExpressionNode extends CFGNormalNode<ConditionInfo> {

    /**
     * ��������m�[�h�ɑΉ����镶��^���ď�����
     * @param statement ��������m�[�h�ɑΉ����镶
     */
    public CFGExpressionNode(final ConditionInfo expression) {
        super(expression);
    }
}
