package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;


public interface MetricsWriter {

    /**
     * ���g���N�X���t�@�C���ɏ����o��
     */
    public abstract void write();

    /**
     * �v���O�C�����Ǘ����Ă���I�u�W�F�N�g���w���萔
     */
    PluginManager PLUGIN_MANAGER = PluginManager.getInstance();

    /**
     * ���g���N�X�l���Ȃ����Ƃ�\������
     */
    char NO_METRIC = '-';

}