package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class CFGVariableDeclarationStatementNode extends
        CFGStatementNode<VariableDeclarationStatementInfo> {

    CFGVariableDeclarationStatementNode(final VariableDeclarationStatementInfo statement) {
        super(statement);
    }

    @Override
    public CFGNode<? extends ExecutableElementInfo> dissolve(final ICFGNodeFactory nodeFactory) {

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

        } else if (expression instanceof BinominalOperationInfo) {

        } else if (expression instanceof CallInfo<?>) {

        } else if (expression instanceof CastUsageInfo) {

        } else if (expression instanceof ClassReferenceInfo) {

        } else if (expression instanceof EmptyExpressionInfo) {

        } else if (expression instanceof ForeachConditionInfo) {

        } else if (expression instanceof LiteralUsageInfo) {

        } else if (expression instanceof MonominalOperationInfo) {

        } else if (expression instanceof NullUsageInfo) {

        } else if (expression instanceof ParenthesesExpressionInfo) {

        } else if (expression instanceof TernaryOperationInfo) {

        } else if (expression instanceof UnknownEntityUsageInfo) {

        } else if (expression instanceof VariableUsageInfo<?>) {

        } else {
            throw new IllegalStateException("unknown expression type.");
        }

        return null;
    }

    /**
     * �E�ӂ�ArrayElementUsage�ł��������𕪉����邽�߂̃��\�b�h
     * 
     * @param arrayElementUsage
     * @param nodeFactory
     * @return
     */
    private CFGNode<? extends ExecutableElementInfo> dissolveArrayElementUsage(
            final ArrayElementUsageInfo arrayElementUsage, final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final ExpressionInfo indexExpression = arrayElementUsage.getIndexExpression();
        final ExpressionInfo qualifiedExpression = arrayElementUsage.getQualifierExpression();

        // ����O�̕�����K�v�ȏ����擾
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int fromColumn = statement.getFromColumn();
        final int toLine = statement.getToLine();
        final int toColumn = statement.getToColumn();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �C���f�b�N�X��\��Expression�����������ꍇ
        final LocalVariableInfo dummyVariable1;
        final VariableDeclarationStatementInfo dummyVariableDeclarationStatement1;
        final LocalVariableUsageInfo dummyVariableUsage1;
        if (CFGUtility.isDissolved(indexExpression)) {
            dummyVariable1 = new LocalVariableInfo(Collections.<ModifierInfo> emptySet(),
                    getDummyVariableName(), indexExpression.getType(), ownerSpace, fromLine,
                    fromColumn - 10, toLine, toColumn - 10);
            dummyVariableDeclarationStatement1 = new VariableDeclarationStatementInfo(
                    LocalVariableUsageInfo.getInstance(dummyVariable1, false, true,
                            outerCallableUnit, fromLine, fromColumn - 10, toLine, toColumn - 10),
                    indexExpression, fromLine, fromColumn - 10, toLine, toColumn - 10);
            dummyVariableUsage1 = LocalVariableUsageInfo.getInstance(dummyVariable1, true, false,
                    outerCallableUnit, fromLine, fromColumn, toLine, toColumn);
        } else {
            dummyVariable1 = null;
            dummyVariableDeclarationStatement1 = null;
            dummyVariableUsage1 = null;
        }

        // ���L�҂�\��Expression�����������ꍇ
        final LocalVariableInfo dummyVariable2;
        final VariableDeclarationStatementInfo dummyVariableDeclarationStatement2;
        final LocalVariableUsageInfo dummyVariableUsage2;
        if (CFGUtility.isDissolved(qualifiedExpression)) {
            dummyVariable2 = new LocalVariableInfo(Collections.<ModifierInfo> emptySet(),
                    getDummyVariableName(), qualifiedExpression.getType(), ownerSpace, fromLine,
                    fromColumn - 5, toLine, toColumn - 5);
            dummyVariableDeclarationStatement2 = new VariableDeclarationStatementInfo(
                    LocalVariableUsageInfo.getInstance(dummyVariable2, false, true,
                            outerCallableUnit, fromLine, fromColumn - 5, toLine, toColumn - 5),
                    qualifiedExpression, fromLine, fromColumn - 5, toLine, toColumn - 5);
            dummyVariableUsage2 = LocalVariableUsageInfo.getInstance(dummyVariable2, true, false,
                    outerCallableUnit, fromLine, fromColumn, toLine, toColumn);
        } else {
            dummyVariable2 = null;
            dummyVariableDeclarationStatement2 = null;
            dummyVariableUsage2 = null;
        }

        // qualifiedExpression�@�� indexExpression�@�����ɕ������ꂽ�Ƃ�
        if (CFGUtility.isDissolved(qualifiedExpression) && CFGUtility.isDissolved(indexExpression)) {

            // �Â��m�[�h���폜
            nodeFactory.removeNode(statement);
            this.remove();

            // �_�~�[�ϐ��𗘗p����ArrayElementUsageInfo�C����т����p������������쐬
            final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                    dummyVariableUsage2, dummyVariableUsage1, outerCallableUnit, fromLine,
                    fromColumn, toLine, toColumn);
            final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                    variableDeclaration, newArrayElementUsage, fromLine, fromColumn, toLine,
                    toColumn);

            final CFGNode<?> indexExpressionNode = nodeFactory
                    .makeNormalNode(dummyVariableDeclarationStatement1);
            final CFGNode<?> qualifiedExpressionNode = nodeFactory
                    .makeNormalNode(dummyVariableDeclarationStatement2);
            final CFGNode<?> arrayElementUsageNode = nodeFactory.makeNormalNode(newStatement);

            final CFGEdge newEdge1 = new CFGNormalEdge(indexExpressionNode, qualifiedExpressionNode);
            indexExpressionNode.addForwardEdge(newEdge1);
            qualifiedExpressionNode.addBackwardEdge(newEdge1);

            final CFGEdge newEdge2 = new CFGNormalEdge(qualifiedExpressionNode,
                    arrayElementUsageNode);
            qualifiedExpressionNode.addForwardEdge(newEdge2);
            arrayElementUsageNode.addBackwardEdge(newEdge2);

            for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
                final CFGNode<?> backwardNode = backwardEdge.getFromNode();
                final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(indexExpressionNode);
                backwardNode.addForwardEdge(newBackwardEdge);
            }
            for (final CFGEdge forwardEdge : this.getForwardEdges()) {
                final CFGNode<?> forwardNode = forwardEdge.getToNode();
                final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(arrayElementUsageNode);
                forwardNode.addBackwardEdge(newForwardEdge);
            }

            // ���o����qualifiedExpression��indexExpression�ɑ΂��Ă͍ċA�I��dissolve�����s
            indexExpressionNode.dissolve(nodeFactory);
            qualifiedExpressionNode.dissolve(nodeFactory);

            return arrayElementUsageNode;
        }

        // indexExpression�̂ݕ��������ꍇ
        else if (CFGUtility.isDissolved(indexExpression)) {

            // �Â��m�[�h���폜
            nodeFactory.removeNode(statement);
            this.remove();

            // �_�~�[�ϐ��𗘗p����ArrayElementUsageInfo�C����т����p������������쐬
            final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                    qualifiedExpression, dummyVariableUsage1, outerCallableUnit, fromLine,
                    fromColumn, toLine, toColumn);
            final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                    variableDeclaration, newArrayElementUsage, fromLine, fromColumn, toLine,
                    toColumn);

            final CFGNode<?> indexExpressionNode = nodeFactory
                    .makeNormalNode(dummyVariableDeclarationStatement1);
            final CFGNode<?> arrayElementUsageNode = nodeFactory.makeNormalNode(newStatement);

            final CFGEdge newEdge = new CFGNormalEdge(indexExpressionNode, arrayElementUsageNode);
            indexExpressionNode.addForwardEdge(newEdge);
            arrayElementUsageNode.addBackwardEdge(newEdge);

            for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
                final CFGNode<?> backwardNode = backwardEdge.getFromNode();
                final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(indexExpressionNode);
                backwardNode.addForwardEdge(newBackwardEdge);
            }
            for (final CFGEdge forwardEdge : this.getForwardEdges()) {
                final CFGNode<?> forwardNode = forwardEdge.getToNode();
                final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(arrayElementUsageNode);
                forwardNode.addBackwardEdge(newForwardEdge);
            }

            // ���o����indexExpression�ɑ΂��Ă͍ċA�I��dissolve�����s
            indexExpressionNode.dissolve(nodeFactory);

            return arrayElementUsageNode;
        }

        // qualifiedExpression�̂ݕ��������ꍇ
        else if (CFGUtility.isDissolved(qualifiedExpression)) {

            // �Â��m�[�h���폜
            nodeFactory.removeNode(statement);
            this.remove();

            // �_�~�[�ϐ��𗘗p����ArrayElementUsageInfo�C����т����p������������쐬
            final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                    dummyVariableUsage2, indexExpression, outerCallableUnit, fromLine, fromColumn,
                    toLine, toColumn);
            final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                    variableDeclaration, newArrayElementUsage, fromLine, fromColumn, toLine,
                    toColumn);

            final CFGNode<?> qualifiedExpressionNode = nodeFactory
                    .makeNormalNode(dummyVariableDeclarationStatement2);
            final CFGNode<?> arrayElementUsageNode = nodeFactory.makeNormalNode(newStatement);

            final CFGEdge newEdge = new CFGNormalEdge(qualifiedExpressionNode,
                    arrayElementUsageNode);
            qualifiedExpressionNode.addForwardEdge(newEdge);
            arrayElementUsageNode.addBackwardEdge(newEdge);

            for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
                final CFGNode<?> backwardNode = backwardEdge.getFromNode();
                final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(qualifiedExpressionNode);
                backwardNode.addForwardEdge(newBackwardEdge);
            }
            for (final CFGEdge forwardEdge : this.getForwardEdges()) {
                final CFGNode<?> forwardNode = forwardEdge.getToNode();
                final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(arrayElementUsageNode);
                forwardNode.addBackwardEdge(newForwardEdge);
            }

            // ���o����qualifiedExpression�ɑ΂��Ă͍ċA�I��dissolve�����s
            qualifiedExpressionNode.dissolve(nodeFactory);

            return arrayElementUsageNode;
        }

        // qualifiedExpression �� indexExpression�@�����ɒ��o����Ȃ������ꍇ�͕����͍s���Ȃ�
        else {
            return null;
        }
    }

    /**
    * �E�ӂ�ArrayInitializerInfo�ł��������𕪉����邽�߂̃��\�b�h
    * 
    * @param arrayInitialier
    * @param nodeFactory
    * @return
    */
    private CFGNode<? extends ExecutableElementInfo> dissolveArrayInitializer(
            final ArrayInitializerInfo arrayInitializer, final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final List<ExpressionInfo> initializers = arrayInitializer.getElementInitializers();

        // ����O�̕�����K�v�ȏ����擾
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int fromColumn = statement.getFromColumn();
        final int toLine = statement.getToLine();
        final int toColumn = statement.getToColumn();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        //�e�C�j�V�����C�U�𕪉����ׂ����`�F�b�N���C�������C��������������V�K�m�[�h���쐬
        int location = initializers.size();
        final List<CFGNode<?>> newNodes = new ArrayList<CFGNode<?>>();
        final List<ExpressionInfo> newInitializers = new ArrayList<ExpressionInfo>();
        for (final ExpressionInfo initializer : initializers) {

            if (CFGUtility.isDissolved(initializer)) {
                final LocalVariableInfo dummyVariable = new LocalVariableInfo(Collections
                        .<ModifierInfo> emptySet(), getDummyVariableName(), initializer.getType(),
                        ownerSpace, fromLine, fromColumn - location, toLine, toColumn - location);
                final VariableDeclarationStatementInfo dummyVariableDeclarationStatement = new VariableDeclarationStatementInfo(
                        LocalVariableUsageInfo.getInstance(dummyVariable, false, true,
                                outerCallableUnit, fromLine, fromColumn - location, toLine,
                                toColumn - location), initializer, fromLine, fromColumn - location,
                        toLine, toColumn - location);
                final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo
                        .getInstance(dummyVariable, true, false, outerCallableUnit, fromLine,
                                fromColumn, toLine, toColumn);

                final CFGNode<?> newNode = nodeFactory
                        .makeNormalNode(dummyVariableDeclarationStatement);
                newNodes.add(newNode);

                newInitializers.add(dummyVariableUsage);
            }

            else {
                newInitializers.add(initializer);
            }

            location--;
        }

        // arrayInitializer���č\�z
        if (0 < newNodes.size()) {

            // �Â��m�[�h���폜
            nodeFactory.removeNode(statement);
            this.remove();

            final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(
                    newInitializers, outerCallableUnit, fromLine, fromColumn, toLine, toColumn);
            final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                    variableDeclaration, newArrayInitializer, fromLine, fromColumn, toLine,
                    toColumn);

            for (int index = 1; index < newNodes.size(); index++) {
                final CFGNode<?> fromNode = newNodes.get(index - 1);
                final CFGNode<?> toNode = newNodes.get(index);
                final CFGEdge edge = new CFGNormalEdge(fromNode, toNode);
                fromNode.addForwardEdge(edge);
                toNode.addBackwardEdge(edge);
            }

            final CFGNode<?> fromNode = newNodes.get(newNodes.size() - 1);
            final CFGNode<?> toNode = nodeFactory.makeNormalNode(newStatement);
            final CFGEdge edge = new CFGNormalEdge(fromNode, toNode);
            fromNode.addForwardEdge(edge);
            toNode.addBackwardEdge(edge);

            // ���o�������ɂ��Ă͍ċA�I��dissolve�����s
            for (final CFGNode<?> node : newNodes) {
                node.dissolve(nodeFactory);
            }

            return toNode;

        } else {
            return null;
        }
    }
}
