package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;


/**
 * Return����\��PDG�m�[�h
 * 
 * @author higo
 *
 */
public class PDGReturnStatementNode extends PDGStatementNode {

    /**
     * �m�[�h�𐶐�����Return����^���ď�����
     * 
     * @param returnStatement
     */
    public PDGReturnStatementNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }
}
