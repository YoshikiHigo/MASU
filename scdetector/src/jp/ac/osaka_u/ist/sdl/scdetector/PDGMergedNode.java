package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGParameterNode;

public class PDGMergedNode extends PDGNormalNode<ExecutableElementInfo> {

	/**
	 * �����ŗ^����ꂽPDG�̒��_���k���s��
	 * 
	 * @param pdg
	 */
	public static void merge(final PDG pdg, final IPDGNodeFactory pdgNodeFactory) {

		for (final PDGNode<?> node : PDGControlNode.getControlNodes(pdg
				.getAllNodes())) {

			// ����m�[�h�̎����m�[�}���m�[�h�ł���Έ��k�\���ǂ����𒲂ׂ�
			if (node instanceof PDGControlNode) {
				for (final PDGEdge edge : PDGExecutionDependenceEdge
						.getExecutionDependenceEdge(node.getForwardEdges())) {
					final PDGNode<?> toNode = edge.getToNode();
					if (toNode instanceof PDGNormalNode<?>) {
						findMergedNodes((PDGNormalNode<?>) toNode,
								pdgNodeFactory);
					}
				}
			}
		}
	}

	/**
	 * �����ŗ^����ꂽ���_����C���s�ˑ��ł��ǂ��Ă����CPDGNormalNode�ł��邩����C���k�����݂�
	 * 
	 * @param node
	 */
	private static void findMergedNodes(final PDGNormalNode<?> node,
			final IPDGNodeFactory pdgNodeFactory) {

		final int hash = Conversion.getNormalizedString(node.getCore())
				.hashCode();
		final PDGMergedNode mergedNode = new PDGMergedNode(node);

		final SortedSet<PDGExecutionDependenceEdge> toEdges = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(node.getForwardEdges());
		if (toEdges.isEmpty()) {
			return;
		}

		PDGNode<?> toNode = toEdges.first().getToNode();
		if (!(toNode instanceof PDGNormalNode<?>)) { // ����toNode���m�[�}���łȂ��ꍇ�͉������Ȃ��Ă���
			return;
		}
		int toHash = Conversion.getNormalizedString(toNode.getCore())
				.hashCode();

		while (hash == toHash) {
			mergedNode.addNode((PDGNormalNode<?>) toNode);
			final SortedSet<PDGExecutionDependenceEdge> forwardEdges = PDGExecutionDependenceEdge
					.getExecutionDependenceEdge(toNode.getForwardEdges());
			if (forwardEdges.isEmpty()) {
				insertMergedNode(mergedNode, pdgNodeFactory);
				return;
			}
			toNode = forwardEdges.first().getToNode();
			if (!(toNode instanceof PDGNormalNode<?>)) {
				insertMergedNode(mergedNode, pdgNodeFactory);
				return;
			}
			toHash = Conversion.getNormalizedString(toNode.getCore())
					.hashCode();
		}
		insertMergedNode(mergedNode, pdgNodeFactory);
		findMergedNodes((PDGNormalNode<?>) toNode, pdgNodeFactory);
	}

	private static void insertMergedNode(final PDGMergedNode mergedNode,
			final IPDGNodeFactory pdgNodeFactory) {

		final List<PDGNormalNode<?>> originalNodes = mergedNode
				.getOriginalNodes();

		// �W��m�[�h�̏W�񐔂�1�̏ꍇ�͂Ȃɂ����Ȃ��Ă���
		if (1 == originalNodes.size()) {
			return;
		}

		// ���̐擪�m�[�h������
		{
			final PDGNormalNode<?> startNode = originalNodes.get(0);
			for (final PDGEdge edge : startNode.getBackwardEdges()) {

				// ���s�ˑ��ӈȊO�͑ΏۊO
				if (!(edge instanceof PDGExecutionDependenceEdge)) {
					continue;
				}

				final PDGNode<?> previousNode = edge.getFromNode();

				// �I���W�i���m�[�h���폜
				for (final PDGEdge previousEdge : PDGExecutionDependenceEdge
						.getExecutionDependenceEdge(previousNode
								.getForwardEdges())) {
					if (previousEdge.getToNode().equals(startNode)) {
						previousNode.removeForwardEdge(previousEdge);
					}
				}

				// �@�W��m�[�h��ǉ�
				final PDGEdge mergedEdge = new PDGExecutionDependenceEdge(
						previousNode, mergedNode);
				previousNode.addForwardEdge(mergedEdge);
				mergedNode.addBackwardEdge(mergedEdge);
			}
		}

		// ���̍Ō�m�[�h������
		{
			final PDGNormalNode<?> endNode = originalNodes.get(originalNodes
					.size() - 1);
			for (final PDGEdge edge : endNode.getForwardEdges()) {

				// ���s�ˑ��ӈȊO�͑ΏۊO
				if (!(edge instanceof PDGExecutionDependenceEdge)) {
					continue;
				}

				final PDGNode<?> postiousNode = edge.getToNode();

				// �I���W�i���m�[�h���폜
				for (final PDGEdge postiousEdge : PDGExecutionDependenceEdge
						.getExecutionDependenceEdge(postiousNode
								.getBackwardEdges())) {
					if (postiousEdge.getFromNode().equals(endNode)) {
						postiousNode.removeBackwardEdge(postiousEdge);
					}
				}

				// �W��m�[�h��ǉ�
				final PDGEdge mergedEdge = new PDGExecutionDependenceEdge(
						mergedNode, postiousNode);
				mergedNode.addForwardEdge(mergedEdge);
				postiousNode.addBackwardEdge(mergedEdge);
			}
		}

		// �m�[�h�t�@�N�g���ւ̓o�^�ƍ폜
		for (final PDGNode<?> node : originalNodes) {
			final Object element = node.getCore();
			pdgNodeFactory.removeNode(element);
		}
		pdgNodeFactory.addNode(mergedNode);

		// �W��m�[�h�ɑ΂���f�[�^�ˑ��ӁC����ˑ��ӂ̍\�z
		{
			final Set<PDGNode<?>> backwardDataDependenceNodes = new HashSet<PDGNode<?>>();
			final Set<PDGNode<?>> forwardDataDependenceNodes = new HashSet<PDGNode<?>>();
			final Set<PDGControlNode> backwardControlDependenceNodes = new HashSet<PDGControlNode>();

			for (final PDGNode<?> originalNode : originalNodes) {

				for (final PDGDataDependenceEdge edge : PDGDataDependenceEdge
						.getDataDependenceEdge(originalNode.getBackwardEdges())) {

					final PDGNode<?> fromNode = edge.getFromNode();
					if (!originalNodes.contains(fromNode)) {
						final VariableInfo<?> variable = edge.getVariable();
						final PDGDataDependenceEdge newEdge = new PDGDataDependenceEdge(
								fromNode, mergedNode, variable);
						fromNode.addForwardEdge(newEdge);
						mergedNode.addBackwardEdge(newEdge);
					}
				}

				for (final PDGDataDependenceEdge edge : PDGDataDependenceEdge
						.getDataDependenceEdge(originalNode.getForwardEdges())) {

					final PDGNode<?> toNode = edge.getToNode();
					if (!originalNodes.contains(toNode)) {
						final VariableInfo<?> variable = edge.getVariable();
						final PDGDataDependenceEdge newEdge = new PDGDataDependenceEdge(
								mergedNode, toNode, variable);
						mergedNode.addForwardEdge(newEdge);
						toNode.addBackwardEdge(newEdge);
					}
				}

				for (final PDGControlDependenceEdge edge : PDGControlDependenceEdge
						.getControlDependenceEdge(originalNode
								.getBackwardEdges())) {

					final PDGControlNode fromNode = (PDGControlNode) edge
							.getFromNode();
					if (!originalNodes.contains(fromNode)) {
						final boolean flag = edge.isTrueDependence();
						final PDGControlDependenceEdge newEdge = new PDGControlDependenceEdge(
								fromNode, mergedNode, flag);
						fromNode.addForwardEdge(newEdge);
						mergedNode.addBackwardEdge(newEdge);
					}
				}
			}
		}
	}

	public PDGMergedNode(final PDGNormalNode<?> node) {
		this.originalNodes = new LinkedList<PDGNormalNode<?>>();
		this.originalNodes.add(node);
		this.core = node.getCore();
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
		final SortedSet<VariableInfo<? extends UnitInfo>> variables = new TreeSet<VariableInfo<? extends UnitInfo>>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			variables.addAll(originalNode.getDefinedVariables());
		}

		return Collections.unmodifiableSortedSet(variables);
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
		final SortedSet<VariableInfo<? extends UnitInfo>> variables = new TreeSet<VariableInfo<? extends UnitInfo>>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			variables.addAll(originalNode.getReferencedVariables());
		}

		return Collections.unmodifiableSortedSet(variables);
	}

	public void addNode(final PDGNormalNode<?> node) {
		this.originalNodes.add(node);
	}

	public List<PDGNormalNode<?>> getOriginalNodes() {
		return Collections.unmodifiableList(this.originalNodes);
	}

	private final List<PDGNormalNode<?>> originalNodes;
}
