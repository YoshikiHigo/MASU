package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.SimpleCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGControlEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class CFGVariableDeclarationStatementNode extends
        CFGStatementNode<VariableDeclarationStatementInfo> {

    CFGVariableDeclarationStatementNode(final VariableDeclarationStatementInfo statement) {
        super(statement);
    }

    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getInitializationExpression();
        // �����������Ȃ��ꍇ�͉������Ȃ��Ŕ�����
        if (null == expression) {
            return null;
        }

        if (expression instanceof ArrayElementUsageInfo) {

            return this.dissolveArrayElementUsage((ArrayElementUsageInfo) expression, nodeFactory);

        } else if (expression instanceof ArrayInitializerInfo) {

            return this.dissolveArrayInitializer((ArrayInitializerInfo) expression, nodeFactory);

        } else if (expression instanceof ArrayTypeReferenceInfo) {

            return null;

        } else if (expression instanceof BinominalOperationInfo) {

            return this.dissolveBinominalOperationInfo((BinominalOperationInfo) expression,
                    nodeFactory);

        } else if (expression instanceof CallInfo<?>) {

            return this.dissolveCall((CallInfo<?>) expression, nodeFactory);

        } else if (expression instanceof CastUsageInfo) {

            return this.dissolveCastUsage((CastUsageInfo) expression, nodeFactory);

        } else if (expression instanceof ClassReferenceInfo) {

            return null;

        } else if (expression instanceof EmptyExpressionInfo) {

            return null;

        } else if (expression instanceof ForeachConditionInfo) {

            // foreach���̏������͑�����̉E���ɗ��Ȃ��̂ŏ�������K�v���Ȃ�
            return null;

        } else if (expression instanceof LiteralUsageInfo) {

            return null;

        } else if (expression instanceof MonominalOperationInfo) {

            // �P�����Z�q�̃I�y�����h�͕ϐ��g�p�������Ȃ��͂��Ȃ̂ŁC��������K�v�����Ȃ�
            return null;

        } else if (expression instanceof NullUsageInfo) {

            return null;

        } else if (expression instanceof ParenthesesExpressionInfo) {

            return this.dissolveParenthesesExpression((ParenthesesExpressionInfo) expression,
                    nodeFactory);

        } else if (expression instanceof TernaryOperationInfo) {

            return this.dissolveTernaryOperation((TernaryOperationInfo) expression, nodeFactory);

        } else if (expression instanceof UnknownEntityUsageInfo) {

            return null;

        } else if (expression instanceof VariableUsageInfo<?>) {

            return null;

        } else {
            throw new IllegalStateException("unknown expression type.");
        }
    }

    /**
     * �E�ӂ�ArrayElementUsage�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param arrayElementUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveArrayElementUsage(final ArrayElementUsageInfo arrayElementUsage,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final ExpressionInfo indexExpression = arrayElementUsage.getIndexExpression();
        final ExpressionInfo qualifiedExpression = arrayElementUsage.getQualifierExpression();

        final boolean indexExpressionIsDissolved = CFGUtility.isDissolved(indexExpression);
        final boolean qualifiedExpressionIsDissolved = CFGUtility.isDissolved(qualifiedExpression);

        // indexExpression��qualifiedExpression����������Ȃ��Ƃ��͉��������ɔ�����
        if (!indexExpressionIsDissolved && !qualifiedExpressionIsDissolved) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        // TODO �{����indexEpxression -> qualifiedExpression �̏��ԂłȂ���΂Ȃ�Ȃ�
        if (qualifiedExpressionIsDissolved) {
            this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (indexExpressionIsDissolved) {
            this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // �_�~�[�ϐ��𗘗p����ArrayElementUsageInfo�C����т����p������������쐬
        int index = 0;
        final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                qualifiedExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : qualifiedExpression,
                indexExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : indexExpression, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newArrayElementUsage, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
    * �E�ӂ�ArrayInitializerInfo�ł��������𕪉����邽�߂̃��\�b�h
    * 
    * @param arrayInitialier
    * @param nodeFactory
    * @return
    */
    private CFG dissolveArrayInitializer(final ArrayInitializerInfo arrayInitializer,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final List<ExpressionInfo> initializers = arrayInitializer.getElementInitializers();

        // ����O�̕�����K�v�ȏ����擾
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        //�e�C�j�V�����C�U�𕪉����ׂ����`�F�b�N���C�������C��������������V�K�m�[�h���쐬
        final List<ExpressionInfo> newInitializers = new ArrayList<ExpressionInfo>();
        for (final ExpressionInfo initializer : initializers) {

            if (CFGUtility.isDissolved(initializer)) {

                this.makeDissolvedNode(initializer, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);

                newInitializers.add(dissolvedVariableUsageList.get(dissolvedVariableUsageList
                        .size() - 1));
            }

            else {
                newInitializers.add(initializer);
            }
        }

        // �������ꂽ�C�j�V�����C�U���Ȃ���Ή��������ɔ�����
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(newInitializers,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newArrayInitializer, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ� BinominalOperation�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param binominalOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveBinominalOperationInfo(final BinominalOperationInfo binominalOperation,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final ExpressionInfo firstOperand = binominalOperation.getFirstOperand();
        final ExpressionInfo secondOperand = binominalOperation.getFirstOperand();

        final boolean firstOperandIsDissolved = CFGUtility.isDissolved(firstOperand);
        final boolean secondOperandIsDissolved = CFGUtility.isDissolved(secondOperand);

        // �����̕K�v�̂Ȃ��ꍇ�͔�����
        if (!firstOperandIsDissolved && !secondOperandIsDissolved) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        // firstOperand���K�v�ł���Ε���
        if (firstOperandIsDissolved) {
            this.makeDissolvedNode(firstOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // secondOperand���K�v�ł���Ε���
        if (secondOperandIsDissolved) {
            this.makeDissolvedNode(secondOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // �V�����񍀉��Z�I�u�W�F�N�g����т�����E�ӂƂ��Ď���������쐬
        int index = 0;
        final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
                binominalOperation.getOperator(),
                firstOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : firstOperand,
                secondOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : secondOperand,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newBinominalOperation, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ� CallInfo<?>�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param call
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCall(final CallInfo<? extends CallableUnitInfo> call,
            final ICFGNodeFactory nodeFactory) {

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        //�@�����𕪉�
        final List<ExpressionInfo> newArguments = new ArrayList<ExpressionInfo>();
        for (final ExpressionInfo argument : call.getArguments()) {
            if (CFGUtility.isDissolved(argument)) {
                this.makeDissolvedNode(argument, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newArguments.add(dissolvedVariableUsageList
                        .get(dissolvedVariableUsageList.size() - 1));
            } else {
                newArguments.add(argument);
            }
        }

        // ���\�b�h�Ăяo���ł���΁CqualifiedExpression�𕪉�
        final ExpressionInfo newQualifiedExpression;
        if (call instanceof MethodCallInfo) {

            final MethodCallInfo methodCall = (MethodCallInfo) call;
            final ExpressionInfo qualifiedExpression = methodCall.getQualifierExpression();
            if (CFGUtility.isDissolved(qualifiedExpression)) {
                this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newQualifiedExpression = dissolvedVariableUsageList.get(dissolvedVariableUsageList
                        .size() - 1);
            } else {
                newQualifiedExpression = qualifiedExpression;
            }
        } else {
            newQualifiedExpression = null;
        }

        // �z��R���X�g���N�^�ł���΁CindexExpression�𕪉�
        final SortedMap<Integer, ExpressionInfo> newIndexExpressions = new TreeMap<Integer, ExpressionInfo>();
        if (call instanceof ArrayConstructorCallInfo) {

            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            for (final Entry<Integer, ExpressionInfo> entry : arrayConstructorCall
                    .getIndexExpressions().entrySet()) {

                final Integer dimension = entry.getKey();
                final ExpressionInfo indexExpression = entry.getValue();

                if (CFGUtility.isDissolved(indexExpression)) {
                    this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                            dissolvedVariableUsageList);
                    newIndexExpressions.put(dimension, dissolvedVariableUsageList
                            .get(dissolvedVariableUsageList.size() - 1));
                } else {
                    newIndexExpressions.put(dimension, indexExpression);
                }
            }
        }

        // �������s���Ȃ������ꍇ�͉��������ɔ�����
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        final CallInfo<? extends CallableUnitInfo> newCall;
        if (call instanceof MethodCallInfo) {
            final MethodCallInfo methodCall = (MethodCallInfo) call;
            newCall = new MethodCallInfo(newQualifiedExpression.getType(), newQualifiedExpression,
                    methodCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ClassConstructorCallInfo) {
            final ClassConstructorCallInfo classConstructorCall = (ClassConstructorCallInfo) call;
            newCall = new ClassConstructorCallInfo(classConstructorCall.getType(),
                    classConstructorCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ArrayConstructorCallInfo) {
            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            newCall = new ArrayConstructorCallInfo(arrayConstructorCall.getType(),
                    outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                    CFGUtility.getRandomNaturalValue());

            for (final Entry<Integer, ExpressionInfo> entry : newIndexExpressions.entrySet()) {
                final Integer dimension = entry.getKey();
                final ExpressionInfo newIndexExpression = entry.getValue();
                ((ArrayConstructorCallInfo) newCall).addIndexExpression(dimension,
                        newIndexExpression);
            }

        } else {
            throw new IllegalStateException();
        }

        // ������ǉ�
        for (final ExpressionInfo newArgument : newArguments) {
            newCall.addArgument(newArgument);
        }

        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newCall, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ� CastUsageInfo�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param castUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCastUsage(final CastUsageInfo castUsage, final ICFGNodeFactory nodeFactory) {

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        final ExpressionInfo castedUsage = castUsage.getCastedUsage();
        if (CFGUtility.isDissolved(castedUsage)) {
            this.makeDissolvedNode(castedUsage, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // �V�����񍀉��Z�I�u�W�F�N�g����т�����E�ӂƂ��Ď���������쐬
        final CastUsageInfo newCastUsage = new CastUsageInfo(castUsage.getType(),
                dissolvedVariableUsageList.get(0), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newCastUsage, fromLine, CFGUtility.getRandomNaturalValue(),
                toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ�ParenthesesExpressionInfo�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param parenthesExpression
     * @param nodeFactory
     * @return
     */
    private CFG dissolveParenthesesExpression(
            final ParenthesesExpressionInfo parenthesesExpression, final ICFGNodeFactory nodeFactory) {

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        final ExpressionInfo innerExpression = parenthesesExpression.getParnentheticExpression();
        if (CFGUtility.isDissolved(innerExpression)) {
            this.makeDissolvedNode(innerExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // �V�����񍀉��Z�I�u�W�F�N�g����т�����E�ӂƂ��Ď���������쐬
        final ParenthesesExpressionInfo newInnerExpression = new ParenthesesExpressionInfo(
                dissolvedVariableUsageList.get(0), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newInnerExpression, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * �E�ӂ�TernaryOperationInfo�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param ternaryOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveTernaryOperation(final TernaryOperationInfo ternaryOperation,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();

        final ConditionInfo condition = ternaryOperation.getCondition();
        final ExpressionInfo trueExpression = ternaryOperation.getTrueExpression();
        final ExpressionInfo falseExpression = ternaryOperation.getFalseExpression();

        // trueExpression���č\�z
        final VariableDeclarationStatementInfo trueStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, trueExpression, fromLine, CFGUtility.getRandomNaturalValue(),
                toLine, CFGUtility.getRandomNaturalValue());

        // falseExpression���č\�z
        final VariableDeclarationStatementInfo falseStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, falseExpression, fromLine, CFGUtility.getRandomNaturalValue(),
                toLine, CFGUtility.getRandomNaturalValue());

        // condition���č\�z
        final IfBlockInfo newIfBlock = new IfBlockInfo(fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        newIfBlock.setOuterUnit(statement.getOwnerSpace());
        final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(newIfBlock,
                condition, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        newIfBlock.setConditionalClause(newConditionalClause);
        final ElseBlockInfo newElseBlock = new ElseBlockInfo(fromLine, CFGUtility
                .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue(), newIfBlock);
        newElseBlock.setOuterUnit(statement.getOwnerSpace());
        newIfBlock.setSequentElseBlock(newElseBlock);
        newIfBlock.addStatement(trueStatement);
        newElseBlock.addStatement(falseStatement);

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // �m�[�h���쐬���C�Ȃ�
        final CFGControlNode conditionNode = nodeFactory.makeControlNode(condition);
        final CFGNode<?> trueNode = nodeFactory.makeNormalNode(trueStatement);
        final CFGNode<?> falseNode = nodeFactory.makeNormalNode(falseStatement);
        final CFGControlEdge trueEdge = new CFGControlEdge(conditionNode, trueNode, true);
        final CFGControlEdge falseEdge = new CFGControlEdge(conditionNode, falseNode, false);
        conditionNode.addForwardEdge(trueEdge);
        conditionNode.addForwardEdge(falseEdge);
        trueNode.addBackwardEdge(trueEdge);
        falseNode.addBackwardEdge(falseEdge);

        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = backwardEdge.getFromNode();
            final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(conditionNode);
            backwardNode.addForwardEdge(newBackwardEdge);
            conditionNode.addBackwardEdge(newBackwardEdge);
        }
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = forwardEdge.getToNode();
            final CFGEdge newTrueForwardEdge = forwardEdge.replaceFromNode(trueNode);
            final CFGEdge newFalseForwardEdge = forwardEdge.replaceFromNode(falseNode);
            forwardNode.addBackwardEdge(newTrueForwardEdge);
            forwardNode.addBackwardEdge(newFalseForwardEdge);
            trueNode.addForwardEdge(newTrueForwardEdge);
            falseNode.addForwardEdge(newFalseForwardEdge);
        }

        conditionNode.dissolve(nodeFactory);
        trueNode.dissolve(nodeFactory);
        falseNode.dissolve(nodeFactory);

        // ���������m�[�h�Q����CFG���\�z
        final SimpleCFG newCFG = new SimpleCFG(nodeFactory);
        newCFG.addNode(conditionNode);
        newCFG.setEnterNode(conditionNode);
        newCFG.addNode(trueNode);
        newCFG.addExitNode(trueNode);
        newCFG.addNode(falseNode);
        newCFG.addExitNode(falseNode);

        final CFG conditionCFG = conditionNode.dissolve(nodeFactory);
        final CFG trueCFG = trueNode.dissolve(nodeFactory);
        final CFG falseCFG = falseNode.dissolve(nodeFactory);

        // ownerSpace�Ƃ̒���
        ownerSpace.removeStatement(statement);
        ownerSpace.addStatement(newIfBlock);
        ownerSpace.addStatement(newElseBlock);

        if (null != conditionCFG) {
            newCFG.removeNode(conditionNode);
            newCFG.addNodes(conditionCFG.getAllNodes());
            newCFG.setEnterNode(conditionCFG.getEnterNode());
        }

        if (null != trueCFG) {
            newCFG.removeNode(trueNode);
            newCFG.addNodes(trueCFG.getAllNodes());
            newCFG.addExitNodes(trueCFG.getExitNodes());
        }

        if (null != falseCFG) {
            newCFG.removeNode(falseNode);
            newCFG.addNodes(falseCFG.getAllNodes());
            newCFG.addExitNodes(falseCFG.getExitNodes());
        }

        return newCFG;
    }

    /**
     * �����ŗ^����ꂽoriginalExpression���E�ӂƂȂ��������쐬����D
     * �쐬�����������CFG�m�[�h��dissolvedNodeList�̍Ō�ɒǉ�����C
     * ������̍��ӂ̕ϐ��g�p�I�u�W�F�N�g��dissolvedVariableUsageList�̍Ō�ɒǉ������D
     * 
     * @param originalExpression
     * @param nodeFactory
     * @param dissolvedNodeList
     * @param dissolvedVariableUsageList
     */
    private void makeDissolvedNode(final ExpressionInfo originalExpression,
            final ICFGNodeFactory nodeFactory, final List<CFGNode<?>> dissolvedNodeList,
            final List<LocalVariableUsageInfo> dissolvedVariableUsageList) {

        final LocalSpaceInfo ownerSpace = originalExpression.getOwnerSpace();
        assert null != ownerSpace : "ownerSpace is null!";
        final CallableUnitInfo outerCallableUnit = originalExpression.getOwnerMethod();
        assert null != outerCallableUnit : "outerCallableUnit is null!";
        final int fromLine = originalExpression.getFromLine();
        final int toLine = originalExpression.getToLine();
        final TypeInfo type = originalExpression.getType();

        final LocalVariableInfo dummyVariable = new LocalVariableInfo(Collections
                .<ModifierInfo> emptySet(), getDummyVariableName(), type, ownerSpace, fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo dummyVariableDeclarationStatement = new VariableDeclarationStatementInfo(
                LocalVariableUsageInfo.getInstance(dummyVariable, false, true, outerCallableUnit,
                        fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                                .getRandomNaturalValue()), originalExpression, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo.getInstance(
                dummyVariable, true, false, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());

        final CFGNode<?> newNode = nodeFactory.makeNormalNode(dummyVariableDeclarationStatement);
        dissolvedNodeList.add(newNode);
        dissolvedVariableUsageList.add(dummyVariableUsage);
    }

    /**
     * ���������m�[�h�Ȃ��C���̏ꏊ�ɓ����
     * 
     * @param dissolvedNodeList
     */
    private void makeEdges(final List<CFGNode<?>> dissolvedNodeList) {

        // ���������m�[�h���Ȃ�
        for (int i = 1; i < dissolvedNodeList.size(); i++) {
            final CFGNode<?> fromNode = dissolvedNodeList.get(i - 1);
            final CFGNode<?> toNode = dissolvedNodeList.get(i);
            final CFGEdge edge = new CFGNormalEdge(fromNode, toNode);
            fromNode.addForwardEdge(edge);
            toNode.addBackwardEdge(edge);
        }

        // ���̏ꏊ�ɓ����
        {
            final CFGNode<?> firstNode = dissolvedNodeList.get(0);
            final CFGNode<?> lastNode = dissolvedNodeList.get(dissolvedNodeList.size() - 1);
            for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
                final CFGNode<?> backwardNode = backwardEdge.getFromNode();
                final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(firstNode);
                backwardNode.addForwardEdge(newBackwardEdge);
                firstNode.addBackwardEdge(newBackwardEdge);
            }
            for (final CFGEdge forwardEdge : this.getForwardEdges()) {
                final CFGNode<?> forwardNode = forwardEdge.getToNode();
                final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(lastNode);
                forwardNode.addBackwardEdge(newForwardEdge);
                lastNode.addForwardEdge(newForwardEdge);
            }
        }
    }

    /**
     * �����ŗ^����ꂽ�m�[�h�Q����CFG���\�z���ĕԂ��D
     * 
     * @param nodeFactory
     * @param dissolvedNodeList
     * @return
     */
    private CFG makeCFG(final ICFGNodeFactory nodeFactory, List<CFGNode<?>> dissolvedNodeList) {

        final SimpleCFG cfg = new SimpleCFG(nodeFactory);

        // enterNode��ݒ�
        {
            final CFGNode<?> firstNode = dissolvedNodeList.get(0);
            final CFG dissolvedCFG = firstNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.setEnterNode(dissolvedCFG.getEnterNode());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.setEnterNode(firstNode);
            }
        }

        // exitNode��ݒ�
        {
            final CFGNode<?> lastNode = dissolvedNodeList.get(dissolvedNodeList.size() - 1);
            final CFG dissolvedCFG = lastNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addExitNodes(dissolvedCFG.getExitNodes());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addExitNode(lastNode);
            }
        }

        // nodes��ݒ�
        for (int index = 1; index < dissolvedNodeList.size() - 1; index++) {
            final CFGNode<?> node = dissolvedNodeList.get(index);
            final CFG dissolvedCFG = node.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addNode(node);
            }
        }

        return cfg;
    }
}
