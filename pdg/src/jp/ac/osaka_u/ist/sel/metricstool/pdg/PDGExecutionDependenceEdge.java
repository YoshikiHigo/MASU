package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * ���s�ˑ��ӂ�\���N���X
 * 
 * @author higo
 *
 */
public class PDGExecutionDependenceEdge extends PDGEdge {

    /**
     * �G�b�W�̏W������C���s�ˑ���\���G�b�W�݂̂𒊏o���C����Set��Ԃ�
     * 
     * @param edges
     * @return
     */
    public static Set<PDGExecutionDependenceEdge> getExecutionDependenceEdge(
            final Set<PDGEdge> edges) {
        final Set<PDGExecutionDependenceEdge> executionDependenceEdges = new HashSet<PDGExecutionDependenceEdge>();
        for (final PDGEdge edge : edges) {
            if (edge instanceof PDGExecutionDependenceEdge) {
                executionDependenceEdges.add((PDGExecutionDependenceEdge) edge);
            }
        }
        return Collections.unmodifiableSet(executionDependenceEdges);
    }

    public PDGExecutionDependenceEdge(final PDGNode<?> fromNode, final PDGNode<?> toNode) {
        super(fromNode, toNode);
    }

    @Override
    public String getDependenceString() {
        return "";
    }

    @Override
    public String getDependenceTypeString() {
        return "Execution Dependency";
    }
}
