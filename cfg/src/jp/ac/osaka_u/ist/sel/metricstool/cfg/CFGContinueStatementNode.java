package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;


/**
 * continue����\���m�[�h�̂��߂̃N���X
 * 
 * @author higo
 *
 */
public class CFGContinueStatementNode extends CFGJumpStatementNode {

    /**
     * �m�[�h�𐶐�����continue����^���ď�����
     * 
     * @param continueStatement
     */
    public CFGContinueStatementNode(final ContinueStatementInfo continueStatement) {
        super(continueStatement);
    }
}
