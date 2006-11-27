package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.DefaultPluginLoader;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.PluginLoadException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.UnavailableLanguageException;

import org.jargp.ArgumentProcessor;
import org.jargp.BoolDef;
import org.jargp.ParameterDef;
import org.jargp.StringDef;

import antlr.RecognitionException;
import antlr.TokenStreamException;


/**
 * 
 * @author y-higo
 * 
 * MetricsTool�̃��C���N���X�D ���݂͉������D
 * 
 * since 2006.11.12
 * 
 */
public class MetricsTool {

    /**
     * 
     * @param args �Ώۃt�@�C���̃t�@�C���p�X
     * 
     * ���݉������D �Ώۃt�@�C���̃f�[�^���i�[������C�\����͂��s���D
     */
    public static void main(String[] args) {
        try {
            //MetricsToolSecurityManager�̃V���O���g���C���X�^���X���\�z���C�������ʌ����X���b�h�ɂȂ�
            MetricsToolSecurityManager sm = MetricsToolSecurityManager.getInstance();
            
            //�V�X�e���̃Z�L�����e�B�[�}�l�[�W���Ƃ��ēo�^���Ă݂�
            System.setSecurityManager(sm);
            
            //�V�X�e���ɓo�^�ł����̂ŁC�Ƃ肠�������M���O�p�[�~�b�V�������O���[�o���ŃZ�b�g
            //TODO �O���[�o���ɗ^����p�[�~�b�V�����͐ݒ�t�@�C���ŋL�q�ł���������������
            sm.addGlobalPermission(new LoggingPermission("control", null));
            
        } catch (final SecurityException e) {
            //���ɃZ�b�g����Ă���Z�L�����e�B�}�l�[�W���ɂ���āC�V���ȃZ�L�����e�B�}�l�[�W���̓o�^��������Ȃ������D
            //�V�X�e���̃Z�L�����e�B�}�l�[�W���Ƃ��Ďg��Ȃ��Ă��C���ʌ����X���b�h�̃A�N�Z�X����͖��Ȃ����삷��̂łƂ肠������������
            Logger.global
                    .info(("Failed to set system security manager. MetricsToolsecurityManager works only to manage privilege threads."));
        }

        Settings settings = new Settings();
        ArgumentProcessor.processArgs(args, parameterDefs, settings);
        checkParameterValidation();

        // �w���v���[�h�̏ꍇ
        if (Settings.isHelpMode()) {

            printUsage();

            // ���\�����[�h�̏ꍇ
        } else if (Settings.isDisplayMode()) {

            // -l �Ō��ꂪ�w�肳��Ă��Ȃ��ꍇ�́C��͉\����ꗗ��\��
            if (Settings.getLanguageString().equals(Settings.INIT)) {

                System.err.println("Available languages;");
                LANGUAGE[] language = LANGUAGE.values();
                for (int i = 0; i < language.length; i++) {
                    System.err.println("\t" + language[0].getName()
                            + ": can be specified with term \"" + language[0].getIdentifierName()
                            + "\"");
                }

                // -l �Ō��ꂪ�w�肳��Ă���ꍇ�́C���̃v���O���~���O����Ŏg�p�\�ȃ��g���N�X�ꗗ��\��
            } else {

                try {
                    LANGUAGE language = Settings.getLanguage();

                    System.err.println("Available metrics for " + language.getName());
                    DefaultPluginLoader loader = new DefaultPluginLoader();
                    for (AbstractPlugin plugin : loader.loadPlugins()) {
                        PluginInfo pluginInfo = plugin.getPluginInfo();
                        if (pluginInfo.isMeasurable(language)) {
                            System.err.println("\t" + pluginInfo.getMetricsName());
                        }
                    }

                    // TODO ���p�\���g���N�X�ꗗ��\��
                } catch (UnavailableLanguageException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                } catch (PluginLoadException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
            }

            // ��̓��[�h�̏ꍇ
        } else if (!Settings.isHelpMode() && !Settings.isDisplayMode()) {

            // ��͑Ώۃt�@�C����o�^
            {
                // �f�B���N�g������ǂݍ���
                if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
                    registerFilesFromDirectory();

                    // ���X�g�t�@�C������ǂݍ���
                } else if (!Settings.getListFile().equals(Settings.INIT)) {
                    registerFilesFromListFile();
                }
            }

            // �Ώۃt�@�C�������
            {
                for (TargetFile targetFile : TargetFileManager.getInstance()) {
                    try {
                        String name = targetFile.getName();
                        System.out.println("processing " + name);
                        Java15Lexer lexer = new Java15Lexer(new FileInputStream(name));
                        Java15Parser parser = new Java15Parser(lexer);
                        parser.compilationUnit();
                        targetFile.setCorrectSytax(true);

                    } catch (FileNotFoundException e) {
                        System.err.println(e.getMessage());
                    } catch (RecognitionException e) {
                        targetFile.setCorrectSytax(false);
                        System.err.println(e.getMessage());
                        // TODO �G���[���N���������Ƃ� TargetFileData �Ȃǂɒʒm���鏈�����K�v
                    } catch (TokenStreamException e) {
                        targetFile.setCorrectSytax(false);
                        System.err.println(e.getMessage());
                        // TODO �G���[���N���������Ƃ� TargetFileData �Ȃǂɒʒm���鏈�����K�v
                    }
                }
            }

            // ���@���̂���t�@�C���ꗗ��\��
            {
                System.err.println("The following files includes uncorrect syntax.");
                System.err.println("Any metrics of them were not measured");
                for (TargetFile targetFile : TargetFileManager.getInstance()) {
                    if (!targetFile.isCorrectSyntax()) {
                        System.err.println("\t" + targetFile.getName());
                    }
                }
            }

        }
    }

    /**
     * �����̎d�l�� Jargp �ɓn�����߂̔z��D
     */
    private static ParameterDef[] parameterDefs = {
            new BoolDef('h', "helpMode", "display usage", true),
            new BoolDef('x', "displayMode", "display available language or metrics", true),
            new StringDef('d', "targetDirectory", "Target directory"),
            new StringDef('i', "listFile", "List file including paths of target files"),
            new StringDef('l', "language", "Programming language"),
            new StringDef('m', "metrics", "Measured metrics"),
            new StringDef('F', "fileMetricsFile", "File storing file metrics"),
            new StringDef('C', "classMetricsFile", "File storing class metrics"),
            new StringDef('M', "methodMetricsFile", "File storing method metrics") };

    /**
     * 
     * �����̐��������m�F���邽�߂̃��\�b�h�D �s���Ȉ������w�肳��Ă����ꍇ�Cmain ���\�b�h�ɂ͖߂炸�C���̊֐����Ńv���O�������I������D
     * 
     */
    private static void checkParameterValidation() {

        // �w���v���[�h�Ə��\�����[�h�������ɃI���ɂȂ��Ă���ꍇ�͕s��
        if (Settings.isHelpMode() && Settings.isDisplayMode()) {
            System.err.println("-h and -x can\'t be set at the same time!");
            printUsage();
            System.exit(0);
        }

        // �w���v���[�h�̏ꍇ
        if (Settings.isHelpMode()) {
            // -h �͑��̃I�v�V�����Ɠ����w��ł��Ȃ�
            if ((!Settings.getTargetDirectory().equals(Settings.INIT))
                    || (!Settings.getListFile().equals(Settings.INIT))
                    || (!Settings.getLanguageString().equals(Settings.INIT))
                    || (!Settings.getMetrics().equals(Settings.INIT))
                    || (!Settings.getFileMetricsFile().equals(Settings.INIT))
                    || (!Settings.getClassMetricsFile().equals(Settings.INIT))
                    || (!Settings.getMethodMetricsFile().equals(Settings.INIT))) {
                System.err.println("-h can\'t be specified with any other options!");
                printUsage();
                System.exit(0);
            }

        }

        // ���\�����[�h�̏ꍇ
        if (Settings.isDisplayMode()) {
            // -d �͎g���Ȃ�
            if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
                System.err.println("-d can\'t be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -i �͎g���Ȃ�
            if (!Settings.getListFile().equals(Settings.INIT)) {
                System.err.println("-i can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -F �͎g���Ȃ�
            if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
                System.err.println("-F can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -C �͎g���Ȃ�
            if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {
                System.err.println("-C can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -M �͎g���Ȃ�
            if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {
                System.err.println("-M can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }
        }

        // ��̓��[�h�̏ꍇ
        if (!Settings.isHelpMode() && !Settings.isDisplayMode()) {

            // -d �� -i �̂ǂ�����w�肳��Ă���͕̂s��
            if (Settings.getTargetDirectory().equals(Settings.INIT)
                    && Settings.getListFile().equals(Settings.INIT)) {
                System.err.println("-d or -i must be specified in the analysis mode!");
                printUsage();
                System.exit(0);
            }

            // -d �� -i �̗������w�肳��Ă���͕̂s��
            if (!Settings.getTargetDirectory().equals(Settings.INIT)
                    && !Settings.getListFile().equals(Settings.INIT)) {
                System.err.println("-d and -i can't be specified at the same time!");
                printUsage();
                System.exit(0);
            }

            try {
                boolean measureFileMetrics = false;
                boolean measureClassMetrics = false;
                boolean measureMethodMetrics = false;

                DefaultPluginLoader loader = new DefaultPluginLoader();
                for (AbstractPlugin plugin : loader.loadPlugins()) {
                    PluginInfo pluginInfo = plugin.getPluginInfo();
                    switch (pluginInfo.getMetricsType()) {
                    case FILE_METRIC:
                        measureFileMetrics = true;
                        break;
                    case CLASS_METRIC:
                        measureClassMetrics = true;
                        break;
                    case METHOD_METRIC:
                        measureMethodMetrics = true;
                        break;
                    }
                }

                //�t�@�C�����g���N�X���v������ꍇ�� -F �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                if (measureFileMetrics && (Settings.getFileMetricsFile().equals(Settings.INIT))) {
                    System.err.println("-F must be used for specifying a file for file metrics!");
                    System.exit(0);
                }

                //�N���X���g���N�X���v������ꍇ�� -C �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                if (measureClassMetrics && (Settings.getClassMetricsFile().equals(Settings.INIT))) {
                    System.err.println("-C must be used for specifying a file for class metrics!");
                    System.exit(0);
                }
                //���\�b�h���g���N�X���v������ꍇ�� -M �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�                
                if (measureMethodMetrics && (Settings.getMethodMetricsFile().equals(Settings.INIT))) {
                    System.err.println("-M must be used for specifying a file for method metrics!");
                    System.exit(0);
                }

            } catch (PluginLoadException e) {
                System.err.println(e.getMessage());
                System.exit(0);
            }
        }
    }

    /**
     * 
     * �c�[���̎g�����i�R�}���h���C���I�v�V�����j��\������D
     * 
     */
    private static void printUsage() {

        System.err.println();
        System.err.println("Available options:");
        System.err.println("\t-d: root directory that you are going to analysis.");
        System.err.println("\t-i: List file including file paths that you are going to analysis.");
        System.err.println("\t-l: Programming language of the target files.");
        System.err
                .println("\t-m: Metrics that you want to get. Metrics names are separated with \',\'.");
        System.err.println("\t-C: File path that the class type metrics are output");
        System.err.println("\t-F: File path that the file type metrics are output.");
        System.err.println("\t-M: File path that the method type metrics are output");

        System.err.println();
        System.err.println("Usage:");
        System.err.println("\t<Help Mode>");
        System.err.println("\tMetricsTool -h");
        System.err.println();
        System.err.println("\t<Display Mode>");
        System.err.println("\tMetricsTool -x -l");
        System.err.println("\tMetricsTool -x -l language -m");
        System.err.println();
        System.err.println("\t<Analysis Mode>");
        System.err
                .println("\tMetricsTool -d directory -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
        System.err
                .println("\tMetricsTool -l listFile -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
    }

    /**
     * 
     * ���X�g�t�@�C������Ώۃt�@�C����o�^����D �ǂݍ��݃G���[�����������ꍇ�́C���̃��\�b�h���Ńv���O�������I������D
     */
    private static void registerFilesFromListFile() {

        try {

            TargetFileManager targetFiles = TargetFileManager.getInstance();
            for (BufferedReader reader = new BufferedReader(new FileReader(Settings.getListFile())); reader
                    .ready();) {
                String line = reader.readLine();
                TargetFile targetFile = new TargetFile(line);
                targetFiles.add(targetFile);
            }

        } catch (FileNotFoundException e) {
            System.err.println("\"" + Settings.getListFile() + "\" is not a valid file!");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("\"" + Settings.getListFile() + "\" can\'t read!");
            System.exit(0);
        }
    }

    /**
     * 
     * registerFilesFromDirectory(File file)���Ăяo���̂݁D main���\�b�h�� new File(Settings.getTargetDirectory)
     * ����̂��C���������������ߍ쐬�D
     * 
     */
    private static void registerFilesFromDirectory() {

        File targetDirectory = new File(Settings.getTargetDirectory());
        registerFilesFromDirectory(targetDirectory);
    }

    /**
     * 
     * @param file �Ώۃt�@�C���܂��̓f�B���N�g��
     * 
     * �Ώۂ��f�B���N�g���̏ꍇ�́C���̎q�ɑ΂��čċA�I�ɏ���������D �Ώۂ��t�@�C���̏ꍇ�́C�Ώی���̃\�[�X�t�@�C���ł���΁C�o�^�������s���D
     */
    private static void registerFilesFromDirectory(File file) {

        // �f�B���N�g���Ȃ�΁C�ċA�I�ɏ���
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                registerFilesFromDirectory(subfiles[i]);
            }

            // �t�@�C���Ȃ�΁C�g���q���Ώی���ƈ�v����Γo�^
        } else if (file.isFile()) {

            try {
                LANGUAGE language = Settings.getLanguage();
                String extension = language.getExtension();
                String path = file.getAbsolutePath();
                if (path.endsWith(extension)) {
                    TargetFileManager targetFiles = TargetFileManager.getInstance();
                    TargetFile targetFile = new TargetFile(path);
                    targetFiles.add(targetFile);
                }
            } catch (UnavailableLanguageException e) {
                System.err.println(e.getMessage());
                System.exit(0);
            }

            // �f�B���N�g���ł��t�@�C���ł��Ȃ��ꍇ�͕s��
        } else {
            System.err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }
    }
}
