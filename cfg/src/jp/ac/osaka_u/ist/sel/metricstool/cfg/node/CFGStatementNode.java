package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;

/**
 * CFG�̕��m�[�h��\���N���X
 * 
 * @author t-miyake
 * 
 */
public class CFGStatementNode extends CFGNormalNode<SingleStatementInfo> {

	/**
	 * ��������m�[�h�ɑΉ����镶��^���ď�����
	 * 
	 * @param statement
	 *            ��������m�[�h�ɑΉ����镶
	 */
	CFGStatementNode(final SingleStatementInfo statement) {
		super(statement);
	}
}
