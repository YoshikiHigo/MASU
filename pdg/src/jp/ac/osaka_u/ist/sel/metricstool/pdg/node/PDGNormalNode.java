package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import sdl.ist.osaka_u.newmasu.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * ����m�[�h�ȊO�̃m�[�h��\��
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class PDGNormalNode<T extends CFGNormalNode<? extends ExecutableElementInfo>>
        extends PDGNode<T> {

    protected PDGNormalNode(final T node) {
        super(node);
    }
}
