package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;

/**
 * CFG�̕��m�[�h��\���N���X
 * @author t-miyake
 *
 */
public class CFGStatementNode extends CFGNode<SingleStatementInfo> {

    /**
     * ��������m�[�h�ɑΉ����镶��^���ď�����
     * @param statement ��������m�[�h�ɑΉ����镶
     */
    public CFGStatementNode(final SingleStatementInfo statement) {
        super(statement);
    }
    
}
