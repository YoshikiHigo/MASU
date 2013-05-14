package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import sdl.ist.osaka_u.newmasu.cfg.DISSOLUTION;
import sdl.ist.osaka_u.newmasu.cfg.IntraProceduralCFG;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGControlNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGNode;
import sdl.ist.osaka_u.newmasu.cfg.node.CFGNormalNode;
import sdl.ist.osaka_u.newmasu.cfg.node.DefaultCFGNodeFactory;
import sdl.ist.osaka_u.newmasu.cfg.node.ICFGNodeFactory;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.DefaultPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGFieldInNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGFieldOutNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGParameterInNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGParameterOutNode;

/**
 * �葱����PDG��\���N���X
 * 
 * @author t-miyake, higo
 * 
 */
public class IntraProceduralPDG extends PDG {

	/**
	 * PDG�̓��m�[�h
	 */
	// protected final SortedSet<PDGNode<?>> enterNodes;
	protected final PDGMethodEnterNode enterNode;

	/**
	 * ��In�m�[�h���Ǘ�����ϐ�
	 */
	protected final Map<ParameterInfo, PDGParameterInNode> parameterInNodes;

	/**
	 * ��Out�m�[�h���Ǘ�����ϐ�
	 */
	protected final Map<ParameterInfo, PDGParameterOutNode> parameterOutNodes;

	protected final Map<FieldInfo, PDGFieldInNode> fieldInNodes;

	protected final Map<FieldInfo, PDGFieldOutNode> fieldOutNodes;

	/**
	 * PDG�̏o��m�[�h
	 */
	protected final SortedSet<PDGNode<?>> exitNodes;

	final CallableUnitInfo unit;

	final boolean buildDataDependence;

	final boolean buildControlDependence;

	final boolean buildExecutionDependence;

	final boolean countObjectStateChange;

	final boolean optimize;

	final DISSOLUTION dissolve;

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
	 *            �f�[�^�ˑ��ӂ����_�Ԃ̋�����臒l�i�s�̍��j
	 * @param controlDependencyDistance
	 *            ����ˑ��ӂ����_�Ԃ̋�����臒l�i�s�̍��j
	 * @param executionDependencyDistance
	 *            ���s�ˑ��ӂ����_�Ԃ̋�����臒l�i�s�̍��j
	 */
	public IntraProceduralPDG(final CallableUnitInfo unit,
			final IPDGNodeFactory pdgNodeFactory,
			final ICFGNodeFactory cfgNodeFactory,
			final boolean buildDataDependency,
			final boolean buildControlDependencey,
			final boolean buildExecutionDependency,
			final boolean countObjectStateChange, final boolean optimize,
			final DISSOLUTION dissolve, final int dataDependencyDistance,
			final int controlDependencyDistance,
			final int executionDependencyDistance) {

		super(pdgNodeFactory, cfgNodeFactory);

		if (null == unit) {
			throw new IllegalArgumentException("method is null.");
		}

		this.enterNode = PDGMethodEnterNode.createNode(unit);
		this.exitNodes = new TreeSet<PDGNode<?>>();
		this.parameterInNodes = new HashMap<ParameterInfo, PDGParameterInNode>();
		this.parameterOutNodes = new HashMap<ParameterInfo, PDGParameterOutNode>();
		this.fieldInNodes = new HashMap<FieldInfo, PDGFieldInNode>();
		this.fieldOutNodes = new HashMap<FieldInfo, PDGFieldOutNode>();
		this.unit = unit;

		this.buildDataDependence = buildDataDependency;
		this.buildControlDependence = buildControlDependencey;
		this.buildExecutionDependence = buildExecutionDependency;
		this.countObjectStateChange = countObjectStateChange;
		this.optimize = optimize;
		this.dissolve = dissolve;
		this.dataDependencyDistance = dataDependencyDistance;
		this.controlDependencyDistance = controlDependencyDistance;
		this.executionDependencyDistance = executionDependencyDistance;

		this.cfg = new IntraProceduralCFG(unit, cfgNodeFactory, optimize,
				dissolve);

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
			final boolean countObjectStateChange, final boolean optimize,
			final DISSOLUTION dissolve) {

		this(unit, pdgNodeFactory, cfgNodeFactory, buildDataDependency,
				buildControlDependency, buildExecutionDependency,
				countObjectStateChange, optimize, dissolve, Integer.MAX_VALUE,
				Integer.MAX_VALUE, Integer.MAX_VALUE);
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
				buildControlDependency, buildExecutionDependency, false, true,
				DISSOLUTION.FALSE, Integer.MAX_VALUE, Integer.MAX_VALUE,
				Integer.MAX_VALUE);
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
				true, true, true, countObjectStateChange, true,
				DISSOLUTION.FALSE);
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
	 * ���m�[�h���擾
	 * 
	 * @return ���m�[�h
	 */
	public final PDGMethodEnterNode getMethodEnterNode() {
		return this.enterNode;
	}

	/**
	 * �o��m�[�h���擾
	 * 
	 * @return �o��m�[�h
	 */
	public final SortedSet<PDGNode<?>> getExitNodes() {
		return Collections.unmodifiableSortedSet(this.exitNodes);
	}

	public final PDGParameterInNode getParameterNode(
			final ParameterInfo parameter) {
		return this.parameterInNodes.get(parameter);
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
			// ���\�b�h�̃G���^�[�m�[�h���璼�ڂ̓������ɑ΂��Đ���ˑ��ӂ���
			if (this.isBuiltControlDependency()) {
				final PDGMethodEnterNode enterNode = this.getMethodEnterNode();
				this.nodes.add(enterNode);
				final CallableUnitInfo unit = this.getMethodInfo();
				this.buildControlDependence(enterNode, unit);
			}

			// ���\�b�h�̃G���^�[�m�[�h���烁�\�b�h���ōŏ��Ɏ��s����镶�Ɏ��s�ˑ��ӂ���
			if (this.isBuiltExecutionDependency()) {
				if (null != cfgEnterNode) {
					final PDGNode<?> toPDGNode = this.makeNode(cfgEnterNode);
					this.enterNode.addExecutionDependingNode(toPDGNode);
				}
			}
		}

		// unit�̈������
		for (final ParameterInfo parameter : this.unit.getParameters()) {

			// ParameterInNode���쐬
			final PDGParameterInNode parameterInNode = PDGParameterInNode
					.getInstance(parameter);
			this.pdgNodeFactory.addNode(parameterInNode);
			this.nodes.add(parameterInNode);
			this.parameterInNodes.put(parameter, parameterInNode);

			// ParameterOutNode���쐬
			if (parameter.getType() instanceof ReferenceTypeInfo) {
				final PDGParameterOutNode parameterOutNode = PDGParameterOutNode
						.getInstance(parameter);
				this.pdgNodeFactory.addNode(parameterOutNode);
				this.nodes.add(parameterOutNode);
				this.parameterOutNodes.put(parameter, parameterOutNode);
			}

			if (null != cfgEnterNode) {
				this.buildDataDependence(cfgEnterNode, parameterInNode,
						parameter, new HashSet<CFGNode<?>>());
			}
		}

		// �Q�Ƃ���Ă���t�B�[���h�ɂ��ď���
		if (null != cfgEnterNode) {
			final Set<FieldInfo> referencedFields = new HashSet<FieldInfo>();
			for (final VariableUsageInfo<? extends VariableInfo<?>> variableUsage : this.unit
					.getVariableUsages()) {
				if (variableUsage instanceof FieldUsageInfo
						&& variableUsage.isReference()) {
					referencedFields.add((FieldInfo) variableUsage
							.getUsedVariable());
				}
			}
			for (final FieldInfo field : referencedFields) {
				final PDGFieldInNode fieldInNode = PDGFieldInNode.getInstance(
						field, this.unit);
				this.pdgNodeFactory.addNode(fieldInNode);
				this.nodes.add(fieldInNode);
				this.fieldInNodes.put(field, fieldInNode);
			}
			for (final Entry<FieldInfo, PDGFieldInNode> entry : this.fieldInNodes
					.entrySet()) {
				final FieldInfo field = entry.getKey();
				final PDGFieldInNode fieldInNode = entry.getValue();
				this.buildDataDependence(cfgEnterNode, fieldInNode, field,
						new HashSet<CFGNode<?>>());
			}
		}

		// CFG�̓��m�[�h���珈�����s��
		final Set<CFGNode<?>> checkedNodes = new HashSet<CFGNode<?>>();
		if (null != cfgEnterNode) {
			this.buildDependence(cfgEnterNode, checkedNodes);
		}

		// CFG�̏o��m�[�h��PDG�̏o��m�[�h�ɂȂ�
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

		// ParameterOutNode�Ƃ̈ˑ��֌W���\�z
		for (final Entry<ParameterInfo, PDGParameterOutNode> entry : this.parameterOutNodes
				.entrySet()) {
			final ParameterInfo parameter = entry.getKey();
			final PDGParameterOutNode parameterOutNode = entry.getValue();
			for (final CFGNode<? extends ExecutableElementInfo> exitNode : this.cfg
					.getExitNodes()) {
				this.buildOutDataDependence(exitNode, parameterOutNode,
						parameter, new HashSet<CFGNode<?>>());
			}
		}

		// FieldOutNode�Ƃ̈ˑ��֌W���\�z
		{
			final Set<FieldInfo> assignedFields = new HashSet<FieldInfo>();
			for (final VariableUsageInfo<? extends VariableInfo<?>> variableUsage : this.unit
					.getVariableUsages()) {
				if (variableUsage instanceof FieldUsageInfo
						&& variableUsage.isAssignment()) {
					assignedFields.add((FieldInfo) variableUsage
							.getUsedVariable());
				}
			}
			for (final FieldInfo field : assignedFields) {
				final PDGFieldOutNode fieldOutNode = PDGFieldOutNode
						.getInstance(field, this.unit);
				this.pdgNodeFactory.addNode(fieldOutNode);
				this.nodes.add(fieldOutNode);
				this.fieldOutNodes.put(field, fieldOutNode);
			}
			for (final Entry<FieldInfo, PDGFieldOutNode> entry : this.fieldOutNodes
					.entrySet()) {
				final FieldInfo field = entry.getKey();
				final PDGFieldOutNode fieldOutNode = entry.getValue();
				for (final CFGNode<? extends ExecutableElementInfo> exitNode : this.cfg
						.getExitNodes()) {
					this.buildOutDataDependence(exitNode, fieldOutNode, field,
							new HashSet<CFGNode<?>>());
				}
			}
		}
	}

	private void buildDependence(final CFGNode<?> cfgNode,
			final Set<CFGNode<?>> checkedNodes) {

		if (null == cfgNode || null == checkedNodes) {
			throw new IllegalArgumentException();
		}

		// ��ɒ�������Ă���m�[�h�ł���ꍇ�͉������Ȃ�
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
		// ���̕ϐ����Q�Ƃ��Ă���m�[�h��DataDependence����
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

		// �^����ꂽCFG�m�[�h����ControlDependence����
		if (this.isBuiltControlDependency()) {
			if (pdgNode instanceof PDGControlNode) {
				final ConditionInfo condition = (ConditionInfo) cfgNode
						.getCore();
				this.buildControlDependence((PDGControlNode) pdgNode, condition
						.getOwnerConditionalBlock());
			}
		}

		// �^����ꂽCFG�m�[�h����ExecutionDependence����
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
	 * ����ŗ^����ꂽCFG�̃m�[�h�ɑ΂��āC����ŗ^����ꂽPDG�m�[�h����̃f�[�^�ˑ������邩�𒲂ׁC ����ꍇ�́C�f�[�^�ˑ��ӂ���
	 * 
	 * @param cfgNode
	 * @param fromPDGNode
	 * @param variable
	 */
	private void buildDataDependence(final CFGNode<?> cfgNode,
			final PDGNode<? extends CFGNode<?>> fromPDGNode,
			final VariableInfo<?> variable,
			final Set<CFGNode<?>> checkedCFGNodes) {

		if (null == cfgNode || null == fromPDGNode || null == variable
				|| null == checkedCFGNodes) {
			throw new IllegalArgumentException();
		}

		// ��ɒ��ׂĂ���m�[�h�ꍇ�͉������Ȃ��Ń��\�b�h�𔲂���
		if (checkedCFGNodes.contains(cfgNode)) {
			return;
		}

		// �����������ׂ��m�[�h���`�F�b�N�����m�[�h�ɒǉ�
		else {
			checkedCFGNodes.add(cfgNode);
		}

		// cfgNode��variable���Q�Ƃ��Ă���ꍇ�́C
		// cfgNode����PDGNode���쐬���CfromPDGNode����f�[�^�ˑ��ӂ���
		if (cfgNode.getReferencedVariables().contains(variable)) {

			final PDGNode<? extends CFGNode<?>> toPDGNode = this
					.makeNode(cfgNode);

			// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W����
			final int distance = Math.abs(toPDGNode.getCore().getFromLine()
					- fromPDGNode.getCore().getToLine()) + 1;
			if (distance <= this.dataDependencyDistance) {
				fromPDGNode.addDataDependingNode(toPDGNode, variable);
			}
		}

		// cfgNode��variable�ɑ��Ă���ꍇ�́C
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
	 * ����ŗ^����ꂽ�m�[�h�ɑ΂��āC����ŗ^����ꂽblock�Ɋ܂܂�镶�ɐ���ˑ��ӂ���
	 * 
	 * @param fromPDGNode
	 * @param block
	 */
	private void buildControlDependence(final PDGControlNode fromPDGNode,
			final LocalSpaceInfo block) {

		for (final StatementInfo innerStatement : block.getStatements()) {

			// �P���̏ꍇ�́CfromPDGNode����̐���ˑ��ӂ���
			// CaseEntry�̏ꍇ�́C����ˑ��ӂ͂���Ȃ�
			// Break���̏ꍇ�CContinue���̏ꍇ������ˑ��ӂ͂���Ȃ�
			if (innerStatement instanceof SingleStatementInfo
					&& !(innerStatement instanceof BreakStatementInfo)
					&& !(innerStatement instanceof ContinueStatementInfo)) {

				final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
				final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
						.getDissolvedNodes(innerStatement);

				// innerStatement��������CFG�m�[�h�ɕ�������Ă����ꍇ
				if (null != dissolvedCFGNodes) {
					for (final CFGNode<? extends ExecutableElementInfo> dissolvedCFGNode : dissolvedCFGNodes) {

						// �������ꂽ�m�[�h��CFG�Ɋ܂܂�Ă��Ȃ��Ƃ��i�m�[�h�W��ɂ��Ȃ��Ȃ����Ƃ��j�͂Ȃɂ����Ȃ�
						if (!this.cfg.getAllNodes().contains(dissolvedCFGNode)) {
							continue;
						}

						final ExecutableElementInfo core = dissolvedCFGNode
								.getCore();
						// �������ꂽSingleStatementInfo��outerUnit��block�łȂ��ꍇ�́C
						// ����ˑ��ӂ͈�Ȃ�
						if (core instanceof SingleStatementInfo) {
							if (!core.getOwnerSpace().equals(block)) {
								continue;
							}
						}
						final PDGNode<?> toPDGNode = this
								.makeNode(dissolvedCFGNode);
						toPDGNodes.add(toPDGNode);
					}
				}

				// ��������Ă��Ȃ��ꍇ
				else {
					final CFGNode<?> cfgNode = this.cfgNodeFactory
							.getNode(innerStatement);
					if (null != cfgNode) {
						final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
						toPDGNodes.add(toPDGNode);
					}
				}

				// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W����
				for (final PDGNode<?> toPDGNode : toPDGNodes) {
					final int distance = Math.abs(toPDGNode.getCore()
							.getFromLine()
							- fromPDGNode.getCore().getToLine()) + 1;
					if (distance <= this.controlDependencyDistance) {
						fromPDGNode.addControlDependingNode(toPDGNode,
								!(block instanceof ElseBlockInfo));
					}
				}
			}

			// Block���̏ꍇ�́C�����t�����ł���΁C�P���̎��Ɠ�������
			// �����łȂ���΁C����ɓ����𒲂ׂ�
			else if (innerStatement instanceof BlockInfo) {

				if (innerStatement instanceof ConditionalBlockInfo) {

					// fromPDGNode����ConditionalBlockInfo�̏������ɃG�b�W����
					{
						final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
								.getConditionalClause().getCondition();

						final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
						final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
								.getDissolvedNodes(condition);

						// innerStatement��������CFG�m�[�h�ɕ�������Ă����ꍇ
						if (null != dissolvedCFGNodes) {
							for (final CFGNode<? extends ExecutableElementInfo> dissolvedCFGNode : dissolvedCFGNodes) {

								// �������ꂽ�m�[�h��CFG�Ɋ܂܂�Ă��Ȃ��Ƃ��i�m�[�h�W��ɂ��Ȃ��Ȃ����Ƃ��j�͂Ȃɂ����Ȃ�
								if (!this.cfg.getAllNodes().contains(
										dissolvedCFGNode)) {
									continue;
								}

								final ExecutableElementInfo core = dissolvedCFGNode
										.getCore();
								// �������ꂽSingleStatementInfo��outerUnit��block�łȂ��ꍇ�́C
								// ����ˑ��ӂ͈�Ȃ�
								if (core instanceof SingleStatementInfo) {
									if (!core.getOwnerSpace().equals(block)) {
										continue;
									}
								}
								final PDGNode<?> toPDGNode = this
										.makeNode(dissolvedCFGNode);
								toPDGNodes.add(toPDGNode);
							}
						}

						// ��������Ă��Ȃ��ꍇ
						else {
							final CFGNode<?> cfgNode = this.cfgNodeFactory
									.getNode(condition);
							if (null != cfgNode) {
								final PDGNode<?> toPDGNode = this
										.makeNode(cfgNode);
								toPDGNodes.add(toPDGNode);
							}
						}

						// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W����
						for (final PDGNode<?> toPDGNode : toPDGNodes) {
							final int distance = Math.abs(toPDGNode.getCore()
									.getFromLine()
									- fromPDGNode.getCore().getToLine()) + 1;
							if (distance <= this.controlDependencyDistance) {
								fromPDGNode.addControlDependingNode(toPDGNode,
										!(block instanceof ElseBlockInfo));
							}
						}
					}

					if (innerStatement instanceof ForBlockInfo) {
						final ForBlockInfo forBlock = (ForBlockInfo) innerStatement;
						for (final ConditionInfo expression : forBlock
								.getInitializerExpressions()) {

							final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
							final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
									.getDissolvedNodes(expression);

							// innerStatement��������CFG�m�[�h�ɕ�������Ă����ꍇ
							if (null != dissolvedCFGNodes) {
								for (final CFGNode<? extends ExecutableElementInfo> dissolvedCFGNode : dissolvedCFGNodes) {

									// �������ꂽ�m�[�h��CFG�Ɋ܂܂�Ă��Ȃ��Ƃ��i�m�[�h�W��ɂ��Ȃ��Ȃ����Ƃ��j�͂Ȃɂ����Ȃ�
									if (!this.cfg.getAllNodes().contains(
											dissolvedCFGNode)) {
										continue;
									}

									final ExecutableElementInfo core = dissolvedCFGNode
											.getCore();
									// �������ꂽSingleStatementInfo��outerUnit��block�łȂ��ꍇ�́C
									// ����ˑ��ӂ͈�Ȃ�
									if (core instanceof SingleStatementInfo) {
										if (!core.getOwnerSpace().equals(block)) {
											continue;
										}
									}
									final PDGNode<?> toPDGNode = this
											.makeNode(dissolvedCFGNode);
									toPDGNodes.add(toPDGNode);
								}
							}

							// ��������Ă��Ȃ��ꍇ
							else {
								final CFGNode<?> cfgNode = this.cfgNodeFactory
										.getNode(expression);
								if (null != cfgNode) {
									final PDGNode<?> toPDGNode = this
											.makeNode(cfgNode);
									toPDGNodes.add(toPDGNode);
								}
							}

							// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W����
							for (final PDGNode<?> toPDGNode : toPDGNodes) {
								final int distance = Math.abs(toPDGNode
										.getCore().getFromLine()
										- fromPDGNode.getCore().getToLine()) + 1;
								if (distance <= this.controlDependencyDistance) {
									fromPDGNode.addControlDependingNode(
											toPDGNode,
											!(block instanceof ElseBlockInfo));
								}
							}
						}
					}
				}

				// else�u���b�N�̏ꍇ�͂����ł́C�ˑ��ӂ͈�Ȃ�
				else if (innerStatement instanceof ElseBlockInfo) {

				}

				// ConditionalBlockInfo�łȂ��ꍇ�́C�P���Ɉ����D
				else {
					this.buildControlDependence(fromPDGNode,
							(BlockInfo) innerStatement);

					// try���̏ꍇ�́Ccatch��, finally���ɂ��ˑ��ӂ���
					if (innerStatement instanceof TryBlockInfo) {
						final SortedSet<CatchBlockInfo> catchBlocks = ((TryBlockInfo) innerStatement)
								.getSequentCatchBlocks();
						for (final CatchBlockInfo catchBlock : catchBlocks) {
							this
									.buildControlDependence(fromPDGNode,
											catchBlock);
						}

						final FinallyBlockInfo finallyBlock = ((TryBlockInfo) innerStatement)
								.getSequentFinallyBlock();
						if (null != finallyBlock) {
							this.buildControlDependence(fromPDGNode,
									finallyBlock);
						}
					}
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
			for (final ExpressionInfo expression : forBlock
					.getIteratorExpressions()) {

				final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
				final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
						.getDissolvedNodes(expression);

				// innerStatement��������CFG�m�[�h�ɕ�������Ă����ꍇ
				if (null != dissolvedCFGNodes) {
					for (final CFGNode<? extends ExecutableElementInfo> dissolvedCFGNode : dissolvedCFGNodes) {

						// �������ꂽ�m�[�h��CFG�Ɋ܂܂�Ă��Ȃ��Ƃ��i�m�[�h�W��ɂ��Ȃ��Ȃ����Ƃ��j�͂Ȃɂ����Ȃ�
						if (!this.cfg.getAllNodes().contains(dissolvedCFGNode)) {
							continue;
						}

						final ExecutableElementInfo core = dissolvedCFGNode
								.getCore();
						// �������ꂽSingleStatementInfo��outerUnit��block�łȂ��ꍇ�́C
						// ����ˑ��ӂ͈�Ȃ�
						if (core instanceof SingleStatementInfo) {
							if (!core.getOwnerSpace().equals(block)) {
								continue;
							}
						}
						final PDGNode<?> toPDGNode = this
								.makeNode(dissolvedCFGNode);
						toPDGNodes.add(toPDGNode);
					}
				}

				// ��������Ă��Ȃ��ꍇ
				else {
					final CFGNode<?> cfgNode = this.cfgNodeFactory
							.getNode(expression);
					if (null != cfgNode) {
						final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
						toPDGNodes.add(toPDGNode);
					}
				}

				// from�m�[�h��to�m�[�h�̋�����臒l�ȓ��ł���΃G�b�W����
				for (final PDGNode<?> toPDGNode : toPDGNodes) {
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
	}

	private void buildOutDataDependence(final CFGNode<?> cfgNode,
			final PDGNode<? extends CFGNode<?>> toPDGNode,
			final VariableInfo<?> variable,
			final Set<CFGNode<?>> checkedCFGNodes) {

		if (null == cfgNode || null == toPDGNode || null == variable
				|| null == checkedCFGNodes) {
			throw new IllegalArgumentException();
		}

		// ��ɒ��ׂĂ���m�[�h�ꍇ�͉������Ȃ��Ń��\�b�h�𔲂���
		if (checkedCFGNodes.contains(cfgNode)) {
			return;
		}

		// �����������ׂ��m�[�h���`�F�b�N�����m�[�h�ɒǉ�
		else {
			checkedCFGNodes.add(cfgNode);
		}

		// cfgNode��variable���`���Ă���̂ł���΁C�f�[�^�ˑ��ӂ���
		if (cfgNode.getDefinedVariables(this.countObjectStateChange).contains(
				variable)) {
			final PDGNode<? extends CFGNode<?>> fromPDGNode = this
					.makeNode(cfgNode);
			fromPDGNode.addDataDependingNode(toPDGNode, variable);
			return;
		}

		// cfgNode��variable���`���Ă��Ȃ��ꍇ�C���̃o�b�N���[�h�m�[�h�ɑ΂��čċA�I�ɏ���
		else {

			for (final CFGNode<?> backwardNode : cfgNode.getBackwardNodes()) {
				this.buildOutDataDependence(backwardNode, toPDGNode, variable,
						checkedCFGNodes);
			}

			// �o�b�N���[�h�m�[�h���Ȃ��ꍇ�́CParameterInNode����ˑ��ӂ���K�v������\������
			// if (0 == cfgNode.getBackwardNodes().size()) {
			// final PDGParameterInNode parameterInNode = this.parameterInNodes
			// .get(variable);
			// if (null != parameterInNode) {
			// parameterInNode.addDataDependingNode(toPDGNode, variable);
			// }
			// }
		}
	}

	private PDGNode<?> makeNode(final CFGNode<?> cfgNode) {

		if (null == cfgNode) {
			throw new IllegalArgumentException();
		}

		if (cfgNode instanceof CFGControlNode) {
			return this.makeControlNode((CFGControlNode) cfgNode);
		} else if (cfgNode instanceof CFGNormalNode<?>) {
			return this.makeNormalNode((CFGNormalNode<?>) cfgNode);
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * ��ŗ^����ꂽCFG�m�[�h����PDG�m�[�h���쐬����
	 * 
	 * @param cfgNode
	 * @return
	 */
	private PDGControlNode makeControlNode(final CFGControlNode cfgNode) {

		final IPDGNodeFactory factory = this.getNodeFactory();
		final PDGControlNode node = factory.makeControlNode(cfgNode);
		if (null == node) {
			return null;
		}

		this.nodes.add(node);
		return node;
	}

	/**
	 * ��ŗ^����ꂽCFG�m�[�h����PDG�m�[�h���쐬����
	 * 
	 * @param cfgNode
	 * @return
	 */
	private PDGNormalNode<?> makeNormalNode(final CFGNormalNode<?> cfgNode) {

		final IPDGNodeFactory factory = this.getNodeFactory();
		final PDGNormalNode<?> node = factory.makeNormalNode(cfgNode);
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
