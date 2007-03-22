package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


/**
 * {@link Visualizable} �� {@link Member} �̏���ێ��E�������� {@link Vertex} �̒��ۃN���X.
 * 
 * {@link Visualizable} �� {@link Member} �̏��͉����̕\���̕ύX�Ɏg�p�����.
 * �ʏ�2�̃����o�͓����I�u�W�F�N�g���w��.
 * 
 * @author rniitani
 */
abstract public class AbstractVertex implements Vertex, Visualizable, Member {

    /** ID */
    final private int id;
    /** �\������镶���� */
    final private String label;
    /** ����p������ */
    final private String sortKey;
    /** ���� */
    final private Visualizable visualizable;
    /** �����o�Ƃ��Ă̈ʒu�Â� */
    final private Member member;

    public AbstractVertex(final int id, final String label, final String sortKey,
            final Visualizable visualizable, final Member member) {
        this.id = id;
        this.label = label;
        this.sortKey = sortKey;
        this.visualizable = visualizable;
        this.member = member;
    }
    
    /**
     * ID ��Ԃ�.
     */
    public int getId() {
        return id;
    }

    /**
     * �N���X����Ԃ�.
     */
    public String getLabel() {
        return label;
    }

    /**
     * ����p�������Ԃ�.
     * @return
     */
    public String getSortKey() {
        return sortKey;
    }

    /**
     * �R���X�g���N�^�œn���ꂽ {@link Member} �̒l.
     */
    public boolean isInstanceMember() {
        return member.isInstanceMember();
    }

    /**
     * �R���X�g���N�^�œn���ꂽ {@link Member} �̒l.
     */
    public boolean isStaticMember() {
        return member.isStaticMember();
    }

    /**
     * �R���X�g���N�^�œn���ꂽ {@link Visualizable} �̒l.
     */
    public boolean isInheritanceVisible() {
        return visualizable.isInheritanceVisible();
    }

    /**
     * �R���X�g���N�^�œn���ꂽ {@link Visualizable} �̒l.
     */
    public boolean isNamespaceVisible() {
        return visualizable.isNamespaceVisible();
    }

    /**
     * �R���X�g���N�^�œn���ꂽ {@link Visualizable} �̒l.
     */
    public boolean isPrivateVisible() {
        return visualizable.isPrivateVisible();
    }

    /**
     * �R���X�g���N�^�œn���ꂽ {@link Visualizable} �̒l.
     */
    public boolean isPublicVisible() {
        return visualizable.isPublicVisible();
    }

}