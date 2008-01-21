package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;


/**
 * ���\�b�h���g���N�X��o�^���邽�߂̃f�[�^�N���X
 * 
 * @author higo
 * 
 */
public final class MethodMetricsInfo extends MetricsInfo<MethodInfo> {

    /**
     * �v���ΏۃI�u�W�F�N�g��^���ď�����
     * 
     * @param �v���Ώۃ��\�b�h
     */
    public MethodMetricsInfo(final MethodInfo method) {
        super(method);
    }

    /**
     * ���b�Z�[�W�̑��M�Җ���Ԃ�
     * 
     * @return ���b�Z�[�W�̑��M�Җ�
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }
}
