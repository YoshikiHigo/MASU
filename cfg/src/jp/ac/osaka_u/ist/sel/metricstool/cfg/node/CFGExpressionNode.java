package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;

/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionNode extends CFGNormalNode<ConditionInfo> {

	/**
	 * ��������m�[�h�ɑΉ����镶��^���ď�����
	 * 
	 * @param statement
	 *            ��������m�[�h�ɑΉ����镶
	 */
	CFGExpressionNode(final ConditionInfo expression) {
		super(expression);
	}
}
