package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;


/**
 * assert����\��CFG�m�[�h
 * 
 * @author higo
 *
 */
public class CFGAssertStatementNode extends CFGStatementNode<AssertStatementInfo> {

    CFGAssertStatementNode(final AssertStatementInfo statement) {
        super(statement);
    }

    /**
     * assert���𕪉�����
     */
    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        if (null == nodeFactory) {
            throw new IllegalArgumentException();
        }

        final AssertStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getAssertedExpression();

        // assert�̔��蕔���������̕K�v���Ȃ��ꍇ�͉��������ɔ�����
        if (!CFGUtility.isDissolved(expression)) {
            return null;
        }

        // ����O�̕�����K�v�ȏ����擾
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        this.makeDissolvedNode(expression, nodeFactory, dissolvedNodeList,
                dissolvedVariableUsageList);
        final AssertStatementInfo newStatement = this.makeNewElement(ownerSpace,
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
        final AssertStatementInfo statement = this.getCore();
        return statement.getAssertedExpression();
    }

    @Override
    AssertStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if (1 != requiredExpression.length) {
            throw new IllegalArgumentException();
        }

        final AssertStatementInfo statement = this.getCore();
        final ExpressionInfo messageExpression = statement.getMessageExpression();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();

        final AssertStatementInfo newStatement = new AssertStatementInfo(ownerSpace,
                requiredExpression[0], messageExpression, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        return newStatement;
    }
}
