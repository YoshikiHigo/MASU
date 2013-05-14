package sdl.ist.osaka_u.newmasu.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;


/**
 * CFG�̕��m�[�h��\���N���X
 * 
 * @author t-miyake
 * 
 */
public abstract class CFGStatementNode<T extends SingleStatementInfo> extends CFGNormalNode<T> {

    /**
     * ��������m�[�h�ɑΉ����镶��^���ď���
     * 
     * @param statement
     *            ��������m�[�h�ɑΉ����镶
     */
    CFGStatementNode(final T statement) {
        super(statement);
    }
}
