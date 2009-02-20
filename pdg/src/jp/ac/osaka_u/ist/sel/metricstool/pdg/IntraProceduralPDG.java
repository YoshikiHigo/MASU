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
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
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

    @Override
    protected void buildPDG() {
        if (null == this.getCFG().getEnterNode()) {
            return;
        }

        for (final ParameterInfo parameter : this.unit.getParameters()) {
            final ParameterNode parameterNode = new ParameterNode(parameter);
            this.enterNodes.add(parameterNode);
            this.buildDataDependence(parameterNode, parameter, this.cfg.getEnterNode(),
                    new HashSet<CFGControlNode>());
        }

        for (final StatementInfo statement : LocalSpaceInfo.getAllStatements(this.unit)) {

            // statement�Ƒ��̕��Ƃ̃f�[�^�ˑ����\�z
            {
                final PDGNode<?> pdgNode = this.getStatementNode(statement);
                if (null == pdgNode) {
                    continue;
                }

                final CFGNode<? extends StatementInfo> cfgNode = this.cfg.getCFGNode(statement);

                // ����pdgNode�ɑΉ����镶�Œ�`����Ă���S�ϐ��Ɋւ��ăf�[�^�ˑ��ӂ��\�z
                for (final VariableInfo<? extends UnitInfo> definedVariable : pdgNode
                        .getDefinedVariables()) {

                    // cfgNode����h�����邷�ׂĂ̌o�H�̃f�[�^�ˑ��ӂ��\�z
                    for (final CFGNode<? extends StatementInfo> forwardCFGNode : cfgNode
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
            final CFGNode<? extends StatementInfo> dependCandidates,
            final Set<CFGControlNode> passedNodeCache) {

        final PDGNode<?> firstCandidate = this.getStatementNode(dependCandidates.getStatement());

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
        for (final CFGNode<? extends StatementInfo> nextCandidate : dependCandidates
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
        final PDGControlNode controlNode = (PDGControlNode) this.getStatementNode(controlStatement);
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

                final PDGNode<?> controlledNode = this.getStatementNode(controlledStatement);

                assert null != controlledNode;

                controlNode.addControlDependingNode(controlledNode);

            } else if (controlledStatement instanceof BlockInfo) {
                // ���䕶�ȊO�̃u���b�N���̏ꍇ�C�����̕��𐧌䂳��镶�ɒǉ�
                this.buildControlFlow(controlNode, ((BlockInfo) controlledStatement)
                        .getStatements());
            }
        }
    }

    protected PDGNode<?> getStatementNode(final StatementInfo statement) {
        final PDGNode<?> node = this.nodeFactory.makeNode(statement);
        if (statement instanceof ReturnStatementInfo) {
            this.exitNodes.add(node);
        }
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

    public final ParameterNode getParamerNode(final ParameterInfo parameter) {
        for (final PDGNode<?> startNode : this.enterNodes) {
            if (startNode instanceof ParameterNode) {
                ParameterNode parameterNode = (ParameterNode) startNode;
                if (parameterNode.getCore().equals(parameter)) {
                    return parameterNode;
                }
            }
        }

        return null;
    }

}
