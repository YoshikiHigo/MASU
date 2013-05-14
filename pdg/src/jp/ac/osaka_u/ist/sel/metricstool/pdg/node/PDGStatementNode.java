package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import sdl.ist.osaka_u.newmasu.cfg.node.CFGStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * PDG��ŒP����\���m�[�h
 * 
 * @author t-miyake, higo
 *
 */
public class PDGStatementNode<T extends CFGStatementNode<? extends ExecutableElementInfo>> extends
        PDGNormalNode<T> {

    /**
     * @param statement
     */
    protected PDGStatementNode(final T node) {
        super(node);
    }
}
