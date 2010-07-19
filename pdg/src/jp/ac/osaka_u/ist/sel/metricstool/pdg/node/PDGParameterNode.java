package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGParameterNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;


/**
 * ������\��PDG�m�[�h
 * 
 * @author higo
 *
 */
public class PDGParameterNode extends PDGNormalNode<CFGParameterNode> {

    /**
     * �����ŗ^����ꂽparameter����PDGParameterNode���쐬���ĕԂ�
     * 
     * @param parameter
     * @return
     */
    public static PDGParameterNode getInstance(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        final CFGParameterNode cfgNode = CFGParameterNode.getInstance(parameter);
        return new PDGParameterNode(cfgNode);
    }

    /**
     * cfg�m�[�h��^���ď������D
     * ���̃N���X���� getInstance���\�b�h����̂݌Ăяo�����
     * 
     * @param parameter
     */
    private PDGParameterNode(final CFGParameterNode node) {
        super(node);
    }
}
