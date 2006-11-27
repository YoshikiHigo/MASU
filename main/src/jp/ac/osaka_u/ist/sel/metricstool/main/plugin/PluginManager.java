package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ConcurrentHashSet;


/**
 * @author kou-tngt
 *
 * �v���O�C���C���X�^���X���Ǘ�����N���X�D
 * 
 */
public class PluginManager {

    /**
     * �V���O���g���C���X�^���X���擾����
     * @return �V���O���g���C���X�^���X
     */
    public static PluginManager getInstance() {
        return SINGLETON;
    }

    /**
     * �v���O�C����o�^����
     * ���̃��\�b�h���Ăяo���ɂ͓��ʌ������K�v�ł���
     * @param plugin �o�^����v���O�C��
     * @throws AccessControlException ���ʌ����X���b�h�łȂ��ꍇ
     * @throws NullPointerException plugin��null�̏ꍇ
     */
    public void addPlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        this.plugins.add(plugin);
        this.pluginInfos.add(plugin.getPluginInfo());
    }

    /**
     * �v���O�C����o�^����
     * ���̃��\�b�h���Ăяo���ɂ͓��ʌ������K�v�ł���
     * @param collection �o�^����v���O�C���Q�����R���N�V����
     * @throws AccessControlException ���ʌ����X���b�h�łȂ��ꍇ
     * @throws NullPointerException collection��null�̏ꍇ
     */
    public void addPlugins(final Collection<AbstractPlugin> collection) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == collection) {
            throw new NullPointerException("collection is null.");
        }

        for (final AbstractPlugin plugin : collection) {
            this.addPlugin(plugin);
        }
    }

    /**
     * �v���O�C���̕ҏW�s��Set��Ԃ�
     * ���ʌ��������X���b�h�ȊO����͌Ăяo���Ȃ�
     * @return �v���O�C����Set
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��X���b�h����̌Ăяo���̏ꍇ
     */
    public Set<AbstractPlugin> getPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.plugins);
    }

    /**
     * �v���O�C�����̕ҏW�s��Set��Ԃ�
     * @return �v���O�C������Set
     */
    public Set<PluginInfo> getPluginInfos() {
        return Collections.unmodifiableSet(this.pluginInfos);
    }

    /**
     * �v���O�C�����폜����
     * ���ʌ����X���b�h�݂̂���Ăяo����.
     * @param plugin �폜����v���O�C��
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��ꍇ
     */
    public void removePlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null != plugin) {
            this.plugins.remove(plugin);
        }
    }

    /**
     * �V���O���g���p�C���private�R���X�g���N�^
     */
    private PluginManager() {
    };

    /**
     * �v���O�C����Set
     */
    private final Set<AbstractPlugin> plugins = new ConcurrentHashSet<AbstractPlugin>();

    /**
     * �v���O�C������Set
     */
    private final Set<PluginInfo> pluginInfos = new ConcurrentHashSet<PluginInfo>();

    /**
     * �V���O���g���C���X�^���X
     */
    private static final PluginManager SINGLETON = new PluginManager();
}
