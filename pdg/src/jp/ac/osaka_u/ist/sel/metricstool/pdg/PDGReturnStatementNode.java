package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;


/**
 * Return文を表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGReturnStatementNode extends PDGStatementNode {

    /**
     * ノードを生成するReturn文を与えて初期化
     * 
     * @param returnStatement
     */
    public PDGReturnStatementNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }
}
