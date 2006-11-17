package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


public interface PluginXmlInterpreter {
    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�v���O�C���N���X����Ԃ����\�b�h
     * @return �v���O�C���N���X��\��������
     */
    public String getPluginClassName();

    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�t�@�C���ւ̃N���X�p�X�w��ꗗ��Ԃ����\�b�h
     * @return �t�@�C���ւ̃N���X�p�X�w���\��������̔z��
     */
    public String[] getClassPathFileNames();

    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�f�B���N�g���ւ̃N���X�p�X�w��ꗗ��Ԃ����\�b�h
     * @return �f�B���N�g���ւ̃N���X�p�X�w���\��������̔z��
     */
    public String[] getClassPathDirectoryNames();

    /**
     * ��͑Ώۂ�xml�t�@�C�����ɋL�q����Ă���C�N���X�p�X�w��ꗗ��Ԃ����\�b�h
     * @return �N���X�p�X�w���\��������̔z��
     */
    public String[] getClassPathAttributeNames();
}
