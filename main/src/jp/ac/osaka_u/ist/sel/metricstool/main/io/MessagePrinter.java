package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * ���b�Z�[�W���o�͕��ɑ��邽�߂̃C���^�t�F�[�X
 * 
 * @author kou-tngt
 *
 */
public interface MessagePrinter {
    /**
     * ���b�Z�[�W�̎��
     * @author kou-tngt
     */
    public static enum MESSAGE_TYPE {
        OUT, ERROR
    };

    /**
     * ���b�Z�[�W�����̂܂܏o�͂���
     * @param message �o�͂��郁�b�Z�[�W
     */
    public void print(String message);

    /**
     * ���b�Z�[�W���o�͂��ĉ��s����
     * @param message �o�͂��郁�b�Z�[�W
     */
    public void println(String message);

    /**
     * �����s�̃��b�Z�[�W�̊ԂɁC���̃��b�Z�[�W�̊��荞�݂��Ȃ��悤�ɏo�͂���.
     * @param messages �o�͂��郁�b�Z�[�W�̔z��
     */
    public void println(String[] messages);
}
