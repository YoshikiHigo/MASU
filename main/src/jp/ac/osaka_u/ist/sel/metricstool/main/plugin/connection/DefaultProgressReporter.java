package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection;


import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * {@link ProgressReporter}�̃f�t�H���g����
 * �R���X�g���N�^�Ƀv���O�C���C���X�^���X��^����.
 * ���̃N���X�̕����̃C���X�^���X�𓯂��v���O�C�������邱�Ƃ͂ł��Ȃ�.
 * @author kou-tngt
 *
 */
public class DefaultProgressReporter implements ProgressReporter {

    /**
     * �B��̃R���X�g���N�^.
     * �����Ƀv���O�C���C���X�^���X���󂯎��C�񍐗p�̐ڑ����m������
     * @param plugin �i���񍐂�����v���O�C��
     * @throws AlreadyConnectedException ���łɓ����v���O�C���C���X�^���X���ʂ̃��|�[�^�[�Őڑ�������Ă���ꍇ
     */
    public DefaultProgressReporter(final AbstractPlugin plugin) throws AlreadyConnectedException {
        //���̃v���O�C���p�̃R�l�N�^������āC�ڑ�
        this.connector = ProgressConnector.getConnection(plugin);
        this.connector.connect(this);
    }

    /**
     * �i������񍐂��郁�\�b�h
     * @param percentage �i���l�i%�j
     * @throws IllegalArgumentException percentage��0-100�̊Ԃɓ����ĂȂ��ꍇ
     * @throws IllegalStateException percentage���O��񍐂����l��艺�������ꍇ
     * @see ProgressReporter#reportProgress(int)
     */
    public void reportProgress(final int percentage) {
        if (0 > percentage || 100 < percentage) {
            throw new IllegalArgumentException("reported value " + percentage + " was out of range(0-100).");
        }

        if (percentage < this.previousValue) {
            //�O��̕񍐂��l��������
            throw new IllegalStateException("reported value was decreased.");
        }

        if (null != this.connector) {
            try {
                this.previousValue = percentage;
                this.connector.reportProgress(percentage);
            } catch (final PluginConnectionException e) {
                //�ڑ�����ĂȂ������ʌ��������X���b�h�ɐؒf���ꂽ���v���O�C�����s�X���b�h���߁X�i�����j�I���������ʒm���Ȃ�
                this.connector = null;
            }
        }
    }

    /**
     * ���̃��|�[�^�[�̐ڑ���
     */
    private ProgressConnector connector;

    /**
     * 1��O�ɂɕ񍐂����i�����l
     */
    private int previousValue;
}
