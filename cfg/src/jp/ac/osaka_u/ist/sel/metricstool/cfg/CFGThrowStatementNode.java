package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;


/**
 * throw����\���m�[�h
 * 
 * @author higo
 *
 */
public class CFGThrowStatementNode extends CFGStatementNode {

    /**
     * �m�[�h�𐶐�����throw����^���ď�����
     * 
     * @param throwStatement
     */
    public CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
        super(throwStatement);
    }
}
