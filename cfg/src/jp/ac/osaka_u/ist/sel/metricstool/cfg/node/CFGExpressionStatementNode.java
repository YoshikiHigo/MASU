package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;


/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionStatementNode extends CFGNormalNode<ExpressionStatementInfo> {

    /**
     * 生成するノードに対応する文を与えて初期化
     * 
     * @param statement
     *            生成するノードに対応する文
     */
    CFGExpressionStatementNode(final ExpressionStatementInfo statement) {
        super(statement);
    }
}
