package sdl.ist.osaka_u.newmasu.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;


/**
 * continue����\��CFG�m�[�h
 * 
 * @author higo
 * 
 */
public class CFGContinueStatementNode extends CFGJumpStatementNode {

    /**
     * �m�[�h�𐶐�����continue����^���ď���
     * 
     * @param continueStatement
     */
    CFGContinueStatementNode(final ContinueStatementInfo continueStatement) {
        super(continueStatement);
    }
}
