package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


public class ClassVertex extends AbstractVertex {
    final private SortedSet<MethodVertex> methods;

    final private SortedSet<FieldVertex> fields;

    /**
     * �R���X�g���N�^.
     * 
     * @param classInfo
     *      �N���X�̏��.
     *      �K�v�ȏ��͂�������擾����.
     */
    public ClassVertex(final int id, final String label, final String sortKey,
            final Visualizable visualizable, final Member member) {
        super(id, label, sortKey, visualizable, member);
        // ���O�Ń\�[�g
        this.methods = new TreeSet<MethodVertex>(new Comparator<MethodVertex>() {
            public int compare(MethodVertex lhs, MethodVertex rhs) {
                return lhs.getLabel().compareTo(rhs.getLabel());
            }

        });
        // ���O�Ń\�[�g
        this.fields = new TreeSet<FieldVertex>(new Comparator<FieldVertex>() {
            public int compare(FieldVertex lhs, FieldVertex rhs) {
                return lhs.getLabel().compareTo(rhs.getLabel());
            }

        });
    }

    /**
     * ���\�b�h�ꗗ.
     */
    public SortedSet<MethodVertex> getMethods() {
        return Collections.unmodifiableSortedSet(methods);
    }

    /**
     * �t�B�[���h�ꗗ.
     */
    public SortedSet<FieldVertex> getFields() {
        return Collections.unmodifiableSortedSet(fields);
    }

    /**
     * ���\�b�h��ǉ�����.
     * @param vertex ���\�b�h���_
     */
    public void addMethod(MethodVertex vertex) {
        methods.add(vertex);
    }

    /**
     * �t�B�[���h��ǉ�����.
     * @param vertex �t�B�[���h���_
     */
    public void addField(FieldVertex vertex) {
        fields.add(vertex);
    }

    /**
     * �N���X�͓����ɃO���t�������Ȃ�.
     * @return false
     */
    public boolean hasSubgraphs() {
        return false;
    }

    /**
     * �N���X�͓����ɒ��_������.
     * @return true
     */
    public boolean hasSubvertices() {
        return true;
    }

}
