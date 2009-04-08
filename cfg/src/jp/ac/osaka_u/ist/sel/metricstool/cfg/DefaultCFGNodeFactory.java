package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;


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
    private final Map<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>> elementToNodeMap;

    /**
     * �I�u�W�F�N�g�𐶐�
     */
    public DefaultCFGNodeFactory() {
        this.elementToNodeMap = new HashMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>>();
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

        this.elementToNodeMap = new HashMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>>(
                nodeFactory.elementToNodeMap);
    }

    /**
     * ExecutableElementInfo�@���� CFG �̃m�[�}���m�[�h�𐶐�
     */
    public CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(
            final ExecutableElementInfo element) {
        CFGNormalNode<? extends ExecutableElementInfo> node = (CFGNormalNode<? extends ExecutableElementInfo>) this
                .getNode(element);

        if (null != node) {
            return node;
        }

        if (element instanceof SingleStatementInfo) {
            node = new CFGStatementNode((SingleStatementInfo) element);
        } else if (element instanceof CaseEntryInfo) {
            node = new CFGCaseEntryNode((CaseEntryInfo) element);
        } else if (element instanceof ConditionInfo) {
            node = new CFGExpressionNode((ConditionInfo) element);
        } else {
            node = new CFGEmptyNode(element);
        }
        this.elementToNodeMap.put(element, node);

        return node;
    }

    /**
     * ConditionInfo ���� CFG �̃R���g���[���m�[�h�𐶐�
     */
    public CFGControlNode makeControlNode(final ConditionInfo condition) {

        CFGControlNode node = (CFGControlNode) this.getNode(condition);
        if (null != node) {
            return node;
        }

        node = new CFGControlNode(condition);
        this.elementToNodeMap.put(condition, node);

        return node;
    }

    public CFGNode<? extends ExecutableElementInfo> getNode(final ExecutableElementInfo statement) {
        return this.elementToNodeMap.get(statement);
    }
}
