package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGAcrossEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class SystemSlicing extends Slicing {

	public SystemSlicing(final PDGNode<?> pointA, final PDGNode<?> pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.checkedNodesA = new HashSet<PDGNode<?>>();
		this.checkedNodesB = new HashSet<PDGNode<?>>();
		this.callStackA = new Stack<CallInfo<?>>();
		this.callStackB = new Stack<CallInfo<?>>();
		this.clonepair = null;
	}

	public ClonePairInfo perform() {
		if (null != this.clonepair) {
			return this.clonepair;
		} else {
			this.clonepair = this.perform(this.pointA, this.pointB);
			return this.clonepair;
		}
	}

	final private PDGNode<?> pointA;
	final private PDGNode<?> pointB;

	final private Set<PDGNode<?>> checkedNodesA;
	final private Set<PDGNode<?>> checkedNodesB;

	final private Stack<CallInfo<?>> callStackA;
	final private Stack<CallInfo<?>> callStackB;

	private ClonePairInfo clonepair;

	public ClonePairInfo perform(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

		// ���̃m�[�h���`�F�b�N�ς݃m�[�h�W���ɒǉ��C���̏����͍ċA�Ăяo���̑O�łȂ���΂Ȃ�Ȃ�
		this.checkedNodesA.add(nodeA);
		this.checkedNodesB.add(nodeB);

		// ��������C�e�G�b�W�̐�ɂ���m�[�h�̏W���𓾂邽�߂̏���
		final SortedSet<PDGEdge> backwardEdgesA = nodeA.getBackwardEdges();
		final SortedSet<PDGEdge> backwardEdgesB = nodeB.getBackwardEdges();
		final SortedSet<PDGEdge> forwardEdgesA = nodeA.getForwardEdges();
		final SortedSet<PDGEdge> forwardEdgesB = nodeB.getForwardEdges();

		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesA = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(backwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesA = PDGDataDependenceEdge
				.getDataDependenceEdge(backwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesA = PDGControlDependenceEdge
				.getControlDependenceEdge(backwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesB = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(backwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesB = PDGDataDependenceEdge
				.getDataDependenceEdge(backwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesB = PDGControlDependenceEdge
				.getControlDependenceEdge(backwardEdgesB);

		// ���\�b�h�ԃo�b�N���[�h�X���C�X�p�̕K�v�ȏ����擾
		// Data Dependency�ɑ΂���������X���C�X�́C�ȉ��̏�Ԃ̂Ƃ��͍s��Ȃ�
		// 1. ���\�b�h�X�^�b�N����̂Ƃ�
		// 2. ���\�b�h�X�^�b�N����łȂ��C�X���C�X��̃m�[�h���X�^�b�N�̍ŏ㕔�̃m�[�h�ƈقȂ郁�\�b�h�ɂ���Ƃ�
		final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesA = new HashMap<PDGNode<?>, CallInfo<?>>();
		if (this.callStackA.isEmpty()) {
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesA
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					iterator.remove();
				}
			}
		} else {
			final CallInfo<?> call = this.callStackA.peek();
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesA
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
					if (!call.equals(acrossEdge.getHolder())) {
						iterator.remove();
					} else {
						acrossBackwardNodesA.put(edge.getFromNode(), acrossEdge
								.getHolder());
					}
				}
			}
		}

		final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesB = new HashMap<PDGNode<?>, CallInfo<?>>();
		if (this.callStackB.isEmpty()) {
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesB
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					iterator.remove();
				}
			}
		} else {
			final CallInfo<?> call = this.callStackB.peek();
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesB
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
					if (!call.equals(acrossEdge.getHolder())) {
						iterator.remove();
					} else {
						acrossBackwardNodesB.put(edge.getFromNode(), acrossEdge
								.getHolder());
					}
				}
			}
		}

		final SortedSet<PDGNode<?>> backwardExecutionNodesA = this
				.getFromNodes(backwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> backwardDataNodesA = this
				.getFromNodes(backwardDataEdgesA);
		final SortedSet<PDGNode<?>> backwardControlNodesA = this
				.getFromNodes(backwardControlEdgesA);
		final SortedSet<PDGNode<?>> backwardExecutionNodesB = this
				.getFromNodes(backwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> backwardDataNodesB = this
				.getFromNodes(backwardDataEdgesB);
		final SortedSet<PDGNode<?>> backwardControlNodesB = this
				.getFromNodes(backwardControlEdgesB);

		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesA = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(forwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesA = PDGDataDependenceEdge
				.getDataDependenceEdge(forwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesA = PDGControlDependenceEdge
				.getControlDependenceEdge(forwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesB = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(forwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesB = PDGDataDependenceEdge
				.getDataDependenceEdge(forwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesB = PDGControlDependenceEdge
				.getControlDependenceEdge(forwardEdgesB);

		// ���\�b�h�ԃt�H���[�h�X���C�X�p�̕K�v�ȏ����擾
		final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesA = new HashMap<PDGNode<?>, CallInfo<?>>();
		for (final PDGDataDependenceEdge edge : forwardDataEdgesA) {
			if (edge instanceof PDGAcrossEdge) {
				final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
				acrossForwardNodesA.put(edge.getToNode(), acrossEdge
						.getHolder());
			}
		}
		final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesB = new HashMap<PDGNode<?>, CallInfo<?>>();
		for (final PDGDataDependenceEdge edge : forwardDataEdgesB) {
			if (edge instanceof PDGAcrossEdge) {
				final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
				acrossForwardNodesB.put(edge.getToNode(), acrossEdge
						.getHolder());
			}
		}

		final SortedSet<PDGNode<?>> forwardExecutionNodesA = this
				.getToNodes(forwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> forwardDataNodesA = this
				.getToNodes(forwardDataEdgesA);
		final SortedSet<PDGNode<?>> forwardControlNodesA = this
				.getToNodes(forwardControlEdgesA);
		final SortedSet<PDGNode<?>> forwardExecutionNodesB = this
				.getToNodes(forwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> forwardDataNodesB = this
				.getToNodes(forwardDataEdgesB);
		final SortedSet<PDGNode<?>> forwardControlNodesB = this
				.getToNodes(forwardControlEdgesB);

		final ClonePairInfo clonepair = new ClonePairInfo();

		// �e�m�[�h�̏W���ɑ΂��Ă��̐�ɂ���N���[���y�A�̍\�z
		// �o�b�N���[�h�X���C�X���g���ݒ�̏ꍇ
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
			this.enlargeClonePair(clonepair, backwardExecutionNodesA,
					backwardExecutionNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			this.enlargeClonePair(clonepair, backwardDataNodesA,
					backwardDataNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			this.enlargeClonePair(clonepair, backwardControlNodesA,
					backwardControlNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
		}

		// �t�H���[�h�X���C�X���g���ݒ�̏ꍇ
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
			this.enlargeClonePair(clonepair, forwardExecutionNodesA,
					forwardExecutionNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			this.enlargeClonePair(clonepair, forwardDataNodesA,
					forwardDataNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			this.enlargeClonePair(clonepair, forwardControlNodesA,
					forwardControlNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
		}

		// ���݂̃m�[�h���N���[���y�A�ɒǉ�
		clonepair.add(nodeA, nodeB);

		return clonepair;
	}

	private void enlargeClonePair(final ClonePairInfo clonepair,
			final SortedSet<PDGNode<?>> nodesA,
			final SortedSet<PDGNode<?>> nodesB,
			final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesA,
			final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesB,
			final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesA,
			final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesB) {

		for (final PDGNode<?> nodeA : nodesA) {

			// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
			// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
			if (this.checkedNodesA.contains(nodeA)
					|| this.checkedNodesB.contains(nodeA)) {
				continue;
			}

			// �m�[�h�̃n�b�V���l�𓾂�
			Integer hashA = NODE_TO_HASH_MAP.get(nodeA);
			if (null == hashA) {
				final ExecutableElementInfo coreA = nodeA.getCore();
				hashA = Conversion.getNormalizedString(coreA).hashCode();
				NODE_TO_HASH_MAP.put(nodeA, hashA);
			}

			for (final PDGNode<?> nodeB : nodesB) {

				// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
				// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
				if (this.checkedNodesB.contains(nodeB)
						|| this.checkedNodesA.contains(nodeB)) {
					continue;
				}

				// �m�[�h�̃n�b�V���l�𓾂�
				Integer hashB = NODE_TO_HASH_MAP.get(nodeB);
				if (null == hashB) {
					final ExecutableElementInfo coreB = nodeB.getCore();
					hashB = Conversion.getNormalizedString(coreB).hashCode();
					NODE_TO_HASH_MAP.put(nodeB, hashB);
				}

				SlicingThread.increaseNumberOfComparison();

				// �m�[�h�̃n�b�V���l���������ꍇ�́C���̃m�[�h�y�A�̐������ɒ���
				if (hashA.equals(hashB)) {

					// �m�[�h�������ꍇ�͒������Ȃ�
					if (nodeA == nodeB) {
						continue;
					}

					// ���\�b�h���܂�����ꍇ�̓R�[���X�^�b�N�̍X�V���s��
					if (acrossBackwardNodesA.containsKey(nodeA)) {
						if (!this.callStackA.peek().equals(
								acrossBackwardNodesA.get(nodeA))) {
							throw new IllegalStateException();
						} else {
							this.callStackA.pop();
						}
					} else if (acrossForwardNodesA.containsKey(nodeA)) {
						this.callStackA.push(acrossForwardNodesA.get(nodeA));
					}
					if (acrossBackwardNodesB.containsKey(nodeB)) {
						if (!this.callStackB.peek().equals(
								acrossBackwardNodesB.get(nodeB))) {
							throw new IllegalStateException();
						} else {
							this.callStackB.pop();
						}
					} else if (acrossForwardNodesB.containsKey(nodeB)) {
						this.callStackB.push(acrossForwardNodesB.get(nodeB));
					}

					final ClonePairInfo priorClonepair = this.perform(nodeA,
							nodeB);
					clonepair.codecloneA.addElements(priorClonepair.codecloneA);
					clonepair.codecloneB.addElements(priorClonepair.codecloneB);

					// ���\�b�h���܂�����ꍇ�̓R�[�X�X�^�b�N�̍X�V���s��
					if (acrossBackwardNodesA.containsKey(nodeA)) {
						this.callStackA.push(acrossBackwardNodesA.get(nodeA));
					} else if (acrossForwardNodesA.containsKey(nodeA)) {
						this.callStackA.pop();
					}
					if (acrossBackwardNodesB.containsKey(nodeB)) {
						this.callStackB.push(acrossBackwardNodesB.get(nodeB));
					} else if (acrossForwardNodesB.containsKey(nodeB)) {
						this.callStackB.pop();
					}
				}
			}
		}
	}

	private SortedSet<PDGNode<?>> getFromNodes(
			final SortedSet<? extends PDGEdge> edges) {

		final SortedSet<PDGNode<?>> fromNodes = new TreeSet<PDGNode<?>>();

		for (final PDGEdge edge : edges) {
			fromNodes.add(edge.getFromNode());
		}

		return fromNodes;
	}

	private SortedSet<PDGNode<?>> getToNodes(
			final SortedSet<? extends PDGEdge> edges) {

		final SortedSet<PDGNode<?>> toNodes = new TreeSet<PDGNode<?>>();

		for (final PDGEdge edge : edges) {
			toNodes.add(edge.getToNode());
		}

		return toNodes;
	}

	private static ConcurrentMap<PDGNode<?>, Integer> NODE_TO_HASH_MAP = new ConcurrentHashMap<PDGNode<?>, Integer>();
}
