package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
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
     * @param callableUnit
     *            �������鐧��t���[�O���t�ɑΉ����郍�[�J�����
     * @param nodeFactory
     *            CFG�m�[�h�̃t�@�N�g��
     */
    public IntraProceduralCFG(final CallableUnitInfo callableUnit, final ICFGNodeFactory nodeFactory) {

        super(nodeFactory);

        if (callableUnit instanceof ExternalMethodInfo
                || callableUnit instanceof ExternalConstructorInfo) {
            throw new IllegalArgumentException("localSpace is an external infromation.");
        }

        if (null == callableUnit) {
            throw new NullPointerException("localSpace is null");
        }

        this.localSpace = callableUnit;

        this.buildCFG(callableUnit);
    }

    /**
     * �������鐧��t���[�O���t�ɑΉ����郍�[�J����Ԃ�^���ď�����
     * 
     * @param callableUnit
     *            �������鐧��t���[�O���t�ɑΉ����郍�[�J�����
     */
    public IntraProceduralCFG(final CallableUnitInfo callableUnit) {
        this(callableUnit, new DefaultCFGNodeFactory());
    }

    private IntraProceduralCFG(final ICFGNodeFactory nodeFactory) {
        super(nodeFactory);

        this.localSpace = null;
    }

    private IntraProceduralCFG(final StatementInfo statement, final ICFGNodeFactory nodeFactory) {
        super(nodeFactory);
        if (statement instanceof BlockInfo) {
            this.localSpace = (BlockInfo) statement;

            this.buildCFG(localSpace);
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
            IfBlockInfo ifBlock = (IfBlockInfo) local;
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
        // �Ή����郍�[�J����Ԃ�for����������while���̏ꍇ
        else if (local instanceof ForBlockInfo || local instanceof WhileBlockInfo) {
            // ���䕶���̂������m�[�h���o���m�[�h
            this.enterNode = this.nodeFactory.makeNode((ConditionalBlockInfo) local);
            this.exitNodes.add(this.enterNode);

            // ������CFG�ƘA���D���[�v���Ȃ̂œ�����CFG����̏ꍇ�͓����m�[�h�̃t�H���[�h�m�[�h�͓����m�[�h
            this.enterNode.addForwardNode(!innerCFG.isEmpty() ? innerCFG.getEnterNode()
                    : this.enterNode);

            for (final CFGNode<? extends StatementInfo> innerExitNode : innerCFG.getExitNodes()) {
                if (innerExitNode.isExitNode(localSpace)) {
                    this.exitNodes.add(innerExitNode);
                } else {
                    innerExitNode.addForwardNode(this.enterNode);
                }
            }

        } else if (local instanceof SwitchBlockInfo) {
            // TODO �ʓ|�Ȃ̂Ŗ�����
            this.enterNode = this.nodeFactory.makeNode((ConditionalBlockInfo) local);
            this.enterNode.addForwardNode(innerCFG.enterNode);
            this.exitNodes.addAll(innerCFG.exitNodes);
        } else if (local instanceof DoBlockInfo) {

            final CFGNode<? extends StatementInfo> controlNode = this.nodeFactory
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

            CFG nextSubCFG = new IntraProceduralCFG(nextStatement, this.nodeFactory);

            if (null != nextSubCFG.getEnterNode()) {
                for (final CFGNode<? extends StatementInfo> preExitNode : preSubCFG.getExitNodes()) {
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
