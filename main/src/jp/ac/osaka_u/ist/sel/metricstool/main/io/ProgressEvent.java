package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventObject;


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
    public ProgressEvent(final ProgressSource source, final int value) {
        super(source);
        this.value = value;
        this.source = source;
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
    public ProgressSource getSource(){
        return this.source;
    }

    
    private final ProgressSource source;
    /**
     * �i���󋵂�\���l�i%�j
     */
    private final int value;

}
