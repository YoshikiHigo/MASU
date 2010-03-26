package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * PDG��\���N���X
 * 
 * @author t-miyake
 * 
 */
public abstract class PDG {

	/**
	 * PDG���\������m�[�h
	 */
	protected final SortedSet<PDGNode<?>> nodes;

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

		this.nodes = new TreeSet<PDGNode<?>>();
		// this.statementNodeCache = new HashMap<StatementInfo,
		// ControllableNode<? extends StatementInfo>>();

	}

	/**
	 * PDG���\�z����
	 */
	protected abstract void buildPDG();

	/**
	 * PDG���̃m�[�h�����擾
	 * 
	 * @return PDG���̃m�[�h��
	 */
	public final int getNumberOfNodes() {
		return this.nodes.size();
	}

	/**
	 * PDG�̃m�[�h���擾�C�Ȃ��ꍇ��null��Ԃ�
	 * 
	 * @param element
	 *            �擾�������m�[�h�ɑΉ�����v�f
	 * @return �m�[�h
	 */
	public final PDGNode<?> getNode(final Object element) {

		if (null == element) {
			throw new IllegalArgumentException();
		}

		final PDGNode<?> node = this.getNodeFactory().getNode(element);
		return null == node ? null : this.getAllNodes().contains(node) ? node
				: null;
	}

	/**
	 * PDG�̑S�m�[�h��Ԃ�
	 * 
	 * @return PDG�̑S�m�[�h
	 */
	public final SortedSet<? extends PDGNode<?>> getAllNodes() {
		return Collections.unmodifiableSortedSet(this.nodes);
	}

	/**
	 * PDG�̑S�G�b�W��Ԃ�
	 * 
	 * @return PDG�̑S�G�b�W
	 */
	public final SortedSet<? extends PDGEdge> getAllEdges() {
		final SortedSet<PDGEdge> edges = new TreeSet<PDGEdge>();
		for (final PDGNode<?> node : this.getAllNodes()) {
			edges.addAll(node.getBackwardEdges());
			edges.addAll(node.getForwardEdges());
		}
		return Collections.unmodifiableSortedSet(edges);
	}

	/**
	 * PDG�\�z�ɗp�����t�@�N�g����Ԃ�
	 * 
	 * @return PDG�\�z�ɗp�����t�@�N�g��
	 */
	public IPDGNodeFactory getNodeFactory() {
		return this.nodeFactory;
	}

	public void addNode(final PDGNode<?> node) {
		this.nodes.add(node);
	}

	public void removeNode(final PDGNode<?> node) {
		this.nodes.remove(node);
	}
}
