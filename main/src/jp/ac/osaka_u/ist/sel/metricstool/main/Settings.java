package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
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

    private static Settings INSTANCE = null;

    public static Settings getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    private Settings() {
        this.verbose = false;
        this.targetDirectories = new HashSet<String>();
        this.listFiles = new HashSet<String>();
        this.language = null;
        this.metrics = null;
        this.fileMetricsFile = null;
        this.classMetricsFile = null;
        this.methodMetricsFile = null;
        this.fieldMetricsFile = null;
        this.statement = true;
    }

    /**
     * �璷�o�͂��s�����ǂ�����Ԃ�
     * 
     * @return �s���ꍇ�� true, �s��Ȃ��ꍇ�� false
     */
    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(final boolean verbose) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.verbose = verbose;
    }

    /**
     * 
     * @return ��͑Ώۃf�B���N�g��
     * 
     * ��͑Ώۃf�B���N�g����Ԃ��D
     * 
     */
    public Set<String> getTargetDirectories() {
        return Collections.unmodifiableSet(this.targetDirectories);
    }

    public void addTargetDirectory(final String targetDirectory) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == targetDirectory) {
            throw new IllegalArgumentException();
        }
        this.targetDirectories.add(targetDirectory);
    }

    /**
     * ��͑Ώۃt�@�C���̋L�q�����Ԃ�
     * 
     * @return ��͑Ώۃt�@�C���̋L�q����
     * @throws UnavailableLanguageException ���p�s�\�Ȍ��ꂪ�w�肳��Ă���ꍇ�ɃX���[�����
     */
    public LANGUAGE getLanguage() throws UnavailableLanguageException {
        assert null != this.language : "\"language\" is not set";
        return this.language;
    }

    public void setLanguage(final String language) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == language) {
            throw new IllegalArgumentException();
        }

        if (language.equalsIgnoreCase("java") || language.equalsIgnoreCase("java15")) {
            this.language = LANGUAGE.JAVA15;
        } else if (language.equalsIgnoreCase("java14")) {
            this.language = LANGUAGE.JAVA14;
        } else if (language.equalsIgnoreCase("java13")) {
            this.language = LANGUAGE.JAVA13;
            // }else if (language.equalsIgnoreCase("cpp")) {
            // return LANGUAGE.C_PLUS_PLUS;
            // }else if (language.equalsIgnoreCase("csharp")) {
            // return LANGUAGE.C_SHARP
        } else if (language.equalsIgnoreCase("csharp")) {
            this.language = LANGUAGE.CSHARP;
        } else {
            throw new UnavailableLanguageException("\"" + language
                    + "\" is not an available programming language!");
        }
    }

    /**
     * 
     * @return ��͑Ώۃt�@�C���̃p�X���L�q���Ă���t�@�C��
     * 
     * ��͑Ώۃt�@�C���̃p�X���L�q���Ă���t�@�C���̃p�X��Ԃ�
     * 
     */
    public Set<String> getListFiles() {
        return Collections.unmodifiableSet(this.listFiles);
    }

    public void addListFile(final String listFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == listFile) {
            throw new IllegalArgumentException();
        }
        this.listFiles.add(listFile);
    }

    /**
     * 
     * @return �v�����郁�g���N�X
     * 
     * �v�����郁�g���N�X�ꗗ��Ԃ�
     * 
     */
    public String[] getMetrics() {
        return this.metrics;
    }

    public void setMetrics(final String metrics) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == metrics) {
            throw new IllegalArgumentException();
        }

        final StringTokenizer tokenizer = new StringTokenizer(metrics, ",", false);
        this.metrics = new String[tokenizer.countTokens()];
        for (int i = 0; i < this.metrics.length; i++) {
            this.metrics[i] = tokenizer.nextToken();
        }
    }

    /**
     * 
     * @return �t�@�C���^�C�v�̃��g���N�X���o�͂���t�@�C��
     * 
     * �t�@�C���^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X��Ԃ�
     * 
     */
    public String getFileMetricsFile() {
        return this.fileMetricsFile;
    }

    public void setFileMetricsFile(final String fileMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.fileMetricsFile = fileMetricsFile;
    }

    /**
     * 
     * @return �N���X�^�C�v�̃��g���N�X���o�͂���t�@�C��
     * 
     * �N���X�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X��Ԃ�
     * 
     */
    public String getClassMetricsFile() {
        return this.classMetricsFile;
    }

    public void setClassMetricsFile(final String classMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.classMetricsFile = classMetricsFile;
    }

    /**
     * 
     * @return ���\�b�h�^�C�v�̃��g���N�X���o�͂���t�@�C��
     * 
     * ���\�b�h�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X��Ԃ�
     * 
     */
    public String getMethodMetricsFile() {
        return methodMetricsFile;
    }

    public void setMethodMetricsFile(final String methodMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.methodMetricsFile = methodMetricsFile;
    }

    /**
     * 
     * @return �t�B�[���h�^�C�v�̃��g���N�X���o�͂���t�@�C��
     */
    public String getFieldMetricsFile() {
        return this.fieldMetricsFile;
    }

    public void setFieldMetricsFile(final String fieldMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.fieldMetricsFile = fieldMetricsFile;
    }

    /**
     * ��������͂��邩�ǂ�����Ԃ�
     * 
     * @return�@��͂���ꍇ��true,���Ȃ��ꍇ��false
     */
    public boolean isStatement() {
        return this.statement;
    }

    /**
     * ��������͂��邩�ǂ������Z�b�g����
     * 
     * @param statement ��͂���ꍇ��true, ���Ȃ��ꍇ��false
     */
    public void setStatement(final boolean statement) {
        this.statement = statement;
    }

    /**
     * �璷�o�̓��[�h���ǂ������L�^���邽�߂̕ϐ�
     */
    private boolean verbose;

    /**
     * ��͑Ώۃf�B���N�g�����L�^���邽�߂̕ϐ�
     */
    private final Set<String> targetDirectories;

    /**
     * ��͑Ώۃt�@�C���̃p�X���L�q�����t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private final Set<String> listFiles;

    /**
     * ��͑Ώۃt�@�C���̋L�q������L�^���邽�߂̕ϐ�
     */
    private LANGUAGE language;

    /**
     * �v�����郁�g���N�X���L�^���邽�߂̕ϐ�
     */
    private String[] metrics;

    /**
     * �t�@�C���^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private String fileMetricsFile;

    /**
     * �N���X�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private String classMetricsFile;

    /**
     * ���\�b�h�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private String methodMetricsFile;

    /**
     * �t�B�[���h�^�C�v�̃��g���N�X���o�͂���t�@�C���̃p�X���L�^���邽�߂̕ϐ�
     */
    private String fieldMetricsFile;

    /**
     * �������擾���邩�ǂ������L�^���邽�߂̕ϐ�
     */
    private boolean statement;
}
