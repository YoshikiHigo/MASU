package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;


/**
 * CFG�̐���m�[�h��\���N���X
 * @author t-miyake
 *
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

    /**
     * ��������m�[�h�ɑΉ����鐧�䕶��^���ď�����
     * @param controlStatement ��������m�[�h�ɑΉ����鐧�䕶
     */
    CFGControlNode(final ConditionInfo condition) {
        super(condition);
    }
}
