package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;


/**
 * return����\���m�[�h
 * 
 * @author higo
 *
 */
public class CFGReturnNode extends CFGStatementNode {

    /**
     * ��������m�[�h�ɑΉ�����return����^���ď�����
     * 
     * @param returnStatement
     */
    public CFGReturnNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }
}
