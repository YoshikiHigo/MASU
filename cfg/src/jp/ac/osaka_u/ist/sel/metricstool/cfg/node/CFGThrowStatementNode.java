package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Collections;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.SimpleCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;


/**
 * throw����\���m�[�h
 * 
 * @author higo
 * 
 */
public class CFGThrowStatementNode extends CFGStatementNode<ThrowStatementInfo> {

    /**
     * �m�[�h�𐶐�����throw����^���ď�����
     * 
     * @param throwStatement
     */
    CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
        super(throwStatement);
    }

    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final ThrowStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getThrownExpression();

        // assert�̔��蕔�����ϐ��g�p�̎��łȂ��ꍇ�͕������s��
        if (!CFGUtility.isDissolved(expression)) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // �_�~�[�ϐ��̐錾�𐶐�
        final LocalVariableInfo dummyVariable = new LocalVariableInfo(Collections
                .<ModifierInfo> emptySet(), getDummyVariableName(), expression.getType(),
                ownerSpace, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final LocalVariableUsageInfo dummyVariableInitialization = LocalVariableUsageInfo
                .getInstance(dummyVariable, false, true, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo dummyVariableDeclaration = new VariableDeclarationStatementInfo(
                dummyVariableInitialization, expression, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());

        // �_�~�[�ϐ��𗘗p����assert���𐶐�
        final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo.getInstance(
                dummyVariable, true, false, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ThrowStatementInfo newThrowStatement = new ThrowStatementInfo(ownerSpace,
                dummyVariableUsage, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // �V��������CFG�m�[�h���쐬���C�t���[�𐶐�
        final CFGNode<?> definitionNode = nodeFactory.makeNormalNode(dummyVariableDeclaration);
        final CFGNode<?> referenceNode = nodeFactory.makeNormalNode(newThrowStatement);
        final CFGEdge newEdge = new CFGNormalEdge(definitionNode, referenceNode);
        definitionNode.addForwardEdge(newEdge);
        referenceNode.addBackwardEdge(newEdge);
        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = backwardEdge.getFromNode();
            final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(definitionNode);
            backwardNode.addForwardEdge(newBackwardEdge);
        }
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = forwardEdge.getToNode();
            final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(referenceNode);
            forwardNode.addBackwardEdge(newForwardEdge);
        }

        final SimpleCFG newCFG = new SimpleCFG(nodeFactory);
        newCFG.addNode(definitionNode);
        newCFG.addNode(referenceNode);
        newCFG.setEnterNode(definitionNode);
        newCFG.addExitNode(referenceNode);

        // ���o����Expression�ɑ΂��Ă͍ċA�I��dissolve�����s
        final CFG definitionCFG = definitionNode.dissolve(nodeFactory);
        if (null != definitionCFG) {
            newCFG.removeNode(definitionNode);
            newCFG.addNodes(definitionCFG.getAllNodes());
            newCFG.setEnterNode(definitionCFG.getEnterNode());
        }

        return newCFG;
    }
}
