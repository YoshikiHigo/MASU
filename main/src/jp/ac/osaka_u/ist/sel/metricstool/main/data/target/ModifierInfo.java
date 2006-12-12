package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X�C���\�b�h�C�t�B�[���h�Ȃǂ̏C���q��\���N���X�D���݈ȉ��́C�C���q��������
 * <ul>
 * <li>public</li>
 * <li>private</li>
 * <li>virtual(abstract)
 * <li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public class ModifierInfo {

    public ModifierInfo() {
        this.publicInfo = false;
        this.privateInfo = false;
        this.virtualInfo = false;
    }

    /**
     * private ���ǂ�����Ԃ�
     * 
     * @return private �ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isPrivateInfo() {
        return privateInfo;
    }

    /**
     * private ���ǂ������Z�b�g����
     * 
     * @param privateInfo private ���ǂ���
     */
    public void setPrivateInfo(boolean privateInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.privateInfo = privateInfo;
    }

    /**
     * public ���ǂ�����Ԃ�
     * 
     * @return public �ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isPublicInfo() {
        return this.publicInfo;
    }

    /**
     * public ���ǂ������Z�b�g����
     * 
     * @param publicInfo public ���ǂ���
     */
    public void setPublicInfo(boolean publicInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.publicInfo = publicInfo;
    }

    /**
     * virtual (abstract) ���ǂ�����Ԃ�
     * 
     * @return virtual (abstract) �ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isVirtualInfo() {
        return this.virtualInfo;
    }

    /**
     * abstract (virtual) ���ǂ�����Ԃ�
     * 
     * @return abstract (virtual) �ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean isAbstractInfo() {
        return this.virtualInfo;
    }

    /**
     * virtual (abstract) ���ǂ������Z�b�g����
     * 
     * @param virtualInfo virtual (abstract) ���ǂ���
     */
    public void setVirtualInfo(boolean virtualInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.virtualInfo = virtualInfo;
    }

    /**
     * abstract (virtual) ���ǂ������Z�b�g����
     * 
     * @param virtualInfo abstract (virtual) ���ǂ���
     */
    public void setAbstractInfo(boolean virtualInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.virtualInfo = virtualInfo;
    }

    /**
     * public ���ǂ�����\���ϐ�
     */
    private boolean publicInfo;

    /**
     * private ���ǂ�����\���ϐ�
     */
    private boolean privateInfo;

    /**
     * virtual(abstract) ���ǂ�����\���ϐ�
     */
    private boolean virtualInfo;
}
