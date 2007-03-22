package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Vertex} �� {@link Edge} ��ID�̏d�����Ǘ����Ă��邾���̒P���ȃO���t.
 * 
 * ���ꂼ��ŗL��ID�������Ă���.
 * 
 * @author rniitani
 */
public class SimpleGraph implements Graph {
    private Set<Edge> edges = new HashSet<Edge>();
    private Set<Vertex> vertices = new HashSet<Vertex>();
    private Set<Integer> registeredIds = new HashSet<Integer>();
    private int nextId = 0;
    
    /**
     * �S�Ă� {@link Edge} �̏W����Ԃ�.
     */
    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    /**
     * �S�Ă� {@link Vertex} �̏W����Ԃ�.
     */
    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }
    
//    /**
//     * {@link Edge} �ɑΉ�����ID��Ԃ�.
//     */
//    public int getEdgeId(Edge e) {
//        return edges.get(e);
//    }
//
//    /**
//     * {@link Vertex} �ɑΉ�����ID��Ԃ�.
//     */
//    public int getVertexId(Vertex v) {
//        return vertices.get(v);
//    }

     public int nextId() {
        return nextId++;
    }

     /**
     * {@link Edge} �̒ǉ�.
     * @param edge
     */
    public void addEdge(Edge edge) {
        assert(vertices.contains(edge.getFrom()));
        assert(vertices.contains(edge.getTo()));
        assert(!registeredIds.contains(edge.getId()));
        
        edges.add(edge);
    }

    /**
     * {@link Vertex} �̒ǉ�.
     * @param vertex
     */
    public void addVertex(Vertex vertex) {
        assert(!registeredIds.contains(vertex.getId()));
        vertices.add(vertex);
    }
    
}
