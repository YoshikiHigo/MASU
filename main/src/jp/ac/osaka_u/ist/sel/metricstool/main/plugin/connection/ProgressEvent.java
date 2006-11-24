package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection;


import java.util.EventObject;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * �i�����C�x���g
 * 
 * @author kou-tngt
 *
 */
public class ProgressEvent extends EventObject {

    /**
     * �R���X�g���N�^
     * 
     * @param source �i���󋵂𑗂����v���O�C��
     * @param value �i���󋵂�\���l(%)
     */
    public ProgressEvent(final AbstractPlugin source, final int value) {
        super(source);
        this.value = value;
        this.plugin = source;
    }

    /**
     * �i���󋵂����o��
     * 
     * @return ���̃C�x���g���\���i���󋵒l
     */
    public int getProgressValue() {
        return this.value;
    }
    
    /**
     * ���̃C�x���g�𔭍s�����v���O�C����Ԃ�
     * @return ���̃C�x���g�𔭍s�����v���O�C��
     * 
     * @see java.util.EventObject#getSource()
     */
    public AbstractPlugin getSource(){
        return this.plugin;
    }

    
    private final AbstractPlugin plugin;
    /**
     * �i���󋵂�\���l�i%�j
     */
    private final int value;

}
