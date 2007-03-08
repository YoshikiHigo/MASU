package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public class UnknownEntityUsageInfo extends EntityUsageInfo {

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��Ԃ�
     * 
     * @return ���̃N���X�̒P��I�u�W�F�N�g
     */
    public static UnknownEntityUsageInfo getInstance() {
        return SINGLETON;
    }

    @Override
    public TypeInfo getType() {
        return UnknownTypeInfo.getInstance();
    }

    private UnknownEntityUsageInfo() {
    }

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g��ۑ����邽�߂̒萔
     */
    private static final UnknownEntityUsageInfo SINGLETON = new UnknownEntityUsageInfo();
}
