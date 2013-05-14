package sdl.ist.osaka_u.newmasu.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public abstract class CFGNormalNode<T extends ExecutableElementInfo> extends CFGNode<T> {

    CFGNormalNode(final T core) {
        super(core);
    }
}
