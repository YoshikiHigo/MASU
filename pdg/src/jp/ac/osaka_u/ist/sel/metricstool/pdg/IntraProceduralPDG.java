package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.IntraProceduralCFG;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * �葱����PDG��\���N���X
 * 
 * @author t-miyake
 *
 */
public class IntraProceduralPDG extends PDG {

    private final CallableUnitInfo unit;

    /**
     * PDG���\�z���ɗ��p����CFG
     */
    private final IntraProceduralCFG cfg;

    public IntraProceduralPDG(final CallableUnitInfo unit, final IPDGNodeFactory pdgNodeFactory,
            final ICFGNodeFactory cfgNodeFactory) {
        super(pdgNodeFactory);
        if (null == unit) {
            throw new IllegalArgumentException("method is null.");
        }

        this.unit = unit;

        this.cfg = new IntraProceduralCFG(unit, cfgNodeFactory);

        this.buildPDG();
    }

    public IntraProceduralPDG(final CallableUnitInfo unit) {
        this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory());
    }

    /**
     * �R���X�g���N�^�ŗ^����ꂽCallableUnitInfo��PDG���\�z����
     */
    @Override
    protected void buildPDG() {

        final CFGNode<?> enterNode = this.cfg.getEnterNode();

        // unit�̈���������
        for (final ParameterInfo parameter : this.unit.getParameters()) {

            final PDGNode<?> pdgNode = this.makeNode(parameter);
            if (null != enterNode) {
                this.buildDataDependence(enterNode, pdgNode, parameter, new HashSet<CFGNode<?>>());
            }
        }

        // CFG�̓����m�[�h���珈�����s��
        if (null != enterNode) {
            this.buildDependence(enterNode, new HashSet<CFGNode<?>>());
        }

        // CFG�̐擪���珇�ɂ��ǂ�Ȃ���CPDG���\�z����

        /*
        if (null == this.getCFG().getEnterNode()) {
            return;
        }

        for (final ParameterInfo parameter : this.unit.getParameters()) {
            final PDGParameterNode parameterNode = (PDGParameterNode) this.getNodeFactory()
                    .makeNode(parameter);
            this.enterNodes.add(parameterNode);
            this.nodes.add(parameterNode);
            this.buildDataDependence(parameterNode, parameter, this.cfg.getEnterNode(),
                    new HashSet<CFGControlNode>());
        }

        for (final StatementInfo statement : LocalSpaceInfo.getAllStatements(this.unit)) {

            // statement�Ƒ��̕��Ƃ̃f�[�^�ˑ����\�z
            {

                final PDGNode<?> pdgNode = this.makeNode(statement);
                if (null == pdgNode) {
                    continue;
                }

                // Return���Ȃ�o���m�[�h�ɒǉ�
                if (statement instanceof ReturnStatementInfo) {
                    this.exitNodes.add(pdgNode);
                }

                final CFGNode<? extends ExecutableElementInfo> cfgNode = this.cfg
                        .getCFGNode(statement);

                // ����pdgNode�ɑΉ����镶�Œ�`����Ă���S�ϐ��Ɋւ��ăf�[�^�ˑ��ӂ��\�z
                for (final VariableInfo<? extends UnitInfo> definedVariable : pdgNode
                        .getDefinedVariables()) {

                    // cfgNode����h�����邷�ׂĂ̌o�H�̃f�[�^�ˑ��ӂ��\�z
                    for (final CFGNode<? extends ExecutableElementInfo> forwardCFGNode : cfgNode
                            .getForwardNodes()) {
                        this.buildDataDependence(pdgNode, definedVariable, forwardCFGNode,
                                new HashSet<CFGControlNode>());
                    }
                }
            }

            if (statement instanceof ConditionalBlockInfo) {
                // statement�����䕶�̏ꍇ�͐���t���[���\�z
                this.buildControlFlow((ConditionalBlockInfo) statement);
            }
        }
        */
    }

    private void buildDependence(final CFGNode<?> cfgNode, final Set<CFGNode<?>> checkedNodes) {

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

        //�^����ꂽCFG�m�[�h�ɑΉ�����PDG�m�[�h���쐬
        final ExecutableElementInfo element = cfgNode.getCore();
        final PDGNode<?> pdgNode = this.makeNode(element);

        //�^����ꂽCFG�m�[�h�Œ�`���ꂽ�e�ϐ��ɑ΂��āC
        //���̕ϐ����Q�Ƃ��Ă���m�[�h��DataDependence������
        for (final VariableInfo<? extends UnitInfo> variable : cfgNode.getDefinedVariables()) {

            for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
                final Set<CFGNode<?>> checkedNodesForDefinedVariables = new HashSet<CFGNode<?>>();
                checkedNodesForDefinedVariables.add(cfgNode);
                this.buildDataDependence(forwardNode, pdgNode, variable,
                        checkedNodesForDefinedVariables);
            }
        }

        //�^����ꂽCFG�m�[�h����ControlDependence������
        if (pdgNode instanceof PDGControlNode) {
            this.buildControlDependence((PDGControlNode) pdgNode, (BlockInfo) element);
        }

        for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
            this.buildDependence(forwardNode, checkedNodes);
        }
    }

    /**
     * �������ŗ^����ꂽCFG�̃m�[�h�ɑ΂��āC�������ŗ^����ꂽPDG�m�[�h����̃f�[�^�ˑ������邩�𒲂ׁC
     * ����ꍇ�́C�f�[�^�ˑ��ӂ�����
     * 
     * @param cfgNode
     * @param fromPDGNode
     * @param variable
     */
    private void buildDataDependence(final CFGNode<?> cfgNode, final PDGNode<?> fromPDGNode,
            final VariableInfo<?> variable, final Set<CFGNode<?>> checkedCFGNodes) {

        if (null == cfgNode || null == fromPDGNode || null == variable || null == checkedCFGNodes) {
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

            final ExecutableElementInfo element = cfgNode.getCore();
            final PDGNode<?> toPDGNode = this.makeNode(element);
            fromPDGNode.addDataDependingNode(toPDGNode);
        }

        // cfgNode��variable�ɑ�����Ă���ꍇ�́C
        // ����ȍ~�̃m�[�h�̃f�[�^�ˑ��͒��ׂȂ�
        if (cfgNode.getDefinedVariables().contains(variable)) {
            return;
        }

        // cfgNode�̃t�H���[�h�m�[�h�ɑ΂��Ă��f�[�^�ˑ��𒲂ׂ�
        for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
            this.buildDataDependence(forwardNode, fromPDGNode, variable, checkedCFGNodes);
        }
    }

    /**
     * �������ŗ^����ꂽ�m�[�h�ɑ΂��āC�������ŗ^����ꂽblock�Ɋ܂܂�镶�ɐ���ˑ��ӂ�����
     * 
     * @param fromPDGNode
     * @param block
     */
    private void buildControlDependence(final PDGControlNode fromPDGNode, final BlockInfo block) {

        for (final StatementInfo innerStatement : block.getStatements()) {

            // �P����P�[�X�G���g���̏ꍇ�́CfromPDGNode����̐���ˑ��ӂ�����
            if (innerStatement instanceof SingleStatementInfo
                    || innerStatement instanceof CaseEntryInfo) {
                final PDGNode<?> toPDGNode = this.makeNode(innerStatement);
                fromPDGNode.addControlDependingNode(toPDGNode);
            }

            // Block���̏ꍇ�́C�����t�����ł���΁C�P���̎��Ɠ�������
            //�@�����łȂ���΁C����ɓ����𒲂ׂ�
            else if (innerStatement instanceof BlockInfo) {

                if (innerStatement instanceof ConditionalBlockInfo) {

                    final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
                            .getConditionalClause().getCondition();
                    final PDGNode<?> toPDGNode = this.makeNode(condition);
                    fromPDGNode.addControlDependingNode(toPDGNode);
                }

                // else�u���b�N�̏ꍇ�͂����ł́C�ˑ��ӂ͈����Ȃ�
                else if (block instanceof ElseBlockInfo) {

                }

                else {
                    this.buildControlDependence(fromPDGNode, (BlockInfo) innerStatement);
                }
            }
        }

        // if���̏ꍇ�́Celse�ւ̑Ή������Ȃ���΂Ȃ�Ȃ�
        if (block instanceof IfBlockInfo) {
            final ElseBlockInfo elseBlock = ((IfBlockInfo) block).getSequentElseBlock();
            if (null != elseBlock) {
                this.buildControlDependence(fromPDGNode, elseBlock);
            }
        }

    }

    /**
     * ��`�m�[�h�ƃf�[�^�ˑ����m�[�h���f�[�^�ˑ��֌W�ɂ���ꍇ�C�f�[�^�ˑ��ӂ��\�z
     * @param definitionNode ��`�m�[�h
     * @param definedVariable ��`�m�[�h�Œ�`����Ă���ϐ��̂����C�\�z����f�[�^�ˑ��ӂɊ֌W����ϐ�
     * @param dependCandidates �f�[�^�ˑ����m�[�h
     * @param passedNodeCache �����ς݂�CFG����m�[�h�̃L���b�V��
     */
    private void buildDataDependence(final PDGNode<?> definitionNode,
            final VariableInfo<? extends UnitInfo> definedVariable,
            final CFGNode<? extends ExecutableElementInfo> dependCandidates,
            final Set<CFGControlNode> passedNodeCache) {

        final PDGNode<?> firstCandidate = this.makeNode(dependCandidates.getCore());

        // ���m�[�h�����݂���ꍇ�C�ŏ��̌��m�[�h�ւ̃f�[�^�ˑ��𒲍�
        if (null != firstCandidate) {

            // ���m�[�h����`�ϐ����Q�Ƃ��Ă���ꍇ�C�f�[�^�ˑ��ς��\�z
            if (firstCandidate.isReferenace(definedVariable)) {
                boolean aleadyAdded = !definitionNode.addDataDependingNode(firstCandidate);
                if (aleadyAdded) {
                    return;
                }
            }

            // ���m�[�h��Œ�`�ϐ����Ē�`����Ă���ꍇ�C
            // �ȍ~�̌o�H�Ō��݂̒�`�m�[�h����̃f�[�^�ˑ��͑��݂��Ȃ��̂ŏI��
            if (firstCandidate.isDefine(definedVariable)) {
                return;
            }
        }

        // �ŏ��̌��m�[�h����h������S�m�[�h�ɑ΂��ăf�[�^�ˑ��𒲍�
        for (final CFGNode<? extends ExecutableElementInfo> nextCandidate : dependCandidates
                .getForwardNodes()) {

            if (nextCandidate instanceof CFGControlNode) {
                if (!passedNodeCache.add((CFGControlNode) nextCandidate)) {
                    continue;
                }
            }

            this.buildDataDependence(definitionNode, definedVariable, nextCandidate,
                    passedNodeCache);
        }
    }

    /**
     * �����ŗ^����ꂽ���䕶����̐���ˑ��ӂ��\�z
     * @param controlStatement
     */
    private void buildControlFlow(final ConditionalBlockInfo controlStatement) {
        final PDGControlNode controlNode = (PDGControlNode) this.makeNode(controlStatement);
        this.buildControlFlow(controlNode, controlStatement.getStatements());

        if (controlStatement instanceof IfBlockInfo) {
            IfBlockInfo ifBlock = (IfBlockInfo) controlStatement;

            if (ifBlock.hasElseBlock()) {
                this.buildControlFlow(controlNode, ifBlock.getSequentElseBlock().getStatements());
            }
        }
    }

    /**
     * ����m�[�h�����2�����ŗ^����ꂽ���ɑ΂��鐧��ˑ��ӂ��\�z
     * @param controlNode ����m�[�h
     * @param controlledStatements ��1�����̐���m�[�h�Ɉˑ����Ă��镶�̏W��
     */
    private void buildControlFlow(final PDGControlNode controlNode,
            final SortedSet<StatementInfo> controlledStatements) {

        for (final StatementInfo controlledStatement : controlledStatements) {

            if (controlledStatement instanceof SingleStatementInfo
                    || controlledStatement instanceof ConditionalBlockInfo) {
                // �P���␧�䕶�̏ꍇ�C���ꎩ�̂𐧌䂳��镶�Ƃ��Ēǉ�

                final PDGNode<?> controlledNode = this.makeNode(controlledStatement);

                assert null != controlledNode;

                controlNode.addControlDependingNode(controlledNode);

            } else if (controlledStatement instanceof BlockInfo) {
                // ���䕶�ȊO�̃u���b�N���̏ꍇ�C�����̕��𐧌䂳��镶�ɒǉ�
                this.buildControlFlow(controlNode, ((BlockInfo) controlledStatement)
                        .getStatements());
            }
        }
    }

    /**
     * �����ŗ^����ꂽ�v�f��PDG�m�[�h���쐬����
     * 
     * @param element �m�[�h���쐬�������v�f
     * @return
     */
    private PDGNode<?> makeNode(final Object element) {

        final IPDGNodeFactory factory = this.getNodeFactory();
        final PDGNode<?> node = factory.makeNode(element);
        if (null == node) {
            return null;
        }

        this.nodes.add(node);
        return node;
    }

    /**
     * PDG�̍\�z�ɗ��p����CFG���擾
     * @return
     */
    public IntraProceduralCFG getCFG() {
        return this.cfg;
    }

    public CallableUnitInfo getMethodInfo() {
        return this.unit;
    }
}
