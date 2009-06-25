package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * �f�[�^�ˑ��ӂ�\���N���X
 * 
 * @author higo
 *
 */
public class PDGDataDependenceEdge extends PDGEdge {

    /**
     * �G�b�W�̏W������C�f�[�^�ˑ���\���G�b�W�݂̂𒊏o���C����Set��Ԃ�
     * 
     * @param edges
     * @return
     */
    public static Set<PDGDataDependenceEdge> getDataDependenceEdge(final Set<PDGEdge> edges) {
        final Set<PDGDataDependenceEdge> dataDependenceEdges = new HashSet<PDGDataDependenceEdge>();
        for (final PDGEdge edge : edges) {
            if (edge instanceof PDGDataDependenceEdge) {
                dataDependenceEdges.add((PDGDataDependenceEdge) edge);
            }
        }
        return Collections.unmodifiableSet(dataDependenceEdges);
    }

    public PDGDataDependenceEdge(final PDGNode<?> fromNode, final PDGNode<?> toNode,
            final VariableInfo<?> data) {
        super(fromNode, toNode);

        this.data = data;
    }

    public VariableInfo<?> getVariable() {
        return this.data;
    }

    @Override
    public String getDependenceString() {
        return this.data.getName();
    }

    @Override
    public String getDependenceTypeString() {
        return "Data Dependency";
    }

    private final VariableInfo<?> data;
}
