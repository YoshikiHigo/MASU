package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;

/**
 * CFG�̐���m�[�h��\���N���X
 * 
 * @author t-miyake, higo
 * 
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

	CFGControlNode(final ConditionInfo condition) {
		super(condition);
	}

	@Override
	public void optimize() {
	}
}
