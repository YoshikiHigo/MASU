package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;


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
}
