package sdl.ist.osaka_u.newmasu.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;


/**
 * break����\��CFG�m�[�h
 * 
 * @author higo
 *
 */
public class CFGBreakStatementNode extends CFGJumpStatementNode {

    CFGBreakStatementNode(final BreakStatementInfo breakStatement) {
        super(breakStatement);
    }
}
