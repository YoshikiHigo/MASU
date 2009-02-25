package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;


/**
 * CFG��ConditionInfo�̃m�[�h��\���N���X
 * @author higo
 *
 */
public class CFGConditionNode extends CFGNode<ConditionInfo> {

    /**
     * ��������m�[�h�ɑΉ�����ConditionInfo��^���ď�����
     * @param condition ��������m�[�h�ɑΉ�����ConditionInfo
     */
    public CFGConditionNode(final ConditionInfo condition) {
        super(condition);
        this.text = condition.getText() + "<" + condition.getFromLine() + ">";
    }
}
