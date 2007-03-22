package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph_builder;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.SimpleGraph;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.ClassVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.EDGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Edge;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.FieldVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Graph;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.MethodVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Vertex;


/**
 * �O���t���\�z����.
 * 
 * ���_���o�^����Ă��Ȃ��ӂ̒ǉ��͂ł��Ȃ�.
 * 
 * @author rniitani
 */
public class SimpleGraphBuilder {
    /**
     * �`Info �N���X�̃I�u�W�F�N�g���炻��ɑΉ����� {@link Vertex} �ւ̃}�b�s���O.
     */
    final Map<Object, Vertex> info2vertex;

    /**
     * ���������O���t.
     */
    final SimpleGraph graph;

    /**
     * �f�t�H���g�R���X�g���N�^.
     */
    public SimpleGraphBuilder() {
        this.info2vertex = new HashMap<Object, Vertex>();
        this.graph = new SimpleGraph();
    }

    /**
     * �\�z���ꂽ�O���t.
     */
    public Graph getResult() {
        return this.graph;
    }

    /**
     * �O���t�ɃN���X��ǉ�����.
     * 
     * @param classInfo �N���X�̏��.
     */
    public void addClass(TargetClassInfo classInfo) {
        if (info2vertex.containsKey(classInfo)) {
            // ���ɓo�^�ς�
            return;
        }
        Vertex classVertex = new ClassVertex(graph.nextId(), extractClassVertexLabel(classInfo),
                classInfo.getClassName(), classInfo, classInfo);
        addVertex(classInfo, classVertex);
    }

    /**
     * �N���X�Ƀ��\�b�h��ǉ�����.
     * 
     * @param classInfo �N���X�̏��.
     * @param methodInfo ���\�b�h�̏��.
     */
    public void addMethod(TargetClassInfo classInfo, TargetMethodInfo methodInfo) {
        assert (info2vertex.containsKey(classInfo));

        ClassVertex classVertex = (ClassVertex) info2vertex.get(classInfo);
        MethodVertex methodVertex = new MethodVertex(graph.nextId(), classVertex,
                extractMethodVertexLabel(methodInfo), methodInfo.getMethodName(), methodInfo,
                methodInfo);
        classVertex.addMethod(methodVertex);
        addVertex(methodInfo, methodVertex);
    }

    /**
     * �N���X�Ƀt�B�[���h��ǉ�����.
     * 
     * @param classInfo �N���X�̏��.
     * @param fieldInfo �t�B�[���h�̏��.
     */
    public void addField(TargetClassInfo classInfo, TargetFieldInfo fieldInfo) {
        assert (info2vertex.containsKey(classInfo));

        ClassVertex classVertex = (ClassVertex) info2vertex.get(classInfo);
        FieldVertex fieldVertex = new FieldVertex(graph.nextId(), classVertex,
                extractFieldVertexLabel(fieldInfo), fieldInfo.getName(), fieldInfo, fieldInfo);
        classVertex.addField(fieldVertex);
        addVertex(fieldInfo, fieldVertex);
    }

    private void addVertex(Object info, Vertex v) {
        graph.addVertex(v);
        info2vertex.put(info, v);
    }

    /**
     * �Ăяo���֌W��ǉ�����.
     * 
     * ���_���O���t�ɑ��݂���Ƃ͌���Ȃ�.
     * ���݂��Ȃ������ꍇ�ӂ𐶐����Ȃ�.
     * 
     * @param caller �Ăяo����.
     * @param callee �Ăяo����.
     */
    public void addCallRelation(TargetMethodInfo caller, MethodInfo callee) {
        addRelation(EDGE_TYPE.CALL, caller, callee);
    }

    /**
     * �e�q�N���X�֌W��ǉ�����.
     * 
     * ���_���O���t�ɑ��݂���Ƃ͌���Ȃ�.
     * ���݂��Ȃ������ꍇ�ӂ𐶐����Ȃ�.
     * 
     * @param superclass �e�N���X.
     * @param subclass �q�N���X.
     */
    public void addSuperclassRelation(TargetClassInfo superclass, ClassInfo subclass) {
        addRelation(EDGE_TYPE.SUPERCLASS, subclass, superclass);
    }

    /**
     * �����N���X�֌W��ǉ�����.
     * 
     * ���_���O���t�ɑ��݂���Ƃ͌���Ȃ�.
     * ���݂��Ȃ������ꍇ�ӂ𐶐����Ȃ�.
     * 
     * @param outer �O���N���X.
     * @param inner �����N���X.
     */
    public void addInnerclassRelation(TargetClassInfo outer, ClassInfo inner) {
        addRelation(EDGE_TYPE.SUPERCLASS, outer, inner);
    }

    /**
     * �֌W��ǉ�����.
     * addXXXRelation �̎���.
     * 
     * ���_���O���t�ɑ��݂���Ƃ͌���Ȃ�.
     * ���݂��Ȃ������ꍇ�ӂ𐶐����Ȃ�.
     * 
     * @param type
     * @param from {@link #info2vertex} �ɓo�^����Ă���I�u�W�F�N�g.
     * @param to {@link #info2vertex} �ɓo�^����Ă���I�u�W�F�N�g.
     */
    private void addRelation(EDGE_TYPE type, Object from, Object to) {
        if (!info2vertex.containsKey(from) || !info2vertex.containsKey(to))
            return;

        Vertex fromVertex = info2vertex.get(from);
        Vertex toVertex = info2vertex.get(to);
        Edge edge = new Edge(graph.nextId(), type, fromVertex, toVertex);

        graph.addEdge(edge);
    }

    private String extractClassVertexLabel(TargetClassInfo classInfo) {
        return classInfo.getClassName();
    }

    private String extractMethodVertexLabel(TargetMethodInfo methodInfo) {
        StringBuffer buffer = new StringBuffer();
        // �Ԓl
        buffer.append(methodInfo.getReturnType().getTypeName());
        buffer.append(' ');
        // ���\�b�h��
        buffer.append(methodInfo.getMethodName());
        buffer.append('(');
        // �������X�g
        // �Ƃ肠�����^����
        // �ȉ� join ���̏��������Ă��邾��
        int paramSize = methodInfo.getParameters().size();
        int paramCount = 0;
        for (ParameterInfo param : methodInfo.getParameters()) {
            buffer.append(param.getType().getTypeName());
            paramCount++;
            if (paramCount < paramSize)
                buffer.append(", ");
        }
        buffer.append(')');
        return buffer.toString();
    }

    private String extractFieldVertexLabel(TargetFieldInfo fieldInfo) {
        return fieldInfo.getType().getTypeName() + " " + fieldInfo.getName();
    }
}
