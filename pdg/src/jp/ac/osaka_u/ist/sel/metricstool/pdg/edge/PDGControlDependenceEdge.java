package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


/**
 * ����ˑ��ӂ�\���N���X
 * @author t-miyake, higo
 *
 */
public class PDGControlDependenceEdge extends PDGEdge {

    /**
     * �G�b�W�̏W������C����ˑ���\���G�b�W�݂̂𒊏o���C����Set��Ԃ�
     * 
     * @param edges
     * @return
     */
    public static SortedSet<PDGControlDependenceEdge> getControlDependenceEdge(
            final Set<PDGEdge> edges) {
        final SortedSet<PDGControlDependenceEdge> controlDependenceEdges = new TreeSet<PDGControlDependenceEdge>();
        for (final PDGEdge edge : edges) {
            if (edge instanceof PDGControlDependenceEdge) {
                controlDependenceEdges.add((PDGControlDependenceEdge) edge);
            }
        }
        return Collections.unmodifiableSortedSet(controlDependenceEdges);
    }

    public PDGControlDependenceEdge(final PDGControlNode fromNode, final PDGNode<?> toNode,
            final boolean trueDependence) {
        super(fromNode, toNode);

        this.trueDependence = trueDependence;

    }

    public boolean isTrueDependence() {
        return this.trueDependence;
    }

    public boolean isFalseDependence() {
        return !this.trueDependence;
    }

    @Override
    public String getDependenceString() {
        return this.trueDependence ? "true" : "false";
    }

    @Override
    public String getDependenceTypeString() {
        return "Control Dependency";
    }

    private final boolean trueDependence;
}
