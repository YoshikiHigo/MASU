package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp.CSharpAntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java13AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java14AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java15AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr.AntlrAstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CSharpLexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CSharpParser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CommonASTWithLineNumber;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java14Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java14Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.MasuAstFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.DefaultPluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.DefaultPluginLoader;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.PluginLoadException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import antlr.ASTFactory;
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

        // ���\���p�̃��X�i���쐬
        final MessageListener outListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        final MessageListener errListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(errListener);

        final Options options = new Options();

        {
            final Option h = new Option("h", "help", false, "display usage");
            h.setRequired(false);
            options.addOption(h);
        }

        {
            final Option v = new Option("v", "verbose", false, "output progress verbosely");
            v.setRequired(false);
            options.addOption(v);
        }

        {
            final Option d = new Option("d", "directory", true, "specify target directory");
            d.setArgName("directory");
            d.setArgs(1);
            d.setRequired(false);
            options.addOption(d);
        }

        {
            final Option i = new Option("i", "input", true,
                    "specify the input that contains the list of target files");
            i.setArgName("input");
            i.setArgs(1);
            i.setRequired(false);
            options.addOption(i);
        }

        {
            final Option l = new Option("l", "language", true, "specify programming language");
            l.setArgName("input");
            l.setArgs(1);
            l.setRequired(false);
            options.addOption(l);
        }

        {
            final Option m = new Option("m", "metrics", true,
                    "specify measured metrics with comma separeted format (e.g., -m rfc,dit,lcom)");
            m.setArgName("metrics");
            m.setArgs(1);
            m.setRequired(false);
            options.addOption(m);
        }

        {
            final Option F = new Option("F", "FileMetricsFile", true,
                    "specify file that measured FILE metrics were stored into");
            F.setArgName("file metrics file");
            F.setArgs(1);
            F.setRequired(false);
            options.addOption(F);
        }

        {
            final Option C = new Option("C", "ClassMetricsFile", true,
                    "specify file that measured CLASS metrics were stored into");
            C.setArgName("class metrics file");
            C.setArgs(1);
            C.setRequired(false);
            options.addOption(C);
        }

        {
            final Option M = new Option("M", "MethodMetricsFile", true,
                    "specify file that measured METHOD metrics were stored into");
            M.setArgName("method metrics file");
            M.setArgs(1);
            M.setRequired(false);
            options.addOption(M);
        }

        {
            final Option A = new Option("A", "AttributeMetricsFile", true,
                    "specify file that measured ATTRIBUTE metrics were stored into");
            A.setArgName("attribute metrics file");
            A.setArgs(1);
            A.setRequired(false);
            options.addOption(A);
        }

        final MetricsTool metricsTool = new MetricsTool();

        try {

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            // "-h"���w�肳��Ă���ꍇ�̓w���v��\�����ďI��
            // ���̂Ƃ��C���̃I�v�V�����͑S�Ė��������
            if (cmd.hasOption("h")) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("MetricsTool", options, true);

                // -l �Ō��ꂪ�w�肳��Ă��Ȃ��ꍇ�́C��͉\����ꗗ��\��
                if (!cmd.hasOption("l")) {
                    err.println("Available languages;");
                    for (final LANGUAGE language : LANGUAGE.values()) {
                        err.println("\t" + language.getName() + ": can be specified with term \""
                                + language.getIdentifierName() + "\"");
                    }

                    // -l �Ō��ꂪ�w�肳��Ă���ꍇ�́C���̃v���O���~���O����Ŏg�p�\�ȃ��g���N�X�ꗗ��\��
                } else {
                    Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
                    err.println("Available metrics for "
                            + Settings.getInstance().getLanguage().getName());
                    for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                            .getPlugins()) {
                        final PluginInfo pluginInfo = plugin.getPluginInfo();
                        if (pluginInfo.isMeasurable(Settings.getInstance().getLanguage())) {
                            err.println("\t" + pluginInfo.getMetricName());
                        }
                    }
                }

                System.exit(0);
            }

            Settings.getInstance().setVerbose(cmd.hasOption("v"));
            if (cmd.hasOption("d")) {
                Settings.getInstance().setTargetDirectory(cmd.getOptionValue("d"));
            }
            if (cmd.hasOption("i")) {
                Settings.getInstance().setListFile(cmd.getOptionValue("i"));
            }
            Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
            Settings.getInstance().setMetrics(cmd.getOptionValue("m"));
            if (cmd.hasOption("F")) {
                Settings.getInstance().setFileMetricsFile(cmd.getOptionValue("F"));
            }
            if (cmd.hasOption("C")) {
                Settings.getInstance().setClassMetricsFile(cmd.getOptionValue("C"));
            }
            if (cmd.hasOption("M")) {
                Settings.getInstance().setMethodMetricsFile(cmd.getOptionValue("M"));
            }
            if (cmd.hasOption("A")) {
                Settings.getInstance().setFieldMetricsFile(cmd.getOptionValue("A"));
            }

            metricsTool.loadPlugins(Settings.getInstance().getMetrics());

            // �R�}���h���C�����������������ǂ����`�F�b�N����
            {
                // -d �� -i �̂ǂ�����w�肳��Ă���͕̂s��
                if (!cmd.hasOption("d") && !cmd.hasOption("l")) {
                    err.println("-d and/or -i must be specified in the analysis mode!");
                    System.exit(0);
                }

                // ���ꂪ�w�肳��Ȃ������͕̂s��
                if (!cmd.hasOption("l")) {
                    err.println("-l must be specified for analysis");
                    System.exit(0);
                }

                {
                    // �t�@�C�����g���N�X���v������ꍇ�� -F �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && !cmd.hasOption("F")) {
                        err.println("-F must be specified for file metrics!");
                        System.exit(0);
                    }

                    // �N���X���g���N�X���v������ꍇ�� -C �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && !cmd.hasOption("C")) {
                        err.println("-C must be specified for class metrics!");
                        System.exit(0);
                    }
                    // ���\�b�h���g���N�X���v������ꍇ�� -M �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && !cmd.hasOption("M")) {
                        err.println("-M must be specified for method metrics!");
                        System.exit(0);
                    }

                    // �t�B�[���h���g���N�X���v������ꍇ�� -A �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && !cmd.hasOption("A")) {
                        err.println("-A must be specified for field metrics!");
                        System.exit(0);
                    }
                }

                {
                    // �t�@�C�����g���N�X���v�����Ȃ��̂� -F�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && cmd.hasOption("F")) {
                        err.println("No file metric is specified. -F is ignored.");
                    }

                    // �N���X���g���N�X���v�����Ȃ��̂� -C�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && cmd.hasOption("C")) {
                        err.println("No class metric is specified. -C is ignored.");
                    }

                    // ���\�b�h���g���N�X���v�����Ȃ��̂� -M�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && cmd.hasOption("M")) {
                        err.println("No method metric is specified. -M is ignored.");
                    }

                    // �t�B�[���h���g���N�X���v�����Ȃ��̂� -A�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && cmd.hasOption("A")) {
                        err.println("No field metric is specified. -A is ignored.");
                    }
                }
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        final long start = System.nanoTime();

        metricsTool.readTargetFiles();
        metricsTool.analyzeTargetFiles();
        metricsTool.launchPlugins();
        metricsTool.writeMetrics();

        out.println("successfully finished.");

        final long end = System.nanoTime();

        if (Settings.getInstance().isVerbose()) {
            out.println("elapsed time: " + (end - start) / 1000000000 + " seconds");
            out.println("number of analyzed files: "
                    + DataManager.getInstance().getFileInfoManager().getFileInfos().size());

            int loc = 0;
            for (final FileInfo file : DataManager.getInstance().getFileInfoManager()
                    .getFileInfos()) {
                loc += file.getLOC();
            }
            out.println("analyzed lines of code: " + loc);
        }

        MessagePool.getInstance(MESSAGE_TYPE.OUT).removeMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).removeMessageListener(errListener);

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
     */
    public void analyzeTargetFiles() {
        // �Ώۃt�@�C�������

        AstVisitorManager<AST> visitorManager = null;

        switch (Settings.getInstance().getLanguage()) {
        case JAVA15:
            visitorManager = new JavaAstVisitorManager<AST>(new AntlrAstVisitor(
                    new Java15AntlrAstTranslator()));
            break;
        case JAVA14:
            visitorManager = new JavaAstVisitorManager<AST>(new AntlrAstVisitor(
                    new Java14AntlrAstTranslator()));
            break;
        case JAVA13:
            visitorManager = new JavaAstVisitorManager<AST>(new AntlrAstVisitor(
                    new Java13AntlrAstTranslator()));
            break;
        case CSHARP:
            visitorManager = new JavaAstVisitorManager<AST>(new AntlrAstVisitor(
                    new CSharpAntlrAstTranslator()));
            break;
        default:
            assert false : "here shouldn't be reached!";
        }

        // �Ώۃt�@�C����AST���疢�����N���X�C�t�B�[���h�C���\�b�h�����擾
        {
            out.println("parsing all target files.");
            final int totalFileNumber = DataManager.getInstance().getTargetFileManager().size();
            int currentFileNumber = 1;
            final StringBuffer fileInformationBuffer = new StringBuffer();

            for (final TargetFile targetFile : DataManager.getInstance().getTargetFileManager()) {
                try {
                    final String name = targetFile.getName();

                    final FileInfo fileInfo = new FileInfo(name);
                    DataManager.getInstance().getFileInfoManager().add(fileInfo);

                    if (Settings.getInstance().isVerbose()) {
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

                    switch (Settings.getInstance().getLanguage()) {
                    case JAVA15:
                        final Java15Lexer java15lexer = new Java15Lexer(new FileInputStream(name));
                        java15lexer.setTabSize(1);
                        final Java15Parser java15parser = new Java15Parser(java15lexer);

                        final ASTFactory java15factory = new MasuAstFactory();
                        java15factory.setASTNodeClass(CommonASTWithLineNumber.class);

                        java15parser.setASTFactory(java15factory);

                        java15parser.compilationUnit();
                        targetFile.setCorrectSytax(true);

                        if (visitorManager != null) {

                            visitorManager.visitStart(java15parser.getAST());
                        }

                        fileInfo.setLOC(java15lexer.getLine());
                        break;

                    case JAVA14:
                        final Java14Lexer java14lexer = new Java14Lexer(new FileInputStream(name));
                        java14lexer.setTabSize(1);
                        final Java14Parser java14parser = new Java14Parser(java14lexer);

                        final ASTFactory java14factory = new MasuAstFactory();
                        java14factory.setASTNodeClass(CommonASTWithLineNumber.class);

                        java14parser.setASTFactory(java14factory);

                        java14parser.compilationUnit();
                        targetFile.setCorrectSytax(true);

                        if (visitorManager != null) {
                            visitorManager.visitStart(java14parser.getAST());
                        }

                        fileInfo.setLOC(java14lexer.getLine());
                        break;
                    case CSHARP:
                        final CSharpLexer csharpLexer = new CSharpLexer(new FileInputStream(name));
                        csharpLexer.setTabSize(1);
                        final CSharpParser csharpParser = new CSharpParser(csharpLexer);

                        final ASTFactory cshaprFactory = new MasuAstFactory();
                        cshaprFactory.setASTNodeClass(CommonASTWithLineNumber.class);

                        csharpParser.setASTFactory(cshaprFactory);

                        csharpParser.compilationUnit();
                        targetFile.setCorrectSytax(true);

                        if (visitorManager != null) {
                            visitorManager.visitStart(csharpParser.getAST());
                        }

                        fileInfo.setLOC(csharpLexer.getLine());
                        break;
                    default:
                        assert false : "here shouldn't be reached!";
                    }

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
                } catch (ASTParseException e) {
                    err.println(e.getMessage());
                }
            }
        }

        out.println("resolving definitions and usages.");
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP1 : resolve class definitions.");
        }
        registClassInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP2 : resolve type parameters of classes.");
        }
        resolveTypeParameterOfClassInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP3 : resolve class inheritances.");
        }
        addInheritanceInformationToClassInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP4 : resolve field definitions.");
        }
        registFieldInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP5 : resolve method definitions.");
        }
        registMethodInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP6 : resolve type parameter usages.");
        }
        addClassTypeParameterInfos();
        addMethodTypeParameterInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP7 : resolve method overrides.");
        }
        addOverrideRelation();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP8 : resolve field and method usages.");
        }
        addReferenceAssignmentCallRelateion();

        // ���@���̂���t�@�C���ꗗ��\��
        // err.println("The following files includes uncorrect syntax.");
        // err.println("Any metrics of them were not measured");
        for (final TargetFile targetFile : DataManager.getInstance().getTargetFileManager()) {
            if (!targetFile.isCorrectSyntax()) {
                err.println("Incorrect syntax file: " + targetFile.getName());
            }
        }
    }

    /**
     * �v���O�C�������[�h����. �w�肳�ꂽ����C�w�肳�ꂽ���g���N�X�Ɋ֘A����v���O�C���݂̂� {@link PluginManager}�ɓo�^����.
     * null ���w�肳�ꂽ�ꍇ�͑Ώی���ɂ����Čv���\�ȑS�Ẵ��g���N�X��o�^����
     * 
     * @param metrics �w�肷�郁�g���N�X�̔z��C�w�肵�Ȃ��ꍇ��null
     */
    public void loadPlugins(final String[] metrics) {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();
        try {
            for (final AbstractPlugin plugin : (new DefaultPluginLoader()).loadPlugins()) {// �v���O�C����S���[�h
                final PluginInfo info = plugin.getPluginInfo();

                // �Ώی���Ōv���\�łȂ���Γo�^���Ȃ�
                if (!info.isMeasurable(settings.getLanguage())) {
                    continue;
                }

                if (null != metrics) {
                    // ���g���N�X���w�肳��Ă���̂ł��̃v���O�C���ƈ�v���邩�`�F�b�N
                    final String pluginMetricName = info.getMetricName();
                    for (final String metric : metrics) {
                        if (metric.equalsIgnoreCase(pluginMetricName)) {
                            pluginManager.addPlugin(plugin);
                            break;
                        }
                    }

                    // ���g���N�X���w�肳��Ă��Ȃ��̂łƂ肠�����S���o�^
                } else {
                    pluginManager.addPlugin(plugin);
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

        out.println("calculating metrics.");

        PluginLauncher launcher = new DefaultPluginLauncher();
        launcher.setMaximumLaunchingNum(1);
        launcher.launchAll(DataManager.getInstance().getPluginManager().getPlugins());

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

        final Settings settings = Settings.getInstance();

        // �f�B���N�g������ǂݍ���
        if (null != settings.getTargetDirectory()) {

            File targetDirectory = new File(settings.getTargetDirectory());
            registerFilesFromDirectory(targetDirectory);

            // ���X�g�t�@�C������ǂݍ���
        } else if (null != settings.getListFile()) {

            try {

                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
                for (BufferedReader reader = new BufferedReader(new FileReader(settings
                        .getListFile())); reader.ready();) {
                    final String line = reader.readLine();
                    final TargetFile targetFile = new TargetFile(line);
                    targetFiles.add(targetFile);
                }

            } catch (FileNotFoundException e) {
                err.println("\"" + settings.getListFile() + "\" is not a valid file!");
                System.exit(0);
            } catch (IOException e) {
                err.println("\"" + settings.getListFile() + "\" can\'t read!");
                System.exit(0);
            }
        }
    }

    /**
     * ���g���N�X���� {@link Settings} �Ɏw�肳�ꂽ�t�@�C���ɏo�͂���.
     */
    public void writeMetrics() {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();

        // �t�@�C�����g���N�X���v������ꍇ
        if (0 < pluginManager.getFileMetricPlugins().size()) {

            try {
                final FileMetricsInfoManager manager = DataManager.getInstance()
                        .getFileMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getFileMetricsFile();
                final CSVFileMetricsWriter writer = new CSVFileMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("File metrics can't be output!");
            }
        }

        // �N���X���g���N�X���v������ꍇ
        if (0 < pluginManager.getClassMetricPlugins().size()) {

            try {
                final ClassMetricsInfoManager manager = DataManager.getInstance()
                        .getClassMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getClassMetricsFile();
                final CSVClassMetricsWriter writer = new CSVClassMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Class metrics can't be output!");
            }
        }

        // ���\�b�h���g���N�X���v������ꍇ
        if (0 < pluginManager.getMethodMetricPlugins().size()) {

            try {
                final MethodMetricsInfoManager manager = DataManager.getInstance()
                        .getMethodMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Method metrics can't be output!");
            }

        }

        // �t�B�[���h���g���N�X���v������ꍇ
        if (0 < pluginManager.getFieldMetricPlugins().size()) {

            try {
                final FieldMetricsInfoManager manager = DataManager.getInstance()
                        .getFieldMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Field metrics can't be output!");
            }
        }
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
     * @param file �Ώۃt�@�C���܂��̓f�B���N�g��
     * 
     * �Ώۂ��f�B���N�g���̏ꍇ�́C���̎q�ɑ΂��čċA�I�ɏ���������D �Ώۂ��t�@�C���̏ꍇ�́C�Ώی���̃\�[�X�t�@�C���ł���΁C�o�^�������s���D
     */
    private void registerFilesFromDirectory(final File file) {

        // �f�B���N�g���Ȃ�΁C�ċA�I�ɏ���
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                registerFilesFromDirectory(subfiles[i]);
            }

            // �t�@�C���Ȃ�΁C�g���q���Ώی���ƈ�v����Γo�^
        } else if (file.isFile()) {

            final LANGUAGE language = Settings.getInstance().getLanguage();
            final String extension = language.getExtension();
            final String path = file.getAbsolutePath();
            if (path.endsWith(extension)) {
                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
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
     * �o�̓��b�Z�[�W�o�͗p�̃v�����^
     */
    protected static MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    protected static MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * �N���X�̒�`�� ClassInfoManager �ɓo�^����DAST �p�[�X�̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ��D
     */
    private void registClassInfos() {

        // �������N���X���}�l�[�W���C �N���X���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

        // �e�������N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            final FileInfo fileInfo = unresolvedClassInfo.getFileInfo();

            //�@�N���X��������
            final TargetClassInfo classInfo = unresolvedClassInfo.resolve(null, null,
                    classInfoManager, null, null);

            fileInfo.addDefinedClass(classInfo);

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
                classInfoManager.add(innerClass);
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

        final TargetInnerClassInfo classInfo = (TargetInnerClassInfo) unresolvedClassInfo.resolve(
                outerClass, null, classInfoManager, null, null);

        // ���̃N���X�̃C���i�[�N���X�ɑ΂��čċA�I�ɏ���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {

            //�@�C���i�[�N���X��������
            final TargetInnerClassInfo innerClass = registInnerClassInfo(unresolvedInnerClassInfo,
                    classInfo, classInfoManager);

            // �������ꂽ�C���i�[�N���X����o�^
            classInfo.addInnerClass(innerClass);
            classInfoManager.add(innerClass);
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
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

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
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
        assert null != classInfo : "classInfo shouldn't be null!";

        // �������N���X��񂩂疢�����^�p�����[�^���擾���C�^�������s������C�����ς݃N���X���ɕt�^����
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : unresolvedClassInfo
                .getTypeParameters()) {

            final TypeInfo typeParameter = unresolvedTypeParameter.resolve(classInfo, null,
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
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // ���O�����s�\�N���X��ۑ����邽�߂̃��X�g
        final List<UnresolvedClassInfo> unresolvableClasses = new LinkedList<UnresolvedClassInfo>();

        // �e Unresolved�N���X�ɑ΂���
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            addInheritanceInformationToClassInfo(unresolvedClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager, unresolvableClasses);
        }

        // ���O�����s�\�N���X����͂���
        for (int i = 0; i < 100; i++) {

            CLASSLOOP: for (final Iterator<UnresolvedClassInfo> classIterator = unresolvableClasses
                    .iterator(); classIterator.hasNext();) {

                // ClassInfo ���擾
                final UnresolvedClassInfo unresolvedClassInfo = classIterator.next();
                final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
                assert null != classInfo : "classInfo shouldn't be null!";

                // �e�e�N���X���ɑ΂���
                for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                        .getSuperClasses()) {

                    TypeInfo superClassType = unresolvedSuperClassType.resolve(classInfo, null,
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
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager,
            final List<UnresolvedClassInfo> unresolvableClasses) {

        // ClassInfo ���擾
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
        assert null != classInfo : "classInfo shouldn't be null!";

        // �e�e�N���X���ɑ΂���
        for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                .getSuperClasses()) {

            TypeInfo superClassType = unresolvedSuperClassType.resolve(classInfo, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);

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
                    fieldInfoManager, methodInfoManager, unresolvableClasses);
        }
    }

    /**
     * �t�B�[���h�̒�`�� FieldInfoManager �ɓo�^����D registClassInfos �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ�
     * 
     */
    private void registFieldInfos() {

        // Unresolved �N���X���}�l�[�W���C�N���X���}�l�[�W���C�t�B�[���h���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // �e Unresolved�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            registFieldInfos(unresolvedClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
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
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // ClassInfo ���擾
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolved();
        assert null != ownerClass : "ownerClass shouldn't be null!";

        // �e�������t�B�[���h�ɑ΂���
        for (final UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo.getDefinedFields()) {

            unresolvedFieldInfo.resolve(ownerClass, null, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registFieldInfos(unresolvedInnerClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    /**
     * ���\�b�h�̒�`�� MethodInfoManager �ɓo�^����DregistClassInfos �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ��D
     */
    private void registMethodInfos() {

        // Unresolved �N���X���}�l�[�W���C �N���X���}�l�[�W���C���\�b�h���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // �e Unresolved�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            registMethodInfos(unresolvedClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
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
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // ClassInfo ���擾
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolved();

        // �e���������\�b�h�ɑ΂���
        for (final UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                .getDefinedMethods()) {

            // ���\�b�h��������
            final TargetMethodInfo methodInfo = unresolvedMethodInfo.resolve(ownerClass, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            // ���\�b�h����o�^
            ownerClass.addDefinedMethod(methodInfo);
            methodInfoManager.add(methodInfo);
        }

        // �e�������R���X�g���N�^�ɑ΂���
        for (final UnresolvedConstructorInfo unresolvedConstructorInfo : unresolvedClassInfo
                .getDefinedConstructors()) {

            //�@�R���X�g���N�^��������
            final TargetConstructorInfo constructorInfo = unresolvedConstructorInfo.resolve(
                    ownerClass, null, classInfoManager, fieldInfoManager, methodInfoManager);
            methodInfoManager.add(constructorInfo);

            // �R���X�g���N�^����o�^            
            ownerClass.addDefinedConstructor(constructorInfo);
            methodInfoManager.add(constructorInfo);

        }

        // �e Unresolved�N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registMethodInfos(unresolvedInnerClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    private void addClassTypeParameterInfos() {

        for (final TargetClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {
            addClassTypeParameterInfos(classInfo);
        }
    }

    private void addClassTypeParameterInfos(final TargetClassInfo classInfo) {

        final List<ClassTypeInfo> superClassTypes = classInfo.getSuperClasses();
        for (final ClassTypeInfo superClassType : superClassTypes) {

            final ClassInfo superClassInfo = superClassType.getReferencedClass();
            if (superClassInfo instanceof TargetClassInfo) {
                addClassTypeParameterInfos((TargetClassInfo) superClassInfo);

                // �e�N���X�ȏ�ɂ�����^�p�����[�^�̎g�p���擾
                final Map<TypeParameterInfo, TypeInfo> typeParameterUsages = ((TargetClassInfo) superClassInfo)
                        .getTypeParameterUsages();
                for (final TypeParameterInfo typeParameterInfo : typeParameterUsages.keySet()) {
                    final TypeInfo usedType = typeParameterUsages.get(typeParameterInfo);
                    classInfo.addTypeParameterUsage(typeParameterInfo, usedType);
                }

                // ���̃N���X�ɂ�����^�p�����[�^�̎g�p���擾
                final List<TypeInfo> typeArguments = superClassType.getTypeArguments();
                for (int index = 0; index < typeArguments.size(); index++) {
                    final TypeInfo usedType = typeArguments.get(index);
                    final TypeParameterInfo typeParameterInfo = ((TargetClassInfo) superClassInfo)
                            .getTypeParameter(index);
                    classInfo.addTypeParameterUsage(typeParameterInfo, usedType);
                }
            }
        }
    }

    private void addMethodTypeParameterInfos() {

        for (final TargetMethodInfo methodInfo : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            addMethodTypeParameterInfos(methodInfo);
        }
    }

    private void addMethodTypeParameterInfos(final TargetMethodInfo methodInfo) {

        //�@�܂��C�I�[�i�[�N���X�ɂ�����^�p�����[�^�g�p�̏���ǉ�����
        {
            final TargetClassInfo ownerClassInfo = (TargetClassInfo) methodInfo.getOwnerClass();
            final Map<TypeParameterInfo, TypeInfo> typeParameterUsages = ownerClassInfo
                    .getTypeParameterUsages();
            for (final TypeParameterInfo typeParameterInfo : typeParameterUsages.keySet()) {
                final TypeInfo usedType = typeParameterUsages.get(typeParameterInfo);
                methodInfo.addTypeParameterUsage(typeParameterInfo, usedType);
            }
        }

        // TODO ���\�b�h���ɂ�����^�p�����[�^�g�p��ǉ����ׂ��H
    }

    /**
     * ���\�b�h�I�[�o�[���C�h�����eMethodInfo�ɒǉ�����DaddInheritanceInfomationToClassInfos �̌� ���� registMethodInfos
     * �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ�
     */
    private void addOverrideRelation() {

        // �S�Ă̑ΏۃN���X�ɑ΂���
        for (final TargetClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {
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

        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

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

        // �e�������R���X�g���N�^���ɑ΂���
        for (final UnresolvedConstructorInfo unresolvedConstructorInfo : unresolvedClassInfo
                .getDefinedConstructors()) {

            // �������R���X�g���N�^�����̗��p�֌W������
            this.addReferenceAssignmentCallRelation(unresolvedConstructorInfo, unresolvedClassInfo,
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
        final LocalSpaceInfo localSpace = unresolvedLocalSpace.getResolved();
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

        // �������̏ꍇ�C�������������̖��O��������
        if (localSpace instanceof ConditionalBlockInfo) {
            final UnresolvedConditionalBlockInfo<?> unresolvedConditionalBlock = (UnresolvedConditionalBlockInfo<?>) unresolvedLocalSpace;

            if (null != unresolvedConditionalBlock.getConditionalClause()) {
                final ConditionalClauseInfo conditionalClause = unresolvedConditionalBlock
                        .getConditionalClause().resolve(ownerClass, ownerMethod, classInfoManager,
                                fieldInfoManager, methodInfoManager);

                try {
                    final Class<?> cls = Class
                            .forName("jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo");
                    final Field filed = cls.getDeclaredField("conditionalClause");

                    filed.setAccessible(true);
                    filed.set(localSpace, conditionalClause);
                } catch (ClassNotFoundException e) {
                    assert false : "Illegal state: ConditionalBlockInfo is not found";
                } catch (NoSuchFieldException e) {
                    assert false : "Illegal state: conditionalClause is not found";
                } catch (IllegalAccessException e) {
                    assert false;
                }
            } else {
                assert false;
            }
        }

        // �e�����������̖��O��������
        for (final UnresolvedStatementInfo<? extends StatementInfo> unresolvedStatement : unresolvedLocalSpace
                .getStatements()) {
            if (!(unresolvedStatement instanceof UnresolvedBlockInfo)) {
                final StatementInfo statement = unresolvedStatement.resolve(ownerClass,
                        ownerMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                localSpace.addStatement(statement);
            }
        }

        // �e�������t�B�[���h�g�p�̖��O��������
        for (final UnresolvedVariableUsageInfo<?> unresolvedVariableUsage : unresolvedLocalSpace
                .getVariableUsages()) {

            // �������ϐ��g�p������
            final ExpressionInfo variableUsage = unresolvedVariableUsage.resolve(ownerClass,
                    ownerMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            // ���O�����ł����ꍇ�͓o�^
            if (variableUsage instanceof VariableUsageInfo) {
                VariableUsageInfo<?> usage = (VariableUsageInfo<?>) variableUsage;
                localSpace.addVariableUsage(usage);
                //usage.getUsedVariable().addUsage(usage);

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
        for (final UnresolvedCallInfo<?> unresolvedCall : unresolvedLocalSpace.getCalls()) {

            final ExpressionInfo memberCall = unresolvedCall.resolve(ownerClass, ownerMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            // ���\�b�h����уR���X�g���N�^�Ăяo���������ł����ꍇ
            if (memberCall instanceof MethodCallInfo) {
                localSpace.addCall((MethodCallInfo) memberCall);
                ((MethodCallInfo) memberCall).getCallee().addCaller(ownerMethod);
            } else if (memberCall instanceof ConstructorCallInfo) {
                localSpace.addCall((ConstructorCallInfo) memberCall);
            }
        }

        //�@�e�C���i�[�u���b�N�ɂ���
        for (final UnresolvedStatementInfo<?> unresolvedStatement : unresolvedLocalSpace
                .getStatements()) {

            // ���������\�b�h�����̗��p�֌W������
            if (unresolvedStatement instanceof UnresolvedBlockInfo) {

                this.addReferenceAssignmentCallRelation(
                        (UnresolvedBlockInfo<?>) unresolvedStatement, unresolvedClassInfo,
                        classInfoManager, fieldInfoManager, methodInfoManager);
            }
        }
    }
}
