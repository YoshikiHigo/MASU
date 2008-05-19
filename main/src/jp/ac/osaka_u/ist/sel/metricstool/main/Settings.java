package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.util.StringTokenizer;

import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.UnavailableLanguageException;


/**
 * 
 * @author higo
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
     * �璷�o�͂��s�����ǂ�����Ԃ�
     * 
     * @return �s���ꍇ�� true, �s��Ȃ��ꍇ�� false
     */
    public static boolean isVerbose() {
        return verbose;
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
     * ��͑Ώۃt�@�C���̋L�q�����Ԃ�
     * 
     * @return ��͑Ώۃt�@�C���̋L�q����
     * @throws UnavailableLanguageException ���p�s�\�Ȍ��ꂪ�w�肳��Ă���ꍇ�ɃX���[�����
     */
    public static LANGUAGE getLanguage() throws UnavailableLanguageException {

        if (language.equalsIgnoreCase("java") || language.equalsIgnoreCase("java15")) {
            return LANGUAGE.JAVA15;
        } else if (language.equalsIgnoreCase("java14")) {
            return LANGUAGE.JAVA14;
        } else if (language.equalsIgnoreCase("java13")) {
            return LANGUAGE.JAVA13;
            // }else if (language.equalsIgnoreCase("cpp")) {
            // return LANGUAGE.C_PLUS_PLUS;
            // }else if (language.equalsIgnoreCase("csharp")) {
            // return LANGUAGE.C_SHARP
        }

        throw new UnavailableLanguageException("\"" + language
                + "\" is not an available programming language!");
    }

    /**
     * 
     * @return language �����ŁC-l �̌��Ŏw�肳�ꂽ�v���O���~���O�����\��������
     * 
     * -l �̌��Ŏw�肳�ꂽ�v���O���~���O�����\���������Ԃ��D �L���łȂ�������ł����Ă��C���̂܂ܕԂ��D
     */
    public static String getLanguageString() {
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
        // TODO ���g���N�X��\���N���X������āC���̌^�ŕԂ��ׂ��H
        return metrics;
    }

    /**
     * 
     * @return -m �Ŏw�肳�ꂽ���g���N�X���ꗗ
     * 
     * -m �Ŏw�肳�ꂽ���g���N�X���ꗗ��Ԃ��D�v���\�łȂ����g���N�X�������̂܂ܕԂ��D
     */
    public static String[] getMetricStrings() {
        StringTokenizer tokenizer = new StringTokenizer(metrics, ",", false);
        String[] metricStrings = new String[tokenizer.countTokens()];
        for (int i = 0; i < metricStrings.length; i++) {
            metricStrings[i] = tokenizer.nextToken();
        }
        return metricStrings;
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
     * ������̈����̏������Ɏg����萔
     */
    public static final String INIT = "INIT";

    /**
     * �w���v���[�h�ł��邩�C�����łȂ������L�^���邽�߂̕ϐ�
     */
    private static boolean helpMode = false;

    /**
     * ���\�����[�h�ł��邩�C�����łȂ������L�^���邽�߂̕ϐ�
     */
    private static boolean displayMode = false;

    /**
     * �璷�o�̓��[�h���ǂ������L�^���邽�߂̕ϐ�
     */
    private static boolean verbose = false;

    /**
     * ��͑Ώۃf�B���N�g�����L�^���邽�߂̕ϐ�
     */
    private static String targetDirectory = INIT;

    /**
     * ��͑Ώۃt�@�C���̃p�X���L�q�����t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String listFile = INIT;

    /**
     * ��͑Ώۃt�@�C���̋L�q������L�^���邽�߂̕ϐ�
     */
    private static String language = INIT;

    /**
     * �v�����郁�g���N�X���L�^���邽�߂̕ϐ�
     */
    private static String metrics = INIT;

    /**
     * �t�@�C���^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String fileMetricsFile = INIT;

    /**
     * �N���X�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String classMetricsFile = INIT;

    /**
     * ���\�b�h�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private static String methodMetricsFile = INIT;
}
