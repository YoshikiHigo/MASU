package jp.ac.osaka_u.ist.sel.metricstool.main;


/**
 * 
 * @author y-higo
 * 
 * ���s���̈��������i�[���邽�߂̃N���X
 * 
 */
public class Settings {

    /**
     * 
     * @return �w���v���[�h�ł��邩�C�����łȂ���
     * 
     * �w���v���[�h�ł��邩�ǂ�����Ԃ��D �w���v���[�h�̎� true�C�����łȂ����Cfalse
     * 
     */
    public static boolean isHelpMode() {
        return helpMode;
    }

    /**
     * 
     * @return ���\�����[�h�ł��邩�C�����łȂ���
     * 
     * ���\�����[�h�ł��邩�ǂ�����Ԃ��D ���\�����[�h�̎� true�C�����łȂ����Cfalse
     * 
     */
    public static boolean isDisplayMode() {
        return displayMode;
    }

    /**
     * 
     * @return ��͑Ώۃf�B���N�g��
     * 
     * ��͑Ώۃf�B���N�g����Ԃ��D
     * 
     */
    public static String getTargetDirectory() {
        return targetDirectory;
    }

    /**
     * 
     * @return ��͑Ώۃt�@�C���̋L�q����
     * 
     * ��͑Ώۃt�@�C���̋L�q�����Ԃ�
     * 
     */
    public static String getLanguage() {
        return language;
    }

    /**
     * 
     * @return ��͑Ώۃt�@�C���̃p�X���L�q���Ă���t�@�C��
     * 
     * ��͑Ώۃt�@�C���̃p�X���L�q���Ă���t�@�C���̃p�X��Ԃ�
     * 
     */
    public static String getListFile() {
        return listFile;
    }

    /**
     * 
     * @return �v�����郁�g���N�X
     * 
     * �v�����郁�g���N�X�ꗗ��Ԃ�
     * 
     */
    public static String getMetrics() {
        return metrics;
    }

    /**
     * 
     * @return �t�@�C���^�C�v�̃��g���N�X���o�͂���t�@�C��
     * 
     * �t�@�C���^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X��Ԃ�
     * 
     */
    public static String getFileMetricsFile() {
        return fileMetricsFile;
    }

    /**
     * 
     * @return �N���X�^�C�v�̃��g���N�X���o�͂���t�@�C��
     * 
     * �N���X�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X��Ԃ�
     * 
     */
    public static String getClassMetricsFile() {
        return classMetricsFile;
    }

    /**
     * 
     * @return ���\�b�h�^�C�v�̃��g���N�X���o�͂���t�@�C��
     * 
     * ���\�b�h�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X��Ԃ�
     * 
     */
    public static String getMethodMetricsFile() {
        return methodMetricsFile;
    }

    /**
     * �w���v���[�h�ł��邩�C�����łȂ������L�^���邽�߂̕ϐ�
     */
    private static boolean helpMode = false;

    /**
     * ���\�����[�h�ł��邩�C�����łȂ������L�^���邽�߂̕ϐ�
     */
    private static boolean displayMode = false;

    /**
     * ��͑Ώۃf�B���N�g�����L�^���邽�߂̕ϐ�
     */
    private static String targetDirectory = null;

    /**
     * ��͑Ώۃt�@�C���̃p�X���L�q�����t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String listFile = null;

    /**
     * ��͑Ώۃt�@�C���̋L�q������L�^���邽�߂̕ϐ�
     */
    private static String language = null;

    /**
     * �v�����郁�g���N�X���L�^���邽�߂̕ϐ�
     */
    private static String metrics = null;

    /**
     * �t�@�C���^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String fileMetricsFile = null;

    /**
     * �N���X�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String classMetricsFile = null;

    /**
     * ���\�b�h�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String methodMetricsFile = null;

}
