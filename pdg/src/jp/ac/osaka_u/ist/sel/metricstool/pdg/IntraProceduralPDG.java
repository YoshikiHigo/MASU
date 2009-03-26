package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNormalNode;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * �葱����PDG��\���N���X
 * 
 * @author t-miyake, higo
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

            final PDGNode<?> pdgNode = this.makeNormalNode(parameter);
            if (null != enterNode) {
                this.buildDataDependence(enterNode, pdgNode, parameter, new HashSet<CFGNode<?>>());
            }
        }

        // CFG�̓����m�[�h���珈�����s��
        if (null != enterNode) {
            this.buildDependence(enterNode, new HashSet<CFGNode<?>>());
        }
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
        final PDGNode<?> pdgNode = this.makeNode(cfgNode);

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
            final ConditionInfo condition = (ConditionInfo) cfgNode.getCore();
            this.buildControlDependence((PDGControlNode) pdgNode, Utility
                    .getOwnerConditionalBlock(condition));
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

            final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
            fromPDGNode.addDataDependingNode(toPDGNode);
        }

        // cfgNode��variable�ɑ�����Ă���ꍇ�́C
        // ����ȍ~�̃m�[�h�̃f�[�^�ˑ��͒��ׂȂ�
        if (cfgNode.getDefinedVariables().contains(variable)) {
            return;
        }

        // cfgNode�̃t�H���[�h�m�[�h�ɑ΂��Ă��f�[�^�ˑ��𒲂ׂ�
        else {
            for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
                this.buildDataDependence(forwardNode, fromPDGNode, variable, checkedCFGNodes);
            }
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
                final PDGNode<?> toPDGNode = this.makeNormalNode(innerStatement);
                fromPDGNode.addControlDependingNode(toPDGNode);
            }

            // Block���̏ꍇ�́C�����t�����ł���΁C�P���̎��Ɠ�������
            //�@�����łȂ���΁C����ɓ����𒲂ׂ�
            else if (innerStatement instanceof BlockInfo) {

                if (innerStatement instanceof ConditionalBlockInfo) {

                    {
                        final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
                                .getConditionalClause().getCondition();
                        final PDGNode<?> toPDGNode = this.makeControlNode(condition);
                        fromPDGNode.addControlDependingNode(toPDGNode);
                    }

                    if (innerStatement instanceof ForBlockInfo) {
                        final ForBlockInfo forBlock = (ForBlockInfo) innerStatement;
                        for (final ConditionInfo expression : forBlock.getInitializerExpressions()) {
                            final PDGNode<?> toPDGNode = this.makeNormalNode(expression);
                            fromPDGNode.addControlDependingNode(toPDGNode);
                        }
                    }
                }

                // else�u���b�N�̏ꍇ�͂����ł́C�ˑ��ӂ͈����Ȃ�
                else if (innerStatement instanceof ElseBlockInfo) {

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

        // for���̌J��Ԃ����ւ̑Ή������Ȃ���΂Ȃ�Ȃ�
        if (block instanceof ForBlockInfo) {

            final ForBlockInfo forBlock = (ForBlockInfo) block;
            final ConditionInfo condition = forBlock.getConditionalClause().getCondition();
            final PDGControlNode extraFromPDGNode = this.makeControlNode(condition);

            for (final ExpressionInfo expression : forBlock.getIteratorExpressions()) {
                final PDGNode<?> extraToPDGNode = this.makeNormalNode(expression);
                extraFromPDGNode.addControlDependingNode(extraToPDGNode);
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
     * @param element �m�[�h���쐬�������v�f
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
     * @param element �m�[�h���쐬�������v�f
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
     * @return
     */
    public IntraProceduralCFG getCFG() {
        return this.cfg;
    }

    public CallableUnitInfo getMethodInfo() {
        return this.unit;
    }
}
