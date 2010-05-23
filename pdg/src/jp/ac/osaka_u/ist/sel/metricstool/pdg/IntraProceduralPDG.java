package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.IntraProceduralCFG;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;

/**
 * �葱����PDG��\���N���X
 * 
 * @author t-miyake, higo
 * 
 */
public class IntraProceduralPDG extends PDG {

	/**
	 * PDG�̓����m�[�h
	 */
	// protected final SortedSet<PDGNode<?>> enterNodes;
	protected final PDGMethodEnterNode enterNode;

	/**
	 * PDG�̏o���m�[�h
	 */
	protected final SortedSet<PDGNode<?>> exitNodes;

	final CallableUnitInfo unit;

	final boolean buildDataDependence;

	final boolean buildControlDependence;

	final boolean buildExecutionDependence;

	final boolean countObjectStateChange;

	final int dataDependencyDistance;

	final int controlDependencyDistance;

	final int executionDependencyDistance;

	/**
	 * PDG���\�z���ɗ��p����CFG
	 */
	private final IntraProceduralCFG cfg;

	/**
	 * PDG�𐶐�����
	 * 
	 * @param unit
	 *            pdg�𐶐����郆�j�b�g
	 * @param pdgNodeFactory
	 *            PDG�̃m�[�h�����ɗp����t�@�N�g��
	 * @param cfgNodeFactory
	 *            CFG�̃m�[�h�����ɗp����t�@�N�g��
	 * @param buildDataDependency
	 *            Data Dependency�𐶐����邩�H
	 * @param buildControlDependencey
	 *            Control Dependency�𐶐����邩�H
	 * @param buildExecutionDependency
	 *            Execution Dependency�𐶐����邩�H
	 * @param countObjectStateChange
	 *            ���\�b�h�����ɂ��I�u�W�F�N�g�̕ύX���l�����邩
	 * @param dataDependencyDistance
	 *            �f�[�^�ˑ��ӂ��������_�Ԃ̋�����臒l�i�s�̍��j
	 * @param controlDependencyDistance
	 *            ����ˑ��ӂ��������_�Ԃ̋�����臒l�i�s�̍��j
	 * @param executionDependencyDistance
	 *            ���s�ˑ��ӂ��������_�Ԃ̋�����臒l�i�s�̍��j
	 */
	public IntraProceduralPDG(final CallableUnitInfo unit,
			final IPDGNodeFactory pdgNodeFactory,
			final ICFGNodeFactory cfgNodeFactory,
			final boolean buildDataDependency,
			final boolean buildControlDependencey,
			final boolean buildExecutionDependency,
			final boolean countObjectStateChange,
			final int dataDependencyDistance,
			final int controlDependencyDistance,
			final int executionDependencyDistance) {

		super(pdgNodeFactory);
		if (null == unit) {
			throw new IllegalArgumentException("method is null.");
		}

		this.enterNode = PDGMethodEnterNode.createNode(unit);
		this.exitNodes = new TreeSet<PDGNode<?>>();

		this.unit = unit;

		this.buildDataDependence = buildDataDependency;
		this.buildControlDependence = buildControlDependencey;
		this.buildExecutionDependence = buildExecutionDependency;
		this.countObjectStateChange = countObjectStateChange;
		this.dataDependencyDistance = dataDependencyDistance;
		this.controlDependencyDistance = controlDependencyDistance;
		this.executionDependencyDistance = executionDependencyDistance;

		this.cfg = new IntraProceduralCFG(unit, cfgNodeFactory);

		this.buildPDG();
	}

	/**
	 * PDG�𐶐�����
	 * 
	 * @param unit
	 *            pdg�𐶐����郆�j�b�g
	 * @param pdgNodeFactory
	 *            PDG�̃m�[�h�����ɗp����t�@�N�g��
	 * @param cfgNodeFactory
	 *            CFG�̃m�[�h�����ɗp����t�@�N�g��
	 * @param buildDataDependency
	 *            Data Dependency�𐶐����邩�H
	 * @param buildControlDependencey
	 *            Control Dependency�𐶐����邩�H
	 * @param buildExecutionDependency
	 *            Execution Dependency�𐶐����邩�H
	 * @param countObjectStateChange
	 *            ���\�b�h�����ɂ��I�u�W�F�N�g�̕ύX���l�����邩
	 */
	public IntraProceduralPDG(final CallableUnitInfo unit,
			final IPDGNodeFactory pdgNodeFactory,
			final ICFGNodeFactory cfgNodeFactory,
			final boolean buildDataDependency,
			final boolean buildControlDependency,
			final boolean buildExecutionDependency,
			final boolean countObjectStateChange) {

		this(unit, pdgNodeFactory, cfgNodeFactory, buildDataDependency,
				buildControlDependency, buildExecutionDependency,
				countObjectStateChange, Integer.MAX_VALUE, Integer.MAX_VALUE,
				Integer.MAX_VALUE);
	}

	/**
	 * PDG�𐶐�����
	 * 
	 * @param unit
	 *            pdg�𐶐����郆�j�b�g
	 * @param pdgNodeFactory
	 *            PDG�̃m�[�h�����ɗp����t�@�N�g��
	 * @param cfgNodeFactory
	 *            CFG�̃m�[�h�����ɗp����t�@�N�g��
	 * @param buildDataDependency
	 *            Data Dependency�𐶐����邩�H
	 * @param buildControlDependencey
	 *            Control Dependency�𐶐����邩�H
	 * @param buildExecutionDependency
	 *            Execution Dependency�𐶐����邩�H
	 */
	public IntraProceduralPDG(final CallableUnitInfo unit,
			final IPDGNodeFactory pdgNodeFactory,
			final ICFGNodeFactory cfgNodeFactory,
			final boolean buildDataDependency,
			final boolean buildControlDependency,
			final boolean buildExecutionDependency) {

		this(unit, pdgNodeFactory, cfgNodeFactory, buildDataDependency,
				buildControlDependency, buildExecutionDependency, false,
				Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public IntraProceduralPDG(final CallableUnitInfo unit,
			final IPDGNodeFactory pdgNodeFactory,
			final ICFGNodeFactory cfgNodeFactory) {
		this(unit, pdgNodeFactory, cfgNodeFactory, true, true, true);
	}

	public IntraProceduralPDG(final CallableUnitInfo unit) {
		this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory());
	}

	public IntraProceduralPDG(final CallableUnitInfo unit,
			final boolean countObjectStateChange) {
		this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory(),
				true, true, true, countObjectStateChange);
	}

	public IntraProceduralPDG(final CallableUnitInfo unit,
			final boolean buildDataDependency,
			final boolean buildControlDependencey,
			final boolean buildExecutionDependency) {
		this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory(),
				buildDataDependency, buildControlDependencey,
				buildExecutionDependency);
	}

	/**
	 * �����m�[�h���擾
	 * 
	 * @return �����m�[�h
	 */
	public final PDGMethodEnterNode getMethodEnterNode() {
		return this.enterNode;
	}

	/**
	 * �o���m�[�h���擾
	 * 
	 * @return �o���m�[�h
	 */
	public final SortedSet<PDGNode<?>> getExitNodes() {
		return Collections.unmodifiableSortedSet(this.exitNodes);
	}

	public boolean isBuiltDataDependency() {
		return this.buildDataDependence;
	}

	public boolean isBuiltControlDependency() {
		return this.buildControlDependence;
	}

	public boolean isBuiltExecutionDependency() {
		return this.buildExecutionDependence;
	}

	/**
	 * �R���X�g���N�^�ŗ^����ꂽCallableUnitInfo��PDG���\�z����
	 */
	@Override
	protected void buildPDG() {

		final CFGNode<?> cfgEnterNode = this.cfg.getEnterNode();

		{
			// ���\�b�h�̃G���^�[�m�[�h���璼�ڂ̓������ɑ΂��Đ���ˑ��ӂ�����
			final PDGMethodEnterNode enterNode = this.getMethodEnterNode();
			this.nodes.add(enterNode);
			final CallableUnitInfo unit = this.getMethodInfo();
			this.buildControlDependence(enterNode, unit);

			// ���\�b�h�̃G���^�[�m�[�h���烁�\�b�h���ōŏ��Ɏ��s����镶�Ɏ��s�ˑ��ӂ�����
			if (null != cfgEnterNode) {
				final PDGNode<?> toPDGNode = this.makeNode(cfgEnterNode);
				enterNode.addExecutionDependingNode(toPDGNode);
			}
		}

		// unit�̈���������
		for (final ParameterInfo parameter : this.unit.getParameters()) {

			final PDGNode<?> pdgNode = this.makeNormalNode(parameter);
			// this.enterNodes.add(pdgNode);
			if (null != cfgEnterNode) {
				this.buildDataDependence(cfgEnterNode, pdgNode, parameter,
						new HashSet<CFGNode<?>>());
			}
		}

		// CFG�̓����m�[�h���珈�����s��
		final Set<CFGNode<?>> checkedNodes = new HashSet<CFGNode<?>>();
		if (null != cfgEnterNode) {

			// �������Ȃ��ꍇ�́CCFG�̓����m�[�h��PDG�̓����m�[�h�ɂȂ�
			/*
			 * if (0 == this.unit.getParameters().size()) { final PDGNode<?>
			 * pdgEnterNode = this.makeNode(cfgEnterNode);
			 * this.enterNodes.add(pdgEnterNode); }
			 */

			this.buildDependence(cfgEnterNode, checkedNodes);
		}

		// CFG�̏o���m�[�h��PDG�̏o���m�[�h�ɂȂ�
		for (final CFGNode<?> cfgExitNode : this.cfg.getExitNodes()) {
			final PDGNode<?> pdgExitNode = this.makeNode(cfgExitNode);
			this.exitNodes.add(pdgExitNode);
		}

		// Unreableble�ȃm�[�h�ɑ΂��Ă��������s��

		if (!this.cfg.isEmpty()) {
			final Set<CFGNode<?>> unreachableNodes = new HashSet<CFGNode<?>>();
			unreachableNodes.addAll(this.cfg.getAllNodes());
			unreachableNodes
					.removeAll(this.cfg.getReachableNodes(cfgEnterNode));
			for (final CFGNode<?> unreachableNode : unreachableNodes) {
				this.buildDependence(unreachableNode, checkedNodes);
			}
		}
	}

	private void buildDependence(final CFGNode<?> cfgNode,
			final Set<CFGNode<?>> checkedNodes) {

		if (null == cfgNode || null == checkedNodes) {
			throw new IllegalArgumentException();
		}

		// ���ɒ�������Ă���m�[�h�ł���ꍇ�͉������Ȃ�
		if (checkedNodes.contains(cfgNode)) {
			return;
		}

		// ���݂̃m�[�h�𒲍��ς݂ɒǉ�
		else {
			checkedNodes.add(cfgNode);
		}

		// �^����ꂽCFG�m�[�h�ɑΉ�����PDG�m�[�h���쐬
		final PDGNode<?> pdgNode = this.makeNode(cfgNode);

		// �^����ꂽCFG�m�[�h�Œ�`���ꂽ�e�ϐ��ɑ΂��āC
		// ���̕ϐ����Q�Ƃ��Ă���m�[�h��DataDependence������
		if (this.isBuiltDataDependency()) {
			for (final VariableInfo<? extends UnitInfo> variable : cfgNode
					.getDefinedVariables(this.countObjectStateChange)) {

				for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
					final Set<CFGNode<?>> checkedNodesForDefinedVariables = new HashSet<CFGNode<?>>();
					// checkedNodesForDefinedVariables.add(cfgNode);
					this.buildDataDependence(forwardNode, pdgNode, variable,
							checkedNodesForDefinedVariables);
				}
			}
		}

		// �^����ꂽCFG�m�[�h����ControlDependence������
		if (this.isBuiltControlDependency()) {
			if (pdgNode instanceof PDGControlNode) {
				final ConditionInfo condition = (ConditionInfo) cfgNode
						.getCore();
				this.buildControlDependence((PDGControlNode) pdgNode,
						PDGUtility.getOwnerConditionalBlock(condition));
			}
		}

		// �^����ꂽCFG�m�[�h����ExecutionDependence������
		if (this.isBuiltExecutionDependency()) {
			final PDGNode<?> fromPDGNode = this.makeNode(cfgNode);
			for (final CFGNode<?> toNode : cfgNode.getForwardNodes()) {
				final PDGNode<?> toPDGNode = this.makeNode(toNode);
				final int distance = Math.abs(toPDGNode.getCore().getFromLine()
						- fromPDGNode.getCore().getToLine()) + 1;
				if (distance <= this.dataDependencyDistance) {
					fromPDGNode.addExecutionDependingNode(toPDGNode);
				}

			}
		}

		for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
			this.buildDependence(forwardNode, checkedNodes);
		}
	}

	/**
	 * �������ŗ^����ꂽCFG�̃m�[�h�ɑ΂��āC�������ŗ^����ꂽPDG�m�[�h����̃f�[�^�ˑ������邩�𒲂ׁC ����ꍇ�́C�f�[�^�ˑ��ӂ�����
	 * 
	 * @param cfgNode
	 * @param fromPDGNode
	 * @param variable
	 */
	private void buildDataDependence(final CFGNode<?> cfgNode,
			final PDGNode<? extends ExecutableElementInfo> fromPDGNode,
			final VariableInfo<?> variable,
			final Set<CFGNode<?>> checkedCFGNodes) {

		if (null == cfgNode || null == fromPDGNode || null == variable
				|| null == checkedCFGNodes) {
			throw new IllegalArgumentException();
		}

		// ���ɒ��ׂĂ���m�[�h�ꍇ�͉������Ȃ��Ń��\�b�h�𔲂���
		if (checkedCFGNodes.contains(cfgNode)) {
			return;
		}

		// �����������ׂ��m�[�h���`�F�b�N�����m�[�h�ɒǉ�
		else {
			checkedCFGNodes.add(cfgNode);
		}

		// cfgNode��variable���Q�Ƃ��Ă���ꍇ�́C
		// cfgNode����PDGNode���쐬���CfromPDGNode����f�[�^�ˑ��ӂ�����
		if (cfgNode.getUsedVariables().contains(variable)) {

			final PDGNode<? extends ExecutableElementInfo> toPDGNode = this
					.makeNode(cfgNode);

			// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W������
			final int distance = Math.abs(toPDGNode.getCore().getFromLine()
					- fromPDGNode.getCore().getToLine()) + 1;
			if (distance <= this.dataDependencyDistance) {
				fromPDGNode.addDataDependingNode(toPDGNode, variable);
			}
		}

		// cfgNode��variable�ɑ�����Ă���ꍇ�́C
		// ����ȍ~�̃m�[�h�̃f�[�^�ˑ��͒��ׂȂ�
		if (cfgNode.getDefinedVariables(this.countObjectStateChange).contains(
				variable)) {
			return;
		}

		// cfgNode�̃t�H���[�h�m�[�h�ɑ΂��Ă��f�[�^�ˑ��𒲂ׂ�
		else {
			for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
				this.buildDataDependence(forwardNode, fromPDGNode, variable,
						checkedCFGNodes);
			}
		}
	}

	/**
	 * 
	 * �������ŗ^����ꂽ�m�[�h�ɑ΂��āC�������ŗ^����ꂽblock�Ɋ܂܂�镶�ɐ���ˑ��ӂ�����
	 * 
	 * @param fromPDGNode
	 * @param block
	 */
	private void buildControlDependence(final PDGControlNode fromPDGNode,
			final LocalSpaceInfo block) {

		for (final StatementInfo innerStatement : block.getStatements()) {

			// �P���̏ꍇ�́CfromPDGNode����̐���ˑ��ӂ�����
			// CaseEntry�̏ꍇ�́C����ˑ��ӂ͂���Ȃ�
			// Break���̏ꍇ�CContinue���̏ꍇ������ˑ��ӂ͂���Ȃ�
			if (innerStatement instanceof SingleStatementInfo
					&& !(innerStatement instanceof BreakStatementInfo)
					&& !(innerStatement instanceof ContinueStatementInfo)) {
				final PDGNode<?> toPDGNode = this
						.makeNormalNode(innerStatement);

				// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W������
				final int distance = Math.abs(toPDGNode.getCore().getFromLine()
						- fromPDGNode.getCore().getToLine()) + 1;
				if (distance <= this.controlDependencyDistance) {
					fromPDGNode.addControlDependingNode(toPDGNode,
							!(block instanceof ElseBlockInfo));
				}
			}

			// Block���̏ꍇ�́C�����t�����ł���΁C�P���̎��Ɠ�������
			// �����łȂ���΁C����ɓ����𒲂ׂ�
			else if (innerStatement instanceof BlockInfo) {

				if (innerStatement instanceof ConditionalBlockInfo) {

					{
						final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
								.getConditionalClause().getCondition();
						final PDGNode<?> toPDGNode = this
								.makeControlNode(condition);

						// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W������
						final int distance = Math.abs(toPDGNode.getCore()
								.getFromLine()
								- fromPDGNode.getCore().getToLine()) + 1;
						if (distance <= this.controlDependencyDistance) {
							fromPDGNode.addControlDependingNode(toPDGNode,
									!(block instanceof ElseBlockInfo));
						}
					}

					if (innerStatement instanceof ForBlockInfo) {
						final ForBlockInfo forBlock = (ForBlockInfo) innerStatement;
						for (final ConditionInfo expression : forBlock
								.getInitializerExpressions()) {
							final PDGNode<?> toPDGNode = this
									.makeNormalNode(expression);

							// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W������
							final int distance = Math.abs(toPDGNode.getCore()
									.getFromLine()
									- fromPDGNode.getCore().getToLine()) + 1;
							if (distance <= this.controlDependencyDistance) {
								fromPDGNode.addControlDependingNode(toPDGNode,
										!(block instanceof ElseBlockInfo));
							}
						}
					}
				}

				// else�u���b�N�̏ꍇ�͂����ł́C�ˑ��ӂ͈����Ȃ�
				else if (innerStatement instanceof ElseBlockInfo) {

				}

				else {
					this.buildControlDependence(fromPDGNode,
							(BlockInfo) innerStatement);
				}
			}
		}

		// if���̏ꍇ�́Celse�ւ̑Ή������Ȃ���΂Ȃ�Ȃ�
		if (block instanceof IfBlockInfo) {
			final ElseBlockInfo elseBlock = ((IfBlockInfo) block)
					.getSequentElseBlock();
			if (null != elseBlock) {
				this.buildControlDependence(fromPDGNode, elseBlock);
			}
		}

		// for���̌J��Ԃ����ւ̑Ή������Ȃ���΂Ȃ�Ȃ�
		if (block instanceof ForBlockInfo) {

			final ForBlockInfo forBlock = (ForBlockInfo) block;
			final ConditionInfo condition = forBlock.getConditionalClause()
					.getCondition();
			final PDGControlNode extraFromPDGNode = this
					.makeControlNode(condition);

			for (final ExpressionInfo expression : forBlock
					.getIteratorExpressions()) {
				final PDGNode<?> extraToPDGNode = this
						.makeNormalNode(expression);
				extraFromPDGNode.addControlDependingNode(extraToPDGNode, true);
			}
		}
	}

	private PDGNode<?> makeNode(final CFGNode<?> cfgNode) {

		final ExecutableElementInfo element = cfgNode.getCore();
		if (cfgNode instanceof CFGControlNode) {
			return this.makeControlNode((ConditionInfo) element);
		} else if (cfgNode instanceof CFGNormalNode<?>) {
			return this.makeNormalNode(element);
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * �����ŗ^����ꂽ������\��PDG����m�[�h���쐬����
	 * 
	 * @param element
	 *            �m�[�h���쐬�������v�f
	 * @return
	 */
	private PDGControlNode makeControlNode(final ConditionInfo condition) {

		final IPDGNodeFactory factory = this.getNodeFactory();
		final PDGControlNode node = factory.makeControlNode(condition);
		if (null == node) {
			return null;
		}

		this.nodes.add(node);
		return node;
	}

	/**
	 * �����ŗ^����ꂽ������\��PDG���ʃm�[�h(����m�[�h�ȊO)���쐬����
	 * 
	 * @param element
	 *            �m�[�h���쐬�������v�f
	 * @return
	 */
	private PDGNormalNode<?> makeNormalNode(final Object element) {

		final IPDGNodeFactory factory = this.getNodeFactory();
		final PDGNormalNode<?> node = factory.makeNormalNode(element);
		if (null == node) {
			return null;
		}

		this.nodes.add(node);
		return node;
	}

	/**
	 * PDG�̍\�z�ɗ��p����CFG���擾
	 * 
	 * @return
	 */
	public IntraProceduralCFG getCFG() {
		return this.cfg;
	}

	public CallableUnitInfo getMethodInfo() {
		return this.unit;
	}
}
