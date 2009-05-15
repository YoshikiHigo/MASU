package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * PDG��\���N���X
 * 
 * @author t-miyake
 *
 */
public abstract class PDG {

    /**
     * PDG�̓����m�[�h
     */
    protected final Set<PDGNode<?>> enterNodes;

    /**
     * PDG�̏o���m�[�h
     */
    protected final Set<PDGNode<?>> exitNodes;

    /**
     * PDG���\������m�[�h
     */
    protected final Set<PDGNode<?>> nodes;

    /**
     * 
     * �m�[�h�쐬���ɗp����t�@�N�g��
     */
    protected final IPDGNodeFactory nodeFactory;

    public PDG(final IPDGNodeFactory nodeFactory) {

        if (null == nodeFactory) {
            throw new IllegalArgumentException();
        }
        this.nodeFactory = nodeFactory;

        this.enterNodes = new HashSet<PDGNode<?>>();
        this.exitNodes = new HashSet<PDGNode<?>>();
        this.nodes = new HashSet<PDGNode<?>>();
        //this.statementNodeCache = new HashMap<StatementInfo, ControllableNode<? extends StatementInfo>>();

    }

    /**
     * PDG���\�z����
     */
    protected abstract void buildPDG();

    /**
     * �����m�[�h���擾
     * @return �����m�[�h
     */
    public final Set<PDGNode<?>> getEnterNodes() {
        return this.enterNodes;
    }

    /**
     * �o���m�[�h���擾
     * @return �o���m�[�h
     */
    public final Set<PDGNode<?>> getExitNodes() {
        return this.exitNodes;
    }

    /**
     * PDG���̃m�[�h�����擾
     * @return PDG���̃m�[�h��
     */
    public final int getNumberOfNodes() {
        return this.nodes.size();
    }

    /**
     * PDG�̃m�[�h���擾�C�Ȃ��ꍇ��null��Ԃ�
     * 
     * @param element �擾�������m�[�h�ɑΉ�����v�f
     * @return �m�[�h
     */
    public final PDGNode<?> getNode(final Object element) {

        if (null == element) {
            throw new IllegalArgumentException();
        }

        final PDGNode<?> node = this.getNodeFactory().getNode(element);
        return null == node ? null : this.getAllNodes().contains(node) ? node : null;
    }

    /**
     * PDG�̑S�m�[�h��Ԃ�
     * @return PDG�̑S�m�[�h
     */
    public final Collection<? extends PDGNode<?>> getAllNodes() {
        return Collections.unmodifiableSet(this.nodes);
    }

    /**
     * PDG�̑S�G�b�W��Ԃ�
     * 
     * @return PDG�̑S�G�b�W
     */
    public final Collection<? extends PDGEdge> getAllEdges() {
        final Set<PDGEdge> edges = new HashSet<PDGEdge>();
        for (final PDGNode<?> node : this.getAllNodes()) {
            edges.addAll(node.getBackwardEdges());
            edges.addAll(node.getForwardEdges());
        }
        return Collections.unmodifiableSet(edges);
    }

    /**
     * PDG�\�z�ɗp�����t�@�N�g����Ԃ�
     * 
     * @return PDG�\�z�ɗp�����t�@�N�g��
     */
    public IPDGNodeFactory getNodeFactory() {
        return this.nodeFactory;
    }

}
