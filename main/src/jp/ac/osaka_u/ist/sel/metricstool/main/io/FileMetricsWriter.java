package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;


/**
 * �t�@�C�����g���N�X�������o���N���X���������Ȃ���΂Ȃ�Ȃ��C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface FileMetricsWriter extends MetricsWriter {

    /**
     * �t�@�C�����̃^�C�g��������
     */
    String FILE_NAME = new String("\"File Name\"");

    /**
     * �t�@�C�����g���N�X��ۑ����Ă���萔
     */
    FileMetricsInfoManager FILE_METRICS_MANAGER = DataManager.getInstance()
            .getFileMetricsInfoManager();

}
