package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java15AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr.AntlrAstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.NameResolver;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCall;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVClassMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVFileMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVMethodMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.DefaultPluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.DefaultPluginLoader;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.PluginLoadException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;

import org.jargp.ArgumentProcessor;
import org.jargp.BoolDef;
import org.jargp.ParameterDef;
import org.jargp.StringDef;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;


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

    static {
        // ���\���p�̃��X�i���쐬
        MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        });

        MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.err.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        });
    }

    /**
     * 
     * @param args �Ώۃt�@�C���̃t�@�C���p�X
     * 
     * ���݉������D �Ώۃt�@�C���̃f�[�^���i�[������C�\����͂��s���D
     */
    public static void main(String[] args) {
        initSecurityManager();

        ArgumentProcessor.processArgs(args, parameterDefs, new Settings());

        // �w���v���[�h�Ə��\�����[�h�������ɃI���ɂȂ��Ă���ꍇ�͕s��
        if (Settings.isHelpMode() && Settings.isDisplayMode()) {
            System.err.println("-h and -x can\'t be set at the same time!");
            printUsage();
            System.exit(0);
        }

        if (Settings.isHelpMode()) {
            // �w���v���[�h�̏ꍇ
            doHelpMode();
        } else {
            LANGUAGE language = getLanguage();
            loadPlugins(language, Settings.getMetricStrings());

            if (Settings.isDisplayMode()) {
                // ���\�����[�h�̏ꍇ
                doDisplayMode(language);
            } else {
                // ��̓��[�h
                doAnalysisMode(language);
            }
        }
    }

    /**
     * �ǂݍ��񂾑Ώۃt�@�C���Q����͂���.
     * 
     * @param language ��͑Ώۂ̌���
     */
    private static void analyzeTargetFiles(final LANGUAGE language) {
        // �Ώۃt�@�C�������

        AstVisitorManager<AST> visitorManager = null;
        if (language.equals(LANGUAGE.JAVA)) {
            visitorManager = new JavaAstVisitorManager<AST>(new AntlrAstVisitor(
                    new Java15AntlrAstTranslator()));
        }

        out.println("Parse all target files.");
        for (TargetFile targetFile : TargetFileManager.getInstance()) {
            try {
                String name = targetFile.getName();

                FileInfo fileInfo = new FileInfo(name);
                FileInfoManager.getInstance().add(fileInfo);

                out.println("parsing " + name);
                Java15Lexer lexer = new Java15Lexer(new FileInputStream(name));
                Java15Parser parser = new Java15Parser(lexer);
                parser.compilationUnit();
                targetFile.setCorrectSytax(true);

                if (visitorManager != null) {
                    visitorManager.setPositionManager(parser.getPositionManger());
                    visitorManager.visitStart(parser.getAST());
                }

                fileInfo.setLOC(lexer.getLine());

            } catch (FileNotFoundException e) {
                err.println(e.getMessage());
            } catch (RecognitionException e) {
                targetFile.setCorrectSytax(false);
                err.println(e.getMessage());
                // TODO �G���[���N���������Ƃ� TargetFileData �Ȃǂɒʒm���鏈�����K�v
            } catch (TokenStreamException e) {
                targetFile.setCorrectSytax(false);
                err.println(e.getMessage());
                // TODO �G���[���N���������Ƃ� TargetFileData �Ȃǂɒʒm���鏈�����K�v
            }
        }

        out.println("Resolve Definitions and Usages.");
        out.println("STEP1 : resolve class definitions.");
        registClassInfos();
        out.println("STEP2 : resolve field definitions.");
        registFieldInfos();
        out.println("STEP3 : resolve method definitions.");
        registMethodInfos();
        out.println("STEP4 : resolve class inheritances.");
        addInheritanceInformationToClassInfos();
        out.println("STEP5 : resolve method overrides.");
        addOverrideRelation();
        out.println("STEP6 : resolve field and method usages.");
        addReferenceAssignmentCallRelateion();

        // ���@���̂���t�@�C���ꗗ��\��
        // err.println("The following files includes uncorrect syntax.");
        // err.println("Any metrics of them were not measured");
        for (TargetFile targetFile : TargetFileManager.getInstance()) {
            if (!targetFile.isCorrectSyntax()) {
                err.println("Incorrect syntax file: " + targetFile.getName());
            }
        }

        out.println("finished.");
    }

    /**
     * 
     * �w���v���[�h�̈����̐��������m�F���邽�߂̃��\�b�h�D �s���Ȉ������w�肳��Ă����ꍇ�Cmain ���\�b�h�ɂ͖߂炸�C���̊֐����Ńv���O�������I������D
     * 
     */
    private static void checkHelpModeParameterValidation() {
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

    /**
     * 
     * ���\�����[�h�̈����̐��������m�F���邽�߂̃��\�b�h�D �s���Ȉ������w�肳��Ă����ꍇ�Cmain ���\�b�h�ɂ͖߂炸�C���̊֐����Ńv���O�������I������D
     * 
     */
    private static void checkDisplayModeParameterValidation() {
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

    /**
     * 
     * ��̓��[�h�̈����̐��������m�F���邽�߂̃��\�b�h�D �s���Ȉ������w�肳��Ă����ꍇ�Cmain ���\�b�h�ɂ͖߂炸�C���̊֐����Ńv���O�������I������D
     * 
     * @param �w�肳�ꂽ����
     * 
     */
    private static void checkAnalysisModeParameterValidation(LANGUAGE language) {
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

        // ���ꂪ�w�肳��Ȃ������͕̂s��
        if (null == language) {
            System.err.println("-l must be specified in the analysis mode.");
            printUsage();
            System.exit(0);
        }

        boolean measureFileMetrics = false;
        boolean measureClassMetrics = false;
        boolean measureMethodMetrics = false;

        for (PluginInfo pluginInfo : PluginManager.getInstance().getPluginInfos()) {
            switch (pluginInfo.getMetricType()) {
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

        // �t�@�C�����g���N�X���v������ꍇ�� -F �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
        if (measureFileMetrics && (Settings.getFileMetricsFile().equals(Settings.INIT))) {
            System.err.println("-F must be used for specifying a file for file metrics!");
            System.exit(0);
        }

        // �N���X���g���N�X���v������ꍇ�� -C �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
        if (measureClassMetrics && (Settings.getClassMetricsFile().equals(Settings.INIT))) {
            System.err.println("-C must be used for specifying a file for class metrics!");
            System.exit(0);
        }
        // ���\�b�h���g���N�X���v������ꍇ�� -M �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
        if (measureMethodMetrics && (Settings.getMethodMetricsFile().equals(Settings.INIT))) {
            System.err.println("-M must be used for specifying a file for method metrics!");
            System.exit(0);
        }
    }

    /**
     * ��̓��[�h�����s����.
     * 
     * @param language �Ώی���
     */
    private static void doAnalysisMode(LANGUAGE language) {
        checkAnalysisModeParameterValidation(language);

        readTargetFiles();
        analyzeTargetFiles(language);
        launchPlugins();
        writeMetrics();
    }

    /**
     * ���\�����[�h�����s����
     * 
     * @param language �Ώی���
     */
    private static void doDisplayMode(LANGUAGE language) {
        checkDisplayModeParameterValidation();

        // -l �Ō��ꂪ�w�肳��Ă��Ȃ��ꍇ�́C��͉\����ꗗ��\��
        if (null == language) {
            System.err.println("Available languages;");
            LANGUAGE[] languages = LANGUAGE.values();
            for (int i = 0; i < languages.length; i++) {
                System.err.println("\t" + languages[0].getName()
                        + ": can be specified with term \"" + languages[0].getIdentifierName()
                        + "\"");
            }

            // -l �Ō��ꂪ�w�肳��Ă���ꍇ�́C���̃v���O���~���O����Ŏg�p�\�ȃ��g���N�X�ꗗ��\��
        } else {
            System.err.println("Available metrics for " + language.getName());
            for (AbstractPlugin plugin : PluginManager.getInstance().getPlugins()) {
                PluginInfo pluginInfo = plugin.getPluginInfo();
                if (pluginInfo.isMeasurable(language)) {
                    System.err.println("\t" + pluginInfo.getMetricName());
                }
            }
            // TODO ���p�\���g���N�X�ꗗ��\��
        }
    }

    /**
     * �w���v���[�h�����s����.
     */
    private static void doHelpMode() {
        checkHelpModeParameterValidation();

        printUsage();
    }

    /**
     * �Ώی�����擾����.
     * 
     * @return �w�肳�ꂽ�Ώی���.�w�肳��Ȃ������ꍇ��null
     */
    private static LANGUAGE getLanguage() {
        if (Settings.getLanguageString().equals(Settings.INIT)) {
            return null;
        }

        return Settings.getLanguage();
    }

    /**
     * {@link MetricsToolSecurityManager} �̏��������s��. �V�X�e���ɓo�^�ł���΁C�V�X�e���̃Z�L�����e�B�}�l�[�W���ɂ��o�^����.
     */
    private static void initSecurityManager() {
        try {
            // MetricsToolSecurityManager�̃V���O���g���C���X�^���X���\�z���C�������ʌ����X���b�h�ɂȂ�
            System.setSecurityManager(MetricsToolSecurityManager.getInstance());
        } catch (final SecurityException e) {
            // ���ɃZ�b�g����Ă���Z�L�����e�B�}�l�[�W���ɂ���āC�V���ȃZ�L�����e�B�}�l�[�W���̓o�^��������Ȃ������D
            // �V�X�e���̃Z�L�����e�B�}�l�[�W���Ƃ��Ďg��Ȃ��Ă��C���ʌ����X���b�h�̃A�N�Z�X����͖��Ȃ����삷��̂łƂ肠������������
            err
                    .println("Failed to set system security manager. MetricsToolsecurityManager works only to manage privilege threads.");
        }
    }

    /**
     * ���[�h�ς݂̃v���O�C�������s����.
     */
    private static void launchPlugins() {
        PluginLauncher launcher = new DefaultPluginLauncher();
        launcher.setMaximumLaunchingNum(1);
        launcher.launchAll(PluginManager.getInstance().getPlugins());

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // �C�ɂ��Ȃ�
            }
        } while (0 < launcher.getCurrentLaunchingNum() + launcher.getLaunchWaitingTaskNum());

        launcher.stopLaunching();
    }

    /**
     * �v���O�C�������[�h����. �w�肳�ꂽ����C�w�肳�ꂽ���g���N�X�Ɋ֘A����v���O�C���݂̂� {@link PluginManager}�ɓo�^����.
     * 
     * @param language �w�肵���ꂽ����.
     */
    private static void loadPlugins(final LANGUAGE language, final String[] metrics) {
        // �w�茾��ɑΉ�����v���O�C���Ŏw�肳�ꂽ���g���N�X���v������v���O�C�������[�h���ēo�^

        // metrics[]���O����Ȃ����C2�ȏ�w�肳��Ă��� or �P�����ǃf�t�H���g�̕����񂶂�Ȃ�
        boolean metricsSpecified = metrics.length != 0
                && (1 < metrics.length || !metrics[0].equals(Settings.INIT));

        final PluginManager pluginManager = PluginManager.getInstance();
        try {
            for (final AbstractPlugin plugin : (new DefaultPluginLoader()).loadPlugins()) {// �v���O�C����S���[�h
                final PluginInfo info = plugin.getPluginInfo();
                if (null == language || info.isMeasurable(language)) {
                    // �Ώی��ꂪ�w�肳��Ă��Ȃ� or �Ώی�����v���\
                    if (metricsSpecified) {
                        // ���g���N�X���w�肳��Ă���̂ł��̃v���O�C���ƈ�v���邩�`�F�b�N
                        final String pluginMetricName = info.getMetricName();
                        for (final String metric : metrics) {
                            if (metric.equals(pluginMetricName)) {
                                pluginManager.addPlugin(plugin);
                                break;
                            }
                        }
                    } else {
                        // ���g���N�X���w�肳��Ă��Ȃ��̂łƂ肠�����S���o�^
                        pluginManager.addPlugin(plugin);
                    }
                }
            }
        } catch (PluginLoadException e) {
            err.println(e.getMessage());
            System.exit(0);
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
     * ��͑Ώۃt�@�C����o�^
     */
    private static void readTargetFiles() {

        // �f�B���N�g������ǂݍ���
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            registerFilesFromDirectory();

            // ���X�g�t�@�C������ǂݍ���
        } else if (!Settings.getListFile().equals(Settings.INIT)) {
            registerFilesFromListFile();
        }
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
            err.println("\"" + Settings.getListFile() + "\" is not a valid file!");
            System.exit(0);
        } catch (IOException e) {
            err.println("\"" + Settings.getListFile() + "\" can\'t read!");
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

            final LANGUAGE language = Settings.getLanguage();
            final String extension = language.getExtension();
            final String path = file.getAbsolutePath();
            if (path.endsWith(extension)) {
                final TargetFileManager targetFiles = TargetFileManager.getInstance();
                final TargetFile targetFile = new TargetFile(path);
                targetFiles.add(targetFile);
            }

            // �f�B���N�g���ł��t�@�C���ł��Ȃ��ꍇ�͕s��
        } else {
            err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }
    }

    /**
     * ���g���N�X�����t�@�C���ɏo��.
     */
    private static void writeMetrics() {
        if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {

            try {
                FileMetricsInfoManager manager = FileMetricsInfoManager.getInstance();
                manager.checkMetrics();

                String fileName = Settings.getFileMetricsFile();
                CSVFileMetricsWriter writer = new CSVFileMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                System.exit(0);
            }
        }

        if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {

            try {
                ClassMetricsInfoManager manager = ClassMetricsInfoManager.getInstance();
                manager.checkMetrics();

                String fileName = Settings.getClassMetricsFile();
                CSVClassMetricsWriter writer = new CSVClassMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                System.exit(0);
            }
        }

        if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {

            try {
                MethodMetricsInfoManager manager = MethodMetricsInfoManager.getInstance();
                manager.checkMetrics();

                String fileName = Settings.getMethodMetricsFile();
                CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                System.exit(0);
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
     * �o�̓��b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * �N���X�̒�`�� ClassInfoManager �ɓo�^����DAST �p�[�X�̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ��D
     */
    private static void registClassInfos() {

        // Unresolved �N���X���}�l�[�W���C �N���X���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // �e Unresolved�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {

            // �C���q�C���S���薼�C�s���C�����C�C���X�^���X�����o�[���ǂ������擾
            final Set<ModifierInfo> modifiers = unresolvedClassInfo.getModifiers();
            final String[] fullQualifiedName = unresolvedClassInfo.getFullQualifiedName();
            final int loc = unresolvedClassInfo.getLOC();
            final boolean privateVisible = unresolvedClassInfo.isPrivateVisible();
            final boolean namespaceVisible = unresolvedClassInfo.isNamespaceVisible();
            final boolean inheritanceVisible = unresolvedClassInfo.isInheritanceVisible();
            final boolean publicVisible = unresolvedClassInfo.isPublicVisible();
            final boolean instance = unresolvedClassInfo.isInstanceMember();
            final int fromLine = unresolvedClassInfo.getFromLine();
            final int fromColumn = unresolvedClassInfo.getFromColumn();
            final int toLine = unresolvedClassInfo.getToLine();
            final int toColumn = unresolvedClassInfo.getToColumn();

            // ClassInfo �I�u�W�F�N�g���쐬���CClassInfoManager�ɓo�^
            final TargetClassInfo classInfo = new TargetClassInfo(modifiers, fullQualifiedName,
                    loc, privateVisible, namespaceVisible, inheritanceVisible, publicVisible,
                    instance, fromLine, fromColumn, toLine, toColumn);
            classInfoManager.add(classInfo);

            // �������N���X���ɉ����ς݃N���X����ǉ����Ă���
            unresolvedClassInfo.setResolvedInfo(classInfo);

            // �e�C���i�[�N���X�ɑ΂��ď���
            for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                    .getInnerClasses()) {
                final TargetInnerClassInfo innerClass = registInnerClassInfo(
                        unresolvedInnerClassInfo, classInfo, classInfoManager);
                classInfo.addInnerClass(innerClass);
            }
        }
    }

    /**
     * �C���i�[�N���X�̒�`�� ClassInfoManager �ɓo�^����D registClassInfos ����̂݌Ă΂��ׂ��ł���D
     * 
     * @param unresolvedClassInfo ���O���������C���i�[�N���X�I�u�W�F�N�g
     * @param outerClass �O���̃N���X
     * @param classInfoManager �C���i�[�N���X��o�^����N���X�}�l�[�W��
     * @return ���������C���i�[�N���X�� ClassInfo
     */
    private static TargetInnerClassInfo registInnerClassInfo(
            final UnresolvedClassInfo unresolvedClassInfo, final TargetClassInfo outerClass,
            final ClassInfoManager classInfoManager) {

        // �C���q�C���S���薼�C�s���C�������擾
        final Set<ModifierInfo> modifiers = unresolvedClassInfo.getModifiers();
        final String[] fullQualifiedName = unresolvedClassInfo.getFullQualifiedName();
        final int loc = unresolvedClassInfo.getLOC();
        final boolean privateVisible = unresolvedClassInfo.isPrivateVisible();
        final boolean namespaceVisible = unresolvedClassInfo.isNamespaceVisible();
        final boolean inheritanceVisible = unresolvedClassInfo.isInheritanceVisible();
        final boolean publicVisible = unresolvedClassInfo.isPublicVisible();
        final boolean instance = unresolvedClassInfo.isInstanceMember();
        final int fromLine = unresolvedClassInfo.getFromLine();
        final int fromColumn = unresolvedClassInfo.getFromColumn();
        final int toLine = unresolvedClassInfo.getToLine();
        final int toColumn = unresolvedClassInfo.getToColumn();

        // ClassInfo �I�u�W�F�N�g�𐶐����CClassInfo�}�l�[�W���ɓo�^
        TargetInnerClassInfo classInfo = new TargetInnerClassInfo(modifiers, fullQualifiedName,
                outerClass, loc, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, instance, fromLine, fromColumn, toLine, toColumn);
        classInfoManager.add(classInfo);

        // �������N���X���ɉ����ς݃N���X����ǉ����Ă���
        unresolvedClassInfo.setResolvedInfo(classInfo);

        // ���̃N���X�̃C���i�[�N���X�ɑ΂��čċA�I�ɏ���
        for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo.getInnerClasses()) {
            final TargetInnerClassInfo innerClass = registInnerClassInfo(unresolvedInnerClassInfo,
                    classInfo, classInfoManager);
            classInfo.addInnerClass(innerClass);
        }

        // ���̃N���X�� ClassInfo ��Ԃ�
        return classInfo;
    }

    /**
     * �N���X�̌p������ ClassInfo �ɒǉ�����D��x�ڂ� AST �p�[�X�̌�C���� registClassInfos �̌�ɂ�т����Ȃ���΂Ȃ�Ȃ��D
     */
    private static void addInheritanceInformationToClassInfos() {

        // Unresolved �N���X���}�l�[�W���C �N���X���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // �e Unresolved�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            addInheritanceInformationToClassInfo(unresolvedClassInfo, classInfoManager);
        }
    }

    /**
     * �N���X�̌p������ InnerClassInfo �ɒǉ�����DaddInheritanceInformationToClassInfos �̒�����̂݌Ăяo�����ׂ�
     * 
     * @param unresolvedClassInfo �p���֌W��ǉ�����i�������j�N���X���
     * @param classInfoManager ���O�����ɗp����N���X�}�l�[�W��
     */
    private static void addInheritanceInformationToClassInfo(
            final UnresolvedClassInfo unresolvedClassInfo, final ClassInfoManager classInfoManager) {

        // ClassInfo ���擾
        final ClassInfo classInfo = (ClassInfo) unresolvedClassInfo.getResolvedInfo();

        // �e�e�N���X���ɑ΂���
        for (UnresolvedTypeInfo unresolvedSuperClassType : unresolvedClassInfo.getSuperClasses()) {
            TypeInfo superClass = NameResolver.resolveTypeInfo(unresolvedSuperClassType,
                    (TargetClassInfo) classInfo, null, classInfoManager, null, null, null);
            assert superClass != null : "resolveTypeInfo returned null!";
            
            // ������Ȃ������ꍇ�͖��O��Ԗ���UNKNOWN�ȃN���X��o�^����
            if (superClass instanceof UnknownTypeInfo) {
                superClass = NameResolver
                        .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedSuperClassType);
                classInfoManager.add((ExternalClassInfo) superClass);
            }

            classInfo.addSuperClass((ClassInfo)superClass);
            ((ClassInfo)superClass).addSubClass(classInfo);
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo.getInnerClasses()) {
            addInheritanceInformationToClassInfo(unresolvedInnerClassInfo, classInfoManager);
        }
    }

    /**
     * �t�B�[���h�̒�`�� FieldInfoManager �ɓo�^����D registClassInfos �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ�
     * 
     */
    private static void registFieldInfos() {

        // Unresolved �N���X���}�l�[�W���C�N���X���}�l�[�W���C�t�B�[���h���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();

        // �e Unresolved�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            registFieldInfos(unresolvedClassInfo, classInfoManager, fieldInfoManager);
        }
    }

    /**
     * �t�B�[���h�̒�`�� FieldInfoManager �ɓo�^����D
     * 
     * @param unresolvedClassInfo �t�B�[���h�����ΏۃN���X
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     */
    private static void registFieldInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager) {

        // ClassInfo ���擾
        final ClassInfo ownerClass = (ClassInfo) unresolvedClassInfo.getResolvedInfo();

        // �e�������t�B�[���h�ɑ΂���
        for (UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo.getDefinedFields()) {

            // �C���q�C���O�C�^�C�����C�C���X�^���X�����o�[���ǂ������擾
            final Set<ModifierInfo> modifiers = unresolvedFieldInfo.getModifiers();
            final String fieldName = unresolvedFieldInfo.getName();
            final UnresolvedTypeInfo unresolvedFieldType = unresolvedFieldInfo.getType();
            TypeInfo fieldType = NameResolver.resolveTypeInfo(unresolvedFieldType,
                    (TargetClassInfo) ownerClass, null, classInfoManager, null, null, null);
            assert fieldType != null : "resolveTypeInfo returned null!";
            if (fieldType instanceof UnknownTypeInfo) {
                if (unresolvedFieldType instanceof UnresolvedReferenceTypeInfo) {
                    fieldType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedFieldType);
                    classInfoManager.add((ExternalClassInfo) fieldType);
                } else if (unresolvedFieldType instanceof UnresolvedArrayTypeInfo) {
                    final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedFieldType)
                            .getElementType();
                    final int dimension = ((UnresolvedArrayTypeInfo) unresolvedFieldType)
                            .getDimension();
                    final TypeInfo elementType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                    classInfoManager.add((ExternalClassInfo) elementType);
                    fieldType = ArrayTypeInfo.getType(elementType, dimension);
                } else {
                    err.println("Can't resolve field type : " + unresolvedFieldType.getTypeName());
                }
            }
            final boolean privateVisible = unresolvedFieldInfo.isPrivateVisible();
            final boolean namespaceVisible = unresolvedFieldInfo.isNamespaceVisible();
            final boolean inheritanceVisible = unresolvedFieldInfo.isInheritanceVisible();
            final boolean publicVisible = unresolvedFieldInfo.isPublicVisible();
            final boolean instance = unresolvedFieldInfo.isInstanceMember();
            final int fromLine = unresolvedFieldInfo.getFromLine();
            final int fromColumn = unresolvedFieldInfo.getFromColumn();
            final int toLine = unresolvedFieldInfo.getToLine();
            final int toColumn = unresolvedFieldInfo.getToColumn();

            // �t�B�[���h�I�u�W�F�N�g�𐶐�
            final TargetFieldInfo fieldInfo = new TargetFieldInfo(modifiers, fieldName, fieldType,
                    ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                    publicVisible, instance, fromLine, fromColumn, toLine, toColumn);

            // �t�B�[���h�����N���X�ƃt�B�[���h���}�l�[�W���ɒǉ�
            ((TargetClassInfo) ownerClass).addDefinedField(fieldInfo);
            fieldInfoManager.add(fieldInfo);

            // �������t�B�[���h���ɂ��ǉ�
            unresolvedFieldInfo.setResolvedInfo(fieldInfo);
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo.getInnerClasses()) {
            registFieldInfos(unresolvedInnerClassInfo, classInfoManager, fieldInfoManager);
        }
    }

    /**
     * ���\�b�h�̒�`�� MethodInfoManager �ɓo�^����DregistClassInfos �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ��D
     */
    private static void registMethodInfos() {

        // Unresolved �N���X���}�l�[�W���C �N���X���}�l�[�W���C���\�b�h���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();

        // �e Unresolved�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            registMethodInfos(unresolvedClassInfo, classInfoManager, methodInfoManager);
        }
    }

    /**
     * ���������\�b�h��`�����������C���\�b�h�}�l�[�W���ɓo�^����D
     * 
     * @param unresolvedClassInfo ���\�b�h�����ΏۃN���X
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    private static void registMethodInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final MethodInfoManager methodInfoManager) {

        // ClassInfo ���擾
        final ClassInfo ownerClass = (ClassInfo) unresolvedClassInfo.getResolvedInfo();

        // �e���������\�b�h�ɑ΂���
        for (UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo.getDefinedMethods()) {

            // �C���q�C���O�C�Ԃ�l�C�s���C�R���X�g���N�^���ǂ����C�����C�C���X�^���X�����o�[���ǂ������擾
            final Set<ModifierInfo> methodModifiers = unresolvedMethodInfo.getModifiers();
            final String methodName = unresolvedMethodInfo.getMethodName();
            final UnresolvedTypeInfo unresolvedMethodReturnType = unresolvedMethodInfo
                    .getReturnType();
            TypeInfo methodReturnType = NameResolver.resolveTypeInfo(unresolvedMethodReturnType,
                    (TargetClassInfo) ownerClass, null, classInfoManager, null, null, null);
            assert methodReturnType != null : "resolveTypeInfo returned null!";
            if (methodReturnType instanceof UnknownTypeInfo) {
                if (unresolvedMethodReturnType instanceof UnresolvedReferenceTypeInfo) {
                    methodReturnType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedMethodReturnType);
                    classInfoManager.add((ExternalClassInfo) methodReturnType);
                } else if (unresolvedMethodReturnType instanceof UnresolvedArrayTypeInfo) {
                    final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                            .getElementType();
                    final int dimension = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                            .getDimension();
                    final TypeInfo elementType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                    classInfoManager.add((ExternalClassInfo) elementType);
                    methodReturnType = ArrayTypeInfo.getType(elementType, dimension);
                } else {
                    err.println("Can't resolve method return type : "
                            + unresolvedMethodReturnType.getTypeName());
                }
            }
            final int methodLOC = unresolvedMethodInfo.getLOC();
            final boolean constructor = unresolvedMethodInfo.isConstructor();
            final boolean privateVisible = unresolvedMethodInfo.isPrivateVisible();
            final boolean namespaceVisible = unresolvedMethodInfo.isNamespaceVisible();
            final boolean inheritanceVisible = unresolvedMethodInfo.isInheritanceVisible();
            final boolean publicVisible = unresolvedMethodInfo.isPublicVisible();
            final boolean instance = unresolvedMethodInfo.isInstanceMember();
            final int methodFromLine = unresolvedMethodInfo.getFromLine();
            final int methodFromColumn = unresolvedMethodInfo.getFromColumn();
            final int methodToLine = unresolvedMethodInfo.getToLine();
            final int methodToColumn = unresolvedMethodInfo.getToColumn();

            // MethodInfo �I�u�W�F�N�g�𐶐����C������ǉ����Ă���
            final TargetMethodInfo methodInfo = new TargetMethodInfo(methodModifiers, methodName,
                    methodReturnType, ownerClass, constructor, methodLOC, privateVisible,
                    namespaceVisible, inheritanceVisible, publicVisible, instance, methodFromLine,
                    methodFromColumn, methodToLine, methodToColumn);
            for (UnresolvedParameterInfo unresolvedParameterInfo : unresolvedMethodInfo
                    .getParameterInfos()) {

                // �C���q�C�p�����[�^���C�^�C�ʒu�����擾
                final Set<ModifierInfo> parameterModifiers = unresolvedParameterInfo.getModifiers();
                final String parameterName = unresolvedParameterInfo.getName();
                final UnresolvedTypeInfo unresolvedParameterType = unresolvedParameterInfo
                        .getType();
                TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedParameterType,
                        (TargetClassInfo) ownerClass, methodInfo, classInfoManager, null, null,
                        null);
                assert parameterType != null : "resolveTypeInfo returned null!";
                if (parameterType instanceof UnknownTypeInfo) {
                    if (unresolvedParameterType instanceof UnresolvedReferenceTypeInfo) {
                        parameterType = NameResolver
                                .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedParameterType);
                        classInfoManager.add((ExternalClassInfo) parameterType);
                    } else if (unresolvedParameterType instanceof UnresolvedArrayTypeInfo) {
                        final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                                .getElementType();
                        final int dimension = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                                .getDimension();
                        final TypeInfo elementType = NameResolver
                                .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                        classInfoManager.add((ExternalClassInfo) elementType);
                        parameterType = ArrayTypeInfo.getType(elementType, dimension);
                    } else {
                        err.println("Can't resolve dummy parameter type : "
                                + unresolvedParameterType.getTypeName());
                    }
                }
                final int parameterFromLine = unresolvedParameterInfo.getFromLine();
                final int parameterFromColumn = unresolvedParameterInfo.getFromColumn();
                final int parameterToLine = unresolvedParameterInfo.getToLine();
                final int parameterToColumn = unresolvedParameterInfo.getToColumn();

                // �p�����[�^�I�u�W�F�N�g�𐶐����C���\�b�h�ɒǉ�
                final TargetParameterInfo parameterInfo = new TargetParameterInfo(
                        parameterModifiers, parameterName, parameterType, parameterFromLine,
                        parameterFromColumn, parameterToLine, parameterToColumn);
                methodInfo.addParameter(parameterInfo);
            }

            // ���\�b�h���Œ�`����Ă���e���������[�J���ϐ��ɑ΂���
            for (UnresolvedLocalVariableInfo unresolvedLocalVariable : unresolvedMethodInfo
                    .getLocalVariables()) {

                // �C���q�C�ϐ����C�^���擾
                final Set<ModifierInfo> localModifiers = unresolvedLocalVariable.getModifiers();
                final String variableName = unresolvedLocalVariable.getName();
                final UnresolvedTypeInfo unresolvedVariableType = unresolvedLocalVariable.getType();
                TypeInfo variableType = NameResolver.resolveTypeInfo(unresolvedVariableType,
                        (TargetClassInfo) ownerClass, methodInfo, classInfoManager, null, null,
                        null);
                assert variableType != null : "resolveTypeInfo returned null!";
                if (variableType instanceof UnknownTypeInfo) {
                    if (unresolvedVariableType instanceof UnresolvedReferenceTypeInfo) {
                        variableType = NameResolver
                                .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedVariableType);
                        classInfoManager.add((ExternalClassInfo) variableType);
                    } else if (unresolvedVariableType instanceof UnresolvedArrayTypeInfo) {
                        final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                                .getElementType();
                        final int dimension = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                                .getDimension();
                        final TypeInfo elementType = NameResolver
                                .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                        classInfoManager.add((ExternalClassInfo) elementType);
                        variableType = ArrayTypeInfo.getType(elementType, dimension);
                    } else {
                        err.println("Can't resolve method local variable type : "
                                + unresolvedVariableType.getTypeName());
                    }
                }
                final int localFromLine = unresolvedLocalVariable.getFromLine();
                final int localFromColumn = unresolvedLocalVariable.getFromColumn();
                final int localToLine = unresolvedLocalVariable.getToLine();
                final int localToColumn = unresolvedLocalVariable.getToColumn();

                // ���[�J���ϐ��I�u�W�F�N�g�𐶐����CMethodInfo�ɒǉ�
                final LocalVariableInfo localVariable = new LocalVariableInfo(localModifiers,
                        variableName, variableType, localFromLine, localFromColumn, localToLine,
                        localToColumn);
                methodInfo.addLocalVariable(localVariable);
            }

            // ���\�b�h�������\�b�h���}�l�[�W���ɒǉ�
            ((TargetClassInfo) ownerClass).addDefinedMethod(methodInfo);
            methodInfoManager.add(methodInfo);

            // ���������\�b�h���ɂ��ǉ�
            unresolvedMethodInfo.setResolvedInfo(methodInfo);
        }

        // �e Unresolved�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo.getInnerClasses()) {
            registMethodInfos(unresolvedInnerClassInfo, classInfoManager, methodInfoManager);
        }
    }

    /**
     * ���\�b�h�I�[�o�[���C�h�����eMethodInfo�ɒǉ�����DaddInheritanceInfomationToClassInfos �̌� ���� registMethodInfos
     * �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ�
     */
    private static void addOverrideRelation() {

        // �S�Ă̑ΏۃN���X�ɑ΂���
        for (TargetClassInfo classInfo : ClassInfoManager.getInstance().getTargetClassInfos()) {
            addOverrideRelation(classInfo);
        }
    }

    /**
     * ���\�b�h�I�[�o�[���C�h�����eMethodInfo�ɒǉ�����D�����Ŏw�肵���N���X�̃��\�b�h�ɂ��ď������s��
     * 
     * @param classInfo �ΏۃN���X
     */
    private static void addOverrideRelation(final TargetClassInfo classInfo) {

        // �e�e�N���X�ɑ΂���
        for (ClassInfo superClassInfo : classInfo.getSuperClasses()) {

            // �e�ΏۃN���X�̊e���\�b�h�ɂ��āC�e�N���X�̃��\�b�h���I�[�o�[���C�h���Ă��邩�𒲍�
            for (MethodInfo methodInfo : classInfo.getDefinedMethods()) {
                addOverrideRelation(superClassInfo, methodInfo);
            }
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (ClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            addOverrideRelation((TargetClassInfo) innerClassInfo);
        }
    }

    /**
     * ���\�b�h�I�[�o�[���C�h����ǉ�����D�����Ŏw�肳�ꂽ�N���X�Œ�`����Ă��郁�\�b�h�ɑ΂��đ�����s��.
     * AddOverrideInformationToMethodInfos()�̒�����̂݌Ăяo�����D
     * 
     * @param classInfo �N���X���
     * @param overrider �I�[�o�[���C�h�Ώۂ̃��\�b�h
     */
    private static void addOverrideRelation(final ClassInfo classInfo, final MethodInfo overrider) {

        if ((null == classInfo) || (null == overrider)) {
            throw new NullPointerException();
        }

        if (!(classInfo instanceof TargetClassInfo)) {
            return;
        }

        for (TargetMethodInfo methodInfo : ((TargetClassInfo) classInfo).getDefinedMethods()) {

            // �R���X�g���N�^�̓I�[�o�[���C�h����Ȃ�
            if (methodInfo.isConstuructor()) {
                continue;
            }

            // ���\�b�h�����Ⴄ�ꍇ�̓I�[�o�[���C�h����Ȃ�
            if (!methodInfo.getMethodName().equals(overrider.getMethodName())) {
                continue;
            }

            // �I�[�o�[���C�h�֌W��o�^����
            overrider.addOverridee(methodInfo);
            methodInfo.addOverrider(overrider);

            // ���ڂ̃I�[�o�[���C�h�֌W�������o���Ȃ��̂ŁC���̃N���X�̐e�N���X�͒������Ȃ�
            return;
        }

        // �e�N���X�Q�ɑ΂��čċA�I�ɏ���
        for (ClassInfo superClassInfo : classInfo.getSuperClasses()) {
            addOverrideRelation(superClassInfo, overrider);
        }
    }

    /**
     * �G���e�B�e�B�i�t�B�[���h��N���X�j�̑���E�Q�ƁC���\�b�h�̌Ăяo���֌W��ǉ�����D
     */
    private static void addReferenceAssignmentCallRelateion() {

        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();
        final MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();
        final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache = new HashMap<UnresolvedTypeInfo, TypeInfo>();

        // �e�������N���X��� �ɑ΂���
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            addReferenceAssignmentCallRelation(unresolvedClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager, resolvedCache);
        }
    }

    /**
     * �G���e�B�e�B�i�t�B�[���h��N���X�j�̑���E�Q�ƁC���\�b�h�̌Ăяo���֌W��ǉ�����D
     * 
     * @param unresolvedClassInfo �����ΏۃN���X
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @param resolvedCache �����ς݌Ăяo�����̃L���b�V��
     */
    private static void addReferenceAssignmentCallRelation(
            final UnresolvedClassInfo unresolvedClassInfo, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // �������N���X��񂩂�C�����ς݃N���X�����擾
        final TargetClassInfo userClass = (TargetClassInfo) unresolvedClassInfo.getResolvedInfo();

        // �e���������\�b�h���ɑ΂���
        for (UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo.getDefinedMethods()) {

            // ���������\�b�h��񂩂�C�����ς݃��\�b�h�����擾
            final TargetMethodInfo caller = (TargetMethodInfo) unresolvedMethodInfo
                    .getResolvedInfo();
            if (null == caller) {
                throw new NullPointerException("UnresolvedMethodInfo#getResolvedInfo is null!");
            }

            // �e�������Q�ƃG���e�B�e�B�̖��O��������
            for (UnresolvedFieldUsage referencee : unresolvedMethodInfo.getFieldReferences()) {

                // �������Q�Ə���������
                NameResolver.resolveFieldReference(referencee, userClass, caller, classInfoManager,
                        fieldInfoManager, methodInfoManager, resolvedCache);
            }

            // ����������G���e�B�e�B�̖��O��������
            for (UnresolvedFieldUsage assignmentee : unresolvedMethodInfo.getFieldAssignments()) {

                // �������������������
                NameResolver.resolveFieldAssignment(assignmentee, userClass, caller,
                        classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            }

            // �e���������\�b�h�Ăяo�������̉�������
            for (UnresolvedMethodCall methodCall : unresolvedMethodInfo.getMethodCalls()) {

                // �e���������\�b�h�Ăяo������������
                NameResolver.resolveMethodCall(methodCall, userClass, caller, classInfoManager,
                        fieldInfoManager, methodInfoManager, resolvedCache);

            }
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo.getInnerClasses()) {
            addReferenceAssignmentCallRelation(unresolvedInnerClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager, resolvedCache);
        }
    }
}
