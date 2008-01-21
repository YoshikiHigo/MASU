package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


/**
 * �N���X��C�t�B�[���h�C���\�b�h�̉�����ݒ肷��C���^�[�t�F�[�X�D �ȉ��̉�����ݒ肷��D
 * 
 * <ul>
 * <li>�N���X������̂ݎQ�Ɖ\</li>
 * <li>�q�N���X����Q�Ɖ\</li>
 * <li>�������O��ԓ�����Q�Ɖ\</li>
 * <li>�ǂ�����ł��Q�Ɖ\</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public interface VisualizableSetting extends Visualizable {

    /**
     * �N���X������̂ݎQ�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param privateVisible �N���X������̂ݎQ�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    void setPrivateVibible(boolean privateVisible);

    /**
     * �������O��ԓ�����Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    void setNamespaceVisible(boolean namespaceVisible);

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param inheritanceVisible �q�N���X����Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    void setInheritanceVisible(boolean inheritanceVisible);

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����ݒ肷��
     * 
     * @param publicVisible �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true�C�����łȂ��ꍇ�� false
     */
    void setPublicVisible(boolean publicVisible);
}
