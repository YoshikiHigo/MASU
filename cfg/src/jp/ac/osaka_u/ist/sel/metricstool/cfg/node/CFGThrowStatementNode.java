package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;

/**
 * throw����\���m�[�h
 * 
 * @author higo
 * 
 */
public class CFGThrowStatementNode extends CFGStatementNode {

	/**
	 * �m�[�h�𐶐�����throw����^���ď�����
	 * 
	 * @param throwStatement
	 */
	CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
		super(throwStatement);
	}
}
