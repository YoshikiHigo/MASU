package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;


/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionStatementNode extends CFGNormalNode<ExpressionStatementInfo> {

    /**
     * ��������m�[�h�ɑΉ����镶��^���ď�����
     * 
     * @param statement
     *            ��������m�[�h�ɑΉ����镶
     */
    CFGExpressionStatementNode(final ExpressionStatementInfo statement) {
        super(statement);
    }
}
