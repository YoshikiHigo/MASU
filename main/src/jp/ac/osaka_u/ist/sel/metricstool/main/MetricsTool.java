package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java15AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr.AntlrAstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;
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
 * @author higo
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

        MetricsTool metricsTool = new MetricsTool();

        ArgumentProcessor.processArgs(args, parameterDefs, new Settings());

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

        // �w���v���[�h�Ə��\�����[�h�������ɃI���ɂȂ��Ă���ꍇ�͕s��
        if (Settings.isHelpMode() && Settings.isDisplayMode()) {
            err.println("-h and -x can\'t be set at the same time!");
            metricsTool.printUsage();
            System.exit(0);
        }

        if (Settings.isHelpMode()) {
            // �w���v���[�h�̏ꍇ
            metricsTool.doHelpMode();
        } else {
            LANGUAGE language = metricsTool.getLanguage();
            metricsTool.loadPlugins(language, Settings.getMetricStrings());

            if (Settings.isDisplayMode()) {
                // ���\�����[�h�̏ꍇ
                metricsTool.doDisplayMode(language);
            } else {
                // ��̓��[�h
                metricsTool.doAnalysisMode(language);
            }
        }
    }

    /**
     * ���������R���X�g���N�^�D �Z�L�����e�B�}�l�[�W���̏��������s���D
     */
    public MetricsTool() {
        initSecurityManager();
    }

    /**
     * {@link #readTargetFiles()} �œǂݍ��񂾑Ώۃt�@�C���Q����͂���.
     * 
     * @param language ��͑Ώۂ̌���
     */
    public void analyzeTargetFiles(final LANGUAGE language) {
        // �Ώۃt�@�C�������

        AstVisitorManager<AST> visitorManager = null;
        if (language.equals(LANGUAGE.JAVA)) {
            visitorManager = new JavaAstVisitorManager<AST>(new AntlrAstVisitor(
                    new Java15AntlrAstTranslator()));
        }

        // �Ώۃt�@�C����AST���疢�����N���X�C�t�B�[���h�C���\�b�h�����擾
        {
            out.println("parsing all target files.");
            final int totalFileNumber = TargetFileManager.getInstance().size();
            int currentFileNumber = 1;
            final StringBuffer fileInformationBuffer = new StringBuffer();

            for (TargetFile targetFile : TargetFileManager.getInstance()) {
                try {
                    final String name = targetFile.getName();

                    final FileInfo fileInfo = new FileInfo(name);
                    FileInfoManager.getInstance().add(fileInfo);

                    if (Settings.isVerbose()) {
                        fileInformationBuffer.delete(0, fileInformationBuffer.length());
                        fileInformationBuffer.append("parsing ");
                        fileInformationBuffer.append(name);
                        fileInformationBuffer.append(" [");
                        fileInformationBuffer.append(currentFileNumber++);
                        fileInformationBuffer.append("/");
                        fileInformationBuffer.append(totalFileNumber);
                        fileInformationBuffer.append("]");
                        out.println(fileInformationBuffer.toString());
                    }

                    final Java15Lexer lexer = new Java15Lexer(new FileInputStream(name));
                    final Java15Parser parser = new Java15Parser(lexer);
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
        }

        out.println("resolving definitions and usages.");
        if (Settings.isVerbose()) {
            out.println("STEP1 : resolve class definitions.");
        }
        registClassInfos();
        if (Settings.isVerbose()) {
            out.println("STEP2 : resolve type parameters of classes.");
        }
        resolveTypeParameterOfClassInfos();
        if (Settings.isVerbose()) {
            out.println("STEP3 : resolve class inheritances.");
        }
        addInheritanceInformationToClassInfos();
        if (Settings.isVerbose()) {
            out.println("STEP4 : resolve field definitions.");
        }
        registFieldInfos();
        if (Settings.isVerbose()) {
            out.println("STEP5 : resolve method definitions.");
        }
        registMethodInfos();
        if (Settings.isVerbose()) {
            out.println("STEP6 : resolve method overrides.");
        }
        addOverrideRelation();
        if (Settings.isVerbose()) {
            out.println("STEP7 : resolve field and method usages.");
        }
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

        {
            /*
             * for (final ClassInfo classInfo :
             * ClassInfoManager.getInstance().getExternalClassInfos()) {
             * out.println(classInfo.getFullQualifiedName(Settings.getLanguage()
             * .getNamespaceDelimiter())); }
             */
        }
    }

    /**
     * �Ώی�����擾����.
     * 
     * @return �w�肳�ꂽ�Ώی���.�w�肳��Ȃ������ꍇ��null
     */
    public LANGUAGE getLanguage() {
        if (Settings.getLanguageString().equals(Settings.INIT)) {
            return null;
        }

        return Settings.getLanguage();
    }

    /**
     * �v���O�C�������[�h����. �w�肳�ꂽ����C�w�肳�ꂽ���g���N�X�Ɋ֘A����v���O�C���݂̂� {@link PluginManager}�ɓo�^����.
     * 
     * @param language �w�肷�錾��.
     * @param metrics �w�肷�郁�g���N�X�̔z��C�w�肵�Ȃ��ꍇ��null�܂��͋�̔z��
     */
    public void loadPlugins(final LANGUAGE language, final String[] metrics) {
        // �w�茾��ɑΉ�����v���O�C���Ŏw�肳�ꂽ���g���N�X���v������v���O�C�������[�h���ēo�^

        // metrics[]���O����Ȃ����C2�ȏ�w�肳��Ă��� or �P�����ǃf�t�H���g�̕����񂶂�Ȃ�
        boolean metricsSpecified = null != metrics && metrics.length != 0
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
                            if (metric.equalsIgnoreCase(pluginMetricName)) {
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
     * ���[�h�ς݂̃v���O�C�������s����.
     */
    public void launchPlugins() {
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
     * {@link Settings}�Ɏw�肳�ꂽ�ꏊ�����͑Ώۃt�@�C����ǂݍ���œo�^����
     */
    public void readTargetFiles() {

        out.println("building target file list.");

        // �f�B���N�g������ǂݍ���
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            registerFilesFromDirectory();

            // ���X�g�t�@�C������ǂݍ���
        } else if (!Settings.getListFile().equals(Settings.INIT)) {
            registerFilesFromListFile();
        }
    }

    /**
     * ���g���N�X���� {@link Settings} �Ɏw�肳�ꂽ�t�@�C���ɏo�͂���.
     */
    public void writeMetrics() {

        // �t�@�C�����g���N�X���v������ꍇ
        if (0 < PluginManager.getInstance().getFileMetricPlugins().size()) {

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

        // �N���X���g���N�X���v������ꍇ
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

        // ���\�b�h���g���N�X���v������ꍇ
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
     * 
     * �w���v���[�h�̈����̐��������m�F���邽�߂̃��\�b�h�D �s���Ȉ������w�肳��Ă����ꍇ�Cmain ���\�b�h�ɂ͖߂炸�C���̊֐����Ńv���O�������I������D
     * 
     */
    private void checkHelpModeParameterValidation() {
        // -h �͑��̃I�v�V�����Ɠ����w��ł��Ȃ�
        if ((!Settings.getTargetDirectory().equals(Settings.INIT))
                || (!Settings.getListFile().equals(Settings.INIT))
                || (!Settings.getLanguageString().equals(Settings.INIT))
                || (!Settings.getMetrics().equals(Settings.INIT))
                || (!Settings.getFileMetricsFile().equals(Settings.INIT))
                || (!Settings.getClassMetricsFile().equals(Settings.INIT))
                || (!Settings.getMethodMetricsFile().equals(Settings.INIT))) {
            err.println("-h can\'t be specified with any other options!");
            printUsage();
            System.exit(0);
        }
    }

    /**
     * 
     * ���\�����[�h�̈����̐��������m�F���邽�߂̃��\�b�h�D �s���Ȉ������w�肳��Ă����ꍇ�Cmain ���\�b�h�ɂ͖߂炸�C���̊֐����Ńv���O�������I������D
     * 
     */
    private void checkDisplayModeParameterValidation() {
        // -d �͎g���Ȃ�
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            err.println("-d can\'t be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -i �͎g���Ȃ�
        if (!Settings.getListFile().equals(Settings.INIT)) {
            err.println("-i can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -F �͎g���Ȃ�
        if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
            err.println("-F can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -C �͎g���Ȃ�
        if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {
            err.println("-C can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -M �͎g���Ȃ�
        if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {
            err.println("-M can't be specified in the display mode!");
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
    private void checkAnalysisModeParameterValidation(LANGUAGE language) {
        // -d �� -i �̂ǂ�����w�肳��Ă���͕̂s��
        if (Settings.getTargetDirectory().equals(Settings.INIT)
                && Settings.getListFile().equals(Settings.INIT)) {
            err.println("-d or -i must be specified in the analysis mode!");
            printUsage();
            System.exit(0);
        }

        // -d �� -i �̗������w�肳��Ă���͕̂s��
        if (!Settings.getTargetDirectory().equals(Settings.INIT)
                && !Settings.getListFile().equals(Settings.INIT)) {
            err.println("-d and -i can't be specified at the same time!");
            printUsage();
            System.exit(0);
        }

        // ���ꂪ�w�肳��Ȃ������͕̂s��
        if (null == language) {
            err.println("-l must be specified in the analysis mode.");
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
            err.println("-F must be used for specifying a file for file metrics!");
            System.exit(0);
        }

        // �N���X���g���N�X���v������ꍇ�� -C �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
        if (measureClassMetrics && (Settings.getClassMetricsFile().equals(Settings.INIT))) {
            err.println("-C must be used for specifying a file for class metrics!");
            System.exit(0);
        }
        // ���\�b�h���g���N�X���v������ꍇ�� -M �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
        if (measureMethodMetrics && (Settings.getMethodMetricsFile().equals(Settings.INIT))) {
            err.println("-M must be used for specifying a file for method metrics!");
            System.exit(0);
        }
    }

    /**
     * ��̓��[�h�����s����.
     * 
     * @param language �Ώی���
     */
    private void doAnalysisMode(LANGUAGE language) {
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
    private void doDisplayMode(LANGUAGE language) {
        checkDisplayModeParameterValidation();

        // -l �Ō��ꂪ�w�肳��Ă��Ȃ��ꍇ�́C��͉\����ꗗ��\��
        if (null == language) {
            err.println("Available languages;");
            LANGUAGE[] languages = LANGUAGE.values();
            for (int i = 0; i < languages.length; i++) {
                err.println("\t" + languages[0].getName() + ": can be specified with term \""
                        + languages[0].getIdentifierName() + "\"");
            }

            // -l �Ō��ꂪ�w�肳��Ă���ꍇ�́C���̃v���O���~���O����Ŏg�p�\�ȃ��g���N�X�ꗗ��\��
        } else {
            err.println("Available metrics for " + language.getName());
            for (AbstractPlugin plugin : PluginManager.getInstance().getPlugins()) {
                PluginInfo pluginInfo = plugin.getPluginInfo();
                if (pluginInfo.isMeasurable(language)) {
                    err.println("\t" + pluginInfo.getMetricName());
                }
            }
            // TODO ���p�\���g���N�X�ꗗ��\��
        }
    }

    /**
     * �w���v���[�h�����s����.
     */
    private void doHelpMode() {
        checkHelpModeParameterValidation();

        printUsage();
    }

    /**
     * {@link MetricsToolSecurityManager} �̏��������s��. �V�X�e���ɓo�^�ł���΁C�V�X�e���̃Z�L�����e�B�}�l�[�W���ɂ��o�^����.
     */
    private final void initSecurityManager() {
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
     * 
     * �c�[���̎g�����i�R�}���h���C���I�v�V�����j��\������D
     * 
     */
    private void printUsage() {

        err.println();
        err.println("Available options:");
        err.println("\t-d: root directory that you are going to analysis.");
        err.println("\t-i: List file including file paths that you are going to analysis.");
        err.println("\t-l: Programming language of the target files.");
        err.println("\t-m: Metrics that you want to get. Metrics names are separated with \',\'.");
        err.println("\t-v: Output progress verbosely.");
        err.println("\t-C: File path that the class type metrics are output");
        err.println("\t-F: File path that the file type metrics are output.");
        err.println("\t-M: File path that the method type metrics are output");

        err.println();
        err.println("Usage:");
        err.println("\t<Help Mode>");
        err.println("\tMetricsTool -h");
        err.println();
        err.println("\t<Display Mode>");
        err.println("\tMetricsTool -x -l");
        err.println("\tMetricsTool -x -l language -m");
        err.println();
        err.println("\t<Analysis Mode>");
        err
                .println("\tMetricsTool -d directory -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
        err
                .println("\tMetricsTool -i listFile -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
    }

    /**
     * 
     * ���X�g�t�@�C������Ώۃt�@�C����o�^����D �ǂݍ��݃G���[�����������ꍇ�́C���̃��\�b�h���Ńv���O�������I������D
     */
    private void registerFilesFromListFile() {

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
    private void registerFilesFromDirectory() {

        File targetDirectory = new File(Settings.getTargetDirectory());
        registerFilesFromDirectory(targetDirectory);
    }

    /**
     * 
     * @param file �Ώۃt�@�C���܂��̓f�B���N�g��
     * 
     * �Ώۂ��f�B���N�g���̏ꍇ�́C���̎q�ɑ΂��čċA�I�ɏ���������D �Ώۂ��t�@�C���̏ꍇ�́C�Ώی���̃\�[�X�t�@�C���ł���΁C�o�^�������s���D
     */
    private void registerFilesFromDirectory(File file) {

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
     * �����̎d�l�� Jargp �ɓn�����߂̔z��D
     */
    private static ParameterDef[] parameterDefs = {
            new BoolDef('h', "helpMode", "display usage", true),
            new BoolDef('x', "displayMode", "display available language or metrics", true),
            new BoolDef('v', "verbose", "output progress verbosely", true),
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
    private void registClassInfos() {

        // �������N���X���}�l�[�W���C �N���X���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // �e�������N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            //�@�N���X��������
            final TargetClassInfo classInfo = unresolvedClassInfo.resolveUnit(null, null,
                    classInfoManager, null, null);

            // �������ꂽ�N���X����o�^
            classInfoManager.add(classInfo);

            // �e�C���i�[�N���X�ɑ΂��ď���
            for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                    .getInnerClasses()) {

                //�@�C���i�[�N���X��������
                final TargetInnerClassInfo innerClass = registInnerClassInfo(
                        unresolvedInnerClassInfo, classInfo, classInfoManager);

                // �������ꂽ�C���i�[�N���X����o�^
                classInfo.addInnerClass(innerClass);
                classInfoManager.add(classInfo);
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
    private TargetInnerClassInfo registInnerClassInfo(
            final UnresolvedClassInfo unresolvedClassInfo, final TargetClassInfo outerClass,
            final ClassInfoManager classInfoManager) {

        final TargetInnerClassInfo classInfo = (TargetInnerClassInfo) unresolvedClassInfo
                .resolveUnit(outerClass, null, classInfoManager, null, null);

        // ���̃N���X�̃C���i�[�N���X�ɑ΂��čċA�I�ɏ���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {

            //�@�C���i�[�N���X��������
            final TargetInnerClassInfo innerClass = registInnerClassInfo(unresolvedInnerClassInfo,
                    classInfo, classInfoManager);

            // �������ꂽ�C���i�[�N���X����o�^
            classInfo.addInnerClass(innerClass);
            classInfoManager.add(classInfo);
        }

        // ���̃N���X�� ClassInfo ��Ԃ�
        return classInfo;
    }

    /**
     * �N���X�̌^�p�����[�^�𖼑O��������DregistClassInfos �̌�C ����addInheritanceInformationToClassInfo
     * �̑O�ɌĂяo���Ȃ���΂Ȃ�Ȃ��D
     * 
     */
    private void resolveTypeParameterOfClassInfos() {

        // �������N���X���}�l�[�W���C �����ς݃N���X�}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // �e�������N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            resolveTypeParameterOfClassInfos(unresolvedClassInfo, classInfoManager);
        }
    }

    /**
     * �N���X�̌^�p�����[�^�𖼑O��������D resolveTypeParameterOfClassInfo() ������̂݌Ăяo�����ׂ�
     * 
     * @param unresolvedClassInfo ���O��������^�p�����[�^�����N���X
     * @param classInfoManager ���O�����ɗp����N���X�}�l�[�W��
     */
    private void resolveTypeParameterOfClassInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager) {

        // �����ς݃N���X�����擾
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolvedUnit();
        assert null != classInfo : "classInfo shouldn't be null!";

        // �������N���X��񂩂疢�����^�p�����[�^���擾���C�^�������s������C�����ς݃N���X���ɕt�^����
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : unresolvedClassInfo
                .getTypeParameters()) {

            final TypeInfo typeParameter = unresolvedTypeParameter.resolveType(classInfo, null,
                    classInfoManager, null, null);
            classInfo.addTypeParameter((TypeParameterInfo) typeParameter);
        }

        // �e�������C���i�[�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            resolveTypeParameterOfClassInfos(unresolvedInnerClassInfo, classInfoManager);
        }
    }

    /**
     * �N���X�̌p������ ClassInfo �ɒǉ�����D��x�ڂ� AST �p�[�X�̌�C���� registClassInfos �̌�ɂ�т����Ȃ���΂Ȃ�Ȃ��D
     */
    private void addInheritanceInformationToClassInfos() {

        // Unresolved �N���X���}�l�[�W���C �N���X���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // ���O�����s�\�N���X��ۑ����邽�߂̃��X�g
        final List<UnresolvedClassInfo> unresolvableClasses = new LinkedList<UnresolvedClassInfo>();

        // �e Unresolved�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            addInheritanceInformationToClassInfo(unresolvedClassInfo, classInfoManager,
                    unresolvableClasses);
        }

        // ���O�����s�\�N���X����͂���
        for (int i = 0; i < 100; i++) {

            CLASSLOOP: for (final Iterator<UnresolvedClassInfo> classIterator = unresolvableClasses
                    .iterator(); classIterator.hasNext();) {

                // ClassInfo ���擾
                final UnresolvedClassInfo unresolvedClassInfo = classIterator.next();
                final TargetClassInfo classInfo = unresolvedClassInfo.getResolvedUnit();
                assert null != classInfo : "classInfo shouldn't be null!";

                // �e�e�N���X���ɑ΂���
                for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                        .getSuperClasses()) {

                    TypeInfo superClassType = unresolvedSuperClassType.resolveType(classInfo, null,
                            classInfoManager, null, null);

                    // null �łȂ��ꍇ�͖��O�����ɐ��������Ƃ݂Ȃ�
                    if (null != superClassType) {

                        // ������Ȃ������ꍇ�͖��O��Ԗ���UNKNOWN�ȃN���X��o�^����
                        if (superClassType instanceof UnknownTypeInfo) {
                            final ExternalClassInfo superClass = new ExternalClassInfo(
                                    unresolvedSuperClassType.getTypeName());
                            classInfoManager.add(superClass);
                            superClassType = new ClassTypeInfo(superClass);
                        }

                        classInfo.addSuperClass((ClassTypeInfo) superClassType);
                        ((ClassTypeInfo) superClassType).getReferencedClass()
                                .addSubClass(classInfo);

                        // null �ȏꍇ�͖��O�����Ɏ��s�����Ƃ݂Ȃ��̂� unresolvedClassInfo �� unresolvableClasses
                        // ����폜���Ȃ�
                    } else {
                        continue CLASSLOOP;
                    }
                }

                classIterator.remove();
            }

            // ����ׂ� unresolvableClasses �����ւ�
            Collections.shuffle(unresolvableClasses);
        }

        if (0 < unresolvableClasses.size()) {
            err.println("There are " + unresolvableClasses.size()
                    + " unresolvable class inheritance");
        }
    }

    /**
     * �N���X�̌p������ InnerClassInfo �ɒǉ�����DaddInheritanceInformationToClassInfos �̒�����̂݌Ăяo�����ׂ�
     * 
     * @param unresolvedClassInfo �p���֌W��ǉ�����i�������j�N���X���
     * @param classInfoManager ���O�����ɗp����N���X�}�l�[�W��
     */
    private void addInheritanceInformationToClassInfo(
            final UnresolvedClassInfo unresolvedClassInfo, final ClassInfoManager classInfoManager,
            final List<UnresolvedClassInfo> unresolvableClasses) {

        // ClassInfo ���擾
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolvedUnit();
        assert null != classInfo : "classInfo shouldn't be null!";

        // �e�e�N���X���ɑ΂���
        for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                .getSuperClasses()) {

            TypeInfo superClassType = unresolvedSuperClassType.resolveType(classInfo, null,
                    classInfoManager, null, null);

            // null �������ꍇ�͉����s�\���X�g�Ɉꎞ�I�Ɋi�[
            if (null == superClassType) {

                unresolvableClasses.add(unresolvedClassInfo);

            } else {

                // ������Ȃ������ꍇ�͖��O��Ԗ���UNKNOWN�ȃN���X��o�^����
                if (superClassType instanceof UnknownTypeInfo) {
                    final ExternalClassInfo superClass = new ExternalClassInfo(
                            unresolvedSuperClassType.getTypeName());
                    classInfoManager.add(superClass);
                }

                classInfo.addSuperClass((ClassTypeInfo) superClassType);
                ((ClassTypeInfo) superClassType).getReferencedClass().addSubClass(classInfo);
            }
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            addInheritanceInformationToClassInfo(unresolvedInnerClassInfo, classInfoManager,
                    unresolvableClasses);
        }
    }

    /**
     * �t�B�[���h�̒�`�� FieldInfoManager �ɓo�^����D registClassInfos �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ�
     * 
     */
    private void registFieldInfos() {

        // Unresolved �N���X���}�l�[�W���C�N���X���}�l�[�W���C�t�B�[���h���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();

        // �e Unresolved�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
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
    private void registFieldInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager) {

        // ClassInfo ���擾
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolvedUnit();
        assert null != ownerClass : "ownerClass shouldn't be null!";

        // �e�������t�B�[���h�ɑ΂���
        for (final UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo.getDefinedFields()) {

            unresolvedFieldInfo.resolveUnit(ownerClass, null, classInfoManager, fieldInfoManager,
                    null);
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registFieldInfos(unresolvedInnerClassInfo, classInfoManager, fieldInfoManager);
        }
    }

    /**
     * ���\�b�h�̒�`�� MethodInfoManager �ɓo�^����DregistClassInfos �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ��D
     */
    private void registMethodInfos() {

        // Unresolved �N���X���}�l�[�W���C �N���X���}�l�[�W���C���\�b�h���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();

        // �e Unresolved�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
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
    private void registMethodInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final MethodInfoManager methodInfoManager) {

        // ClassInfo ���擾
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolvedUnit();

        // �e���������\�b�h�ɑ΂���
        for (final UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                .getDefinedMethods()) {

            // ���\�b�h��������
            final TargetMethodInfo methodInfo = unresolvedMethodInfo.resolveUnit(ownerClass, null,
                    classInfoManager, null, methodInfoManager);

            // ���\�b�h����o�^
            ownerClass.addDefinedMethod(methodInfo);
            methodInfoManager.add(methodInfo);
        }

        // �e�������R���X�g���N�^�ɑ΂���
        for (final UnresolvedConstructorInfo unresolvedConstructorInfo : unresolvedClassInfo
                .getDefinedConstructors()) {

            //�@�R���X�g���N�^��������
            final TargetConstructorInfo constructorInfo = unresolvedConstructorInfo.resolveUnit(
                    ownerClass, null, classInfoManager, null, methodInfoManager);
            methodInfoManager.add(constructorInfo);

            // �R���X�g���N�^����o�^            
            ownerClass.addDefinedConstructor(constructorInfo);
            methodInfoManager.add(constructorInfo);

        }

        // �e Unresolved�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registMethodInfos(unresolvedInnerClassInfo, classInfoManager, methodInfoManager);
        }
    }

    /**
     * ���\�b�h�I�[�o�[���C�h�����eMethodInfo�ɒǉ�����DaddInheritanceInfomationToClassInfos �̌� ���� registMethodInfos
     * �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ�
     */
    private void addOverrideRelation() {

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
    private void addOverrideRelation(final TargetClassInfo classInfo) {

        // �e�e�N���X�ɑ΂���
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            // �e�ΏۃN���X�̊e���\�b�h�ɂ��āC�e�N���X�̃��\�b�h���I�[�o�[���C�h���Ă��邩�𒲍�
            for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {
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
    private void addOverrideRelation(final ClassInfo classInfo, final MethodInfo overrider) {

        if ((null == classInfo) || (null == overrider)) {
            throw new NullPointerException();
        }

        if (!(classInfo instanceof TargetClassInfo)) {
            return;
        }

        for (final TargetMethodInfo methodInfo : ((TargetClassInfo) classInfo).getDefinedMethods()) {

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
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            addOverrideRelation(superClassInfo, overrider);
        }
    }

    /**
     * �G���e�B�e�B�i�t�B�[���h��N���X�j�̑���E�Q�ƁC���\�b�h�̌Ăяo���֌W��ǉ�����D
     */
    private void addReferenceAssignmentCallRelateion() {

        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();
        final MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();

        // �e�������N���X��� �ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            addReferenceAssignmentCallRelation(unresolvedClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager);
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
    private void addReferenceAssignmentCallRelation(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // �������N���X��񂩂�C�����ς݃N���X�����擾
        // final TargetClassInfo ownerClass = unresolvedClassInfo.getResolvedUnit();

        // �e���������\�b�h���ɑ΂���
        for (final UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                .getDefinedMethods()) {

            // ���������\�b�h�����̗��p�֌W������
            this.addReferenceAssignmentCallRelation(unresolvedMethodInfo, unresolvedClassInfo,
                    classInfoManager, fieldInfoManager, methodInfoManager);
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            addReferenceAssignmentCallRelation(unresolvedInnerClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }
    }

    /**
     * �G���e�B�e�B�i�t�B�[���h��N���X�j�̑���E�Q�ƁC���\�b�h�̌Ăяo���֌W��ǉ�����D
     * 
     * @param unresolvedLocalSpace ��͑Ώۖ��������[�J���̈�
     * @param unresolvedClassInfo �����ΏۃN���X
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @param resolvedCache �����ς݌Ăяo�����̃L���b�V��
     */
    private void addReferenceAssignmentCallRelation(
            final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace,
            final UnresolvedClassInfo unresolvedClassInfo, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // ���������\�b�h��񂩂�C�����ς݃��\�b�h�����擾
        final LocalSpaceInfo localSpace = unresolvedLocalSpace.getResolvedUnit();
        assert null != localSpace : "UnresolvedLocalSpaceInfo#getResolvedInfo is null!";

        // ���L�N���X���擾
        final TargetClassInfo ownerClass = (TargetClassInfo) localSpace.getOwnerClass();
        final CallableUnitInfo ownerMethod;
        if (localSpace instanceof CallableUnitInfo) {
            ownerMethod = (CallableUnitInfo) localSpace;
        } else if (localSpace instanceof BlockInfo) {
            ownerMethod = ((BlockInfo) localSpace).getOwnerMethod();
        } else {
            ownerMethod = null;
            assert false : "Here shouldn't be reached!";
        }

        // �e�������t�B�[���h�g�p�̖��O��������
        for (final UnresolvedVariableUsageInfo unresolvedVariableUsage : unresolvedLocalSpace
                .getVariableUsages()) {

            // �������ϐ��g�p������
            final EntityUsageInfo variableUsage = unresolvedVariableUsage.resolveEntityUsage(
                    ownerClass, ownerMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            // ���O�����ł��ꍇ�͓o�^
            if (variableUsage instanceof VariableUsageInfo) {
                ownerMethod.addVariableUsage((VariableUsageInfo<?>) variableUsage);

                // �t�B�[���h�̏ꍇ�́C���p�֌W�������
                if (variableUsage instanceof FieldUsageInfo) {
                    final boolean reference = ((FieldUsageInfo) variableUsage).isReference();
                    final FieldInfo usedField = ((FieldUsageInfo) variableUsage).getUsedVariable();
                    if (reference) {
                        usedField.addReferencer(ownerMethod);
                    } else {
                        usedField.addAssignmenter(ownerMethod);
                    }
                }
            }
        }

        // �e���������\�b�h�Ăяo���̉�������
        for (final UnresolvedCallInfo unresolvedCall : unresolvedLocalSpace.getCalls()) {

            final EntityUsageInfo memberCall = unresolvedCall.resolveEntityUsage(ownerClass,
                    ownerMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            // ���\�b�h����уR���X�g���N�^�Ăяo���������ł����ꍇ
            if (memberCall instanceof MethodCallInfo) {
                ownerMethod.addCall((MethodCallInfo) memberCall);
                ((MethodCallInfo) memberCall).getCallee().addCaller(ownerMethod);
            } else if (memberCall instanceof ConstructorCallInfo) {
                ownerMethod.addCall((ConstructorCallInfo) memberCall);
            }
        }

        //�@�e�C���i�[�u���b�N�ɂ���
        for (final UnresolvedBlockInfo<?> unresolvedBlockInfo : unresolvedLocalSpace.getInnerBlocks()) {
            
            // ���������\�b�h�����̗��p�֌W������
            this.addReferenceAssignmentCallRelation(unresolvedBlockInfo, unresolvedClassInfo,
                    classInfoManager, fieldInfoManager, methodInfoManager);
        }
    }
}
