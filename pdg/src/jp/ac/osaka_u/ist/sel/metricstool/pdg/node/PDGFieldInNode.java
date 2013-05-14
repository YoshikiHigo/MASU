package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import sdl.ist.osaka_u.newmasu.cfg.node.CFGFieldInNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;


public class PDGFieldInNode extends PDGDataNode<CFGFieldInNode> implements PDGDataInNode {

    /**
     * ��ŗ^����ꂽfield����PDGFieldInNode���쐬���ĕԂ�
     * 
     * @param field
     * @param unit
     * @return
     */
    public static PDGFieldInNode getInstance(final FieldInfo field, final CallableUnitInfo unit) {

        if (null == field || null == unit) {
            throw new IllegalArgumentException();
        }

        final CFGFieldInNode cfgNode = CFGFieldInNode.getInstance(field, unit);
        return new PDGFieldInNode(cfgNode);
    }

    private PDGFieldInNode(final CFGFieldInNode node) {
        super(node);
    }
}
