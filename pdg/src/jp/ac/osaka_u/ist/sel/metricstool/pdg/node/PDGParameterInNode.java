package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import sdl.ist.osaka_u.newmasu.cfg.node.CFGParameterInNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;


/**
 * ���\��PDG�m�[�h
 * 
 * @author higo
 * 
 */
public class PDGParameterInNode extends PDGDataNode<CFGParameterInNode> implements PDGDataInNode {

    /**
     * ��ŗ^����ꂽparameter����PDGParameterNode���쐬���ĕԂ�
     * 
     * @param parameter
     * @return
     */
    public static PDGParameterInNode getInstance(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        final CFGParameterInNode cfgNode = CFGParameterInNode.getInstance(parameter);
        return new PDGParameterInNode(cfgNode);
    }

    /**
     * cfg�m�[�h��^���ď���D ���̃N���X���� getInstance���\�b�h����̂݌Ăяo�����
     * 
     * @param parameter
     */
    private PDGParameterInNode(final CFGParameterInNode node) {
        super(node);
    }
}
