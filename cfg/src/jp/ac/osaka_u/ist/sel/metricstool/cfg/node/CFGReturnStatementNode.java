package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;


/**
 * return����\���m�[�h
 * 
 * @author higo
 * 
 */
public class CFGReturnStatementNode extends CFGStatementNode<ReturnStatementInfo> {

    /**
     * ��������m�[�h�ɑΉ�����return����^���ď�����
     * 
     * @param returnStatement
     */
    CFGReturnStatementNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }

    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final ReturnStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getReturnedExpression();

        // assert�̔��蕔�����ϐ��g�p�̎��łȂ��ꍇ�͕������s��
        if (!CFGUtility.isDissolved(expression)) {
            return null;
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // ����O�̕�����K�v�ȏ����擾
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        this.makeDissolvedNode(expression, nodeFactory, dissolvedNodeList,
                dissolvedVariableUsageList);
        final ReturnStatementInfo newStatement = this.makeNewElement(ownerSpace,
                dissolvedVariableUsageList.getFirst());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    @Override
    ExpressionInfo getDissolvingTarget() {
        final ReturnStatementInfo statement = this.getCore();
        return statement.getReturnedExpression();
    }

    @Override
    ReturnStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if (1 != requiredExpression.length) {
            throw new IllegalArgumentException();
        }

        final ReturnStatementInfo statement = this.getCore();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();

        final ReturnStatementInfo newStatement = new ReturnStatementInfo(ownerSpace,
                requiredExpression[0], fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());
        return newStatement;
    }
}
