package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ClosableLinkedBlockingQueue;


/**
 * �v���O�C�������s���郉���`���[
 * �قƂ�ǂ̃p�u���b�N���\�b�h�̎��s�ɓ��ʌ�����K�v�Ƃ���.
 * @author kou-tngt
 *
 */
public final class DefaultPluginLauncher implements PluginLauncher {

    /**
     * �v���O�C���̎��s���L�����Z�����郁�\�b�h.
     * ���ʌ��������X���b�h���炵�����s�ł��Ȃ�.
     * @param plugin �L�����Z������v���O�C��
     * @return �L�����Z���ł����ꍇ��true�ł��Ȃ�������C���łɏI�����Ă����ꍇ��false
     * @throws NullPointerException plugin��null�̏ꍇ
     * @throws AccessControlException ���ʌ����������Ȃ��ꍇ
     */
    public boolean cancel(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        if (this.futureMap.containsKey(plugin)) {
            final Future<Boolean> future = this.futureMap.get(plugin);
            this.futureMap.remove(plugin);
            return future.cancel(true);
        }
        return false;
    }

    /**
     * ���s���܂Ƃ߂ăL�����Z�����郁�\�b�h.
     * @param plugins �L�����Z������v���O�C���Q���܂ރR���N�V����
     * @throws NullPointerException plugins��null�̏ꍇ
     */
    public void cancelAll(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        for (final AbstractPlugin plugin : plugins) {
            this.cancel(plugin);
        }
    }

    /**
     * ���s���C���s�҂��̃^�X�N��S�ăL�����Z������.
     */
    public void cancelAll() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        for (final AbstractPlugin plugin : this.futureMap.keySet()) {
            this.cancel(plugin);
        }
    }

    /**
     * ���ݎ��s���̃v���O�C���̐���Ԃ����\�b�h.
     * @return ���s���̃v���O�C���̐�.
     */
    public int getCurrentLaunchingNum() {
        return this.threadPool.getActiveCount();
    }

    /**
     * ���݂̓������s�ő吔��Ԃ����\�b�h
     * @return �������s�ő吔
     */
    public int getMaximumLaunchingNum() {
        return this.threadPool.getMaximumPoolSize();
    }

    /**
     * �v���O�C�������s���郁�\�b�h.
     * ���ʌ��������X���b�h���炵�����s�ł��Ȃ�.
     * @param plugin ���s����v���O�C��
     * @throws AccessControlException ���ʌ����������Ȃ��X���b�h����Ăяo���ꂽ�ꍇ
     * @throws NullPointerException plugin��null�̏ꍇ
     */
    public void launch(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (this.stoped) {
            throw new IllegalStateException("launcher was already stoped.");
        }
        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        final RunnablePlugin runnablePlugin = new RunnablePlugin(plugin);
        final Future future = this.threadPool.submit(runnablePlugin);
        this.futureMap.put(runnablePlugin.getPlugin(), future);
    }

    /**
     * �v���O�C�����܂Ƃ߂Ď��s���郁�\�b�h.
     * ���ʌ��������X���b�h���炵�����s�ł��Ȃ�.
     * @param plugins ���s����v���O�C���Q���܂ރR���N�V����
     * @throws NullPointerException plugins��null�̏ꍇ
     * @throws AccessControlException ���ʌ����������Ȃ��X���b�h����Ăяo���ꂽ�ꍇ
     */
    public void launchAll(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        for (final AbstractPlugin plugin : plugins) {
            this.launch(plugin);
        }
    }

    /**
     * �������s�ő吔��ݒ肷�郁�\�b�h
     * @param size �������s�ő吔
     * @throws IllegalArgumentException size��0�ȉ��������ꍇ
     */
    public void setMaximumLaunchingNum(final int size) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (1 > size) {
            throw new IllegalArgumentException("parameter size must be natural number.");
        }
        this.threadPool.setCorePoolSize(size);
    }

    /**
     *  �����`���[���I������.
     *  ���s���̃^�X�N�͏I���܂ő҂�.
     */
    public void stopLaunching() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.stoped = true;
        this.workQueue.close();
        this.threadPool.setCorePoolSize(0);
    }

    /**
     * �����`���[���I������.
     * ���s���̃^�X�N���S�ăL�����Z������.
     */
    public void stopLaunchingNow() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.stopLaunching();
        this.cancelAll();
    }

    /**
     * �v���O�C�����s�p�X���b�h�̃t�@�N�g���N���X
     * @author kou-tngt
     *
     */
    private class PluginThreadFactory implements ThreadFactory {
        /**
         * �v���O�C�����s�p�̃X���b�h���쐬���郁�\�b�h.
         * �v���O�C���X���b�h�Ƃ��ēo�^������.
         * @see ThreadFactory#newThread(Runnable)
         */
        public Thread newThread(final Runnable r) {
            final Thread thread = new Thread(this.PLUGIN_THREAD_GROUP, r, "plugin_"
                    + ++this.threadNameCount);
            MetricsToolSecurityManager.getInstance().addPluginThread(thread);
            return thread;
        }

        /**
         * �v���O�C���X���b�h�p�̃X���b�h�O���[�v
         */
        private final ThreadGroup PLUGIN_THREAD_GROUP = new ThreadGroup("PluginThreads");

        /**
         * �X���b�h�̃i���o�����O�p�ϐ�
         */
        private int threadNameCount = 0;
    }

    /**
     * �v���O�C�����X���b�h�P�ʂŎ��s���邽�߂� {@link Runnable} �ȃN���X
     * @author kou-tngt
     *
     */
    private class RunnablePlugin implements Runnable {
        public void run() {
            try {
                //�v���O�C���f�B���N�g���ւ̃t�@�C���A�N�Z�X��v�����Ă���v���O�C����execute���Ăяo��
                //MetricsToolSecurityManager���V�X�e���̃Z�L�����e�B�}�l�[�W���[�ł͂Ȃ��ꍇ�C
                //�t�@�C���A�N�Z�X�������^�����邩�ǂ�����
                //�V�X�e���̃Z�L�����e�B�}�l�[�W���[����
                MetricsToolSecurityManager.getInstance().requestPluginDirAccessPermission(
                        this.plugin);
                this.plugin.execute();
            } catch (final Exception e) {
                (new DefaultMessagePrinter(this.plugin, MessagePrinter.MESSAGE_TYPE.ERROR))
                        .println(e.getMessage());
            }
            DefaultPluginLauncher.this.futureMap.remove(this.plugin);
        }

        /**
         * �R���X�g���N�^
         * @param plugin ���̃N���X�Ŏ��s����v���O�C��
         */
        private RunnablePlugin(final AbstractPlugin plugin) {
            this.plugin = plugin;
        }

        /**
         * ���̃C���X�^���X�Ŏ��s����v���O�C����Ԃ�
         * @return ���̃C���X�^���X�Ŏ��s����v���O�C��
         */
        private AbstractPlugin getPlugin() {
            return this.plugin;
        }

        /**
         * ���̃C���X�^���X�Ŏ��s����v���O�C��
         */
        private final AbstractPlugin plugin;

    }

    /**
     * �e {@link RunnablePlugin} ��Future��ۑ�����}�b�v
     */
    private final Map<AbstractPlugin, Future> futureMap = new ConcurrentHashMap<AbstractPlugin, Future>();

    /**
     * �����`���[���~���ꂽ���ǂ�����\���ϐ�
     */
    private boolean stoped = false;

    /**
     * �X���b�h�v�[���Ɏg�p������L���[
     */
    private final ClosableLinkedBlockingQueue<Runnable> workQueue = new ClosableLinkedBlockingQueue<Runnable>();

    /**
     * �����I�Ɏ��ۂɃX���b�h�����s����X���b�h�v�[��
     */
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Integer.MAX_VALUE,
            Integer.MAX_VALUE, 0, TimeUnit.SECONDS, this.workQueue, new PluginThreadFactory());
}
