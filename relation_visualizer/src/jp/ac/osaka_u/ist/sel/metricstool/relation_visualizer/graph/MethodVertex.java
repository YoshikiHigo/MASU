package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


public class MethodVertex extends AbstractVertex {
    final private ClassVertex ownerClass;

    /**
     * �R���X�g���N�^.
     * 
     * @param methodInfo
     *      ���\�b�h�̏��.
     *      �K�v�ȏ��͂�������擾����.
     */
    public MethodVertex(final int id, final ClassVertex ownerClass, final String label, final String sortKey,
            final Visualizable visualizable, final Member member) {
        super(id, label, sortKey, visualizable, member);
        this.ownerClass = ownerClass;
    }

    public ClassVertex getOwnerClass() {
        return ownerClass;
    }
}
