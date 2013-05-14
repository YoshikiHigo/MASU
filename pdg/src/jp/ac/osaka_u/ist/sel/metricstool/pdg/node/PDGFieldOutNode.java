package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import sdl.ist.osaka_u.newmasu.cfg.node.CFGFieldOutNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;


public class PDGFieldOutNode extends PDGDataNode<CFGFieldOutNode> implements PDGDataOutNode {

    /**
     * ��ŗ^����ꂽfield����PDGFieldOutNode���쐬���ĕԂ�
     * 
     * @param field
     * @param unit
     * @return
     */
    public static PDGFieldOutNode getInstance(final FieldInfo field, final CallableUnitInfo unit) {

        if (null == field || null == unit) {
            throw new IllegalArgumentException();
        }

        final CFGFieldOutNode cfgNode = CFGFieldOutNode.getInstance(field, unit);
        return new PDGFieldOutNode(cfgNode);
    }

    private PDGFieldOutNode(final CFGFieldOutNode node) {
        super(node);
    }
}
