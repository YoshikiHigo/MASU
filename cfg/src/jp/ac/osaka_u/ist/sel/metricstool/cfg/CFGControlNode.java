package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;


/**
 * CFG�̐���m�[�h��\���N���X
 * @author t-miyake, higo
 *
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

    public CFGControlNode(final ConditionInfo condition) {
        super(condition);
    }

    @Override
    protected void optimize() {
    }
}
