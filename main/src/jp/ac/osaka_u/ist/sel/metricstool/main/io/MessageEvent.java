package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventObject;


/**
 * ���b�Z�[�W�C�x���g�N���X
 * 
 * @author kou-tngt
 *
 */
public class MessageEvent extends EventObject {

    /**
     * �R���X�g���N�^
     * @param source ���b�Z�[�W���M��
     * @param message ���b�Z�[�W
     */
    public MessageEvent(final MessageSource source, final String message) {
        super(source);
        this.source = source;
        this.message = message;
    }

    /**
     * ���b�Z�[�W���擾���郁�\�b�h
     * @return ���b�Z�[�W
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * ���b�Z�[�W���M�҂��擾���郁�\�b�h
     * @return ���b�Z�[�W���M��
     * @see java.util.EventObject#getSource()
     */
    @Override
    public MessageSource getSource() {
        return this.source;
    }

    /**
     * ���b�Z�[�W���M��
     */
    private final MessageSource source;

    /**
     * ���b�Z�[�W
     */
    private final String message;

}
