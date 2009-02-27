package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * CFG�̐���m�[�h��\���N���X
 * @author t-miyake
 *
 */
public class CFGControlNode extends CFGNode<ConditionalBlockInfo> {

    /**
     * ��������m�[�h�ɑΉ����鐧�䕶��^���ď�����
     * @param controlStatement ��������m�[�h�ɑΉ����鐧�䕶
     */
    CFGControlNode(final ConditionalBlockInfo controlStatement) {
        super(controlStatement);
        this.text = controlStatement.getConditionalClause().getCondition().getText() + "<"
                + controlStatement.getConditionalClause().getCondition().getFromLine() + ">";
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this.getCore()
                .getConditionalClause().getCondition().getVariableUsages()));
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getUsedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getConditionalClause().getCondition().getVariableUsages()));
    }
}
