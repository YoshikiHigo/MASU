package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;


/**
 * �葱���Ԃ����f���Ȃ�����t���[�O���t(CFG)��\���N���X
 * 
 * @author t-miyake
 * 
 */
public class IntraProceduralCFG extends CFG {

    /**
     * ����t���[�O���t�ɑΉ����郍�[�J�����
     */
    private final LocalSpaceInfo localSpace;

    /**
     * �������鐧��t���[�O���t�ɑΉ����郍�[�J����Ԃ�CFG�m�[�h�̃t�@�N�g����^���ď�����
     * 
     * @param unit
     *            �������鐧��t���[�O���t�ɑΉ����郍�[�J�����
     * @param nodeFactory
     *            CFG�m�[�h�̃t�@�N�g��
     */
    public IntraProceduralCFG(final CallableUnitInfo unit, final ICFGNodeFactory nodeFactory) {

        super(nodeFactory);

        if (null == unit) {
            throw new IllegalArgumentException("unit is null");
        }

        if (unit instanceof ExternalMethodInfo || unit instanceof ExternalConstructorInfo) {
            throw new IllegalArgumentException("unit is an external infromation.");
        }

        this.localSpace = unit;

        this.buildCFG(unit);
    }

    /**
     * �������鐧��t���[�O���t�ɑΉ����郍�[�J����Ԃ�^���ď�����
     * 
     * @param unit
     *            �������鐧��t���[�O���t�ɑΉ����郍�[�J�����
     */
    public IntraProceduralCFG(final CallableUnitInfo unit) {
        this(unit, new DefaultCFGNodeFactory());
    }

    private IntraProceduralCFG(final ICFGNodeFactory nodeFactory) {
        super(nodeFactory);

        this.localSpace = null;
    }

    private IntraProceduralCFG(final StatementInfo statement, final ICFGNodeFactory nodeFactory) {
        super(nodeFactory);
        if (statement instanceof BlockInfo) {
            this.localSpace = (BlockInfo) statement;

            this.buildCFG(this.localSpace);
        } else {
            this.localSpace = null;
            this.enterNode = this.nodeFactory.makeNode(statement);
            this.exitNodes.add(this.enterNode);
        }
    }

    /**
     * CFG���\�z
     * 
     * @param local
     *            �\�z�����CFG�ɑΉ����郍�[�J�����
     */
    private void buildCFG(final LocalSpaceInfo local) {

        this.enterNode = null;
        this.exitNodes.clear();

        CFG innerCFG = this.buildInnerCFG(local);

        // �Ή����郍�[�J����Ԃ����䕶�łȂ��ꍇ
        if (!(local instanceof ConditionalBlockInfo)) {
            // ����CFG�����̂܂�CFG�ƂȂ�
            this.enterNode = innerCFG.getEnterNode();
            this.exitNodes.addAll(innerCFG.getExitNodes());

            return;
        }

        // �Ή����郍�[�J����Ԃ�if���̏ꍇ
        if (local instanceof IfBlockInfo) {
            final IfBlockInfo ifBlock = (IfBlockInfo) local;
            this.enterNode = this.nodeFactory.makeNode(ifBlock);

            if (!innerCFG.isEmpty()) {
                this.enterNode.addForwardNode(innerCFG.getEnterNode());
            } else {
                this.exitNodes.add(this.enterNode);
            }

            this.exitNodes.addAll(innerCFG.getExitNodes());

            if (ifBlock.hasElseBlock()) {
                final CFG elseCFG = new IntraProceduralCFG(ifBlock.getSequentElseBlock(),
                        this.nodeFactory);
                if (!elseCFG.isEmpty()) {
                    this.enterNode.addForwardNode(elseCFG.getEnterNode());
                    this.exitNodes.addAll(elseCFG.getExitNodes());
                } else {
                    this.exitNodes.add(this.enterNode);
                }
            } else {
                this.exitNodes.add(this.enterNode);
            }

        }
        // �Ή����郍�[�J����Ԃ�while���̏ꍇ
        else if (local instanceof WhileBlockInfo) {
            // ���䕶���̂������m�[�h���o���m�[�h
            this.enterNode = this.nodeFactory.makeNode((ConditionalBlockInfo) local);
            this.exitNodes.add(this.enterNode);

            // ������CFG�ƘA���D���[�v���Ȃ̂œ�����CFG����̏ꍇ�͓����m�[�h�̃t�H���[�h�m�[�h�͓����m�[�h
            this.enterNode.addForwardNode(!innerCFG.isEmpty() ? innerCFG.getEnterNode()
                    : this.enterNode);

            for (final CFGNode<? extends ExecutableElementInfo> innerExitNode : innerCFG
                    .getExitNodes()) {
                if (innerExitNode.isExitNode(this.localSpace)) {
                    this.exitNodes.add(innerExitNode);
                } else {
                    innerExitNode.addForwardNode(this.enterNode);
                }
            }
        }
        // �Ή����郍�[�J����Ԃ�for���̏ꍇ
        else if (local instanceof ForBlockInfo) {

            //�����m�[�h�͏��������C���������������Ȃ��ꍇ�͏�����
            //�o���͏�����
            final List<CFGNode<? extends ExecutableElementInfo>> initializerNodes = new LinkedList<CFGNode<? extends ExecutableElementInfo>>();
            for (final ConditionInfo initializer : ((ForBlockInfo) local)
                    .getInitializerExpressions()) {

                final CFGNode<? extends ExecutableElementInfo> initializerNode = this.nodeFactory
                        .makeNode(initializer);
                initializerNodes.add(initializerNode);
            }

            final CFGNode<? extends ExecutableElementInfo>[] initializerNodeArray = initializerNodes
                    .toArray(new CFGNode<?>[0]);
            for (int i = 0; i < initializerNodeArray.length - 1; i++) {
                initializerNodeArray[i].addForwardNode(initializerNodeArray[i + 1]);
            }

            final CFGNode<? extends ExecutableElementInfo> conditionNode = this.nodeFactory
                    .makeNode((ForBlockInfo) local);
            this.exitNodes.add(conditionNode);

            if (0 < initializerNodes.size()) {
                this.enterNode = initializerNodeArray[0];
                initializerNodeArray[initializerNodeArray.length - 1].addForwardNode(conditionNode);
            } else {
                this.enterNode = conditionNode;
            }

            // ������CFG�ƘA���D���[�v���Ȃ̂œ�����CFG����̏ꍇ�͓����m�[�h�̃t�H���[�h�m�[�h�͓����m�[�h
            conditionNode.addForwardNode(!innerCFG.isEmpty() ? innerCFG.getEnterNode()
                    : conditionNode);

            // �J��Ԃ�����ǉ�
            final List<CFGNode<? extends ExecutableElementInfo>> iteratorNodes = new LinkedList<CFGNode<? extends ExecutableElementInfo>>();
            for (final ExpressionInfo iterator : ((ForBlockInfo) local).getIteratorExpressions()) {

                final CFGNode<? extends ExecutableElementInfo> iteratorNode = this.nodeFactory
                        .makeNode(iterator);
                iteratorNodes.add(iteratorNode);
            }

            final CFGNode<? extends ExecutableElementInfo>[] iteratorNodeArray = iteratorNodes
                    .toArray(new CFGNode<?>[0]);
            for (int i = 0; i < iteratorNodeArray.length - 1; i++) {
                iteratorNodeArray[i].addForwardNode(iteratorNodeArray[i + 1]);
            }

            if (0 < iteratorNodes.size()) {
                for (final CFGNode<? extends ExecutableElementInfo> innerExitNode : innerCFG
                        .getExitNodes()) {
                    innerExitNode.addForwardNode(iteratorNodeArray[0]);
                }
                iteratorNodeArray[iteratorNodeArray.length - 1].addForwardNode(conditionNode);
            } else {
                for (final CFGNode<? extends ExecutableElementInfo> innerExitNode : innerCFG
                        .getExitNodes()) {
                    innerExitNode.addForwardNode(conditionNode);
                }
            }

        } else if (local instanceof SwitchBlockInfo) {
            // TODO �ʓ|�Ȃ̂Ŗ�����
            this.enterNode = this.nodeFactory.makeNode((ConditionalBlockInfo) local);
            this.enterNode.addForwardNode(innerCFG.enterNode);
            this.exitNodes.addAll(innerCFG.exitNodes);
        } else if (local instanceof DoBlockInfo) {

            final CFGNode<? extends ExecutableElementInfo> controlNode = this.nodeFactory
                    .makeNode((ConditionalBlockInfo) local);

            this.enterNode = !innerCFG.isEmpty() ? innerCFG.getEnterNode() : controlNode;
            this.exitNodes.add(controlNode);
            controlNode.addForwardNode(this.enterNode);
        }
    }

    private CFG buildInnerCFG(final LocalSpaceInfo local) {
        final CFG innerCFG = new IntraProceduralCFG(this.nodeFactory);

        final Iterator<StatementInfo> innerStatements = local.getStatements().iterator();

        if (!innerStatements.hasNext()) {
            return innerCFG;
        }

        CFG preSubCFG = new IntraProceduralCFG(innerStatements.next(), this.nodeFactory);
        innerCFG.enterNode = preSubCFG.getEnterNode();

        while (innerStatements.hasNext()) {
            final StatementInfo nextStatement = innerStatements.next();

            if (nextStatement instanceof ElseBlockInfo) {
                // else����if����CFG�Ɠ����ɍ\�z�����̂Ōʂɂ͍\�z���Ȃ�
                continue;
            }

            final CFG nextSubCFG = new IntraProceduralCFG(nextStatement, this.nodeFactory);

            if (null != nextSubCFG.getEnterNode()) {
                for (final CFGNode<? extends ExecutableElementInfo> preExitNode : preSubCFG
                        .getExitNodes()) {
                    if (preExitNode.isExitNode(local)) {
                        innerCFG.exitNodes.add(preExitNode);
                    } else {
                        preExitNode.addForwardNode(nextSubCFG.getEnterNode());
                    }
                }

                preSubCFG = nextSubCFG;
            }
        }

        innerCFG.exitNodes.addAll(preSubCFG.getExitNodes());

        return innerCFG;
    }

}
