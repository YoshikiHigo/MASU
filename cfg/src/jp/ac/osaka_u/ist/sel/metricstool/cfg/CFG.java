package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


public abstract class CFG {

    protected final ICFGNodeFactory nodeFactory;

    protected CFGNode<? extends StatementInfo> enterNode;

    protected final Set<CFGNode<? extends StatementInfo>> exitNodes;

    protected CFG(final ICFGNodeFactory nodeFactory) {
        if (null == nodeFactory) {
            throw new IllegalArgumentException();
        }

        this.nodeFactory = nodeFactory;

        this.exitNodes = new HashSet<CFGNode<? extends StatementInfo>>();

    }

    protected void connectNodes(final Set<CFGNode<? extends StatementInfo>> fromNodes,
            CFGNode<? extends StatementInfo> toNode) {
        for (final CFGNode<? extends StatementInfo> fromNode : fromNodes) {
            fromNode.addForwardNode(toNode);
        }
    }

    /**
     * CFG�̓�����m�[�h��Ԃ�
     * @return CFG�̓�����m�[�h
     */
    public final CFGNode<? extends StatementInfo> getEnterNode() {
        return this.enterNode;
    }

    /**
     * CFG�̏o���m�[�h��Ԃ�
     * @return CFG�̏o���m�[�h
     */
    public final Set<CFGNode<? extends StatementInfo>> getExitNodes() {
        return Collections.unmodifiableSet(this.exitNodes);
    }

    /**
     * CFG�̑S�m�[�h��Ԃ�
     * 
     * @return CFG�̑S�m�[�h
     */
    public final Set<CFGNode<? extends StatementInfo>> getAllNodes() {
        return null != this.enterNode ? this.getReachableNodes(this.enterNode)
                : new HashSet<CFGNode<? extends StatementInfo>>();
    }

    /**
     * �����ŗ^����ꂽ�m�[�h���瓞�B�\�ȃm�[�h��Ԃ�
     * 
     * @param startNode�@�J�n�m�[�h
     * @return �����ŗ^����ꂽ�m�[�h���瓞�B�\�ȃm�[�h
     */
    public final Set<CFGNode<? extends StatementInfo>> getReachableNodes(
            final CFGNode<? extends StatementInfo> startNode) {

        if (null == startNode) {
            throw new IllegalArgumentException();
        }

        final Set<CFGNode<? extends StatementInfo>> nodes = new HashSet<CFGNode<? extends StatementInfo>>();
        this.getReachableNodes(startNode, nodes);

        return Collections.unmodifiableSet(nodes);
    }

    private final void getReachableNodes(final CFGNode<? extends StatementInfo> startNode,
            final Set<CFGNode<? extends StatementInfo>> nodes) {

        if ((null == startNode) || (null == nodes)) {
            throw new IllegalArgumentException();
        }

        nodes.add(startNode);
        for (final CFGNode<? extends StatementInfo> node : startNode.getForwardNodes()) {
            this.getReachableNodes(node, nodes);
        }
    }

    public boolean isEmpty() {
        return null == this.enterNode;
    }

    public CFGNode<? extends StatementInfo> getCFGNode(final StatementInfo statement) {
        return this.nodeFactory.getNode(statement);
    }

}
