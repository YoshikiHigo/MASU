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
        OUT, INFO, WARNING, ERROR
    };

    /**
     * ���b�Z�[�W�����̂܂܏o�͂���
     * @param message �o�͂��郁�b�Z�[�W
     */
    public void print(Object o);

    /**
     * ���s����
     */
    public void println();

    /**
     * ���b�Z�[�W���o�͂��ĉ��s����
     * @param message �o�͂��郁�b�Z�[�W
     */
    public void println(Object o);

    /**
     * �����s�̃��b�Z�[�W�̊ԂɁC���̃��b�Z�[�W�̊��荞�݂��Ȃ��悤�ɏo�͂���.
     * @param messages �o�͂��郁�b�Z�[�W�̔z��
     */
    public void println(Object[] o);
}
