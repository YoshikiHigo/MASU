package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


/**
 * �f�t�H���g��CFG�m�[�h�t�@�N�g��
 * ���̃t�@�N�g���Ő������ꂽ�m�[�h�̓}�b�v�ŊǗ������D
 * @author t-miyake
 *
 */
public class DefaultCFGNodeFactory implements ICFGNodeFactory {

    /**
     * �������ꂽ�m�[�h���Ǘ�����}�b�v
     */
    private final Map<StatementInfo, CFGNode<? extends StatementInfo>> statementToNodeMap;

    /**
     * �I�u�W�F�N�g�𐶐�
     */
    public DefaultCFGNodeFactory() {
        this.statementToNodeMap = new HashMap<StatementInfo, CFGNode<? extends StatementInfo>>();
    }

    /**
     * �����̃m�[�h�t�@�N�g����^���ď������D
     * ���̃R���X�g���N�^�Ő��������I�u�W�F�N�g�͊����̃t�@�N�g���̃m�[�h�\�z�����p������D
     * @param nodeFactory �����̃m�[�h�t�@�N�g��
     */
    public DefaultCFGNodeFactory(final DefaultCFGNodeFactory nodeFactory) {
        if (null == nodeFactory) {
            throw new NullPointerException("nodeFactory is null");
        }

        this.statementToNodeMap = new HashMap<StatementInfo, CFGNode<? extends StatementInfo>>(
                nodeFactory.statementToNodeMap);
    }

    public CFGNode<? extends StatementInfo> makeNode(StatementInfo statement) {
        CFGNode<? extends StatementInfo> node = this.getNode(statement);

        if (null != node) {
            return node;
        }

        if (statement instanceof SingleStatementInfo) {
            node = new CFGStatementNode((SingleStatementInfo) statement);
            this.statementToNodeMap.put(statement, node);
        } else if (statement instanceof ConditionalBlockInfo) {
            node = new CFGControlNode((ConditionalBlockInfo) statement);
            this.statementToNodeMap.put(statement, node);
        } else {
            node = new CFGEmptyNode(statement);
        }

        return node;
    }

    public CFGNode<? extends StatementInfo> getNode(final StatementInfo statement) {
        return this.statementToNodeMap.get(statement);
    }
}
