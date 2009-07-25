package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;


/**
 * 
 * @author t-miyake, higo
 *
 */
public class IntraProceduralCFG extends CFG {

    /**
     * CFG�\�z�Ώۗv�f
     */
    private final Object element;

    /**
     * �Ăяo���\���j�b�g�ƃm�[�h�t�@�N�g����^���āC����t���[�O���t�𐶐�
     * 
     * @param unit �Ăяo���\���j�b�g
     * @param nodeFactory �m�[�h�t�@�N�g��
     */
    public IntraProceduralCFG(final CallableUnitInfo unit, final ICFGNodeFactory nodeFactory) {

        super(nodeFactory);

        if (null == unit) {
            throw new IllegalArgumentException("unit is null");
        }

        this.element = unit;

        if (unit instanceof ExternalMethodInfo || unit instanceof ExternalConstructorInfo) {
            throw new IllegalArgumentException("unit is an external infromation.");
        }

        final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(unit
                .getStatements(), nodeFactory);
        this.enterNode = statementsCFG.getEnterNode();
        this.exitNodes.addAll(statementsCFG.getExitNodes());

        //�K�v�̂Ȃ��m�[�h���폜
        this.optimizeCFG();
    }

    /**
     * �Ăяo���\���j�b�g��^���āC����t���[�O���t�𐶐�
     * 
     * @param unit �Ăяo���\���j�b�g
     */
    public IntraProceduralCFG(final CallableUnitInfo unit) {
        this(unit, new DefaultCFGNodeFactory());
    }

    /**
     * ���̐���t���[�O���t�𐶐�����
     * 
     * @param statement
     * @param nodeFactory
     */
    IntraProceduralCFG(final StatementInfo statement, final ICFGNodeFactory nodeFactory) {

        super(nodeFactory);

        if (null == statement) {
            throw new IllegalArgumentException();
        }

        this.element = statement;

        // �쐬����CFG���L���b�V���Ƃ��Ď���
        statementCFG.put(statement, this);

        //�P���̏ꍇ
        if (statement instanceof SingleStatementInfo) {
            final CFGNormalNode<?> node = nodeFactory.makeNormalNode(statement);
            assert null != node : "node is null!";
            this.enterNode = node;
            this.exitNodes.add(node);

            // break���̏ꍇ�͑Ή�����u���b�N��exitNodes�ɒǉ�����
            if (statement instanceof BreakStatementInfo) {
                final BreakStatementInfo breakStatement = (BreakStatementInfo) statement;
                final BlockInfo correspondingBlock = breakStatement.getCorrespondingBlock();
                final CFG correspondingBlockCFG = getCFG(correspondingBlock, nodeFactory);
                correspondingBlockCFG.exitNodes.add(node);
            }

            // ��O�Ɋւ��鏈��
            for (final ClassTypeInfo thrownException : statement.getThrownExceptions()) {
                final CatchBlockInfo correspondingCatchBlock = CatchBlockInfo
                        .getCorrespondingCatchBlock(statement, thrownException);

                if (null != correspondingCatchBlock) {
                    final CFG catchBlockCFG = new IntraProceduralCFG(correspondingCatchBlock,
                            nodeFactory);
                    final CFGExceptionEdge edge = new CFGExceptionEdge(node, catchBlockCFG
                            .getEnterNode(), correspondingCatchBlock.getCaughtException().getType());
                    node.addForwardEdge(edge);
                }
            }
        }

        // case�G���g���̏ꍇ
        else if (statement instanceof CaseEntryInfo) {

            final CaseEntryInfo caseEntry = (CaseEntryInfo) statement;
            final CFGNormalNode<?> node = nodeFactory.makeNormalNode(caseEntry);
            this.enterNode = node;
            this.exitNodes.add(node);
        }

        // Label�̏ꍇ
        else if (statement instanceof LabelInfo) {
            // �������Ȃ��Ă����͂�
        }

        // if���̏ꍇ
        else if (statement instanceof IfBlockInfo) {

            //if���̏���������R���g���[���m�[�h�𐶐�
            final IfBlockInfo ifBlock = (IfBlockInfo) statement;
            final ConditionInfo condition = ifBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.enterNode = controlNode;

            // if���̓���������
            {
                final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(ifBlock
                        .getStatements(), nodeFactory);

                // if���̓�������̏ꍇ�́Cif���̏�������exit�m�[�h�ɂȂ�
                if (statementsCFG.isEmpty()) {
                    this.exitNodes.add(controlNode);
                }

                // if���̓�������łȂ��ꍇ�́C�����̍Ō�̕���exit�m�[�h�ɂȂ�
                else {
                    final CFGControlEdge edge = new CFGControlEdge(controlNode, statementsCFG
                            .getEnterNode(), true);
                    controlNode.addForwardEdge(edge);
                    this.exitNodes.addAll(statementsCFG.getExitNodes());
                }
            }

            //�Ή�����else��������ꍇ�̏���
            if (ifBlock.hasElseBlock()) {
                final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(ifBlock
                        .getSequentElseBlock().getStatements(), nodeFactory);

                // else���̓�������̏ꍇ�́Cif���̏�������exit�m�[�h�ɂȂ�
                if (statementsCFG.isEmpty()) {
                    this.exitNodes.add(controlNode);
                }

                // else���̓������`�łȂ��ꍇ�́C�����̕��̍Ō�̕���exit�m�[�h�ɂȂ�
                else {
                    final CFGControlEdge edge = new CFGControlEdge(controlNode, statementsCFG
                            .getEnterNode(), false);
                    controlNode.addForwardEdge(edge);
                    this.exitNodes.addAll(statementsCFG.getExitNodes());
                }
            }

            //�Ή�����else�����Ȃ��ꍇ�́Cif���̏�������exit�m�[�h�ɂȂ�
            else {
                this.exitNodes.add(controlNode);
            }
        }

        // while���̏ꍇ
        else if (statement instanceof WhileBlockInfo) {

            // while���̏���������R���g���[���m�[�h�𐶐�
            final WhileBlockInfo whileBlock = (WhileBlockInfo) statement;
            final ConditionInfo condition = whileBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.enterNode = controlNode;
            this.exitNodes.add(controlNode);

            // while�������̏���
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(whileBlock
                    .getStatements(), nodeFactory);

            // ��������łȂ��ꍇ�͏������s��
            if (!statementsCFG.isEmpty()) {

                {
                    final CFGControlEdge edge = new CFGControlEdge(controlNode, statementsCFG
                            .getEnterNode(), true);
                    controlNode.addForwardEdge(edge);
                }

                for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

                    //return���̏ꍇ��exit�m�[�h�ɒǉ�
                    if (exitNode instanceof CFGReturnStatementNode) {
                        this.exitNodes.add(exitNode);
                    }

                    // continue���̏ꍇ
                    else if (exitNode instanceof CFGContinueStatementNode) {

                        final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
                                .getCore();
                        final BlockInfo correspondingBlock = continueStatement
                                .getCorrespondingBlock();

                        // continue���̂ɑΉ����Ă���̂�����while���̎�
                        if (statement == correspondingBlock) {
                            final CFGJumpEdge edge = new CFGJumpEdge(exitNode, controlNode);
                            exitNode.addForwardEdge(edge);
                        }

                        // continue���̂ɑΉ����Ă���̂�����while���ł͂Ȃ���
                        else {
                            this.exitNodes.add(exitNode);
                        }
                    }

                    else {
                        final CFGNormalEdge edge = new CFGNormalEdge(exitNode, controlNode);
                        exitNode.addForwardEdge(edge);
                    }
                }
            }
        }

        // else ���̏ꍇ
        else if (statement instanceof ElseBlockInfo) {
            //else���͑Ή�����if���ŏ������Ă��邽�߁C�����ł͂Ȃɂ����Ȃ�
        }

        // do���̏ꍇ
        else if (statement instanceof DoBlockInfo) {

            // do���̏���������R���g���[���m�[�h�𐶐�
            final DoBlockInfo doBlock = (DoBlockInfo) statement;
            final ConditionInfo condition = doBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.exitNodes.add(controlNode);

            // do�������̏���
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(doBlock
                    .getStatements(), nodeFactory);

            // �R���g���[���m�[�h����do�������֑J��
            {
                final CFGControlEdge edge = new CFGControlEdge(controlNode, statementsCFG
                        .getEnterNode(), true);
                controlNode.addForwardEdge(edge);
            }

            // ��������̎��́Cdo���̏�������enter�m�[�h�ɂȂ�
            if (statementsCFG.isEmpty()) {
                this.enterNode = controlNode;
            }

            // ��łȂ��ꍇ�́C����CFG��enter�m�[�h���C����CFG��enter�m�[�h�ɂȂ�
            else {
                this.enterNode = statementsCFG.getEnterNode();
                for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

                    //Return���̏ꍇ��exit�m�[�h�ɒǉ�
                    if (exitNode instanceof CFGReturnStatementNode) {
                        this.exitNodes.add(exitNode);
                    }

                    else {
                        final CFGNormalEdge edge = new CFGNormalEdge(exitNode, controlNode);
                        exitNode.addForwardEdge(edge);
                    }
                }
            }
        }

        // for���̏ꍇ
        else if (statement instanceof ForBlockInfo) {

            // for���̏���������R���g���[���m�[�h�𐶐�
            final ForBlockInfo forBlock = (ForBlockInfo) statement;
            final ConditionInfo condition = forBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null";

            //EmptyExpression�łȂ���΁CexitNodes�ɒǉ�
            if (!(condition instanceof EmptyExpressionInfo)) {
                this.exitNodes.add(controlNode);
            }

            //������������CFG�𐶐�
            final SortedSet<ConditionInfo> initializers = forBlock.getInitializerExpressions();
            final SequentialExpressionsCFG initializersCFG = new SequentialExpressionsCFG(
                    initializers, nodeFactory);

            //����������for����CFG�ɒǉ�
            if (initializersCFG.isEmpty()) {
                this.enterNode = controlNode;
            } else {
                this.enterNode = initializersCFG.getEnterNode();
                for (final CFGNode<?> exitNode : initializersCFG.getExitNodes()) {
                    final CFGNormalEdge edge = new CFGNormalEdge(exitNode, controlNode);
                    exitNode.addForwardEdge(edge);
                }
            }

            //�J��Ԃ�������CFG�𐶐�
            final SortedSet<ExpressionInfo> iterators = forBlock.getIteratorExpressions();
            final SequentialExpressionsCFG iteratorsCFG = new SequentialExpressionsCFG(iterators,
                    nodeFactory);

            // for���̓����̏���
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(forBlock
                    .getStatements(), nodeFactory);
            // for���̓�������̏ꍇ
            if (statementsCFG.isEmpty()) {

                //�J��Ԃ�������̏ꍇ
                if (iteratorsCFG.isEmpty()) {
                    final CFGControlEdge edge = new CFGControlEdge(controlNode, controlNode, true);
                    controlNode.addForwardEdge(edge);
                }

                //�J��Ԃ�������łȂ��ꍇ
                else {
                    {
                        final CFGControlEdge edge = new CFGControlEdge(controlNode, iteratorsCFG
                                .getEnterNode(), true);
                        controlNode.addForwardEdge(edge);
                    }
                    for (final CFGNode<?> exitNode : iteratorsCFG.getExitNodes()) {

                        // Return���̏ꍇ��exit�m�[�h�ɒǉ�
                        if (exitNode instanceof CFGReturnStatementNode) {
                            this.exitNodes.add(exitNode);
                        }

                        // continue���̏ꍇ
                        else if (exitNode instanceof CFGContinueStatementNode) {

                            final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
                                    .getCore();
                            final BlockInfo correspondingBlock = continueStatement
                                    .getCorrespondingBlock();
                            // continue���̎��Ɏ��s�����̂��C����while���̏������̎�
                            if (statement == correspondingBlock) {
                                final CFGJumpEdge edge = new CFGJumpEdge(exitNode, controlNode);
                                exitNode.addForwardEdge(edge);
                            }

                            // continue���̎��Ɏ��s�����̂��C����while���̏������ł͂Ȃ���
                            else {
                                this.exitNodes.add(exitNode);
                            }

                        }

                        else {
                            final CFGNormalEdge edge = new CFGNormalEdge(exitNode, controlNode);
                            exitNode.addForwardEdge(edge);
                        }
                    }
                }
            }

            // for���̓�������łȂ��ꍇ
            else {

                {
                    final CFGControlEdge edge = new CFGControlEdge(controlNode, statementsCFG
                            .getEnterNode(), true);
                    controlNode.addForwardEdge(edge);
                }

                //�J��Ԃ�������̏ꍇ
                if (iteratorsCFG.isEmpty()) {

                    for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

                        //Return���̏ꍇ��exit�m�[�h�ɒǉ�
                        if (exitNode instanceof CFGReturnStatementNode) {
                            this.exitNodes.add(exitNode);
                        }

                        // continue���̏ꍇ
                        else if (exitNode instanceof CFGContinueStatementNode) {

                            final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
                                    .getCore();
                            final BlockInfo correspondingBlock = continueStatement
                                    .getCorrespondingBlock();
                            // continue���̎��Ɏ��s�����̂��C����while���̏������̎�
                            if (statement == correspondingBlock) {
                                final CFGJumpEdge edge = new CFGJumpEdge(exitNode, controlNode);
                                exitNode.addForwardEdge(edge);
                            }

                            // continue���̎��Ɏ��s�����̂��C����while���̏������ł͂Ȃ���
                            else {
                                this.exitNodes.add(exitNode);
                            }

                        }

                        else {
                            final CFGNormalEdge edge = new CFGNormalEdge(exitNode, controlNode);
                            exitNode.addForwardEdge(edge);
                        }
                    }
                }

                //�J��Ԃ�������łȂ��ꍇ
                else {

                    for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

                        //Return���̏ꍇ��exit�m�[�h�ɒǉ�
                        if (exitNode instanceof CFGReturnStatementNode) {
                            this.exitNodes.add(exitNode);
                        }

                        // continue���̏ꍇ
                        else if (exitNode instanceof CFGContinueStatementNode) {

                            final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
                                    .getCore();
                            final BlockInfo correspondingBlock = continueStatement
                                    .getCorrespondingBlock();
                            // continue���̎��Ɏ��s�����̂��C����while���̏������̎�
                            if (statement == correspondingBlock) {
                                final CFGJumpEdge edge = new CFGJumpEdge(exitNode, controlNode);
                                exitNode.addForwardEdge(edge);
                            }

                            // continue���̎��Ɏ��s�����̂��C����while���̏������ł͂Ȃ���
                            else {
                                this.exitNodes.add(exitNode);
                            }

                        }

                        else {
                            final CFGNormalEdge edge = new CFGNormalEdge(exitNode, iteratorsCFG
                                    .getEnterNode());
                            exitNode.addForwardEdge(edge);
                        }
                    }

                    for (final CFGNode<?> exitNode : iteratorsCFG.getExitNodes()) {

                        //Return���̏ꍇ��exit�m�[�h�ɒǉ�
                        if (exitNode instanceof CFGReturnStatementNode) {
                            this.exitNodes.add(exitNode);
                        }

                        // continue���̏ꍇ
                        else if (exitNode instanceof CFGContinueStatementNode) {

                            final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
                                    .getCore();
                            final BlockInfo correspondingBlock = continueStatement
                                    .getCorrespondingBlock();
                            // continue���̎��Ɏ��s�����̂��C����while���̏������̎�
                            if (statement == correspondingBlock) {
                                final CFGJumpEdge edge = new CFGJumpEdge(exitNode, controlNode);
                                exitNode.addForwardEdge(edge);
                            }

                            // continue���̎��Ɏ��s�����̂��C����while���̏������ł͂Ȃ���
                            else {
                                this.exitNodes.add(exitNode);
                            }

                        }

                        else {
                            final CFGNormalEdge edge = new CFGNormalEdge(exitNode, controlNode);
                            exitNode.addForwardEdge(edge);
                        }
                    }
                }
            }
        }

        else if (statement instanceof ForeachBlockInfo) {

            // foreach���̕ϐ��C������R���g���[���m�[�h�𐶐�
            final ForeachBlockInfo foreachBlock = (ForeachBlockInfo) statement;
            final ForeachConditionInfo condition = (ForeachConditionInfo) foreachBlock
                    .getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.enterNode = controlNode;
            this.exitNodes.add(controlNode);

            // foreach�������̏���
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(foreachBlock
                    .getStatements(), nodeFactory);

            // ��������łȂ��ꍇ�͏������s��
            if (!statementsCFG.isEmpty()) {
                {
                    final CFGControlEdge edge = new CFGControlEdge(controlNode, statementsCFG
                            .getEnterNode(), true);
                    controlNode.addForwardEdge(edge);
                }
                for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

                    //return���̏ꍇ��exit�m�[�h�ɒǉ�
                    if (exitNode instanceof CFGReturnStatementNode) {
                        this.exitNodes.add(exitNode);
                    }

                    // continue���̏ꍇ
                    else if (exitNode instanceof CFGContinueStatementNode) {

                        final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
                                .getCore();
                        final BlockInfo correspondingBlock = continueStatement
                                .getCorrespondingBlock();

                        // continue���̂ɑΉ����Ă���̂�����while���̎�
                        if (statement == correspondingBlock) {
                            final CFGJumpEdge edge = new CFGJumpEdge(exitNode, controlNode);
                            exitNode.addForwardEdge(edge);
                        }

                        // continue���̂ɑΉ����Ă���̂�����while���ł͂Ȃ���
                        else {
                            this.exitNodes.add(exitNode);
                        }
                    }

                    else {
                        final CFGNormalEdge edge = new CFGNormalEdge(exitNode, controlNode);
                        exitNode.addForwardEdge(edge);
                    }
                }
            }
        }

        // switch���̏ꍇ
        else if (statement instanceof SwitchBlockInfo) {

            // switch���̏���������R���g���[���m�[�h�𐶐�
            final SwitchBlockInfo switchBlock = (SwitchBlockInfo) statement;
            final ConditionInfo condition = switchBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.enterNode = controlNode;
            this.exitNodes.add(controlNode);

            // ���CFG����菜������
            final List<IntraProceduralCFG> statementCFGs = new ArrayList<IntraProceduralCFG>();
            for (final StatementInfo innerStatement : switchBlock.getStatements()) {
                final IntraProceduralCFG innerStatementCFG = new IntraProceduralCFG(innerStatement,
                        nodeFactory);
                if (!innerStatementCFG.isEmpty()) {
                    statementCFGs.add(innerStatementCFG);
                }
            }

            for (int i = 0; i < statementCFGs.size() - 1; i++) {

                final IntraProceduralCFG fromCFG = statementCFGs.get(i);
                final IntraProceduralCFG toCFG = statementCFGs.get(i + 1);

                for (final CFGNode<?> exitNode : fromCFG.getExitNodes()) {

                    // Return���ł���΁Cexit�m�[�h�ł���
                    if (exitNode instanceof CFGReturnStatementNode) {
                        this.exitNodes.add(exitNode);
                    }

                    // �v�f����1�ł���C�����Break���ł���΁C�����switch����break�ł���
                    else if (exitNode instanceof CFGBreakStatementNode
                            && 1 == fromCFG.getAllNodes().size()) {
                        this.exitNodes.add(exitNode);
                    }

                    // ����ȊO�̃m�[�h�ł���΁C�Ȃ�
                    else {
                        final CFGNormalEdge edge = new CFGNormalEdge(exitNode, toCFG.getEnterNode());
                        exitNode.addForwardEdge(edge);
                    }
                }

                //fromCFG��case���ł���ꍇ�́Cswitch���̏���������ˑ��ӂ�����
                if (fromCFG.getElement() instanceof CaseEntryInfo) {
                    final CFGControlEdge edge = new CFGControlEdge(controlNode, fromCFG
                            .getEnterNode(), true);
                    controlNode.addForwardEdge(edge);
                }

                //fromCFG��default���ł���ꍇ�́Cswitch����exitNodes����controlNode������
                if (toCFG.getElement() instanceof DefaultEntryInfo) {
                    this.exitNodes.remove(controlNode);
                }
            }

            if (0 < statementCFGs.size()) {
                final IntraProceduralCFG lastCFG = statementCFGs.get(statementCFGs.size() - 1);
                this.exitNodes.addAll(lastCFG.getExitNodes());
            } else {
                this.exitNodes.add(controlNode);
            }
        }

        // try���̏ꍇ
        else if (statement instanceof TryBlockInfo) {

            final TryBlockInfo tryBlock = (TryBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(tryBlock
                    .getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();

            final FinallyBlockInfo finallyBlock = tryBlock.getSequentFinallyBlock();
            //finally�u���b�N���Ȃ��ꍇ
            if (null == finallyBlock) {
                //try���̍Ōオ�Cexit�m�[�h�ɂȂ肤��
                this.exitNodes.addAll(statementsCFG.getExitNodes());

                // �Ή�����catch����exit�m�[�h���C����try����exit�m�[�h�Ƃ݂Ȃ�
                for (final CatchBlockInfo catchBlock : tryBlock.getSequentCatchBlocks()) {
                    final CFG catchBlockCFG = new IntraProceduralCFG(catchBlock, nodeFactory);
                    this.exitNodes.addAll(catchBlockCFG.getExitNodes());
                }
            }

            // finally�u���b�N������ꍇ
            else {

                final CFG finallyBlockCFG = new IntraProceduralCFG(finallyBlock, nodeFactory);

                // finally�u���b�N����̏ꍇ�́Cfinally�u���b�N���Ȃ��ꍇ�Ɠ��l�̏���
                if (finallyBlockCFG.isEmpty()) {

                    //try���̍Ōオ�Cexit�m�[�h�ɂȂ肤��
                    this.exitNodes.addAll(statementsCFG.getExitNodes());

                    // �Ή�����catch����exit�m�[�h���C����try����exit�m�[�h�Ƃ݂Ȃ�
                    for (final CatchBlockInfo catchBlock : tryBlock.getSequentCatchBlocks()) {
                        final CFG catchBlockCFG = new IntraProceduralCFG(catchBlock, nodeFactory);
                        this.exitNodes.addAll(catchBlockCFG.getExitNodes());
                    }
                }

                //finally�u���b�N����łȂ��ꍇ�́Cfinally�u���b�N�̍Ōオtry�u���b�N�̏o���ɂȂ�
                else {
                    this.exitNodes.addAll(finallyBlockCFG.getExitNodes());

                    //try���̓�������Ȃ�
                    for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {
                        final CFGNormalEdge edge = new CFGNormalEdge(exitNode, finallyBlockCFG
                                .getEnterNode());
                        exitNode.addForwardEdge(edge);
                    }

                    // �ecatch������Ȃ� 
                    for (final CatchBlockInfo catchBlock : tryBlock.getSequentCatchBlocks()) {
                        final CFG catchBlockCFG = new IntraProceduralCFG(catchBlock, nodeFactory);
                        for (final CFGNode<?> exitNode : catchBlockCFG.getExitNodes()) {
                            final CFGNormalEdge edge = new CFGNormalEdge(exitNode, finallyBlockCFG
                                    .getEnterNode());
                            exitNode.addForwardEdge(edge);
                        }
                    }
                }
            }
        }

        // catch���̏ꍇ
        else if (statement instanceof CatchBlockInfo) {

            final CatchBlockInfo catchBlock = (CatchBlockInfo) statement;

            final LocalVariableInfo exception = catchBlock.getCaughtException();
            exception.getDeclarationStatement();
            final VariableDeclarationStatementInfo declarationStatement;
            if (null == exception.getDeclarationStatement()) {
                final int fromLine = exception.getFromLine();
                final int fromColumn = exception.getFromColumn();
                final int toLine = exception.getToLine();
                final int toColumn = exception.getToColumn();
                final LocalVariableUsageInfo exceptionUsage = LocalVariableUsageInfo.getInstance(
                        exception, false, true, catchBlock.getOwnerMethod(), fromLine, fromColumn,
                        toLine, toColumn);
                declarationStatement = new VariableDeclarationStatementInfo(exceptionUsage, null,
                        fromLine, fromColumn, toLine, toColumn);
            } else {
                declarationStatement = exception.getDeclarationStatement();
            }
            final CFG declarationStatementCFG = new IntraProceduralCFG(declarationStatement,
                    nodeFactory);
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(catchBlock
                    .getStatements(), nodeFactory);

            this.enterNode = declarationStatementCFG.getEnterNode();

            //�����X�e�[�g�����g�����݂���ꍇ�́C��O��CFG�Ɠ����X�e�[�g�����g�̕����Ȃ�
            if (!statementsCFG.isEmpty()) {
                for (final CFGNode<?> exitNode : declarationStatementCFG.getExitNodes()) {
                    final CFGNormalEdge edge = new CFGNormalEdge(exitNode, statementsCFG
                            .getEnterNode());
                    exitNode.addForwardEdge(edge);
                }
                this.exitNodes.addAll(statementsCFG.getExitNodes());
            } else {
                this.exitNodes.addAll(declarationStatementCFG.getExitNodes());
            }
        }

        // finally���̏ꍇ
        else if (statement instanceof FinallyBlockInfo) {

            final FinallyBlockInfo finallyBlock = (FinallyBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(finallyBlock
                    .getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        // simple���̏ꍇ
        else if (statement instanceof SimpleBlockInfo) {

            final SimpleBlockInfo simpleBlock = (SimpleBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(simpleBlock
                    .getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        // synchorized���̏ꍇ
        else if (statement instanceof SynchronizedBlockInfo) {

            final SynchronizedBlockInfo synchronizedBlock = (SynchronizedBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
                    synchronizedBlock.getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        else {
            assert false : "Here shouldn't be reached!";
        }
    }

    /**
     * CFG�\�z�Ώۗv�f��Ԃ�
     * 
     * @return CFG�\�z�Ώۗv�f
     */
    public Object getElement() {
        return this.element;
    }

    CFGNode<?> getFirstNode(final StatementInfo statement, final ICFGNodeFactory nodeFactory) {

        //�P���̏ꍇ
        if (statement instanceof SingleStatementInfo) {
            final CFGNormalNode<?> node = nodeFactory.makeNormalNode(statement);
            assert null != node : "node is null!";
            return node;
        }

        // case�G���g���̏ꍇ
        else if (statement instanceof CaseEntryInfo) {

            final CaseEntryInfo caseEntry = (CaseEntryInfo) statement;
            final CFGNormalNode<?> node = nodeFactory.makeNormalNode(caseEntry);
            assert null != node : "node is null!";
            return node;
        }

        // Label�̏ꍇ
        else if (statement instanceof LabelInfo) {
            // �������Ȃ��Ă����͂�
        }

        // if���̏ꍇ
        else if (statement instanceof IfBlockInfo) {

            //if���̏���������R���g���[���m�[�h�𐶐�
            final IfBlockInfo ifBlock = (IfBlockInfo) statement;
            final ConditionInfo condition = ifBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            return controlNode;
        }

        // while���̏ꍇ
        else if (statement instanceof WhileBlockInfo) {

            // while���̏���������R���g���[���m�[�h�𐶐�
            final WhileBlockInfo whileBlock = (WhileBlockInfo) statement;
            final ConditionInfo condition = whileBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            return controlNode;
        }

        // else ���̏ꍇ
        else if (statement instanceof ElseBlockInfo) {
            // �������Ȃ��Ă����͂�
        }

        // do���̏ꍇ
        else if (statement instanceof DoBlockInfo) {

            final DoBlockInfo doBlock = (DoBlockInfo) statement;
            final SortedSet<StatementInfo> statements = doBlock.getStatements();
            final CFGNode<?> firstNode = this.getFirstNode(statements.first(), nodeFactory);
            assert null != firstNode : "controlNode is null!";
            return firstNode;
        }

        // for���̏ꍇ
        else if (statement instanceof ForBlockInfo) {

            // for���̏���������R���g���[���m�[�h�𐶐�
            final ForBlockInfo forBlock = (ForBlockInfo) statement;
            final ConditionInfo condition = forBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null";

            //������������CFG�𐶐�
            final SortedSet<ConditionInfo> initializers = forBlock.getInitializerExpressions();
            final SequentialExpressionsCFG initializersCFG = new SequentialExpressionsCFG(
                    initializers, nodeFactory);

            if (!initializersCFG.isEmpty()) {
                return initializersCFG.getEnterNode();
            } else {
                return controlNode;
            }
        }

        // switch���̏ꍇ
        else if (statement instanceof SwitchBlockInfo) {

            // switch���̏���������R���g���[���m�[�h�𐶐�
            final SwitchBlockInfo switchBlock = (SwitchBlockInfo) statement;
            final ConditionInfo condition = switchBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            return controlNode;
        }

        // try���̏ꍇ
        else if (statement instanceof TryBlockInfo) {

            final TryBlockInfo tryBlock = (TryBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(tryBlock
                    .getStatements(), nodeFactory);
            return statementsCFG.getEnterNode();
        }

        // catch���̏ꍇ
        else if (statement instanceof CatchBlockInfo) {

            final CatchBlockInfo catchBlock = (CatchBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(catchBlock
                    .getStatements(), nodeFactory);
            return statementsCFG.getEnterNode();
        }

        // finally���̏ꍇ
        else if (statement instanceof FinallyBlockInfo) {

            final FinallyBlockInfo finallyBlock = (FinallyBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(finallyBlock
                    .getStatements(), nodeFactory);
            return statementsCFG.getEnterNode();
        }

        // simple���̏ꍇ
        else if (statement instanceof SimpleBlockInfo) {

            final SimpleBlockInfo simpleBlock = (SimpleBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(simpleBlock
                    .getStatements(), nodeFactory);
            return statementsCFG.getEnterNode();
        }

        // synchorized���̏ꍇ
        else if (statement instanceof SynchronizedBlockInfo) {

            final SynchronizedBlockInfo synchronizedBlock = (SynchronizedBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
                    synchronizedBlock.getStatements(), nodeFactory);
            return statementsCFG.getEnterNode();
        }

        assert false : "Here shouldn't be reached!";
        return null;
    }

    /**
     * EmptyExpression��CaseEntry���폜
     */
    private void optimizeCFG() {

        for (final CFGNode<?> node : this.getAllNodes()) {
            node.optimize();
        }
    }

    /**
     * StatementInfo�̗񂩂�CFG���쐬����N���X
     * 
     * @author higo
     *
     */
    private class SequentialStatementsCFG extends CFG {

        SequentialStatementsCFG(final SortedSet<StatementInfo> statements,
                final ICFGNodeFactory nodeFactory) {

            super(nodeFactory);

            // ���CFG, catch���Cfinally���Celse����CFG���������鏈��
            final List<IntraProceduralCFG> statementCFGs = new ArrayList<IntraProceduralCFG>();
            for (final StatementInfo statement : statements) {

                if (statement instanceof CatchBlockInfo || statement instanceof FinallyBlockInfo
                        || statement instanceof ElseBlockInfo) {
                    continue;
                }

                final IntraProceduralCFG statementCFG = new IntraProceduralCFG(statement,
                        nodeFactory);
                if (statementCFG.isEmpty()) {
                    continue;
                }

                statementCFGs.add(statementCFG);
            }

            if (0 == statementCFGs.size()) {
                return;
            }

            //�ŏ��̕�����enter�m�[�h�𐶐�
            {
                this.enterNode = statementCFGs.get(0).getEnterNode();
            }

            //�Ō�̕�����exit�m�[�h�𐶐�
            {

                // break���łȂ����exit�m�[�h�ɒǉ�
                final StatementInfo lastStatement = statements.last();
                if (!(lastStatement instanceof BreakStatementInfo)) {
                    final int lastIndex = statementCFGs.size() - 1;
                    this.exitNodes.addAll(statementCFGs.get(lastIndex).getExitNodes());
                }
            }

            //statements���琶������CFG�����ԂɂȂ��ł���
            {
                for (int i = 0; i < statementCFGs.size() - 1; i++) {
                    final IntraProceduralCFG fromCFG = statementCFGs.get(i);
                    final IntraProceduralCFG toCFG = statementCFGs.get(i + 1);

                    for (final CFGNode<?> exitNode : fromCFG.getExitNodes()) {

                        // Return���̏ꍇ��exitNodes�ɒǉ�
                        if (exitNode instanceof CFGReturnStatementNode) {
                            this.exitNodes.add(exitNode);
                        }

                        // continue���̏ꍇ
                        else if (exitNode instanceof CFGContinueStatementNode) {

                            final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
                                    .getCore();
                            final BlockInfo correspondingBlock = continueStatement
                                    .getCorrespondingBlock();
                            final SortedSet<StatementInfo> innerStatements = LocalSpaceInfo
                                    .getAllStatements(correspondingBlock);

                            final StatementInfo statement = (StatementInfo) toCFG.getElement();

                            // statement �� innerStatements �Ɋ܂܂�Ă���ꍇ�́Ccontinue���̎x�z���ɂ���                           
                            if (innerStatements.contains(statement)) {
                                this.exitNodes.add(exitNode);
                            } else {
                                final CFGNormalEdge edge = new CFGNormalEdge(exitNode, toCFG
                                        .getEnterNode());
                                exitNode.addForwardEdge(edge);
                            }
                        }

                        // controlNode�̏ꍇ
                        else if (exitNode instanceof CFGControlNode) {
                            final CFGControlEdge edge = new CFGControlEdge(exitNode, toCFG
                                    .getEnterNode(), false);
                            exitNode.addForwardEdge(edge);
                        }

                        else {
                            final CFGNormalEdge edge = new CFGNormalEdge(exitNode, toCFG
                                    .getEnterNode());
                            exitNode.addForwardEdge(edge);
                        }
                    }
                }
            }
        }
    }

    /**
     * ExpressionInfo�̗�C�܂���COnditionInfo�̗񂩂�CFG���쐬����N���X
     * 
     * @author higo
     *
     */
    private class SequentialExpressionsCFG extends CFG {

        SequentialExpressionsCFG(final SortedSet<? extends ConditionInfo> expressions,
                final ICFGNodeFactory nodeFactory) {

            super(nodeFactory);

            if (0 == expressions.size()) {
                return;
            }

            // �ŏ��̎�����enter�m�[�h�𐶐�
            {
                final ConditionInfo firstExpression = expressions.first();
                final CFGNormalNode<?> firstExpressionNode = nodeFactory
                        .makeNormalNode(firstExpression);
                this.enterNode = firstExpressionNode;
            }

            // �Ō�̎�����exit�m�[�h�𐶐�
            {
                final ConditionInfo lastExpression = expressions.last();
                final CFGNormalNode<?> lastExpressionNode = nodeFactory
                        .makeNormalNode(lastExpression);
                this.exitNodes.add(lastExpressionNode);
            }

            // expressions ���琶�������m�[�h�����ԂɂȂ��ł���
            final ConditionInfo[] expressionArray = expressions.toArray(new ConditionInfo[0]);
            for (int i = 0; i < expressionArray.length - 1; i++) {
                final CFGNormalNode<?> fromNode = nodeFactory.makeNormalNode(expressionArray[i]);
                final CFGNormalNode<?> toNode = nodeFactory.makeNormalNode(expressionArray[i + 1]);
                final CFGNormalEdge edge = new CFGNormalEdge(fromNode, toNode);
                fromNode.addForwardEdge(edge);
            }
        }
    }

    private static final Map<StatementInfo, CFG> statementCFG = new HashMap<StatementInfo, CFG>();

    static CFG getCFG(final StatementInfo statement, final ICFGNodeFactory nodeFactory) {

        CFG cfg = statementCFG.get(statement);
        if (null == cfg) {
            throw new IllegalStateException();
        }
        return cfg;
    }
}
