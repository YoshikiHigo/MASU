package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection;


import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * �i���󋵂�񍐂��� {@link ProgressReporter} �Ƃ�����󂯎�� {@link ProgressListener}�̋��n��������N���X.
 * �v���O�C���P�΂��Ă��̃N���X�̃C���X�^���X���P�����.
 * �P�̃C���X�^���X�ɑ΂��āC�����̃��X�i�[���o�^�ł���.
 * <p>
 * {@link ProgressReporter}����������N���X�́C {@link #getConnection(AbstractPlugin)}���\�b�h��
 * �񍐂���v���O�C���C���X�^���X��n�����ƂŁC���̃N���X�̃C���X�^���X���擾����.
 * ���ɁC {@link #connect(ProgressReporter)}���\�b�h�Ɏ������g��n�����ƂŁC
 * �R�l�N�V�������m������.
 * <p>
 *  {@link ProgressListener}����������N���X�́C{@link #getConnection(AbstractPlugin)}���\�b�h��
 *  �񍐂��󂯎�肽���v���O�C���C���X�^���X��n�����ƂŁC���̃N���X�̃C���X�^���X���擾���C
 *  ���ɁC {@link #addProgressListener(ProgressListener)}���\�b�h�Ɏ��g��n�����ƂŁC
 *  �R�l�N�V�������m������.
 * <p>
 * ���ʌ��������X���b�h�́C���̃N���X�̃C���X�^���X�ɑ΂��āC {@link #disconnect()}���\�b�h���Ăяo�����ƂŁC
 * �R�l�N�V�����������I�ɉ��������邱�Ƃ��ł���.
 * �R�l�N�V�������������ꂽ���Ƃ̓��X�i�[���ɂ͑����ɒʒm����C�v���O�C�����ɂ͎���ȍ~�̐i���񍐎��� {@link ProgressConnectionException}���X���[�����
 * 
 * @author kou-tngt
 *
 */
public final class ProgressConnector {

    /**
     * �t�@�N�g�����\�b�h.
     * �����Ƀv���O�C���C���X�^���X��^���邱�ƂŁC���̃v���O�C������̐i���񍐂����n������R�l�N�^���쐬
     * @param plugin �v���O�C���C���X�^���X
     * @return plugin�C���X�^���X����̐i���񍐂����n������R�l�N�^
     */
    public static synchronized ProgressConnector getConnection(final AbstractPlugin plugin) {
        if (connectionsMap.containsKey(plugin)) {
            //�}�b�v�ɃC���X�^���X���o�^����Ă����̂ŁC���̂܂ܕԂ�
            return connectionsMap.get(plugin);
        } else {
            //�Ȃ������̂ŐV��������ēo�^���ĕԂ�.
            final ProgressConnector connection = new ProgressConnector(plugin);
            connectionsMap.put(plugin, connection);
            return connection;
        }
    }

    /**
     * �i���񍐂��󂯎�郊�X�i�[��o�^����
     * @param listener �i���񍐂��󂯎�郊�X�i�[
     * @throws NullPointerException�@listner��null�̏ꍇ
     */
    public final synchronized void addProgressListener(final ProgressListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }
        this.listeners.add(listener);
    }

    /**
     * �R�l�N�V�����������I�ɉ������郁�\�b�h
     * @throws AccessControlException ���̃��\�b�h���Ăяo�����X���b�h�����ʌ����������Ȃ��ꍇ
     */
    public final synchronized void disconnect() {
        //�A�N�Z�X���`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        //�����ς݃t���O�𗧂ĂāC�v���O�C������̃��|�[�^�[��null�ɂ���
        this.connectionState = STATE.DISCONNECTED;
        this.reporter = null;
        
        //���X�i�[�ɒʒm���Ă���S�폜
        for(ProgressListener listener: listeners){
            listener.disconnected();
        }
        this.listeners.clear();
    }

    /**
     * ���X�i�[���폜����
     * @param listener�@�폜���郊�X�i�[
     */
    public final synchronized void removeProgressListener(final ProgressListener listener) {
        if (null != listener) {
            this.listeners.remove(listener);
        }
    }

    /**
     * �����ɗ^����ꂽ�v���O�C�����̃��|�[�^�[�Ƃ̐ڑ����m������
     * @param reporter �ڑ����郌�|�[�^�[
     * @throws AlreadyConnectedException �ʂ�reporter�Ƃ̐ڑ����m������Ă��鎞�ɁC�����v���O�C������̕ʂ̃��|�[�^�[���ڑ����Ă����ꍇ
     * @throws NullPointerException�@reporter��null�̏ꍇ
     */
    synchronized void connect(final ProgressReporter reporter) throws AlreadyConnectedException {
        if (null == reporter) {
            throw new NullPointerException("reporter is null.");
        }

        if (null != this.reporter) {
            //���̃��|�[�^�[�Ƃ̐ڑ����m����
            throw new AlreadyConnectedException("New progress connection was refused.");
        }

        this.reporter = reporter;
        this.connectionState = STATE.CONNECTED;
    }

    /**
     * �i������񍐂���
     * 
     * ���̃p�b�P�[�W�ȊO����͌Ăяo���Ȃ�.
     * ���̃��\�b�h���Ăяo�����́C�����̐������͎��O�Ƀ`�F�b�N���Ă����Ȃ���΂Ȃ�Ȃ�.
     * 
     * @param percentage �i�����i%�j
     * @throws DisconnectedException �R�l�N�V�������ؒf����Ă���ꍇ
     * @throws PluginConnectionException �R�l�N�V�������m������Ă��Ȃ��ꍇ
     */
    void reportProgress(final int percentage) throws DisconnectedException,PluginConnectionException{
        if (STATE.INIT == this.connectionState) {
            throw new PluginConnectionException("No Connection was created.");
        } else if (STATE.DISCONNECTED == this.connectionState) {
            throw new DisconnectedException("Already disconnected.");
        }

        //�R�l�N�V�����Ǘ��̖{���Ƃ͊O���̂ŁC��O�ł͂Ȃ��A�T�[�V�����ň����̐��������`�F�b�N
        //�Ăяo�����ň����`�F�b�N����O���������Ă����ׂ�
        assert (0 <= percentage && 100 >= percentage) : "Illegal parameter : percentage was "
                + percentage;

        if (STATE.CONNECTED == this.connectionState) {
            //�ڑ����Ȃ̂ŃC�x���g������ă��X�i�ɓ�����
            this.fireProgress(new ProgressEvent(this.plugin, percentage));
        }
    }

    /**
     * ���X�i�ɐi������ʒm���郁�\�b�h
     * @param event�@�ʒm����C�x���g
     */
    private void fireProgress(final ProgressEvent event) {
        if (null == event) {
            throw new NullPointerException("event is null.");
        }

        synchronized (this) {
            for (final ProgressListener listener : this.listeners) {
                listener.updataProgress(event);
            }
        }
    }

    /**
     * private �R���X�g���N�^.
     * �����Ƀv���O�C���C���X�^���X�����.
     * @param plugin �v���O�C���C���X�^���X
     */
    private ProgressConnector(final AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * �ڑ���Ԃ�\��
     * ��ԑJ�ڂ� INIT -> CONNECTED -> DISCONNECTED -> CONNECTED -> ...
     * @author kou-tngt
     */
    private static enum STATE {
        INIT, CONNECTED, DISCONNECTED
    };

    /**
     * ���̃N���X�̃C���X�^���X���Ǘ�����Map
     */
    private static final Map<AbstractPlugin, ProgressConnector> connectionsMap = new HashMap<AbstractPlugin, ProgressConnector>();

    /**
     * ���X�i�[���Ǘ�����Set.
     * �����ɂ����Q�Ƃ������Ă��Ӗ��������̂ŁC��Q�ƂŎ���.
     */
    private final Set<ProgressListener> listeners = new WeakHashSet<ProgressListener>();

    /**
     * ���̃C���X�^���X�̐ڑ����
     */
    private STATE connectionState = STATE.INIT;

    /**
     * ���̃C���X�^���X���ڑ�����v���O�C��
     */
    private final AbstractPlugin plugin;

    /**
     * ���̃C���X�^���X�ɒ��ڐi���񍐂����Ă��郊�|�[�^�[
     */
    private ProgressReporter reporter;

}
