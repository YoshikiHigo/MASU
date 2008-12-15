package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;


/**
 * �t�B�[���h���g���N�X�������o���N���X���������Ȃ���΂Ȃ�Ȃ��C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface FieldMetricsWriter extends MetricsWriter {

    /**
     * �t�B�[���h���̃^�C�g��������
     */
    String FIELD_NAME = new String("\"Field Name\"");

    /**
     * �t�B�[���h���g���N�X��ۑ����Ă���萔
     */
    FieldMetricsInfoManager FIELD_METRICS_MANAGER = DataManager.getInstance()
            .getFieldMetricsInfoManager();

}
