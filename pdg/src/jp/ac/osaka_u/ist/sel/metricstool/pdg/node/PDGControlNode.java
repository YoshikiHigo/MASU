package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import sdl.ist.osaka_u.newmasu.cfg.node.CFGControlNode;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;


/**
 * PDG��Ő���m�[�h��\���N���X
 * 
 * @author t-miyake
 *
 */
public class PDGControlNode extends PDGNode<CFGControlNode> {

    /**
     * �m�[�h�̏W������C����m�[�h�݂̂𒊏o���C����SortedSet��Ԃ�
     * 
     * @param nodes
     * @return
     */
    public static SortedSet<PDGControlNode> getControlNodes(final Set<? extends PDGNode<?>> nodes) {
        final SortedSet<PDGControlNode> controlNodes = new TreeSet<PDGControlNode>();
        for (final PDGNode<?> node : nodes) {
            if (node instanceof PDGControlNode) {
                controlNodes.add((PDGControlNode) node);
            }
        }
        return Collections.unmodifiableSortedSet(controlNodes);
    }

    /**
     * CFGControlNode��^���āC����
     * 
     * @param controlNode
     */
    protected PDGControlNode(final CFGControlNode controlNode) {
        super(controlNode);
    }

    /**
     * ���̐���m�[�h�ɐ��䂳���m�[�h��ǉ�
     * @param controlledNode ���䂳���m�[�h
     */
    public void addControlDependingNode(final PDGNode<?> controlledNode,
            final boolean trueDependence) {
        if (null == controlledNode) {
            throw new IllegalArgumentException();
        }

        final PDGControlDependenceEdge controlFlow = new PDGControlDependenceEdge(this,
                controlledNode, trueDependence);
        this.addForwardEdge(controlFlow);
        controlledNode.addBackwardEdge(controlFlow);
    }

}
