package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * �t�@�C�����g���N�X��CSV�@�C���ɏ����o���N���X
 * 
 * @author y-higo
 * 
 */
public final class CSVFileMetricsWriter implements FileMetricsWriter, CSVWriter, MessageSource {

    /**
     * CSV�t�@�C����^����
     * 
     * @param fileName CSV�t�@�C����
     */
    public CSVFileMetricsWriter(final String fileName) {

        if (null == fileName) {
            throw new NullPointerException();
        }

        this.fileName = fileName;
    }

    /**
     * �t�@�C�����g���N�X��CSV�t�@�C���ɏ����o��
     */
    public void write() {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName));

            // ���g���N�X���Ȃǂ������o��
            writer.write(FILE_NAME);
            for (AbstractPlugin plugin : PLUGIN_MANAGER.getPlugins()) {
                PluginInfo pluginInfo = plugin.getPluginInfo();
                if (METRIC_TYPE.FILE_METRIC == pluginInfo.getMetricType()) {
                    String metricName = pluginInfo.getMetricName();
                    writer.write(SEPARATOR);
                    writer.write(metricName);
                }
            }

            // ���g���N�X�l�������o��
            for (FileMetricsInfo fileMetricsInfo : FILE_METRICS_MANAGER) {
                FileInfo fileInfo = fileMetricsInfo.getFileInfo();

                String fileName = fileInfo.getName();
                writer.write(fileName);
                for (AbstractPlugin plugin : PLUGIN_MANAGER.getPlugins()) {
                    PluginInfo pluginInfo = plugin.getPluginInfo();
                    if (METRIC_TYPE.FILE_METRIC == pluginInfo.getMetricType()) {

                        try {
                            writer.write(SEPARATOR);
                            Float value = fileMetricsInfo.getMetric(plugin);
                            writer.write(value.toString());
                        } catch (MetricNotRegisteredException e) {
                            writer.write(NO_METRIC);
                        }
                    }
                }
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {

            MessagePrinter printer = new DefaultMessagePrinter(this,
                    MessagePrinter.MESSAGE_TYPE.ERROR);
            printer.println("IO Error Happened on " + this.fileName);
        }
    }

    /**
     * MessagerPrinter ��p���邽�߂ɕK�v�ȃ��\�b�h
     * @see MessagePrinter
     * @see MessageSource
     * 
     * @return ���b�Z�[�W���M�Җ���Ԃ�
     */
    public String getMessageSourceName() {
        return this.getClass().toString();
    }

    /**
     * �t�@�C�����g���N�X�����������t�@�C������ۑ����邽�߂̃��g���N�X
     */
    private final String fileName;
}
