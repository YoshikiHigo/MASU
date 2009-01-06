package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;


/**
 * ���g���N�X�������o��j�N���X���������Ȃ���΂Ȃ�Ȃ��C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface MetricsWriter {

    /**
     * ���g���N�X���t�@�C���ɏ����o��
     */
    public abstract void write();

    /**
     * �v���O�C�����Ǘ����Ă���I�u�W�F�N�g���w���萔
     */
    PluginManager PLUGIN_MANAGER = DataManager.getInstance().getPluginManager();

    /**
     * ���g���N�X�l���Ȃ����Ƃ�\������
     */
    char NO_METRIC = '-';

}